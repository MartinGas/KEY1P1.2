package key1p12.tetris.bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import key1p12.tetris.game.Direction;
import key1p12.tetris.game.Game.SimulGame;
import key1p12.tetris.game.GameMove;

public class GeneticBot extends Bot 
{
	/**
	 * Simple parametric constructor
	 * @param pMeasure performance measure to be used by bot
	 * @param nameDataBase file containing naming instructions to be used by NameGenerator
	 * @param mutationProb probability for mutation to occur
	 * @param popSize size of population used to generate best result
	 * @param searchDepth number of game iterations
	 * @param evoCycles number of mutation-selection cycles
	 * @throws FileNotFoundException
	 */
	public GeneticBot(ArrayList <PerfMeasure> pMeasures, ArrayList <Double> weights, File nameDataBase, double mutationProb, int popSize, int searchDepth, int evoCycles) throws FileNotFoundException
	{
		super(pMeasures, weights, nameDataBase);
		mPopSize = popSize;
		mSearchDepth = searchDepth;
		mEvoCycles = evoCycles;
		mMutProb = mutationProb;
		mRandGen = new Random (System.currentTimeMillis());
	}

	/**
	 * Updates bot's move target
	 * @param state current state of game
	 */
	public void update(SimulGame state) 
	{
		if (DEBUG)
			System.out.println ("Updating genetic bot");
		InstructionSet origin = new InstructionSet(state);
		//generate starting population of subsequent moves
		LinkedList <ArrayList <InstructionSet>> pop = genPopulation (origin, mSearchDepth, mPopSize);
		//for numberOfIterations
		for (int cEvo = 0; cEvo < mEvoCycles; cEvo++)
		{
			produceOffspring (origin, pop);
			//kill weak ones
			selectPop (pop, mPopSize);
		}
			
		//find best solution
		double bestScore = Double.MIN_VALUE;
		InstructionSet ideal = null;
		for (ArrayList <InstructionSet> mem : pop)
		{
			double score = getPMeasure (mem);
			if (score > bestScore)
			{
				bestScore = score;
				ideal = mem.get(0);
			}
		}
		//set move to ideal solution
		setMove (ideal);
		if (DEBUG)
		{
			System.out.println ("Best move ");
			ideal.printQueue();
		}
	}
	
	/**
	 * Generates starting population
	 * @param start current state of game
	 * @param depth simulation depth
	 * @param popSize population size
	 * @return generated population of InstructionSets
	 */
	private LinkedList <ArrayList <InstructionSet>> genPopulation (InstructionSet start, int depth, int popSize)
	{
		//stores population
		LinkedList <ArrayList <InstructionSet>> pop = new LinkedList <ArrayList <InstructionSet>>();
		//add members to population until it reaches popSize
		for ( ; pop.size() < popSize; )
		{
			//generate member: Sequence of move targets
			ArrayList <InstructionSet> newPopMember = new ArrayList <InstructionSet>();
			for ( ; newPopMember.size() < depth; )
			{
				//generate random move target
				InstructionSet latestInstruction = this.genRandMoves (start);
				latestInstruction.doMove();
				newPopMember.add (latestInstruction);
			}
			pop.add (newPopMember);
		}
		if (DEBUG)
			System.out.println ("Population of size " + pop.size() + " generated");
		
		return pop;
	}
	
	/**
	 * @param origin state prior to move target
	 * @return random move target
	 */
	private InstructionSet genRandMoves (InstructionSet origin)
	{
		InstructionSet last = new InstructionSet (origin);
		boolean done = false;
		//add arbitrary number of moves
		while (!done)
		{
			switch (mRandGen.nextInt(4))
			{
			case 0: done = true;
			break;
			case 1: if (!last.checkMoveCancel (Direction.LEFT))
						last = new InstructionSet (last, GameMove.MLEFT);
			break;
			case 2: if (!last.checkMoveCancel (Direction.RIGHT))
						last = new InstructionSet (last, GameMove.MRIGHT);
			break;
			case 3: if (!last.checkRotateDuplicate())
						last = new InstructionSet (last, GameMove.TURN);
			break;
			}
		}
		if (DEBUG)
		{
			System.out.println ("Generated new instruction set ");
			last.printQueue();
		}
		return last;
	}
	
