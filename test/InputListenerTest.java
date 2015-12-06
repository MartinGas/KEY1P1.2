package test;

import javax.swing.*;

import key1p12.tetris.game.*;
import key1p12.tetris.gui.*;

public class InputListenerTest extends JFrame
{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InputListenerTest t = new InputListenerTest();

	}
	
	public InputListenerTest() 
	{
		setSize (400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		HumanPlayer hp = new HumanPlayer("bla");
		
		addKeyListener(hp.new InputListener());
		
		setVisible (true);
		while (!hasFocus())
			requestFocus();
		
	}

}
