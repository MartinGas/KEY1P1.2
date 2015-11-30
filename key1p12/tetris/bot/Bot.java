package key1p12.tetris.bot;

import key1p12.tetris.game.*;
import key1p12.tetris.game.Game.SimulGame;

import java.io.*;
import java.util.LinkedList;
import java.util.ArrayList;

//abstract class every bot should inherit from
public abstract class Bot implements Player
{	
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
			if (event == GameAction.PICK)
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
			if (event == GameAction.PICK)
			{
				Bot.this.update(new Game.SimulGame(state));
				
			}
		}
	}
	
	/**
	 * constructor: initializes name, performance measure, ideal move column
	 * @param pMeasure desired performance measure the bot should use
	 * @param nameBase data base of names to be used by the NameGenerator
	 * @throws FileNotFoundException
	 */
	public Bot (PerfMeasure pMeasure, File nameBase) throws FileNotFoundException
	{
		mPMeasure = pMeasure;
		NameGenerator chooseName = new NameGenerator (nameBase);
		mName = chooseName.getName();
		mIdealMove = null;
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
	public int evaluateState (Game.SimulGame state)
	{
		return mPMeasure.getPerf(state);
	}
	
	/**
	 * determines move to move pentomino in ideal location
	 * @param state current state of game
	 * @return direction pentomino should move to
	 */
	public GameMove getMove()
	{
		assert (!mIdealMove.isEmpty());
		GameMove nextMove = mIdealMove.getMove();
		mIdealMove.eraseHead();
		return nextMove;
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
			assert (move == GameMove.MRIGHT || move == GameMove.MLEFT || move == GameMove.TURN);
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
			mPScore = mPMeasure.getPerf(mState);
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
		public int getPMeasure ()
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
		
		private LinkedList <GameMove> mMoveQueue;
		private Game.SimulGame mState;
		private Direction mMoveDirection;
		private int mRemainRotations;
		private int mPScore;
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
	
	private PerfMeasure mPMeasure;
	private String mName;
	private InstructionSet mIdealMove;
}