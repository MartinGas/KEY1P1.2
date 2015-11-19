import java.util.*;

import key1p12.tetris.game.Board;
import key1p12.tetris.game.Pentomino;

public class SolvePents{
	
	
	
	/**constructor, boolean needed for distinction: partial vs non-partial solving*/
	public SolvePents(ArrayList<Pentomino> pentominoes, Board board, boolean partial){
		mListofSolutions = new ArrayList<Board>();
		mNodeCounter = 0;
		ArrayList<ArrayList<Pentomino>> ListofRotPentSets = Pentomino.generateVersions(pentominoes);
		if (partial)
			solveMax (ListofRotPentSets, board);
		else
			solve(ListofRotPentSets, board);
	}
	
	/**debug constructor
	*gets two partial solutions (need to be preconfigured)
	*partial solutions will be assembled using Board.assembleBoardsH*/
	public SolvePents (ArrayList <Pentomino> pentominoes, Board partialS1, Board partialS2)
	{
		mListofSolutions = new ArrayList <Board>();
		mNodeCounter = 0;
		
		//put boards together
		Board assembled = Board.assembleBoardsH (partialS1, partialS2, partialS1.getHeight() + partialS2.getHeight());
		//get remaining pentominoes
		ArrayList <Pentomino> remainingPents = new ArrayList <Pentomino>();
		for (Pentomino pent : pentominoes)
			remainingPents.add (pent.clone());
		remainingPents = partialS1.unusedPents (remainingPents);
		remainingPents = partialS2.unusedPents (remainingPents);
		//generate rotations
		ArrayList <ArrayList <Pentomino>> rotPentSets = Pentomino.generateVersions (remainingPents);
		//solve
		solve (rotPentSets, assembled);
	}
	
	/**optimized constructor*/
	public SolvePents (ArrayList<Pentomino> pentominoes, Board boardToSolve)
	{
		Scanner in = new Scanner (System.in);
		
		//initialze members
		mListofSolutions = new ArrayList <Board>();
		mNodeCounter = 0;
		//gets sets containing rotated pentominos
		ArrayList <ArrayList <Pentomino>> rotPentSets = Pentomino.generateVersions (pentominoes);
		//gets optimally splitted board
		Board subBoard = boardToSolve.splitHalfFloor ((int)Math.floor (Board.mAREA / Board.mNO_PENTS));
		
		//create partial solutions
		SolvePents partialSolution = new SolvePents (pentominoes, subBoard, true);
		//update node counter (nodes of partial solutions should be part of nodes of total solution)
		mNodeCounter += partialSolution.getNodeCounter();
		
		//try match partial solutions to generate final solution
		ArrayList <Board> partSolvedBoards = partialSolution.getSolutions();
		for (Board sol1 : partSolvedBoards)
		{
			for (Board sol2 : partSolvedBoards)
			{
				//make sure partial solutions do not contain the same pentomino
				if (!sol1.pentsPlacedIntersect (sol2))
				{
					System.out.println ("partial board 1");
					sol1.printBoard();
					System.out.println ("partial board 2");
					sol2.printBoard();
					System.out.println();
					in.nextInt();
						
					
					//copy original pentominoes
					ArrayList <Pentomino> uniquePents = new ArrayList <Pentomino>();
					for (Pentomino pent : pentominoes)
						uniquePents.add (pent.clone());
					//get Pentominoes not used on either partially solved board
					uniquePents = sol1.unusedPents (uniquePents);
					uniquePents = sol2.unusedPents (uniquePents);
					//generate rotations
					ArrayList <ArrayList <Pentomino>> rotSetUniquePents = Pentomino.generateVersions (uniquePents);
					
					//reassemble board using partial solutions & solve
					Board assembledBoard = Board.assembleAuto (sol1, sol2, boardToSolve);
					solve (rotSetUniquePents, assembledBoard);
				}
			}
		}
					
					
	}

