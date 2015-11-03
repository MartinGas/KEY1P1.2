public class Position {
	
	//Constructor
	/** @param posNum gives position number that indicates a postion in postion number formats
	* @param rows the number of roms in the board
	* this constructour looks for the x and y using the rows and posNum**/
	public Position(int posNum, int rows){
		x = (int) (posNum/(rows-1));
		y = (posNum%rows -1)
	}
	
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/** moddify the x values **/
	public void addX(int m){
		x += m;
	}
	
	/** moddify the y values **/
	public void addY(int n){
		y += n;
	}	
	
	//getMethods
	public int getX(){
		return x;	
	}
	
	public int getY(){
		return y;
	}
	
	/** @return the posNum
	* this gets the postion number using x */
	public int getPosNum(){
		return (int) (x*rows) + 1 + y;
		
	}
	
	/** x differences between two different positions **/
	public int getDiffX(Position dx){
		int diff = this.x - dx.x;
	}
	
	/** y differences between two different positions **/
	public int getDiffY(Position dy){
		int diff = this.y - dy.y;
	}
	
	private int x;
	private int y;
}