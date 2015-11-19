import java.util.ArrayList;
import java.util.Random;

//Enumerated type that describes the direction pent moves 
enum Direction
{
UP, DOWN, LEFT, RIGHT, NONE
}

public class Game implements Cloneable
{
	//suggestion: use classes implementing interface to set, store and modify timer
	//add to pending changes file, once it exists
	public final long mFALL_TIME = 1500;
	//Constructors
	public Game(Board initBoard, /*Hscore currentHScores,*/ ArrayList<Pentomino> pieces)
	{
		fallTimer = new Timer (mFALL_TIME);
		
		field = initBoard.clone();
		//look for nicer solution
		blocks = (ArrayList<Pentomino>)pieces.clone();
		//highScore = currentHScores;
		pentPicker();
	}
	
	
	//Play method
	/**@return Gives the highscore including the current game*/
	//Plays the game
	
	public /*HScore*/void play()

	{
		//generate random number for simulated user input
		Random genMove = new Random();
		//stores wheter game is over
		boolean isOver = false;
		//repeats while game is not over
		while (!isOver)
		{
			//if pentomino is at the bottom
			if (!checkMove (Direction.DOWN))
			{
				rowClearer();
				pentPicker();
				if (gameOverChecker())
					isOver = true;
			}
			if (!isOver)
			{
				pentFaller();
				//get user input once player class is done
				//select random move instead
				int randMove = genMove.nextInt(3);
				Direction randDirec = null;
				switch (randMove)
				{
				case 0: randDirec = Direction.LEFT;
				break;
				case 1: randDirec = Direction.RIGHT;
				break;
				case 2: randDirec = Direction.DOWN;
				break;
				}
				assert (randDirec != null);
				//apply move
				if (randMove < 2 && checkMove (randDirec))
					move (randDirec);
				else if (randMove == 2)
					fallPlace();
					
			}
		}
		
		System.out.println ("Oh no, the game is over!");
	}
	
	//Checking Methods
	/**@param direc Takes the direction game wants to move the pent 
	 * @return True if move is valid, false if not
	 */
	public boolean checkMove(Direction direc)
	{	
		assert (direc == Direction.LEFT || direc == Direction.RIGHT || direc == Direction.DOWN);
		Pentomino pentClone = pentUsed.clone();	
		//Moving to the right
		if (direc == Direction.RIGHT)
		{
			//Get the int position of left top if moved to the right
			Position right = new Position (pentPosition.getX(), pentPosition.getY());
			right.addX (1);
			int rPos = right.getPosNum(copyField.getHeight());
			//Check if it fits
			if (copyField.pentFits(pentClone, rPos))
			{
				return true;
			}
			return false;
		}
		//Moving to the left
		if (direc == Direction.LEFT)
		{
			//Get the int position of left top if moved to the left
			Position left = new Position (pentPosition.getX(), pentPosition.getY());
			left.addX (-1);
			int lPos = left.getPosNum(copyField.getHeight());
			//Check if it fits
			if (copyField.pentFits(pentClone, lPos))
			{
				return true;
			}
			return false;
		}
		if (direc == Direction.DOWN)
		{
			//Get the int position of left top if moved down
			Position down = new Position (pentPosition.getX(), pentPosition.getY());
			down.addY (1);
			int dPos = down.getPosNum(copyField.getHeight());
			//Check if it fits
			if (copyField.pentFits(pentClone, dPos))
			{
				return true;
			}
			return false;
		}
		
		//Moving downwards
		if (direc == Direction.DOWN)
		{
			//Get int position if pent moved downwards
			Position below = new Position (pentPosition.getX(), pentPosition.getY());
			below.addY (1);
			if (below.getY() >= field.getHeight() && 
				field.pentFits(pentClone, below.getPosNum (field.getHeight())))
				return true;
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
			pentClone.rotate();
			
			//Check if this rotated pent can fit
			if (field.pentFits (pentClone, pentPosition.getPosNum(field.getHeight())))
			{
				System.out.println("rotated");
				return true;
			}
			System.out.println("not rotated");
			return false;
		}
		return false;
	}
	
