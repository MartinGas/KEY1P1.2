package key1p12.tetris.game;

public interface IGameListener 
{
	/**
	 * @param event action to check sensitivity for
	 * @return true if listener should react to given action
	 */
	public boolean isSensitive (GameAction event);
	
	/**
	 * Performs action associated with event and state
	 * @param state state of the game
	 * @param event event that occurred in the game
	 */
	public void performAction (Game.SimulGame state, GameAction event);
}
