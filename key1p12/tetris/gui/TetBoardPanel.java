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
		//initialize data members
		//set layout
	}
	
	/**
	 * Sets block matrix to current state of board
	 * @param state
	 */
	public void update (Game state)
	{
		
	}
	
	/**
	 * Paint component
	 * g graphics object provided by caller
	 */
	public void paint (Graphics g)
	{
		
	}
	
	//stores matrix of blocks representing board
	ArrayList <ArrayList <Block>> mBlockMat;
}