	/** @return True if game over, else false
	 * If any overlap, game over
	 */
	public boolean gameOverChecker()
	{
		//This method should return the cell where the left top of the pentomino should be placed when it appears on the board
		int pos = pentPosition.getPosNum(field.getHeight());
		//Checks if the pentomino can even be placed
		if (field.pentFits(pentUsed, pos))
		{
			//System.out.println("Can still be placed");
			return false;
		}
		//System.out.println("Can't be placed");
		return true;
	}

	/**@return void
	 * Makes the pentomino fall by 1 block after a set amount of time has elapsed
	 */
	private void pentFaller()
	{
		if (fallTimer.hasElapsed())
		{
			if (this.checkMove (Direction.DOWN))
				this.move (Direction.DOWN);
		}
	}
	
	/** @param direc indicates the direction the pentomino should move
	 * @return void
	 * Moves the block in a direction
	 */
	private void move(Direction direc)
	{
		assert direc == Direction.LEFT || direc == Direction.RIGHT || direc == Direction.DOWN;

		if (direc == Direction.LEFT && checkMove(Direction.LEFT)==true)
			pentPosition.addX(-1);
			
		else if (direc == Direction.RIGHT && checkMove(Direction.RIGHT)==true)
			pentPosition.addX(1);
		else if (direc == Direction.DOWN && checkMove(Direction.DOWN)==true){
			pentPosition.addY(1);
		}
	}
	
	/**@param direc indicates the direction the pentomino should move
	 * @return void
	 * Turns the block in a direction
	 */
	private void turn (Direction direc)
	{
		//temporary storage for pentPosition
		Position tempPos = pentPosition;
		//pentPosition to be changed
		pentPosition = smartRotate();
		
		if (direc == Direction.UP && checkRotate(Direction.UP))
		{
			//If check is true, then rotate pentomino
			this.pentUsed.rotate();
		}
		else 
		{
			pentPosition = tempPos;
		}
	}
	
	/**@return void
	 * Places the block in the bottom most row (with a block)
	 */
	private void fallPlace()
	{
		//not desired:
		
		/*if (field.pentFits(this.pentUsed, pentPosition.getPosNum(field.getHeight()))) 
		{
			for (int i=0; i< this.pentUsed.getWidth(); i++) 
			{
				for (int j=0; j< this.pentUsed.getHeight(); j++) 
				{
					if (this.pentUsed.getElement(i,j) != 0) 
					{
						mMatrix.setCell(i + pentPosition.getX(), j + pentPosition.getY(), this.pentUsed.getElement(i,j));
					}
				}
			}
			blocks.add (pentUsed.clone());
		}*/
		/*what this method does:
		check whether pentomino fits in current position
		iterate through cells of pentomino and set value of an uninitialized data field mMatrix 
		to value of pentomino at respective position
		what this method should do:
		while pentomino may fall down by one row make it fall down
		once the respective bottom has been reached, place the pentomino on the Board ('field')*/
		
		
		//make pentomino fall down until it reaches the bottom
		while (this.checkMove(Direction.DOWN))
			this.move(Direction.DOWN);
		//place pentomino on the board
		field.placePent(pentUsed, pentPosition.getPosNum(field.getHeight()));
	}
	
	/**@return void
	 * Chooses from 1 of the pentominos and places it at the top of the board
	 */
	//Maybe consider "smart placing" of initial pentomino
	public void pentPicker()
	{
		//Saves board in previous state
		copyField = field.clone();
		fallTimer.reset();
		Random random = new Random();
		int index = random.nextInt(blocks.size());
		pentUsed = blocks.get(11);
		pentPosition = new Position((int)Math.ceil(field.getWidth() / 2),0);
	}
	
	
	/**@return void
	 * Checks for filled rows (bottom up) and removes them
	 */
	private void rowClearer()
	{
		int cRow = field.getHeight() - 1;
		while (cRow >= 0 && !field.isRowEmpty(cRow))
		{
			while (field.isRowFilled (cRow))
			{
				field.clearRow (cRow);
				rowMover (cRow);
			}
			--cRow;
		}
	}
	
