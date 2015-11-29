package key1p12.tetris.game;

public interface Player
{
	/**
	 * @return player's name
	 */
	public String getName();
	
	/**
	 * @return player's next move
	 */
	public GameMove getMove();
}