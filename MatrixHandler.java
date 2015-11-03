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
		assert (direc == LEFT || direc == RIGHT);
		Position pos = new Position(position, this.getHeight());
		int x = pos.getX();
		int y = pos.getY();
		//Check columns to the left
		if (direc == LEFT) {
			for (int i=x; i>=0; i--) {
				if (mStoreArray[i][y] != 0) return i;
			}
		}
		//Check columns to the right
		if (direc == RIGHT) {
			for (int i=x; i<this.getWidth(); i++) {
				if (mStoreArray[i][y] != 0) return i;
			}
		}
	}

	/**
	 * @param direc Which direction to look for the closest filled cell on the same col
	 * @param position The cell coordinates which contain x and y
	 * @return Index of row the closest filled cell is in or -1 if there are none (borders = restrictions)
	 */
	public int getCloseRow(Direction direc, int position) 
	{
		assert(direc == UP || direc == DOWN);
		Position pos = new Position(position, this.getHeight());
		int x = pos.getX();
		int y = pos.getY();
		//Check rows above
		if (direc == UP) {
			for (int i=y; i>=0; i--) {
				if (mStoreArray[x][i] != 0) return i;
			}
		}
		//Check rows below
		if (direc == DOWN) {
			for (int i=y; i<this.getHeight(); i++) {
				if (mStoreArray[x][i] != 0) return i;
			}
		}
	}

	/**
	 * @param column
	 *            Index of column
	 * @return True if column is completely filled
	 */
	public boolean isColumnFilled(int column) {
		for (int i = 0; i < this.getHeight(); i++) {
			if (mStoreArray[column][i] == 0)
				return false;
		}
		return true;
	}

	/**
	 * @param row Index of row
	 * @return True if row is completely filled
	 */
	public boolean isRowFilled(int row) {
		for (int i = 0; i < this.getWidth(); i++) {
			if (mStoreArray[i][row] == 0)
				return false;
		}
		return true;
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