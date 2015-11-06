import java.util.ArrayList;
import java.util.Random;

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
	
	
	/*Play method
	//@return Gives the highscore including the current game
	//Plays the game
	
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
		return false;
	}
	
	/**@param direc Takes the direction game wants to rotate the pent 
	 * @return True if move is valid, false if not
	 */
	public boolean checkRotate(Direction direc)
	{
		return true;
	}
	
	/** @return True if game over, else false
	 * If any overlap, game over
	 */
	public boolean gameOverChecker()
	{
		return false;
	}

	/**@return void
	 * Makes the pentomino fall by 1 block after a set amount of time has elapsed
	 */
	private void pentFaller()
	{
		//A check involving the timer needs to be added

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
		/* discuss: necessity?
		else if (direc == Direction.DOWN){
			//pentFaller x2-faster
		}*/
	}
	
	/**@param direc indicates the direction the pentomino should move
	 * @return void
	 * Turns the block in a direction
	 */
	private void turn(Direction direc)
	{
		if (direc == Direction.LEFT && checkRotate(Direction.LEFT)==true){
			this.pentUsed.rotate();
			this.pentUsed.rotate();
			this.pentUsed.rotate();
		}
		else if (direc == Direction.RIGHT && checkRotate(Direction.RIGHT)==true){
			this.pentUsed.rotate();
		}
		
	}
	
	/**@return void
	 * Places the block in the bottom most row (with a block)
	 */
	private void fallPlace()
	{
		//not desired:
		if (field.pentFits(this.pentUsed, pentPosition.getPosNum(this.getHeight()))) 
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
			blocks.add (pent.clone());
		}
		/*what this method does:
		check whether pentomino fits in current position
		iterate through cells of pentomino and set value of an uninitialized data field mMatrix 
		to value of pentomino at respective position
		
		what this method should do
		while pentomino may fall down by one row make it fall down
		once the respective bottom has been reached, place the pentomino on the Board ('field')*/
		
	}
	
	/**@return void
	 * Chooses from 1 of the pentominos and places it at the top of the board
	 */
	//Maybe consider "smart placing" of initial pentomino
	private void pentPicker()
	{
		Random random = new Random();
		int index = random.nextInt(blocks.size());
		pentUsed = blocks.get(index);
		pentPosition = new Position(field.getWidth()/2,0);
		
	}
	
	
	/**@return void
	 * Checks for filled rows (bottom up) and removes them
	 */
	private void rowClearer()
	{
		for (int i=field.getHeight()-1; i >= 0 ;i--){
			while (field.isRowFilled(i) == true){
				field.clearRow(i);
				//not exactly correct: you want to move every row above down by one row, not just one
				while (field.isRowEmpty(i) == false)
					rowMover(i);
			}
		}	
	}
	
	/**@return void
	 * After a row is cleared, replaces cleared rows with above rows.
	 * Does this until rows with all zeros is going to be moved
	 */
	//good idea to write a helper method!
	private void rowMover(int index)
	{
		/*suggestion: transfer functionality
		 * int iMove = index;
		 * while (iMove > 0 && field.isRowFilled (iMove - 1))
		 * 		field.moveRows(iMove);
		 */
		field.moveRows (index);
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
	//private Clock fallTimer;

	private MatrixHandler mMatrix;

}
