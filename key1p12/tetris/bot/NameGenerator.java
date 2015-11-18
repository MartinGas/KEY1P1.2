package key1p12.tetris.bot;
import java.io.*;
import java.util.*;

public class NameGenerator
{
	/**
	 * reads name data base and stores it
	 * @param genList name data base file
	 * @throws FileNotFoundException
	 */
	public NameGenerator (File genList) throws FileNotFoundException
	{
		assert (genList != null && genList.canRead());
		read (genList);
		
	}
	
	/**
	 * @return randomly generated name from data base
	 */
	public String getName()
	{
		Random rNum = new Random();
		String rName = new String();
		for (ArrayList <String> col : mNameMat)
			rName += col.get (rNum.nextInt (col.size())) + " ";
		return rName;
	}
	
	//does the actual reading of the file
	//and is responsible for storing 
	private void read (File genList) throws FileNotFoundException
	{
		mNameMat.clear();
		Scanner inName = new Scanner (genList);
		while (inName.hasNextLine())
		{
			String in = inName.nextLine();
			if (in.equals("=NEWCOL="))
					mNameMat.add(new ArrayList <String>());
			else
				mNameMat.get(mNameMat.size() - 1).add(in);
		}
		inName.close();
	}
	
	private ArrayList <ArrayList <String>> mNameMat;
}