	/**
	 * @param comp InstructionSet to evaluate
	 * @return performance value
	 */
	private double getPMeasure (ArrayList <InstructionSet> comp)
	{
		//TODO does not work
		//TODO need to consider every following state
		double score = 0;
		for (int cDepth = 1; cDepth < comp.size(); ++cDepth)
		{
		}
		for (InstructionSet ins : comp)
			score += ins.getPMeasure();
		
		return score;
	}
	
	/**
	 * Eliminates weakest members of population
	 * @param pop population
	 * @param survivors number of members to keep for next iteration
	 */
	private void selectPop (LinkedList <ArrayList <InstructionSet>> pop, int survivors)
	{
		if (DEBUG)
			System.out.println ("Selection start " + pop.size() + " individuals");
		//stores amount <survivors> best scores
		LinkedList <Double> bestScores  = new LinkedList <Double>();
		//determine best scores by iterating through population
		for (ArrayList <InstructionSet> member : pop)
		{
			//compute best score of current member
			double score = getPMeasure (member);
			ListIterator <Double> findPos = bestScores.listIterator();
			//find place in list
			while (findPos.hasNext() && findPos.next().compareTo (new Double (score)) > 0) {}
			//if not at end of list: insert & remove last if necessary
			if (findPos.hasNext())
			{
				findPos.add (new Double (score));
				if (bestScores.size() > survivors)
					bestScores.removeLast();
			}
			//store if at end and size is inferior to maximum size
			else if (bestScores.size() < survivors)
				bestScores.add (new Double (score));
		}
		//eliminate members having performance values less than weakest survivor
		ListIterator <ArrayList <InstructionSet>> findElim = pop.listIterator();
		while (findElim.hasNext())
		{
			double score = getPMeasure(findElim.next());
			int comparison = bestScores.getLast().compareTo (new Double (score));
			if (pop.size() > mPopSize && comparison >= 0 || pop.size() <= mPopSize && comparison > 0)
				findElim.remove();
		}
		if (DEBUG)
			System.out.println ("Selection end " + pop.size() + " individuals");
	}
	
	private void produceOffspring (InstructionSet origin, LinkedList <ArrayList <InstructionSet>> pop)
	{
		if (DEBUG)
			System.out.println ("Recombination start " + pop.size() + " individuals");
		ArrayList <ArrayList <InstructionSet>> offspring = new ArrayList<ArrayList<InstructionSet>>();
		for (int cPop = 0; cPop < pop.size(); ++cPop)
		{
			int iMate = mRandGen.nextInt (pop.size());
			if (iMate != cPop)
			{
				ArrayList <InstructionSet> child = recombine (pop.get(cPop), pop.get(iMate));
				mutateIndividual (origin, child);
				offspring.add(child);
			}
		}
		pop.addAll(offspring);
		if (DEBUG)
			System.out.println ("Recombination end " + pop.size() + " individuals");
	}
	
	private void mutateIndividual (InstructionSet origin, ArrayList <InstructionSet> i)
	{
		if (mRandGen.nextDouble() <= mMutProb)
		{
			int mutMove = mRandGen.nextInt (i.size());
			InstructionSet previous = null;
			if (mutMove == 0)
				previous = origin;
			else
				previous = i.get(mutMove - 1);

			InstructionSet mutatedGene = genRandMoves(previous);
			mutatedGene.doMove();
			i.set(mutMove, mutatedGene);
		}
	}
	
	private ArrayList <InstructionSet> recombine (ArrayList <InstructionSet> p1, ArrayList<InstructionSet> p2)
	{
		ArrayList <InstructionSet> child = new ArrayList<InstructionSet>();
		for (int cGenes = 0; cGenes < p1.size() && cGenes < p2.size(); ++cGenes)
		{
			if (cGenes % 2 == 0)
				child.add (p1.get(cGenes).clone());
			else
				child.add (p2.get(cGenes).clone());
		}
		return child;
	}
	
	private Random mRandGen;
	private int mPopSize, mSearchDepth, mEvoCycles;
	private double mMutProb;
}
