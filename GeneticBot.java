import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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
	public GeneticBot(PerfMeasure pMeasure, File nameDataBase, double mutationProb, int popSize, int searchDepth, int evoCycles) throws FileNotFoundException
	{
		super(pMeasure, nameDataBase);
		mPopSize = popSize;
		mSearchDepth = searchDepth;
		mEvoCycles = evoCycles;
		mMutProb = mutationProb;
	}

	/**
	 * Updates bot's move target
	 * @param state current state of game
	 */
	public void update(Game state) 
	{
		//generate starting population of subsequent moves
		LinkedList <ArrayList <InstructionSet>> pop = genPopulation (new InstructionSet (state), mSearchDepth, mPopSize);
		//for numberOfIterations
		for (int cEvo = 0; cEvo < mEvoCycles; cEvo++)
		{
			//kill weak ones
			selectPop (pop, mPopSize);
			//mutate remaining ones
			mutatePop (new InstructionSet (state), pop);
		}
			
		//find best solution
		int bestScore = Integer.MIN_VALUE;
		InstructionSet ideal = null;
		for (ArrayList <InstructionSet> mem : pop)
		{
			int score = getPMeasure (mem);
			if (score > bestScore)
			{
				bestScore = score;
				ideal = mem.get(0);
			}
		}
		//set move to ideal solution
		setMove (ideal);
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
		
		return pop;
	}
	
	/**
	 * @param origin state prior to move target
	 * @return random move target
	 */
	private InstructionSet genRandMoves (InstructionSet origin)
	{
		Random rand = new Random();
		InstructionSet last = new InstructionSet (origin);
		boolean done = false;
		//add arbitrary number of moves
		while (!done)
		{
			switch (rand.nextInt(4))
			{
			case 0: done = true;
			break;
			case 1: last = new InstructionSet (last, Direction.LEFT);
			break;
			case 2: last = new InstructionSet (last, Direction.RIGHT);
			break;
			case 3: last = new InstructionSet (last, Direction.UP);
			break;
			}
		}
		return last;
	}
	
	/**
	 * @param comp InstructionSet to evaluate
	 * @return performance value
	 */
	private int getPMeasure (ArrayList <InstructionSet> comp)
	{
		int score = 0;
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
		//stores amount <survivors> best scores
		LinkedList <Integer> bestScores  = new LinkedList <Integer>();
		//determine best scores by iterating through population
		for (ArrayList <InstructionSet> member : pop)
		{
			//compute best score of current member
			int score = getPMeasure (member);
			ListIterator <Integer> findPos = bestScores.listIterator();
			//find place in list
			while (findPos.hasNext() && findPos.next().compareTo (new Integer (score)) > 0) {}
			//if not at end of list: insert & remove last if necessary
			if (findPos.hasNext())
			{
				findPos.add (new Integer (score));
				if (bestScores.size() > survivors)
					bestScores.removeLast();
			}
			//store if at end and size is inferior to maximum size
			else if (bestScores.size() < survivors)
				bestScores.add (new Integer (score));
		}
		//eliminate members having performance values less than weakest survivor
		ListIterator <ArrayList <InstructionSet>> findElim = pop.listIterator();
		while (findElim.hasNext())
		{
			int score = getPMeasure(findElim.next());
			if (bestScores.getLast().compareTo (new Integer (score)) >= 0)
				findElim.remove();
		}
	}
	
	/**
	 * Generate mutations of population
	 * @param origin current state
	 * @param pop population
	 */
	private void mutatePop (InstructionSet origin, LinkedList <ArrayList <InstructionSet>> pop)
	{
		Random rand = new Random();
		//iterate through population
		ListIterator <ArrayList <InstructionSet>> iList = pop.listIterator();
		while (iList.hasNext())
		{
			//if random
			if (rand.nextDouble() <= mMutProb)
			{
				ArrayList <InstructionSet> mutate = (ArrayList<InstructionSet>) iList.next().clone();
				//determine which move will be recomputed => "mutated"
				int mutMove = rand.nextInt (mutate.size());
				InstructionSet prevMove = null;
				if (mutMove == 0)
					prevMove = origin;
				else
					prevMove = mutate.get (mutMove - 1);
				//compute mutated move, insert into sequence
				mutate.set (mutMove, genRandMoves (prevMove));
				mutate.get(mutMove).doMove();
				//add mutated child to beginning of population (=> prevent repeated mutation!)
				pop.addFirst (mutate);
			}
			else
				iList.next();
		}
	}
	
	private int mPopSize, mSearchDepth, mEvoCycles;
	private double mMutProb;
}
