import java.util.Random;

public abstract class Bot implements Player
{
	
	public Bot (PerfMeasure pMeasure, String[] nameDataBase)
	{
		mName = new String();
		Random randIName = new Random();
		for (int cNames = 0; cNames < mNAMES_ADD; ++cNames)
			mName += nameDataBase[randIName.nextInt (nameDataBase.length)] + " ";
	}
	
	public String getName()
	{
		return mName;
	}
	
	public abstract Direction getMove();
	
	//public abstract <return type> analyze (<parameter>);
	
	private static final int mNAMES_ADD = 2;
	private PerfMeasure mPMeasure;
	private String mName;
}