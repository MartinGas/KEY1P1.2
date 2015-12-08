package key1p12.tetris.bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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
		makeDecision (state);
	}
	
	/**
	 * @param noMove result obtained 
	 * @return array list of moves possible for state
	 */
	public ArrayList <InstructionSet> genPossibleMoves (InstructionSet noMove)
	{
		//generate instruction sets for every possible rotation and store number of generated instruction sets
		ArrayList <InstructionSet> basicRotations = new ArrayList <InstructionSet>();
		boolean rotDupl = false;
		do
		{
			if (basicRotations.isEmpty())
				basicRotations.add (noMove);
			else
			{
				InstructionSet last = basicRotations.get (basicRotations.size() - 1);
				if (!last.checkRotateDuplicate())
					basicRotations.add (new InstructionSet (last, GameMove.TURN));
				else
					rotDupl = true;
			}
		} while (!rotDupl);
		
		//stores possible moves as InstructionSets
		ArrayList <InstructionSet> pblts = new ArrayList <InstructionSet>();
		for (InstructionSet bRot : basicRotations)
		{
			//add basic rotation
			pblts.add (bRot);
			//generate all moves to the left based on basic rotation
			while (pblts.get (pblts.size() - 1).checkMove (Direction.LEFT))
				pblts.add (new InstructionSet (pblts.get (pblts.size() - 1), GameMove.MLEFT));
			//generate all moves to the right based on basic rotation
			if (bRot.checkMove (Direction.RIGHT))
			{
				pblts.add (new InstructionSet (bRot, GameMove.MRIGHT));
				while (pblts.get (pblts.size() - 1).checkMove (Direction.RIGHT))
					pblts.add (new InstructionSet (pblts.get (pblts.size() - 1), GameMove.MRIGHT));
			}
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
	private void makeDecision (SimulGame state)
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
}
