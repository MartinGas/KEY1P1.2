package key1p12.tetris.bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import key1p12.tetris.game.Game;
import key1p12.tetris.game.GameMove;
import key1p12.tetris.game.Direction;
import key1p12.tetris.game.Game.SimulGame;

public class GreedyBot extends Bot {

	/**
	 * constructor: calls Bot constructor
	 * @param pMeasure desired performance measure the bot should use
	 * @param nameBase data base of names to be used by the NameGenerator
	 * @throws FileNotFoundException
	 */
	public GreedyBot(ArrayList <PerfMeasure> pMeasures, ArrayList <Double> weights, File nameDataBase) throws FileNotFoundException 
	{
		super(pMeasures, weights, nameDataBase);
	}
	
	/**
	 * adapts best move to updated state, stores result
	 * @param state current state of the game
	 */
	public void update (SimulGame state)
	{
		//generate all possible moves
		ArrayList <InstructionSet> moves = genPossibleMoves (new InstructionSet (state));
		
		//search for move maximizing the performance measure
		double pmMax = Double.MIN_VALUE;
		for (InstructionSet move : moves)
		{
			double pmCurrent = move.getPMeasure();
			if (pmCurrent > pmMax)
			{
				pmMax = pmCurrent;
				setMove (move);
			}
		}
	}
	
	/**
	 * @param noMove result obtained 
	 * @return array list of moves possible for state
	 */
	public ArrayList <InstructionSet> genPossibleMoves (InstructionSet noMove)
	{
		ArrayList <InstructionSet> possible = new ArrayList <InstructionSet>();
		
		ArrayList <InstructionSet> basicRotations = generateRotations (noMove);
		basicRotations.add (noMove);
		possible.addAll (basicRotations);
		
		for (InstructionSet bRot : basicRotations)
		{
			ArrayList <InstructionSet> moveLeft = generateMoves (bRot, GameMove.MLEFT);
			ArrayList <InstructionSet> moveRight = generateMoves (bRot, GameMove.MRIGHT);
			ArrayList <InstructionSet> allMoves = new ArrayList <InstructionSet>();

			allMoves.addAll (moveLeft);
			allMoves.addAll (moveRight);
			possible.addAll (allMoves);
			
			/* not working code: branching
			for (InstructionSet everyMove : allMoves)
			{
				ArrayList <InstructionSet> leftBranches = everyMove.getBranches (Direction.LEFT);
				ArrayList <InstructionSet> rightBranches = everyMove.getBranches (Direction.RIGHT);
				possible.addAll (leftBranches);
				possible.addAll (rightBranches);
			}
			*/
		}
		
		//apply moves for each InstructionSet
		for (InstructionSet possibility : possible)
		{
			possibility.doMove();
		}
		
		return possible;
	}
	
	public ArrayList <InstructionSet> generateRotations (InstructionSet init)
	{
		//generate instruction sets for every possible rotation and store number of generated instruction sets
		ArrayList <InstructionSet> rotations = new ArrayList <InstructionSet>();
		InstructionSet last = init;
		while (!last.checkRotateDuplicate())
		{
			last = new InstructionSet (last, GameMove.TURN);
			rotations.add (last);
		}
		return rotations;
	}
	
	public ArrayList <InstructionSet> generateMoves (InstructionSet init, GameMove move)
	{
		ArrayList <InstructionSet> moves = new ArrayList <InstructionSet>();
		InstructionSet last = init;
		while (last.checkMove (Game.getDirectionFromMove (move)))
		{
			last = new InstructionSet (last, move);
			moves.add (last);
		}
		
		return moves;
	}
}
