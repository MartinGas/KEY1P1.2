import java.io.File;
import java.io.IOException;

import key1p12.tetris.game;

public class TestHScore{
	
	public static void main(String[] args) throws IOException
	{
		File file = new File("HighScores.txt");
		if (!file.exists())
			file.createNewFile();
		
		HScore testScore = new HScore(file, "Maxim");
		testScore.increaseScore (5);
		testScore.writeToFile (file);
		
	}
}