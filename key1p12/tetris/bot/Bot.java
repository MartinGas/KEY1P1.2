package key1p12.tetris.bot;

import key1p12.tetris.game.*;
import key1p12.tetris.game.Game.SimulGame;

import java.io.*;
import java.util.LinkedList;
import java.util.ArrayList;

import javax.management.ImmutableDescriptor;

//abstract class every bot should inherit from
public abstract class Bot implements Player
{	
	public static boolean DEBUG = false;
	//non-static section
	//public section
	public class PickListener implements IGameListener
	{
		/**
		 * @param event GameAction to check for
		 * @return true if listener is sensitive to PICK action
		 */
		@Override
		public boolean isSensitive (GameAction event) 
		{
			if (event == GameAction.PICK || event == GameAction.RESUME || event == GameAction.MOVE)
				return true;
			return false;
		}
		
		/**
		 * Updates the bot's move target on PICK event
		 * @param state state of the game
		 * @param event event that occurred
		 */
		@Override
		public void performAction(Game state, GameAction event) 
		{
			if (event == GameAction.PICK || event == GameAction.RESUME)
			{
				Bot.this.update(new Game.SimulGame(state));
			}
			else
				Bot.this.react (new Game.SimulGame (state));
		}
	}
	
	/**
	 * constructor: initializes name, performance measure, ideal move column
	 * @param pMeasure desired performance measure the bot should use
	 * @throws FileNotFoundException
	 */
	public Bot (ArrayList <PerfMeasure> pMeasures, ArrayList <Double> weights, String name) throws FileNotFoundException
	{
		assert (pMeasures.size() == weights.size());
		mPMeasures = pMeasures;
		mPMeasureWeights = (ArrayList<Double>) weights.clone();
		mName = name;
		mIdealMove = null;
		mIdealNextMove = GameMove.NONE;
	}
	
	/**
	 * constructor: initializes name, performance measure, ideal move column
	 * @param pMeasure desired performance measure the bot should use
	 * @param nameBase data base of names to be used by the NameGenerator
	 * @throws FileNotFoundException
	 */
	public Bot (ArrayList <PerfMeasure> pMeasures, ArrayList <Double> weights, File nameBase) throws FileNotFoundException
	{
		assert (pMeasures.size() == weights.size());
		mPMeasures = pMeasures;
		mPMeasureWeights = (ArrayList<Double>) weights.clone();
		NameGenerator chooseName = new NameGenerator (nameBase);
		mName = "Bot" + chooseName.getName();
		mIdealMove = null;
		mIdealNextMove = GameMove.NONE;
	}
	
	/**
	 * @return name of the bot as chosen upon construction
	 */
	public String getName()
	{
		return mName;
	}
	
	/**
	 * Accessor method to use performance measure
	 * @param state current state of game
	 * @return performance measure return value
	 */
	public double evaluateState (Game.SimulGame state)
	{
		double weightedSum = 0.0;
		for (int cMeasure = 0; cMeasure < mPMeasures.size(); ++cMeasure)
		{
			weightedSum += mPMeasures.get (cMeasure).getPerf (state) * mPMeasureWeights.get(cMeasure);
		}
		return weightedSum;
	}
	
	/**
	 * determines move to move pentomino in ideal location
	 * @param state current state of game
	 * @return direction pentomino should move to
	 */
	public GameMove getMove()
	{
		assert (mIdealNextMove != null);
		if (mIdealNextMove == GameMove.WAIT)
		{
			System.out.println ("Waiting to move pent to the " + mIdealMove.getMove());
		}
		
		GameMove exec = GameMove.NONE;
		//consume stored move if actual move is stored
		if (mIdealNextMove != GameMove.WAIT && mIdealNextMove != GameMove.NONE)
		{
			exec = mIdealNextMove;
			//reset => react gets next move from queue
			mIdealNextMove = GameMove.NONE;
		}
		
		if (DEBUG)
			System.out.println ("Bot returns move " + exec);
		return exec;
	}
	
	/**
	 * Perform calculation deciding whether to execute next move in queue
	 * @param state game state
	 */
	public void react (Game.SimulGame state)
	{
		if (mIdealNextMove == GameMove.WAIT)
		{
			//Decide whether wait is over == move following wait
			if (!state.isGameMovePossible (mIdealMove.getMove()))
				mIdealNextMove = GameMove.NONE;
		}
		if (mIdealNextMove != GameMove.WAIT)
		{
			//Decide whether to execute move next turn
			if (mIdealNextMove == GameMove.NONE && 
				!mIdealMove.isEmpty() &&
				state.isGameMovePossible (mIdealMove.getMove()))
			{
				//if move possible: execute
				mIdealNextMove = mIdealMove.getMove();
				mIdealMove.eraseHead();
			}
			
		}
			
	}
	
