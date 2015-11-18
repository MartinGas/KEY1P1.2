package key1p12.tetris.bot;
import Direction;
import Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GreedyBot extends Bot {

	/**
	 * constructor: calls Bot constructor
	 * @param pMeasure desired performance measure the bot should use
	 * @param nameBase data base of names to be used by the NameGenerator
	 * @throws FileNotFoundException
	 */
	public GreedyBot(PerfMeasure pMeasure, File nameDataBase) throws FileNotFoundException 
	{
		super(pMeasure, nameDataBase);
	}
	
	/**
	 * adapts best move to updated state, stores result
	 * @param state current state of the game
	 */
	public void update (Game state)
	{
		makeDecision (state);
	}
	
	/**
	 * @param noMove result obtained 
	 * @return array list of moves possible for state
	 */
	public ArrayList <InstructionSet> genPossibleMoves (InstructionSet noMove)
	{
		//stores possible moves as InstructionSets
		ArrayList <InstructionSet> pblts = new ArrayList <InstructionSet>();
		//TODO make use of number of rotations
		final int rotations = 4;
		//add InstrucionSet == empty set
		pblts.add (noMove);
		//generate all InstructionSets moving to the left
		while (pblts.get (pblts.size()).checkMove(Direction.LEFT))
			pblts.add (new InstructionSet (pblts.get (pblts.size()), Direction.LEFT));
		//generate all InstructionSets moving to the right
		if (noMove.checkMove(Direction.RIGHT))
		{
			pblts.add (new InstructionSet (noMove, Direction.RIGHT));
			while (pblts.get (pblts.size()).checkMove(Direction.RIGHT))
				pblts.add (new InstructionSet (pblts.get (pblts.size()), Direction.RIGHT));
		}
		//apply moves for each InstructionSet
		for (InstructionSet pbl : pblts)
			pbl.doMove();
		
		return pblts;
	}
	
	/**
	 * Evaluates the outcome of every possible move
	 * best outcome determined by highest performance measure
	 * @param state current state of game
	 */
	private void makeDecision (Game state)
	{
		//generate all possible moves
		ArrayList <InstructionSet> moves = genPossibleMoves (new InstructionSet (state));
		
		//search for move maximizing the performance measure
		int pmMax = Integer.MIN_VALUE;
		for (InstructionSet move : moves)
		{
			int pmCurrent = move.getPMeasure();
			if (pmCurrent > pmMax)
			{
				pmMax = pmCurrent;
				setMove (move);
			}
		}
	}
}
