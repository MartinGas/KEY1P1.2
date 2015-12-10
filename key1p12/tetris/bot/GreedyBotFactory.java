package key1p12.tetris.bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import key1p12.tetris.game.IGameListener;
import key1p12.tetris.game.Player;
import key1p12.tetris.game.PlayerFactory;

public class GreedyBotFactory extends PlayerFactory 
{
	
	public GreedyBotFactory (ArrayList <PerfMeasure> pMeasures, ArrayList <Double> weights, File nameBase)
	{
		mPMeasures = pMeasures;
		mPMWeights = weights;
		mNameBase = nameBase;
	}

	@Override
	public Player producePlayer() throws PlayerNotConstructedExc
	{
		try
		{
			return new GreedyBot(mPMeasures, mPMWeights, mNameBase);
		}
		catch (FileNotFoundException fnfe)
		{
			throw new PlayerNotConstructedExc ("Bad name base file");
		}
	}
	
	@Override
	public ArrayList <IGameListener> produceListeners (Player p) 
	{
		GreedyBot gb = (GreedyBot) p;
		ArrayList <IGameListener> listeners = new ArrayList<IGameListener>();
		listeners.add (gb.new PickListener());
		return listeners;
	}
	
	private ArrayList <PerfMeasure> mPMeasures;
	private ArrayList <Double> mPMWeights;
	private File mNameBase;
}
