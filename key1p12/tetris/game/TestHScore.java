
import java.io.File;
import java.io.IOException;




public class TestHScore{
	
	public static void main(String[] args) throws IOException
	{
		File file = new File("HighScores.txt");
		if (!file.exists())
			file.createNewFile();
		
		ExponentialScore scmp = new ExponentialScore (2, 1, 1);
		HScore testScore = new HScore(file, "Maxim", scmp);
		testScore.increaseScore (5);
		testScore.writeToFile ();
		
	}
}