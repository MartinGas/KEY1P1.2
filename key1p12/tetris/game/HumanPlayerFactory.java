package key1p12.tetris.game;

import java.util.ArrayList;

public class HumanPlayerFactory extends PlayerFactory
{
	public HumanPlayerFactory (String name)
	{
		mName = name;
	}
	
	public Player producePlayer() 
	{
		return new HumanPlayer(mName);
	}
	
	private String mName;

	@Override
	public ArrayList<IGameListener> produceListeners(Player p) 
	{
		return new ArrayList <IGameListener>();
	}
}
