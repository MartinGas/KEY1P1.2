package key1p12.tetris.gui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;

//models a block on the tetris board
public class Block extends JComponent 
{
	private final int DEFAULT = 0;
	/**
	 * constructor
	 * @param sharedColMap reference to color map shared among related Block instances
	 */
	public Block (HashMap <Integer, Color> sharedColMap)
	{
		mColMap = sharedColMap;
		mColor = (Color)sharedColMap.get(DEFAULT);
	}
	
	/**
	 * @return current color of block
	 */
	public Color getState()
	{
		return mColor;
	}
	
	/**
	 * @return Color matching given key
	 */
	public Color getColor(Integer key)
	{
		//get value from map if key is defined
		if (mColMap.containsKey (key))
			return mColMap.get (key);
		//generate new value if key is undefined
		Random genRgb = new Random();
		//generate random color: r, g, b values as multiples of 15 (15 * 17 = 255)
		Color randCol = null;
		do
		{
			//magic numbers => hard to understand & improve!			
			randCol = new Color (genRgb.nextInt (18) * 15, genRgb.nextInt (18) * 15, genRgb.nextInt (18) * 15);
		} while (mColMap.containsValue (randCol));
					
		mColMap.put (key, randCol);
		return randCol;
	}
	
	/**
	 * Sets the color of the block to the color corresponding to the cellNum in mColMap
	 * @param cellNum content of cell on board
	 */
	public void setState (int cellNum)
	{
		mColor = mColMap.get(cellNum);
	}
	
	/**
	 * paint the block
	 * @param g graphics object provided by caller
	 */
	public void paint (Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(mColor);
		g2.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	//color map: number => color
	private HashMap <Integer, Color> mColMap;
	//currently used color
	private Color mColor;
}
