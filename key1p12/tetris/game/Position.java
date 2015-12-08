package key1p12.tetris.game;
public class Position implements Cloneable
{
	
	/**factory method for constructing Position using a position number
	 * @param posNum position number to be converted to coordinates
	 * @param rows rows of matrix posNum is referring to
	 * @return Position object holding coordinates obtained by conversion
	 */
	public static Position fromPosNum (int posNum, int rows)
	{
		return new Position ((int)Math.ceil ((posNum - 1) / rows), (int)(posNum - 1) % rows);
	}
	
	/** @param x x coordinate of the position to be constructed
	* @param y y coordinate of the position to be constructed**/
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/** @param m value to be added to x coordinate **/
	public void addX(int m){
		x += m;
	}
	
	/** @param value to be added to y coordinate **/
	public void addY(int n){
		y += n;
	}	
	
	//getMethods
	
	public Position clone()
	{
		return new Position (x, y);
	}
	
	/** @return x coordinate of position**/
	public int getX(){
		return x;	
	}
	
	/** @return y coordinate of position**/
	public int getY(){
		return y;
	}
	
	/** @param rows number of rows in matrix the position number refers to
	* @return the posNum
	* this gets the postion number using x */
	public int getPosNum (int rows){
		return (int) (x*rows) + y + 1;
		
	}
	
	/** @return x difference between parameter position and position stored in this object **/
	public int getDiffX(Position dx){
		 return (this.x - dx.x);
	}
	
	/** @return y differences between parameter position and position stored in this object **/
	public int getDiffY(Position dy){
		return (this.y - dy.y);
	}
	
	private int x;
	private int y;
}