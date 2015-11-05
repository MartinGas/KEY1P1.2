import java.util.ArrayList;
import.java.util.Random;

	//Enumerated type that describes the direction pent moves 
	enum Direction
	{
	UP, DOWN, LEFT, RIGHT
	}

public class Game
{

	/**@return void
	 * Makes the pentomino fall by 1 block after a set amount of time has elapsed
	 */
	private void pentFaller()
	{
		//A check involving the timer needs to be added

	}
	
	/** @param direc indicates the direction the pentomino should move
	 * @return void
	 * Moves the block in a direction
	 */
	//works way easier: only adjust the position of the pentomino
	//use assertion to prevent a method from feeding direc == UP (consult the book)
	private void move(Direction direc)
	{
		assert direc == LEFT || direc == RIGHT || direc == DOWN;

		if (direc == LEFT && checkMove(LEFT)==true){
			coord.addX(-1);
			
		}
		if (direc == RIGHT && checkMove(RIGHT)==true){
			coord.addX(1)
			
		}
		if (direc == DOWN){
			//pentFaller x2-faster
		}
	}
	
	/**@param direc indicates the direction the pentomino should move
	 * @return void
	 * Turns the block in a direction
	 */
	//good job ;)
	private void turn(Direction direc)
	{
		if (direc == LEFT){
			this.item.rotate();
			this.item.rotate();
			this.item.rotate();
		}
		if (direc == RIGHT){
			this.item.rotate();
		}
		
	}
	
	/**@return void
	 * Places the block in the bottom most row (with a block)
	 */
	private void fallPlace()
	{
		if (nBoard.pentFits(this.item, coord)) 
		{
			for (int i=0; i< this.item.getWidth(); i++) 
			{
				for (int j=0; j< this.item.getHeight(); j++) 
				{
					if (this.item.getElement(i,j) != 0) 
					{
						mMatrix.setCell(i + coord.getX(), j + coord.getY(), this.item.getElement(i,j));
					}
				}
			}
			mListPents.add (pent.clone());
		}
	}
	
	/**@return void
	 * Chooses from 1 of the pentominos and places it at the top of the board
	 */
	//Maybe consider "smart placing" of initial pentomino
	//good job ;)
	private void pentPicker()
	{
		Random random = new Random();
		int index = random.nextInt(pentList.size());
		Pentomino item = pentList.get(index);
		
	}
	
	
	/**@return void
	 * Checks for filled rows (bottom up) and removes them
	 */
	//works but full rows are just being assigned value of 0
// row mover needs to be called
	private void rowClearer()
	{
		for (int i=nBoard.getHeight()-1; i=0;i--){
			while (nBoard.isRowFilled(i) == true){
				nBoard.clearRow(i);
				rowMover();
			}
		}	
	}
	
	/**@return void
	 * After a row is cleared, replaces cleared rows with above rows.
	 * Does this until rows with all zeros is going to be moved
	 */
	//good idea to write a helper method!
	private void rowMover()
	{
		nBoard.moveRows()
	}
	private MatrixHandler mMatrix;
	private Board nBoard;
	private Position coord;