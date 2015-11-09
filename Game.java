import java.util.ArrayList;

//Enumerated type that describes the direction pent moves
enum Direction
{
	UP, DOWN, LEFT, RIGHT
}

public class Game
{
	//Constructors
	public Game(Board initBoard, /*Hscore currentHScores,*/ ArrayList<Pentomino> pieces)
	{
		
	}
	
	/**
	//Play method
	/** @return Gives the highscore including the current game
	 * Plays the game
	
	public HScore play()
	{
		
	}
	*/
	
	//Checking Methods
	/**@param direc Takes the direction game wants to move the pent 
	 * @return True if move is valid, false if not
	 */
	public boolean checkMove(Direction direc)
	{	
		Pentomino pentClone = pentUsed.clone();	
		//Moving to the right
		if (direc == Direction.RIGHT)
		{
			//Get the int position of left top if moved to the right
			Position right = pentPosition.addX(1);
			int rPos = right.getPosNum(field.getHeight());
			//Check if it fits
			if (pentFits(pentClone, rPos))
			{
				return true;
			}
			return false;
		}
		//Moving to the left
		if (direc == Direction.LEFT)
		{
			//Get the int position of left top if moved to the left
			Position left = pentPosition.addX(-1);
			int lPos = left.getPosNum(field.getHeight());
			//Check if it fits
			if (pentFits(pentClone, lPos))
			{
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**@param direc Takes the direction game wants to rotate the pent 
	 * @return True if move is valid, false if not
	 */
	public boolean checkRotate(Direction direc)
	{
		if (direc == Direction.UP)
		{
			//Clone Pentomino and rotate clockwise
			Pentomino pentClone = pentUsed.clone();
			pentClone.turn();
			//Find the adjusted position to place left-top as an int
			Position adjustedPos = pentUsed.smartRotate();
			int newPos = adjustedPos.getPosNum(field.getHeight());
			//Check if this rotated pent can fit
			if (pentFits(pentClone, newPos)
				{
					return true;
				}
				return false;
		}
		return false;
	}
	
	/** @return True if game over, else false
	 * If any overlap, game over
	 */
	public boolean gameOverChecker()
	{
		//This method should return the cell where the left top of the petomino should be placed when it appears on the board
		int pos = pentPosition.getPosNum(field.getHeight());
		//Checks if the pentomino can even be placed
		if (pentFits(pentUsed, pos))
		{
			return false;
		}
		return true;
	}
	
	//Modifying Methods
	
	/**@return void
	 * Makes the pentomino fall by 1 block after a set amount of time has elapsed
	 */
	private void pentFaller()
	{
		
	}
	
	/** @param direc indicates the direction the pentomino should move
	 * @return void
	 * Moves the block in a direction
	 */
	private void move(Direction direc)
	{
		
	}
	
	/**@param direc indicates the direction the pentomino should turn
	 * @return void
	 * Turns the block in a direction
	 */
	private void turn(Direction direc)
	{
	
	}
	
	/**@return void
	 * Places the block in the bottom most row (with a block)
	 */
	private void fallPlace()
	{
		
	}
	
	/**@return void
	 * Chooses from 1 of the pentominos and places it at the top of the board
	 */
	//Maybe consider "smart placing" of initial pentomino
	private void pentPicker()
	{
		
	}
	
	
	/**@return void
	 * Checks for filled rows (bottom up) and removes them
	 */
	private void rowClearer()
	{
		
	}
	
	/**@return void
	 * After a row is cleared, replaces cleared rows with above rows.
	 * Does this until rows with all zeros is going to be moved
	 */
	private void rowMover()
	{
		
	}
	
	public int getRows()
	{
		int row = board.getHeight();
		return row;
	}
	
	public int getColumns()
	{
		int column = board.getWidth();
		return column;
	}
	
	/**@param int Takes left top cell of the matrix the pentomino is in (not the left top cell of the pentomino) as an int
	 * @return new postion of left top after 'smart' rotating
	 */
	public Position smartRotate()
	{
		//Get Pent Position
		int moveLeft = 0;
		int moveUp = 0;
		//Get x-coordinate of the Pentomino
		int xCoor = pentUsed.getX();					
		int xCoorMax = board.getColumns();
			
		int yCoor = pentUsed.getY();
		int yCoorMax = board.getRows();
		//How many columns are to the right
		int columnCounter = xCoorMax-xCoor;
		//How may rows are there to the bottom of the board
		int rowCounter = yCoorMax-yCoor;
		
		//How many times should we move the pent to the left
		if ((pentUsed.getHeight() - 1 - columnCounter)>0)
		{
			int moveLeft = (pentUsed.getHeight() - 1) - columnCounter;
		}
		
		//How many times should we move the pent up
		if ((pentUsed.getWidth() - 1 - rowCounter)>0)
		{
			int moveUp = (pentUsed.getWidth() - 1) - rowCounter;
		}
		
		//Get new position that adjust for overlap
		Position adjustedPent = pentPosition;
		adjustedPent.addX(-1*moveLeft);
		adjustedPent.addY(-1*moveUp);
		return adjustedPent;
	}
	
	//Contains a Board
	private Board field;
	
	//Contains the list of highscores
	//private HScore highScore;
	
	//Contains all the tetris pieces
	private ArrayList<Pentomino> blocks;
	
	//Contains the current Pentomino the game is using
	private Pentomino pentUsed;
	
	//Contains the (x,y) of a Pentomino
	private Position pentPosition;
	
	//Timer for the falling of tetris block
	private Clock fallTimer;

	
}