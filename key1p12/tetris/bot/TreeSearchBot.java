package key1p12.tetris.bot;

import java.io.*;
import java.util.ArrayList;

import key1p12.tetris.game.Game.SimulGame;

public class TreeSearchBot extends GreedyBot {
	
	/**
	 * simple parametric constructor
	 * @param pMeasure performance measure to be used by the bot
	 * @param nameDataBase file containing naming instructions to be used by NameGenerator
	 * @param searchDepth search depth of tree
	 * @throws FileNotFoundException
	 */
	public TreeSearchBot(ArrayList <PerfMeasure> pMeasures, ArrayList <Double> weights,  File nameDataBase, int searchDepth) throws FileNotFoundException 
	{
		super(pMeasures, weights, nameDataBase);
		mSearchDepth = searchDepth;
	}
	
	/**
	 * updates move target
	 * @param state current state of game
	 */
	public void update (SimulGame state)
	{
		makeDecision (state);
	}
	
	/**
	 * determines best move
	 * @param state current state of game
	 */
	public void makeDecision (SimulGame state)
	{
		//compute immediate moves
		ArrayList <InstructionSet> firstMoves = super.genPossibleMoves (new InstructionSet (state));
		InstructionSet bestAveragedMove = null;
		double bestAveragedMoveScore = Double.MIN_VALUE;
		//determine average score for each of immediate moves
		for (InstructionSet firstMove : firstMoves)
		{
			//averaging tree search
			double averagedMoveScore = iterBranch (firstMove, 0, mSearchDepth);
			//compare to best result
			if (averagedMoveScore > bestAveragedMoveScore)
			{
				bestAveragedMoveScore = averagedMoveScore;
				bestAveragedMove = firstMove;
			}
		}
		//set move target to best move found
		assert (bestAveragedMove != null);
		setMove (bestAveragedMove);
	}
	
	private double iterBranch (InstructionSet root, int depth, int maxDepth)
	{
		//TODO implement performance measure based pruning
			
				
				
					
		//calculate average for best moves of pentominoes
		double averageScore = 0;
		//simulate every pentomino
		//TODO implements follow ups method in abstract bot
		ArrayList <InstructionSet> foUps = root.getFollowUp();
		for (InstructionSet foUp : foUps)
		{
			//get moves for every pentomino
			ArrayList <InstructionSet> moves = super.genPossibleMoves (foUp);
			double maxScore = Double.MIN_VALUE;
			//if depth == maxDepth: store best move value for this pentomino
			if (depth >= maxDepth)
			{
				for (InstructionSet move : moves)
				{
					if (move.getPMeasure() > maxScore)
						maxScore = move.getPMeasure();
				}
			}
			else
			{
				//else for every move: recursion
				for (InstructionSet move : moves)
				{
					//averaged score of best moves + current performance value
					double branchScore = iterBranch (move, depth + 1, maxDepth) + move.getPMeasure();
					//compare to maximum
					if (branchScore > maxScore)
						maxScore = branchScore;
				}
			}
			//add best score to average
			averageScore += maxScore;
		}
		//compute average & return
		averageScore = (int) Math.round (averageScore / (double)foUps.size());
		return averageScore;
	}
	
	
	private int mSearchDepth;
}
