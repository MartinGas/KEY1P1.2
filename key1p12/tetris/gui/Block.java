package key1p12.tetris.gui;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;

//models a block on the tetris board
public class Block extends JComponent 
{
	/**
	 * constructor
	 * @param sharedColMap reference to color map shared among related Block instances
	 */
	public Block (HashMap <Integer, Color> sharedColMap)
	{
		
	}
	
	/**
	 * @return current color of block
	 */
	public Color getState()
	{
		return mColor;
	}
	
	/**
	 * Sets the color of the block to the color corresponding to the cellNum in mColMap
	 * @param cellNum content of cell on board
	 */
	public void setState (int cellNum)
	{
		
	}
	
	/**
	 * paint the block
	 * @param g graphics object provided by caller
	 */
	public void paint (Graphics g)
	{
		
	}
	
	//color map: number => color
	private HashMap <Integer, Color> mColMap;
	//currently used color
	private Color mColor;
}
