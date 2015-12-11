package key1p12.tetris.game;
//Java API imports

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game
{
	
	public static Direction getDirectionFromMove (GameMove m)
	{
		switch (m)
		{
		case MLEFT: return Direction.LEFT;
		case MRIGHT: return Direction.RIGHT;
		case TURN: return Direction.RIGHT;
		case FALL: return Direction.DOWN;
		default: return Direction.NONE;
		}
	}
	
	public static boolean DEBUG = false;
	public static long mFALL_TIME = 500, mPLACE_TIME = 50;
	
	public static class SimulGame implements Cloneable
	{
		@SuppressWarnings("serial")
		public class SimulSetupExc extends CloneNotSupportedException
		{
			public SimulSetupExc() { super(); }
			
			public SimulSetupExc (String description)  { super (description); }
			
			public SimulSetupExc (String description, Throwable cause) 
			{ 	super (description); 
				super.initCause(cause);
			}
			
			public String toString()
			{
				return "simulation setup exception " + super.toString();
			}
		}
		
		public SimulGame (Game source)
		{
			Board clonedBoard = source.field.clone();
			mGame = new Game (clonedBoard, source.blocks, source.mPlayer, source.mHighScore.clone());
			mGame.pentPosition = source.pentPosition.clone();
			mGame.pentUsed = source.pentUsed.clone();
			
			assert (mGame.mListeners.isEmpty());
		}
		
		public SimulGame clone ()
		{
			return new SimulGame (mGame);
		}
		
		public ArrayList <Pentomino> getBlocks()
		{
			return (ArrayList <Pentomino>)mGame.blocks.clone();
		}
		
		public Board getBoard()
		{
			return mGame.field;
		}
		
		public Pentomino getUsedPent()
		{
			return mGame.pentUsed;
		}
		
		public Score getCurrScore()
		{
			return mGame.getCurrScore();
		}
		
		public Position getPentPosition()
		{
			return mGame.pentPosition.clone();
		}
		
		public int getLastScoreDifference()
		{
			return mDeltaScore;
		}
		
		public String toString()
		{
			String s = new String ("");
			for (int cRow = 0; cRow < mGame.getHeight(); ++cRow)
			{
				
				for (int cCol = 0; cCol < mGame.getWidth(); ++cCol)
				{
					s = s + mGame.getElementAndPent(cCol, cRow) + " ";
				}
				s = s + "\n";
			}
			return s;
		}
		
		public int getElement (int x, int y)
		{
			return mGame.getElement (x, y);
		}
		
		public int getElementAndPent (int x, int y)
		{
			return mGame.getElementAndPent (x, y);
		}
		
		public int getHeight()
		{
			return mGame.getHeight();
		}
		
		public int getWidth()
		{
			return mGame.getWidth();
		}
		
		/**
		 * @param rowIndex index of row to count
		 * @return number of set cells in row
		 */
		public int countRow (int rowIndex)
		{
			return mGame.countRow (rowIndex);
		}
		
		/**
		 * @param rowIndex index of row to count
		 * @param startIndex index to start counting at
		 * @param stopIndex index to stop counting at
		 * @return number of set cells in row
		 */
		public int countRowPart (int rowIndex, int startIndex, int stopIndex)
		{
			return mGame.countRowPart (rowIndex, startIndex, stopIndex);
		}
		
		/**
		 * @param colIndex index of column to count
		 * @return number of set cells in column
		 */
		public int countCol (int colIndex)
		{
			return mGame.countCol (colIndex);
		}
		
		/**
		 * @param colIndex index of column to count
		 * @param startIndex index to start counting at
		 * @param stopIndex index to stop counting at
		 * @return number of set cells in column
		 */
		public int countColPart (int colIndex, int startIndex, int stopIndex)
		{
			return mGame.countColPart(colIndex, startIndex, stopIndex);
		}
		
		public int getFilledHeight (int col)
		{
			return mGame.getFilledHeight (col);
		}
		
		public boolean isGameMovePossible (GameMove m)
		{
			switch (m)
			{
			case MLEFT: case MRIGHT:
				return checkMove (Game.getDirectionFromMove(m));
			case TURN: 
				return checkRotate (Game.getDirectionFromMove (m));
			default: return true;
			}
		}
		
		public boolean checkMove (Direction d)
		{
			try { return mGame.checkMove(d);}
			catch (Exception e) { return false; }
		}
		
		public boolean checkRotate (Direction d)
		{
			return mGame.checkRotate(d);
		}
		
		public boolean isGameOver()
		{
			return mGame.gameOverChecker();
		}
		
		public void move (Direction d)
		{
			mGame.move(d);
			try {  }
			catch (Exception e) {}
		}
		
		public void pentRotate()
		{
			mGame.turn(Direction.RIGHT);
		}
		
		public void fallPlace()
		{
			long scoreBefore = mGame.getCurrScore().getScore();
			mGame.fallPlace();
			int mDeltaScore = (int) (mGame.getCurrScore().getScore() - scoreBefore);
		}
		
		public void pentPicker()
		{
			mGame.pentPicker();
		}
		
		public void pickPent (int index)
		{
			assert (index >= 0 && index < mGame.blocks.size());
			mGame.pentUsed = mGame.blocks.get (index).clone();
			mGame.pentPosition = new Position ((mGame.getWidth() - mGame.pentUsed.getWidth()) / 2, 0);
		}
		
		private Game mGame;
		private int mDeltaScore;
	}
	
	//Constructors
	public Game(Board initBoard, ArrayList<Pentomino> pieces, Player player, HScore currentHScores)
	{
		mFallTimer = new Timer (mFALL_TIME);
		mPlaceTimer = new Timer (mPLACE_TIME);
		
		field = initBoard.clone();
		//look for nicer solution
		blocks = (ArrayList<Pentomino>)pieces.clone();
		mPlayer = player;
		mHighScore = currentHScores.clone();
		mListeners = new HashMap <GameAction, ArrayList <IGameListener>>();
		mIsOver = false;
		mIsPaused = true;
		pentPicker();
	}
	
	//Play method
	/**@return Gives the highscore including the current game*/
	//Plays the game
	
	public void play() 
	{
		if (DEBUG)
			System.out.println ("next turn");
		if (isGamePaused())
		{
			if (DEBUG)
				System.out.println ("Resuming game");
			resume();
		}
		
		if (!isGameOver())
		{
			if (DEBUG)
				System.out.println ("Pent faller");
			pentFaller();
			if (DEBUG)
			System.out.println ("react to player input");
			GameMove playerInput = mPlayer.getMove();
			switch (playerInput)
			{
			case MLEFT:	move (Direction.LEFT);
			break;
			case MRIGHT: move (Direction.RIGHT);
			break;
			case TURN: turn(Direction.RIGHT);
			break;
			case FALL: fallPlace();
			break;
			default: assert (false);
				break;
			}
			
			//if pentomino is at the bottom
			if (!checkMove (Direction.DOWN))
			{
				//if place timer should be activated
				if (mPlaceTimer.isStopped())
				{
					mPlaceTimer.start();
					if (DEBUG)
						System.out.println ("Start place timer " + mPlaceTimer.getElapsedTime());
				}
				//if place timer has elapsed
				if (mPlaceTimer.hasElapsed())
					placer();
			}
			//if place timer is running but pentomino can fall
			else if (!mPlaceTimer.isStopped())
				mPlaceTimer.stop();
		}
		
	}
	
	public void debugPrint()
	{
		for (int cRow = 0; cRow < getHeight(); ++cRow)
		{
			for (int cCol = 0; cCol< getWidth(); ++cCol)
				System.out.print (getElementAndPent(cCol, cRow));
			System.out.println();
		}
	}
	
	/**
	 * @return High score object if game is over, null otherwise
	 */
	public HScore getHighScoreList()
	{
		if (mIsOver)
			return mHighScore;
		return null;
	}
	
	/**
	 * @return score object of highest score
	 */
	public Score getHighScore()
	{
		return mHighScore.getScore(0);
	}
	
	/**
	 * @return score object of current score
	 */
	public Score getCurrScore()
	{
		return new Score (mHighScore.getScore(), mHighScore.getName());
	}
	
	/**
	 * @return width of the board
	 */
	public int getWidth()
	{
		return field.getWidth();
	}
	
	/**
	 * @return height of the board
	 */
	public int getHeight()
	{
		return field.getHeight();
	}
	
	/**
	 * @param col column
	 * @return index of the last unfilled cell (counting from top) (-1 if there is none)
	 */
	public int getFilledHeight (int col)
	{
		assert (col >= 0 && col < getWidth());
		int closeIndex = field.getCloseRow(Direction.DOWN, new Position (col, 0).getPosNum(getHeight()));
		if (closeIndex == -1)
			return (getHeight() - 1);
		else 
			return (closeIndex - 1);
	}
	
	/**
	 * @param rowIndex index of row to count
	 * @return number of set cells in row
	 */
	public int countRow (int rowIndex)
	{
		return field.countRow (rowIndex);
	}
	
	/**
	 * @param rowIndex index of row to count
	 * @param startIndex index to start counting at
	 * @param stopIndex index to stop counting at
	 * @return number of set cells in row
	 */
	public int countRowPart (int rowIndex, int startIndex, int stopIndex)
	{
		return field.countRowPart (rowIndex, startIndex, stopIndex);
	}
	
	/**
	 * @param colIndex index of column to count
	 * @return number of set cells in column
	 */
	public int countCol (int colIndex)
	{
		return field.countCol (colIndex);
	}
	
	/**
	 * @param colIndex index of column to count
	 * @param startIndex index to start counting at
	 * @param stopIndex index to stop counting at
	 * @return number of set cells in column
	 */
	public int countColPart (int colIndex, int startIndex, int stopIndex)
	{
		return field.countColPart(colIndex, startIndex, stopIndex);
	}
	
	/**
	 * @param x column of cell
	 * @param y row of cell
	 * @return content of specified cell
	 */
	public int getElement (int x, int y)
	{
		assert (x < getWidth() && y < getHeight());
		return field.getElement(x, y);
	}
	
	/**
	 * Behaves as if pentUsed was placed at current position on the board
	 * @param x column of cell
	 * @param y row of cell
	 * @return content of specified cell
	 */
	public int getElementAndPent (int x, int y)
	{
		assert (x < getWidth() && y < getHeight());
		if (x >= pentPosition.getX() && x < pentPosition.getX() + pentUsed.getWidth() &&
			y >= pentPosition.getY() && y < pentPosition.getY() + pentUsed.getHeight())
		{
			int pentVal = pentUsed.getElement(x - pentPosition.getX(), y - pentPosition.getY());
			if (pentVal != 0)
				return pentVal;
		}
		return field.getElement(x, y);
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
			int rPos = right.getPosNum(field.getHeight());
			//Check if it fits
			if (field.pentFits(pentClone, rPos))
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
			int lPos = left.getPosNum(field.getHeight());
			//Check if it fits
			if (field.pentFits(pentClone, lPos))
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
			/*if (below.getY() >= field.getHeight() && 
				field.pentFits(pentClone, below.getPosNum (field.getHeight())))*/
			if (pentPosition.getY() + pentUsed.getHeight() <= field.getHeight() && 
					field.pentFits(pentClone, below.getPosNum (getHeight())))
				return true;
		}
		return false;
	}
	
	/**@param direc Takes the direction game wants to rotate the pent 
	 * @return True if move is valid, false if not
	 */
	public boolean checkRotate(Direction direc)
	{
		if (direc == Direction.RIGHT)
		{
			//Clone Pentomino and rotate clockwise
			Pentomino pentClone = pentUsed.clone();
			pentClone.rotate();
			
			//Check if this rotated pent can fit
			if (field.pentFits (pentClone, pentPosition.getPosNum(field.getHeight())))
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
		//This method should return the cell where the left top of the pentomino should be placed when it appears on the board
		int pos = pentPosition.getPosNum(field.getHeight());
		//Checks if the pentomino can even be placed
		if (field.pentFits(pentUsed, pos))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * @return cached boolean indicating whether game is over
	 */
	public boolean isGameOver()
	{
		return mIsOver;
	}
	
	
	public boolean isGamePaused()
	{
		return mIsPaused;
	}
	
	/**
	 * freezes current state of the game
	 */
	public void pause()
	{
		mIsPaused = true;
		mFallTimer.stop();
		mPlaceTimer.stop();
	}
	
	/**
	 * Adds game listener to this game object
	 * @param listener listener to add
	 */
	public void addListener (IGameListener listener)
	{
		for (GameAction a : GameAction.values())
		{
			if (listener.isSensitive (a))
			{
				if (!mListeners.containsKey (a))
					mListeners.put (a, new ArrayList <IGameListener>());
				mListeners.get(a).add (listener);
			}
		}
	}
	
	
	private void resume()
	{
		mIsPaused = false;
		mFallTimer.start();
		mPlaceTimer.start();
		notifyListeners (GameAction.RESUME);
	}
	
	/**@return void
	 * Makes the pentomino fall by 1 block after a set amount of time has elapsed
	 */
	private void pentFaller() 
	{
		if (mFallTimer.hasElapsed())
		{
			if (this.checkMove (Direction.DOWN))
			{
				mFallTimer.reset();
				this.move (Direction.DOWN);
			}
		}
	}
	
	/** @param direc indicates the direction the pentomino should move
	 * @return void
	 * Moves the block in a direction
	 */
	private void move(Direction direc) 
	{
		assert (direc == Direction.LEFT || direc == Direction.RIGHT || direc == Direction.DOWN);

		if (direc == Direction.LEFT && checkMove(Direction.LEFT))
			pentPosition.addX(-1);
		else if (direc == Direction.RIGHT && checkMove(Direction.RIGHT))
			pentPosition.addX(1);
		else if (direc == Direction.DOWN && checkMove (Direction.DOWN))
			pentPosition.addY(1);
		notifyListeners (GameAction.MOVE);
		if (DEBUG)
			System.out.println ("Pent moved in " + direc);
	}
	
	/**@param direc indicates the direction the pentomino should move
	 * @return void
	 * Turns the block in a direction
	 */
	private void turn (Direction direc) 
	{
		assert (direc == Direction.RIGHT);
		//temporary storage for pentPosition
		Position tempPos = pentPosition;
		//pentPosition to be changed
		pentPosition = smartRotate();
		
		if (checkRotate(direc))
		{
			//If check is true, then rotate pentomino
			this.pentUsed.rotate();
		}
		else 
		{
			pentPosition = tempPos;
		}
		notifyListeners (GameAction.TURN);
		if (DEBUG)
			System.out.println ("Pent turned");
	}
	
	/**@return void
	 * Places the block in the bottom most row (with a block)
	 */
	private void fallPlace() 
	{
		while (this.checkMove(Direction.DOWN))
			this.move(Direction.DOWN);
		if (DEBUG)
			System.out.println ("Pent dropped");
		placer();
	}
	
	/**@return void
	 * Chooses from 1 of the pentominos and places it at the top of the board
	 */
	//Maybe consider "smart placing" of initial pentomino
	public void pentPicker() 
	{
		mFallTimer.reset();
		Random random = new Random ();
		int index = random.nextInt(blocks.size());
		pentUsed = blocks.get(index);
		pentPosition = new Position((int)Math.ceil(field.getWidth() / 2), 0);
		notifyListeners (GameAction.PICK);
		if (DEBUG)
			System.out.println ("new pent picked");
	}
	
	
	/**@return void
	 * Checks for filled rows (bottom up) and removes them
	 */
	private void rowClearer() 
	{
		int cRowsCleared = 0;
		int cRow = field.getHeight() - 1;
		while (cRow >= 0 && !field.isRowEmpty(cRow))
		{
			while (field.isRowFilled (cRow))
			{
				cRowsCleared++;
				field.clearRow (cRow);
				rowMover (cRow);
			}
			--cRow;
		}
		if (cRowsCleared > 0)
		{
			mHighScore.increaseScore (cRowsCleared);
			notifyListeners (GameAction.CLEAR);
		}
	}
	
	/**@return void
	 * After a row is cleared, replaces cleared rows with above rows.
	 * Does this until rows with all zeros is going to be moved
	 */
	private void rowMover(int index)
	{
		int lastLineClear = index;
		if (index > 0)
 		{
			do
			{
				field.moveRow (index);
				index--;
				lastLineClear = index;
			}while (index > 0 && field.isRowEmpty (index) == false);
 		}
		field.clearRow(lastLineClear);
	}
	
	/**
	 * Notifies all listeners sensitive to event
	 * @param event event that occurred
	 */
	private void notifyListeners (GameAction event) 
	{
		if (mListeners.containsKey(event))
		{
			for (IGameListener listener : mListeners.get(event))
				listener.performAction (this, event);
		}
	}
	
	/**
	 * Sets state of game to over
	 */
	private void setGameOver()
	{
		mIsOver = true;
		mHighScore.lock();
	}
	
	private void placer()
	{
		if (DEBUG)
			System.out.println ("Pent is placed");
		//place the pentomino on the board first
		int pos = pentPosition.getPosNum(field.getHeight());
		field.placePent(pentUsed, pos);
		rowClearer();
		pentPicker();
		if (gameOverChecker())
			setGameOver();
		mPlaceTimer.reset();
		mPlaceTimer.stop();
	}
	
	/** @return position of left top of current pentomino
	 */
	public Position pentUsedPos()
	{
		Position currentPent = new Position (pentPosition.getX(), pentPosition.getY());
		return currentPent;
	}
	
	/** @return new postion of left top after 'smart' rotating
	 */ 
	private Position smartRotate()
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
	
	
	
	//Contains a Board
	private Board field;
	
	//holds reference to player playing game
	private Player mPlayer;
	
	//Contains the list of highscores
	private HScore mHighScore;
	
	//Contains all the tetris pieces
	private ArrayList<Pentomino> blocks;
	
	//Contains the current Pentomino the game is using
	//made public for testing purposes
	public Pentomino pentUsed;
	
	//Contains the (x,y) of a Pentomino
	private Position pentPosition;
	
	//timer indicating whether pentomino should fall
	private Timer mFallTimer;
	
	//timer indicating when pentomino should be placed permanently after touching bottom
	private Timer mPlaceTimer;
	
	//maps game actions to related listeners added to the game
	private HashMap <GameAction, ArrayList <IGameListener>> mListeners;
	
	//tells whether game is paused
	private boolean mIsPaused;
	
	//stores whether game is over
	private boolean mIsOver;
}
