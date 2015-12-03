package key1p12.tetris.gui;
//java API imports
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
//own imports
import key1p12.tetris.game.*;

public class TetBoardPanel extends JPanel 
{
	public static Color DEFAULT_GRID_COLOR = Color.magenta;
	
	//Listener class belonging to board panel
	public class GameListener implements IGameListener
	{
		/**
		 * @return to which game action events the listener should respond
		 */
		public boolean isSensitive(GameAction event) 
		{
			if (event == GameAction.CLEAR || event == GameAction.FALL || event == GameAction.MOVE || event == GameAction.TURN || event == GameAction.PICK)
				return true;
			return false;
		}

		/**
		 * @para state: current state of the game
		 * @event game action event occurring
		 * updates the panel
		 */
		public void performAction(Game state, GameAction event) {
			update (state);
			repaint();
		}
		
	}
	
	/**
	 * constructor
	 * @param state state of game the panel should be initialized to
	 */
	public TetBoardPanel (Game state)
	{
		columns = state.getWidth();
		rows = state.getHeight();
		mBlockMat = new ArrayList <ArrayList <Block>>();
		mGridColor = DEFAULT_GRID_COLOR;
		setLayout (new GridLayout (rows, columns));
	}
	
	public TetBoardPanel (Board b)
	{
		columns = b.getWidth();
		rows = b.getHeight();
		mBlockMat = new ArrayList <ArrayList <Block>>();
		mGridColor = DEFAULT_GRID_COLOR;
		setLayout (new GridLayout (rows, columns));
	}
	
	/**
	 * Sets block matrix to current state of board
	 * @param state
	 */
	public void update (Game state)
	{
		for (int cRow = 0; cRow < rows; cRow++)
		{
			for (int cCol = 0; cCol < columns; cCol++)
			{
				(mBlockMat.get (cRow).get(cCol)).setState (state.getElementAndPent(cCol, cRow));
			}
		}
	}
	
	public void update (Board b)
	{
		for (int cRow = 0; cRow < rows; cRow++)
		{
			for (int cCol = 0; cCol < columns; cCol++)
			{
				(mBlockMat.get (cRow).get (cCol)).setState (b.getElement (cCol, cRow));
			}
		}
	}
	
	/**
	 * Creates necessary objects depending on colMap
	 * @param colMap color map shared among all instances
	 */
	public void setup(HashMap <Integer, Color> colMap)
	{
		//Add blocks to current panel
		for (int cRow = 0; cRow < rows; cRow++)
		{
			ArrayList <Block> temp = new ArrayList <Block>();
			for (int cCol = 0; cCol < columns; cCol++)
			{
				Block block = new Block (colMap);
				temp.add(block);
				add (block);
			}
			mBlockMat.add(temp);
		}
	}
	
	/**
	 * Paint component
	 * g graphics object provided by caller
	 */
	public void paint (Graphics g)
	{
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
	    int distW = getWidth() / columns, distH = getHeight() / rows;
	    
		for (int cRow = 0; cRow < rows; cRow++)
		{
			g2.setColor(mGridColor);
			g2.drawLine (cRow * distW, 0, cRow * distW, getHeight());
			g2.drawLine (0, cRow * distH, getWidth(), cRow * distH);
		}
	}
	
	/**
	 * @param newGridColor new grid color
	 * makes panel use newGridColor as color for drawing the grid
	 */
	public void setGridColor (Color newGridColor)
	{
		mGridColor = newGridColor;
	}
	
	
	//stores matrix of blocks representing board
	private ArrayList <ArrayList <Block>> mBlockMat;
	private Color mGridColor;
	private int rows, columns;
}