	//protected section
	/**
	 * Abstract method to trigger update ideal move
	 * @param state current state of game 
	 */
	protected class InstructionSet
	{
		/**
		 * Constructs instruction set from state of the game
		 * @param state state the game is in
		 */
		public InstructionSet (SimulGame state)
		{
			mMoveQueue = new LinkedList <GameMove> ();
			mState = state.clone();
			mMoveDirection = Direction.NONE;
			mRemainRotations = Pentomino.generateRotations (state.getUsedPent()).size() - 1;
			mPScore = 0;
			mMoveOccurred = false;
		}
		
		/**
		 * Constructs instruction set from state contained in InstructionSet => allows tree structure
		 * @param lastGen instruction set of the last move
		 */
		public InstructionSet (InstructionSet lastGen)
		{
			mMoveQueue = new LinkedList <GameMove> ();
			assert (lastGen.moveExecuted()):
			mState = lastGen.mState.clone();
			mMoveDirection = Direction.NONE;
			mRemainRotations = Pentomino.generateRotations (lastGen.mState.getUsedPent()).size() - 1;
			mPScore = 0;
			mMoveOccurred = false;
		}
		
		/**
		 * Constructs instruction set by adding another move to the previous move
		 * @param origin instruction set to be extended
		 * @param move move to extend origin
		 */
		public InstructionSet (InstructionSet origin, GameMove move)
		{
			assert (move == GameMove.MRIGHT || move == GameMove.MLEFT || move == GameMove.TURN || move == GameMove.WAIT);
			assert (!origin.moveExecuted());
			mMoveQueue = new LinkedList <GameMove> (origin.mMoveQueue);
			mMoveQueue.add (move);
			mState = origin.mState.clone();
			mMoveDirection = origin.mMoveDirection;
			mRemainRotations = origin.mRemainRotations;
			switch (move)
			{
			case MLEFT: mState.move (Direction.LEFT);
						mMoveDirection = Direction.LEFT;
						break;
			case MRIGHT: mState.move (Direction.RIGHT); 
						mMoveDirection = Direction.RIGHT;
						break;
			case TURN: 	mState.pentRotate(); 
						--mRemainRotations;
						break;
			case WAIT:	break;
			default: break;
			}
			mPScore = 0;
			mMoveOccurred = false;
		}
		
		public ArrayList <InstructionSet> getFollowUp()
		{
			ArrayList <InstructionSet> foUps = new ArrayList <InstructionSet>();
			//TODO add game states containing all different pentominoes in current use
			
			return foUps;
		}
		
		/**
		 * Performs move defined by internal state
		 * Precondition: move was not executed already
		 * Postcondition: It is no longer possible to extend this InstructionSet
		 * Postcondition: It is possible to construct InstructionSets based on the state of this InstructionSet
		 */
		public void doMove()
		{
			assert (!moveExecuted());
			mMoveOccurred = true;
			mState.fallPlace();
			mPScore = evaluateState (mState);
			
			if (DEBUG)
			{
				System.out.println ("Move tested " + mMoveQueue.toString() + " score = " + mPScore);
				System.out.println (mState.toString());
			}
		}
		
		/**
		 * erases first element of move queue
		 */
		public void eraseHead()
		{
			assert (!isEmpty());
			mMoveQueue.removeFirst();
		}
		
		/**
		 * Creates instruction sets containing wait instructions 
		 * @return all branches of this instruction set
		 */
		public ArrayList <InstructionSet> getBranches (Direction d)
		{
			ArrayList <InstructionSet> branches = new ArrayList <InstructionSet>();
			if (canWait (d))
			{
				InstructionSet branch = new InstructionSet (this, d);
				//ArrayList <InstructionSet> sub = branch.getBranches (d);
				branches.add (branch);
				//branches.addAll (sub);
			}
			
			return branches;
		}
		
		/**
		 * @return First element of move queue
		 */
		public GameMove getMove()
		{
			assert (!isEmpty());
			return mMoveQueue.getFirst();
		}
		
		/**
		 * Precondition: Move was executed already
		 * @return performance value of internal state
		 */
		public double getPMeasure ()
		{
			assert (moveExecuted());
			return mPScore;
		}
		
		/**
		 * @return true if move queue is empty
		 */
		public boolean isEmpty()
		{
			return mMoveQueue.isEmpty();
		}
		
		/**
		 * @return true if move was executed already
		 */
		public boolean moveExecuted()
		{
			return mMoveOccurred;
		}
		
