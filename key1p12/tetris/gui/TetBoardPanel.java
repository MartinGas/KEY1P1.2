package key1p12.tetris.gui;
//java API imports
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
//own imports
import key1p12.tetris.game.*;

public class TetBoardPanel extends JPanel 
{
	//Listener class belonging to board panel
	public class GameListener implements IGameListener
	{

		@Override
		public boolean isSensitive(GameAction event) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void performAction(Game state, GameAction event) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * constructor
	 * @param state state of game the panel should be initialized to
	 */
	public TetBoardPanel (Game state)
	{
		int width = state.getWidth();
		int height = state.getHeight();
		JPanel Board = new JPanel();
		Board.setLayout(new GridLayout(height, width));
	}
	
	/**
	 * Sets block matrix to current state of board
	 * @param state
	 */
	public void update (Game state)
	{
		columns = state.getWidth();
		rows = state.getHeight();
		for (i=0, i<columns, i++)
		{
			for (j=0,j<rows, j++)
			{
				(mBlockMat.get(i).get(j)).setState(state.getElementAndPent(i,j));
			}
		}
	}
	
	public void setup(Hashmap <Integer, Color> colMap)
	{
		Block blocks = new Block(colMap);
		//Add blocks to current panel
		this.add(blocks);
		ArrayList<Blocks> temp= new ArrayList<Blocks>();
		for (i=0, i<rows, i++)
		{
			temp.set(blocks);
			mBlockMat.add(temp);
		}
		//This is probably wrong. What Im looking to do is add "columns" amount 
		//of blocks to temp (not sure how to do that). And then add temp to mBlockMat
		//and repeat the process "rows" amount of times

	
	}
	
	
	/**
	 * Paint component
	 * g graphics object provided by caller
	 */
	public void paint (Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
	    Blocks.draw(g2);
	}
	
	//stores matrix of blocks representing board
	ArrayList <ArrayList <Block>> mBlockMat;
	int rows, columns;
}
