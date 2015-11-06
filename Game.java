import java.util.ArrayList;

//Enumerated type that describes the direction pent moves
enum Direction
{
	UP, DOWN, LEFT, RIGHT
}

public class Game
{
	//suggestion: use classes implementing interface to set, store and modify timer
	//add to pending changes file, once it exists
	public final long mFALL_TIME = 1500;
	//Constructors
	public Game(Board initBoard, /*Hscore currentHScores,*/ ArrayList<Pentomino> pieces)
	{
		fallTimer = new Timer (mFALL_TIME);
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
	
	//Modifying Methods
	
	/**@return void
	 * Makes the pentomino fall by 1 block after a set amount of time has elapsed
	 */
	private void pentFaller()
	{
		if (fallTimer.hasElapsed() && this.checkMove (Direction.DOWN))
			pentPosition.addY (1);
	}
	
	/** @param direc indicates the direction the pentomino should move
	 * @return void
	 * Moves the block in a direction
	 */
	private void move(Direction direc)
	{
		
	}
	
	/**@param direc indicates the direction the pentomino should move
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
		fallTimer.reset();
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
	private Timer fallTimer;

	
}