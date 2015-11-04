public class Position {
	
	//Constructor
	/** @param posNum gives position number that indicates a postion in postion number formats
	* @param rows the number of roms in the board
	* this constructour looks for the x and y using the rows and posNum**/
	public Position(int posNum, int rows){
		x = Math.ceil (posNum / rows) - 1;
		y = (posNum%rows -1)
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
		int diff = this.x - dx.x;
	}
	
	/** @return y differences between parameter position and position stored in this object **/
	public int getDiffY(Position dy){
		int diff = this.y - dy.y;
	}
	
	private int x;
	private int y;
}