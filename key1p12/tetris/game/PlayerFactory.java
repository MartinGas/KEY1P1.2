package key1p12.tetris.game;

import java.util.ArrayList;

public abstract class PlayerFactory 
{	
	/**
	 * @author martin
	 * throws in player cannot be constructed by factory
	 */
	public static class PlayerNotConstructedExc extends IllegalArgumentException 
	{
		/**
		 * empty constructor, no message
		 */
		public PlayerNotConstructedExc() {super(); }
		
		/**
		 * @param message message to store
		 */
		public PlayerNotConstructedExc(String message) 
		{
			super (message);
		}
	}
	
	/**
	 * @return player objects produced by factory
	 * @throws PlayerNotConstructedExc
	 */
	public abstract Player producePlayer() throws PlayerNotConstructedExc;
	
	/**
	 * Generates listeners the player needs to communicate with the game
	 * @param p player needing listeners; precondition: type of player must be obtainable by this factory
	 * @return array list of game listeners
	 */
	public abstract ArrayList <IGameListener> produceListeners (Player p);
}