		/**
		 * Precondition: move was not executed already
		 * @param d direction to move to
		 * @return true if pentomino used in game can be moved in direction d
		 */
		public boolean checkMove (Direction d)
		{
			assert (!moveExecuted());
			assert (d == Direction.LEFT || d == Direction.RIGHT);
			return mState.checkMove(d);
		}
		
		/**
		 * @param d direction to check for
		 * @return true if there is a move in the move queue that cancels the effect of moving in d
		 */
		public boolean checkMoveCancel (Direction d)
		{
			return (d != mMoveDirection);
		}
		
		/**
		 * Precondition: move was not executed already
		 * @param d direction to rotate to
		 * @return true if pentomino used in game can be rotated in direction d
		 */
		public boolean checkRotate (Direction d)
		{
			return mState.checkRotate(d);
		}
		
		/**
		 * @return true if rotating the pentomino reverts the pentomino to a previous state
		 */
		public boolean checkRotateDuplicate()
		{
			return (mRemainRotations  <= 0);
		}
		
		/**
		 * @param d direction for wait move
		 * @return true if there is a place in the adjacent column in d where the pentomino cannot be moved by dropping (cave)
		 */
		public boolean canWait (Direction d)
		{
			assert (d == Direction.RIGHT || d == Direction.LEFT);
			
			Position pentPos = mState.getPentPosition();
			//if wait for move left
			if (d == Direction.LEFT && pentPos.getX() > 0)
			{
				int pentLeftSideHeight = mState.getUsedPent().countRow (0);
				int firstFilledIndex = mState.getFilledHeight (pentPos.getX() - 1) + 1;
				int caveHeight = mState.getHeight() - firstFilledIndex - mState.countColPart(pentPos.getX() - 1, firstFilledIndex, mState.getHeight() - 1);
				if (pentLeftSideHeight <= caveHeight)
					return true;
			}
			
			//if wait for move right
			if (d == Direction.LEFT && pentPos.getX() < mState.getWidth() - 1)
			{
				int pentLeftSideHeight = mState.getUsedPent().countRow (mState.getUsedPent().getWidth() - 1);
				int firstFilledIndex = mState.getFilledHeight (pentPos.getX() + 1) + 1;
				int caveHeight = mState.getHeight() - firstFilledIndex - mState.countColPart(pentPos.getX() + 1, firstFilledIndex, mState.getHeight() - 1);
				if (pentLeftSideHeight <= caveHeight)
					return true;
			}
				
			return false;
		}
		
		/**
		 * Wait constructor
		 * @param origin instruction set to add wait to
		 * @param waitDir direction to move after wait
		 */
		private InstructionSet (InstructionSet origin, Direction waitDir)
		{
			this (origin, GameMove.WAIT);
			assert (waitDir == Direction.LEFT || waitDir == Direction.RIGHT);
			
			doWait (waitDir);
			switch (waitDir)
			{
			case LEFT: mMoveQueue.add (GameMove.MLEFT);
			break;
			case RIGHT: mMoveQueue.add (GameMove.MRIGHT);
			break;
			}
		}
		
		/**
		 * moves pent until move in d is blocked, then moves until move in d is not blocked anymore
		 * @param d direction for wait move
		 */
		private void doWait (Direction d)
		{
			//fall at least once
			//fall while move in d is possible
			boolean fell = false;
			do
			{
				if (mState.checkMove (Direction.DOWN))
				{
					fell = true;
					mState.move (Direction.DOWN);
				}
			}while (mState.checkMove (Direction.DOWN) && mState.checkMove (d));
			
			if (fell)
			{
				//fall while move in d is blocked and fall is possible
				do
				{
					mState.move (Direction.DOWN);
				}while (mState.checkMove (Direction.DOWN) && !mState.checkMove (d));
				
				//if move in d is possible: move as long as possible
				while (mState.checkMove (d))
				{
					mState.move (d);
				}
			}
		}
		
		private LinkedList <GameMove> mMoveQueue;
		private Game.SimulGame mState;
		private Direction mMoveDirection;
		private int mRemainRotations;
		private double mPScore;
		private boolean mMoveOccurred;
	}
	
	public abstract void update (Game.SimulGame state);
	
	/**
	 * Sets ideal move variable
	 * @param newIdealCol new ideal move column
	 */
	protected void setMove (InstructionSet newIdealMove)
	{
		mIdealMove = newIdealMove;
	}
	
	//private section
	
	private ArrayList <PerfMeasure> mPMeasures;
	private ArrayList <Double> mPMeasureWeights;
	private String mName;
	private InstructionSet mIdealMove;
	private GameMove mIdealNextMove;
}