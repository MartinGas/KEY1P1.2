package key1p12.tetris.game;

import java.awt.event.*;


public class HumanPlayer implements Player
{

	public class InputListener extends KeyAdapter
	{
		public void KeyPressed (KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				mMove = GameMove.MLEFT;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				mMove = GameMove.MRIGHT;
	        }
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				mMove = GameMove.TURN;
	        }
			if (e.getKeyCode() == KeyEvent.VK_SPACE){
				mMove = GameMove.FALL; 
			}
		}
	}
	
	public HumanPlayer (String name)
	{
		mName = name;
	}
        
	public GameMove getMove(){
		GameMove temp = mMove;
		mMove = null;
		return temp;
    }
	
	public String getName(){
		return mName;
	}
	
	private String mName;
	private GameMove mMove;
}	