	/**@return void
	 * After a row is cleared, replaces cleared rows with above rows.
	 * Does this until rows with all zeros is going to be moved
	 */
	private void rowMover(int index)
	{
		int lastLineClear = index;
		while (index > 0 && field.isRowEmpty (index) == false)
		{
			field.moveRow (index);
			index--;
			lastLineClear = index;
		}
		field.clearRow(lastLineClear);
	}
	
	public void mutateMove(Direction direc)
	{
			this.move(direc);
			//this.mutatePlace();
	}
	
	public void mutateRotate(Direction direc)
	{
			//turn pentomino
			this.turn(direc);
			
			//Change the position of turned pentomino
			//pentPosition = this.smartRotate();
			//this.mutatePlace();
		
	}
	
	public void mutatePicker()
	{
		this.pentPicker();
		gameOverChecker();
		//mutatePlace();
	}
	
	public void mutatePlace()
	{
		int pos = pentPosition.getPosNum(field.getHeight());
		//Remove copyField later after testing
		Board temp = copyField.clone();
		temp.placePent(pentUsed, pos);
		field = temp;
	}
	
	/** @return Postion of left top of current pentomino
	 */
	public Position pentUsedPos()
	{
		Position currentPent = new Position (pentPosition.getX(), pentPosition.getY());
		return currentPent;
	}
	
	/** @return new postion of left top after 'smart' rotating
	 */ 
	public Position smartRotate()
	{
		int moveLeft = 0;
		int moveUp = 0;
		//Get x-coordinate of the Pentomino
		int xCoor = pentPosition.getX();
		int xCoorMax = field.getWidth();
		//Get y-coordinate of the Pentomino	
		int yCoor = pentPosition.getY();
		int yCoorMax = field.getHeight();
		//How many columns are to the right
		int columnCounter = xCoorMax-xCoor-1;
		//How may rows are there to the bottom of the board
		int rowCounter = yCoorMax-yCoor-1;
		//How many times should we move the pent to the left
		if ((pentUsed.getHeight() - 1 - columnCounter)>0)
		{
			moveLeft = (pentUsed.getHeight() - 1) - columnCounter;
		}
		//How many times should we move the pent up
		if ((pentUsed.getWidth() - 1 - rowCounter)>0)
		{
			moveUp = (pentUsed.getWidth() - 1) - rowCounter;
		}
		//Get new position that adjusts for overlap
		Position adjustedPent = pentPosition;
		adjustedPent.addX(-1*moveLeft);
		adjustedPent.addY(-1*moveUp);
		return adjustedPent;
	}
	
	
	public int getCellValue(Position pos)
	{
		int x = pos.getX();
		int y = pos.getY();
		int value = pentUsed.getElement(x, y);
		return value;
	}
	
	//Cloning method
	public Object clone() throws CloneNotSupportedException
	{
		Game cloned = (Game)super.clone();
		return cloned;
	}
	
	//Testing method
	public void printGame()
	{
		field.printBoard();
	}
	
	public void fall()
	{
		pentFaller();
	}
	public void rowClearMove()
	{
		rowClearer();
		rowMover(4);
	}
	public void fallPlacer()
	{
		this.fallPlace();
	}
	
	//Contains a Board
	private Board field;
	
	//Contains a copy of Field WITHOUT pentUsed placed
	private Board copyField;
	
	//Contains the list of highscores
	//private HScore highScore;
	
	//Contains all the tetris pieces
	private ArrayList<Pentomino> blocks;
	
	//Contains the current Pentomino the game is using
	//made public for testing purposes
	public Pentomino pentUsed;
	
	//Contains the (x,y) of a Pentomino
	private Position pentPosition;
	
	//Timer for the falling of tetris block
	private Timer fallTimer;

	private MatrixHandler mMatrix;

}
