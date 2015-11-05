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
	public boolean checkOverlap(Direction direc)
	{	
		//Get the x,y coordinate of the Pent (method still needs to be written in Coord class)
		Coord position = pentUsed.getCoor();		//Convert x,y to a int (method still needs to be written)
		int pos = getPosNum(x,y);
		//Moving to the right
		int rPos = pos + field.getHeight();
		if (direc == Direction.RIGHT)
		{
			if (pentFits(pentUsed, rPos))
			{
				return true;
			}
		}
		//Moving to the left
		int lPos = pos - field.getHeight();
		if (direc == Direction.LEFT)
		{
			if (pentFits(pentUsed, lPos))
			{
				return true;
			}
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
			//Get x-coordinate of the Pentomino
			int xCoor = pentUsed.getX();					
			int xCoorMax = board.getColumns();
			
			int yCoor = pentUsed.getY();
			int yCoorMax = board.getRows();
			//How many columns are to the right
			int columnCounter = xCoorMax-xCoor;
			//How may rows are there to the bottom of the board
			int rowCounter = yCoorMax-yCoor
			
			//How many times should we move the pent to the left
			if ((pentUsed.getHeight() - 1 - columnCounter)>0)
			{
				int moveLeft = (pentUsed.getHeight() - 1) - columnCounter;
			}else
			{ 
				int moveleft = 0;
			}
			if ((pentUsed.getWidth() - 1 - rowCounter)>0)
			{
			int moveUp = (pentUsed.getWidth() - 1) - rowCounter;
			}else 
			{
				int moveUp = 0;
			}
			//Rotate the pent now clockwise
			pentUsed.Rotate();
			//Where is the left-top of this rotated pent?
			pentUsed.getCoor();							
			//Converting x,y to a pos and also subtracting moveLeft from x
			int pos = getPosNum(x-moveLeft,y+moveUp);
			//Check if this rotated pent can fit
			if (pentFits(pentUsed, pos)
				{
					return true
				}
		}
		return false
	}
	
	/** @return True if game over, else false
	 * If any overlap, game over
	 */
	public boolean gameOverChecker()
	{
		//Get the x,y coordinate of the Pent (method still needs to be written in Coord class)
		Coord position = pentUsed.getCoor();
		//This method should return the cell where the left top of the petomino should be placed when it appears on the board
		int pos = getInitialPostion();
		//Checks if the pentomino can even be placed
		if (pentFits(pentUsed, pos))
		{
			return true;
		} else 
		{
			//Checks if rotated pent can be placed
			pentUsed.Rotate();
			if (pentFits(pentUsed, pos))
			{
				return true;
			}
			return false
		}
		return false;
	}
	//Check if pentomino in its current position can fit (is this even necessary?)
	int currentPos = getPosNum(x,y);
	if (pentFits(pentUsed, currentPos))
	{
		return true;
	} else
	{
		return false;
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
	
	
	
	//Contains a Board
	private Board field;
	
	//Contains the list of highscores
	//private HScore highScore;
	
	//Contains all the tetris pieces
	private ArrayList<Pentomino> blocks;
	
	//Contains the current Pentomino the game is using
	private Pentomino pentUsed;
	
	//Contains the (x,y) of a Pentomino
	//private Coord pentPosition;
	
	//Timer for the falling of tetris block
	//private Clock fallTimer;

	
}