package key1p12.tetris.game;

import java.util.*;

public class GameSetup 
{
	public static class GameSetupNotCompleteExc extends IllegalStateException
	{
		public GameSetupNotCompleteExc() 
		{
			super();
		}
		
		public GameSetupNotCompleteExc (String message) 
		{
			super (message);
		}
	}
	
	public GameSetup() 
	{
		mMessage = new String();
	}
	
	public GameSetup (String message)
	{
		mMessage = message;
	}
	
	public void loadBoard (Board board)
	{
		mBoard = board.clone();
	}
	
	public void loadPlayerFactory (PlayerFactory playerFax)
	{
		mPlayerFax = playerFax;
	}
	
	public void loadBlocks (ArrayList <Pentomino> blocks)
	{
		mBlocks = (ArrayList<Pentomino>)blocks.clone();
	}
	
	public void loadHighScore (HScore highScore)
	{
		mHighScore = highScore.clone();
	}
	
	public Game construct() throws GameSetupNotCompleteExc
	{
		try
		{
			Player p = mPlayerFax.producePlayer();
			Game g = new Game (mBoard, mBlocks, p, mHighScore);
			ArrayList <IGameListener> gameListeners = mPlayerFax.produceListeners (p);
			return g;
		}
		catch (NullPointerException npe)
		{
			String message = "Mising objects for setup: ";
			if (mBoard == null)
				message = message + " board ";
			if (mBlocks == null)
				message = message + " list of pentominoes ";
			if (mPlayerFax == null)
				message = message + " player factory ";
			if (mHighScore == null)
				message = message + " high score object ";
			throw new GameSetupNotCompleteExc (message);
		}
	}
	
	public String getMessage()
	{
		return mMessage;
	}
	
	private Board mBoard;
	private PlayerFactory mPlayerFax;
	private ArrayList <Pentomino> mBlocks;
	private HScore mHighScore;
	private String mMessage;
}