	public void solveMax (ArrayList <ArrayList <Pentomino>> pentominoRotationList, Board board)
	{
		for (int i=0; i<pentominoRotationList.size(); i++){
			for (int j=0; j<pentominoRotationList.get(i).size(); j++){
				mNodeCounter++;
				
				Board copyBoard = board.clone();
				Pentomino pentToPlace = pentominoRotationList.get(i).get(j);
				if (copyBoard.putLeftTop(pentToPlace)){
					//System.out.println("PentToPlace worked.");
					//copyBoard.printBoard();
					if (copyBoard.isFilled()) {
						checkDuplicateSolution (copyBoard);
					} else 
					{
						ArrayList<ArrayList<Pentomino>> copyPossibleRotations = new ArrayList <ArrayList <Pentomino>>();
						for (ArrayList <Pentomino> rotations : pentominoRotationList)
						{
							copyPossibleRotations.add (new ArrayList <Pentomino>());
							for (Pentomino rotPent : rotations)
									copyPossibleRotations.get (copyPossibleRotations.size() - 1).add (rotPent);
						}
						copyPossibleRotations.remove(i);
						solve(copyPossibleRotations, copyBoard);
					}		
				}
				//add board with maximum pents on it to "solutions"
				else
					checkDuplicateSolution (copyBoard);
			}
		}
	}
	
	public void solve(ArrayList <ArrayList <Pentomino>> pentominoRotationList, Board board){
		//Scanner in = new Scanner(System.in);
		//System.out.println("PentList: " + pentominoRotationList.size());
		for (int i=0; i<pentominoRotationList.size(); i++){
			for (int j=0; j<pentominoRotationList.get(i).size(); j++){
				mNodeCounter++;
				
				Board copyBoard = board.clone();
				Pentomino pentToPlace = pentominoRotationList.get(i).get(j);
				if (copyBoard.putLeftTop(pentToPlace)){
					//System.out.println("PentToPlace worked.");
					//copyBoard.printBoard();
					if (copyBoard.isFilled()) {
						if (checkDuplicateSolution (copyBoard))
							copyBoard.printBoard();
					} else {
						ArrayList<ArrayList<Pentomino>> copyPossibleRotations = new ArrayList <ArrayList <Pentomino>>();
						for (ArrayList <Pentomino> rotations : pentominoRotationList)
						{
							copyPossibleRotations.add (new ArrayList <Pentomino>());
							for (Pentomino rotPent : rotations)
									copyPossibleRotations.get (copyPossibleRotations.size() - 1).add (rotPent);
						}
						copyPossibleRotations.remove(i);
						solve(copyPossibleRotations, copyBoard);
					}		
				}		
			}
		}	
	}
	
	public ArrayList<Board> getSolutions() {
		return mListofSolutions;
	}
	
	public long getNodeCounter() {
		return mNodeCounter;
	}
	
	public int getNumberofSolutions() {
		return mListofSolutions.size();
	}
	
	public void displaySolution(int indexSolution){
		if (indexSolution < mListofSolutions.size()){
			PentoGui gui = new PentoGui (mListofSolutions.get(indexSolution));
			PentoGui.launch (gui);
		}
	}
	
	private boolean checkDuplicateSolution (Board solution)
	{
		Board solutionHorFlip = solution.clone();
		solutionHorFlip.flipHor();
		Board solutionVerFlip = solution.clone();
		solutionVerFlip.flipVer();
		Board solutionDoubleRotate = solution.clone();
		solutionDoubleRotate.rotate();
		solutionDoubleRotate.rotate();
		
		int indSolutionExist = 0;
		while (indSolutionExist < mListofSolutions.size())
		{
			if (mListofSolutions.get (indSolutionExist).equals (solution) ||
			mListofSolutions.get (indSolutionExist).equals (solutionHorFlip) ||
			mListofSolutions.get (indSolutionExist).equals (solutionVerFlip) ||
			mListofSolutions.get (indSolutionExist).equals (solutionDoubleRotate))
				return false;
			indSolutionExist++;
		}
		mListofSolutions.add(solution);
		return true;
	}
		
	//Members 
	/**List of Solved Boards*/
	private ArrayList<Board> mListofSolutions;
	
	/**Counter of explored Nodes*/
	private long mNodeCounter;
}
			
			
	