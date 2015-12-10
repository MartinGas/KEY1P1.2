package key1p12.tetris.game;
/**
 * We define columns first and rows second (x,y)
 */

public class MatrixHandler {

	// Constructor
	public MatrixHandler(int w, int h) {
		mStoreArray = new int[w][h];
	}

	public MatrixHandler(int[][] array) {
		mStoreArray = new int[array.length][array[0].length];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				mStoreArray[i][j] = array[i][j];
			}
		}
	}

	// Added for Project 1.2
	// Please rearrange the methods later and group them accordingly
	/**
	 * @param direc Which direction to look for the closest filled cell on the same row
	 * @param position The cell number (in position number format)
	 * @return Index of column the closest filled cell is in or -1 if there are none (borders = restrictions)
	 */
	public int getCloseCol(Direction direc, int position)
	{
		assert (direc == Direction.LEFT || direc == Direction.RIGHT);
		Position pos = new Position(position, this.getHeight());
		int x = pos.getX();
		int y = pos.getY();
		//Check columns to the left
		if (direc == Direction.LEFT) {
			for (int i=x; i>=0; i--) {
				if (mStoreArray[i][y] != 0) return i;
			}
		}
		//Check columns to the right
		if (direc == Direction.RIGHT) {
			for (int i=x; i<this.getWidth(); i++) {
				if (mStoreArray[i][y] != 0) return i;
			}
		}
		return -1;
	}

	/**
	 * @param direc Which direction to look for the closest filled cell on the same col
	 * @param position The cell coordinates which contain x and y
	 * @return Index of row the closest filled cell is in or -1 if there are none (borders = restrictions)
	 */
	public int getCloseRow(Direction direc, int position) 
	{
		assert(direc == Direction.UP || direc == Direction.DOWN);
		Position pos = Position.fromPosNum(position, getHeight());
		int x = pos.getX();
		int y = pos.getY();
		//Check rows above
		if (direc == Direction.UP) {
			for (int i=y; i>=0; i--) {
				if (mStoreArray[x][i] != 0) return i;
			}
		}
		//Check rows below
		if (direc == Direction.DOWN) {
			for (int i=y; i<this.getHeight(); i++) {
				if (mStoreArray[x][i] != 0) return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * @param rowIndex index of row to count
	 * @return Number of set cells in rowIndex
	 */
	public int countRow (int rowIndex)
	{
		int cnt = 0;
		for (int cCol = 0; cCol < getWidth(); ++cCol)
		{
			if (getCell(cCol, rowIndex) != 0)
				++cnt;
		}
		return cnt;
	}
	
	/**
	 * @param index of row to count
	 * @param startIndex  index to start counting at
	 * @param stopIndex index to stop counting at
	 * @return number of set cells in row in given interval
	 */
	public int countRowPart (int rowIndex, int startIndex, int stopIndex)
	{
		int cnt = 0;
		for (int cCol = startIndex; cCol <= stopIndex; ++cCol)
		{
			if (getCell(cCol, rowIndex) != 0)
				++cnt;
		}
		return cnt;
	}
	
	/**
	 * @param colIndex index of column to count
	 * @return Number of set cells in colIndex
	 */
	public int countCol (int colIndex)
	{
		int cnt = 0;
		for (int cRow = 0; cRow < getWidth(); ++cRow)
		{
			if (getCell (colIndex, cRow) != 0)
				++cnt;
		}
		return cnt;
	}
	
	/**
	 * @param colIndex index of column to count
	 * @param startIndex  index to start counting at
	 * @param stopIndex index to stop counting at
	 * @return Number of set cells in colIndex
	 */
	public int countColPart (int colIndex, int startIndex, int stopIndex)
	{
		int cnt = 0;
		for (int cRow = startIndex; cRow <= stopIndex; ++cRow)
		{
			if (getCell (colIndex, cRow) != 0)
				++cnt;
		}
		return cnt;
	}

	/**
	 * @param row Index of the row
	 * This method sets the value of each cell in the row to zero
	 */
	
	public void clearRow(int row) {
		for (int i=0; i<this.getWidth(); i++)
			mStoreArray[i][row] = 0;
	}
	
	/**
	 * @param index of row the line above will be moved to. Precondition: Row > 0
	 * This method copies the row from above to one row below
	 */
	public void moveRow(int row) {
		assert (row > 0);
		for (int j=0; j<this.getWidth(); j++) {
				mStoreArray[j][row] = mStoreArray[j][row-1];	
		}
	}

	/**
	 * @param row The index of the row that should be checked
	 * @return True if row is filled
	 */
	public boolean isRowFilled(int row){
		for (int i=0; i<mStoreArray.length; i++){
			if (mStoreArray[i][row] == 0) return false;
		}
		return true;
	}
	
	/**
	 * @param row The index of the column that should be checked
	 * @return True if column is filled
	 */
	public boolean isColFilled(int col){
		for (int i=0; i<mStoreArray[0].length; i++) {
			if (mStoreArray[col][i] == 0) return false;
		}
		return true;
	}
	
	/**
	 * @param row index of row to check
	 * @return True if every element in the row is 0
	 */
	public boolean isRowEmpty (int row)
	{
		for (int cCol = 0; cCol < this.getWidth(); cCol++)
		{
			if (this.getCell(cCol, row) != 0)
				return false;
		}
		return true;
	}
	
	/**
	 * @param column col index of column to check
	 * @return True if every element in the column is 0
	 */
	public boolean isColEmpty (int col)
	{
		for (int cRow = 0; cRow < this.getHeight(); cRow++)
		{
			if (this.getCell (col, cRow) != 0)
				return false;
		}
		return true;
	}
	// End for Added for Project 1.2

	public int getWidth() {
		return mStoreArray.length;
	}

	public int getHeight() {
		return mStoreArray[0].length;
	}

	public boolean checkArray() {
		return (mStoreArray != null);
	}

	/** Gets the value of the cell safely and returns -1 if not successful */
	public int getCell(int x, int y) {
		if (checkArray()) {
			if ((x >= 0 && x < this.getWidth()) && (y >= 0 && y < this.getHeight())) {
				return mStoreArray[x][y];
			}
		}
		return -1;
	}

	/**
	 * Checks if it can set the value of the cell and returns true/false
	 * depending on if it succeeded
	 */
	public boolean setCell(int x, int y, int value) {
		if (checkArray()) {
			if ((x >= 0 && x < this.getWidth()) && (y >= 0 && y < this.getHeight())) {
				mStoreArray[x][y] = value;
				return true;
			}
		}
		return false;
	}

	public MatrixHandler clone() {
		MatrixHandler cloned = new MatrixHandler(mStoreArray);
		return cloned;
	}

	public MatrixHandler createSubRegion(int x, int y, int newW, int newH) {
		if (x + newW <= this.getWidth() && y + newH <= this.getHeight()) {
			MatrixHandler subRegion = new MatrixHandler(newW, newH);
			for (int i = 0; i < newW; i++) {
				for (int j = 0; j < newH; j++) {
					subRegion.setCell(i, j, this.getCell(x + i, x + j));
				}
			}
			return subRegion;
		}
		MatrixHandler falseReturn = new MatrixHandler(0, 0);
		return falseReturn;
	}

	public boolean equals(MatrixHandler compare) {
		if ((this.getHeight() == compare.getHeight()) && (this.getWidth() == compare.getWidth())) {
			for (int i = 0; i < compare.getWidth(); i++) {
				for (int j = 0; j < compare.getHeight(); j++) {
					if (this.getCell(i, j) != compare.getCell(i, j)) {
						/**
						 * As long as the compared cells arent the same return
						 * false
						 */
						return false;
					}
				}
			}
			/** If all cells are the same return true */
			return true;
		} /** If the size of the matrixes are different return false */
		return false;
	}

	public void rotate() {
		this.transpose();
		this.flipVer();
	}

	public void print() {
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				System.out.print(" " + getCell(j, i) + " ");
			}
			System.out.println("");
		}
	}

	public void flipHor() {
		for (int i = 0; i < getWidth(); i++) {
			int middleIndex = (int) Math.floor((this.getHeight()) / 2);
			for (int j = 0; j <= middleIndex; j++) {
				int temp = this.mStoreArray[i][j];
				this.mStoreArray[i][j] = this.mStoreArray[i][getHeight() - 1 - j];
				this.mStoreArray[i][getHeight() - 1 - j] = temp;
			}
		}
	}

	public void flipVer() {
		int middleIndex = (int) Math.floor((this.getWidth()) / 2);
		for (int i = 0; i < middleIndex; i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				int temp = this.mStoreArray[i][j];
				this.mStoreArray[i][j] = this.mStoreArray[getWidth() - 1 - i][j];
				this.mStoreArray[getWidth() - 1 - i][j] = temp;
			}
		}
	}

	// Here starts the private section! Staff only!

	/**
	 * This method creates a new array temp into which the transposal of
	 * mStoreArray is stored
	 */
	private void transpose() {
		int[][] temp = new int[this.getWidth()][this.getHeight()];
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[i].length; j++) {
				temp[i][j] = this.mStoreArray[i][j];
			}
		}
		mStoreArray = new int[this.getHeight()][this.getWidth()];
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				mStoreArray[i][j] = temp[j][i];
			}
		}
	}

	// Data Members
	private int[][] mStoreArray;

}