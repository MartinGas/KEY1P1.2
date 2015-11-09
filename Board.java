import java.util.*;

/** We ll still stick to columns first and rows second for the counting */
public class Board {
	
	public static Board assembleAuto (Board part1, Board part2, Board original)
	{
		if (original.getWidth() - part1.getWidth() - part2.getWidth() >= 0 && original.getWidth() - part1.getWidth() - part2.getWidth() <= 1)
			return assembleBoardsW (part1, part2, original.getWidth());
		else if (original.getHeight() - part1.getHeight() - part2.getHeight() >= 0 && original.getHeight() - part1.getHeight() - part2.getHeight() <= 1)
			return assembleBoardsH (part1, part2, original.getHeight());
		return new Board (0, 0);
	}
	
	/**puts two seperate Boards together, on top of each other => along the width*/
	public static Board assembleBoardsW (Board part1, Board part2, int desiredW)
	{
		//if added widths == desiredW (+1 optional) and heights are equal
		if (desiredW - part1.getWidth() - part2.getWidth() >= 0 && desiredW - part1.getWidth() - part2.getWidth() <= 1 && part1.getHeight() == part2.getHeight())
		{
			int startPart2 = desiredW - part2.getWidth();
			//matrix of assembled board
			MatrixHandler newBoardMatrix = new MatrixHandler (desiredW, part1.getHeight());
			//generate clone from part2 to ensure that no values outside method call are changed
			part2 = part2.clone();
			//rotates part2 by 180 degrees
			part2.rotate();
			part2.rotate();
			//copies values from parts to new matrix
			for (int cCol = 0; cCol < desiredW; cCol++)
			{
				for (int cRow = 0; cRow < part1.getHeight(); cRow++)
				{
					if (cCol < part1.getWidth())
						newBoardMatrix.setCell (cCol, cRow, part1.getElement (cCol, cRow));
					else if (cCol >= startPart2)
						newBoardMatrix.setCell (cCol, cRow, part2.getElement (cCol - startPart2, cRow));
				}
			}
			//returns assembled board on success
			return new Board (newBoardMatrix);			
		}
		//returns crap if failure
		return new Board (0, 0);
	}
	/**puts two seperate Boards together, side by side => along the height*/
	public static Board assembleBoardsH (Board part1, Board part2, int desiredH)
	{
		if (desiredH - part1.getHeight() - part2.getHeight() >= 0 && desiredH - part1.getHeight() - part2.getHeight() <= 1 && part1.getWidth() == part2.getWidth())
		{
			int startPart2 = desiredH - part2.getHeight();
			MatrixHandler newBoardMatrix = new MatrixHandler (part1.getWidth(), desiredH);
			part2 = part2.clone();
			part2.rotate();
			part2.rotate();
			for (int cCol = 0; cCol < part1.getWidth(); cCol++)
			{
				for (int cRow = 0; cRow < desiredH; cRow++)
				{
					if (cRow < part1.getHeight())
						newBoardMatrix.setCell (cCol, cRow, part1.getElement (cCol, cRow));
					else if (cRow >= startPart2)
						newBoardMatrix.setCell (cCol, cRow, part2.getElement (cCol, cRow - startPart2));
				}
			}
			return new Board (newBoardMatrix);			
		}
		
		return new Board (0, 0);
	}
	
	
	/**checks if pentominoes can be placed on board given the size*/
	public static boolean sizeCheck(int w, int h){
		return (h >= 3 && w >= 3 && (h * w == 60));
	}
	
	/** The constant number of Pentominos */	
	public static final int mNO_PENTS = 12;
	/** The constant number of cells in the board */
	public static final int mAREA = 60;
			
	
	/** Takes width and height and creates an object of type MatrixHandler and the arraylist of Pentominos */
	public Board(int w, int h) {
		mMatrix = new MatrixHandler(w, h);
		mListPents = new ArrayList<Pentomino>();
	}
	
	/** Takes an array of type MatrixHandler, clones it and creates the arraylist of Pentominos */
	public Board(MatrixHandler array) {
		mMatrix = array.clone();
		mListPents = new ArrayList<Pentomino>();
	}
	
	/** Takes the board, clones it and creates the arraylist of Pentominos */
	public Board(Board copyBoard) {
		mMatrix = copyBoard.mMatrix.clone();
		mListPents = new ArrayList<Pentomino>();
		
		for (int i=0; i < copyBoard.mListPents.size(); i++) {
			mListPents.add(copyBoard.mListPents.get(i));
		}
	}
	
	/** Returns the width of mMatrix */
	public int getWidth() {
		return mMatrix.getWidth();
	}
	
	/** Returns the height of mMatrix */
	public int getHeight() {
		return mMatrix.getHeight();
	}
	
	/** This method checks if there are empty cells left on the Board and returns false if that's the case */
	public boolean isFilled() {
		for (int i=0; i<this.getWidth(); i++) {
			for (int j=0; j<this.getHeight(); j++) {
				//changed mMatrix.getElement() to this.getElement()
				if (this.getElement(i,j) == 0) return false;
			}
		}
		return true;
	}
	/** This method puts the Pentomino on the LeftTop and returns true if succeeded */
	public boolean putLeftTop(Pentomino pent) {
		int position = this.getLeftTop();
		int x = getXCoor(position);
		int y = getYCoor(position);
		int xpent = pent.getXCoor(pent.getLeftTop());
		int ypent = pent.getYCoor(pent.getLeftTop());
		//System.out.println("Pent.getlefttop: " + pent.getLeftTop());
		int newX = x - xpent;
		int newY = y - ypent;
		//System.out.println("X: " + x + " Y: " + y );
		//System.out.println(pent.getLeftTop());
		//System.out.println("newX: " + newX + " newY: " + newY);
		if (newX < 0 || newY < 0) 
		{
			//System.out.println ("Failed adjusted coordinate check");
			return false;
		}
		
		if (this.pentFits(pent, position)) {
			//System.out.println("Pent is now being placed.");
			for (int i=0; i< pent.getWidth(); i++) {
				for (int j=0; j< pent.getHeight(); j++) {
					if (pent.getElement(i,j) != 0) {
						mMatrix.setCell(i + newX, j + newY, pent.getElement(i,j));
					}
				}
			}
			mListPents.add (pent.clone());
			return isSolvable(position, pent.getWidth(), pent.getHeight());
		}
		//System.out.println ("Failed fit check!");
		return false;
	}
	
	/**returns wether there is at least one pentomino placed on both boards*/
	public boolean pentsPlacedIntersect (Board compare)
	{
		for (Pentomino iThis : this.mListPents)
		{
			for (Pentomino iCompare : compare.mListPents)
			{
				if (iThis.getId() == iCompare.getId())
					return true;
			}
		}
		return false;
	}
	
	/** This method counts the empty cells and returns whether there is a blocked empty cell */
	public boolean isSolvable(int position, int lastPentW, int lastPentH) {
		int x = getXCoor(position);
		int y = getYCoor(position);
		//Checks if region is at the boundaries
		boolean rightBorder = false;
		boolean bottomBorder = false;
		if (x + lastPentW == this.getWidth()) rightBorder = true;
		if (y + lastPentH == this.getHeight()) bottomBorder = true;
		if (x>0) {
			x--;
			lastPentW++;
		}
		if (y>0) {
			y--;
			lastPentH++;
		}
		MatrixHandler region = mMatrix.createSubRegion(x, y, lastPentW, lastPentH);
		for (int i=0; i<lastPentW; i++) {
			for (int j=0; j<lastPentH; j++) {
				if (region.getCell(i, j) == 0) 
					if (!checkFill(region, i, j, rightBorder, bottomBorder)) 
					{
						//System.out.println ("Solvable check fails at " + (x + i) + "|" + (y + j));
						return false;
					}
			}
		}
		return true;
	}
	/** Checks recursively the region around the LeftTop empty cell if there are any 'bridges' to the empty cells on the right side of the board. */
	public boolean checkFill(MatrixHandler region, int x, int y, boolean rightB, boolean bottomB) {
		//System.out.println ("Recursive check at " + x + "|" + y);
		if ((x == region.getWidth() - 1 && !rightB) || (y == region.getHeight() - 1 && !bottomB))
		{
			if (region.getCell (x, y) == 0 || region.getCell (x, y) == mNO_PENTS + 1) {
				region.setCell(x, y, mNO_PENTS +2);
				return true;
			}
			else if (region.getCell (x, y) == mNO_PENTS + 2)
				return true;
		}
		
		/** Sets the value of the cell that is currently checked to 13 so that the recursion knows that this cell has already been checked */
		region.setCell(x, y, mNO_PENTS + 1);
		
		for (int moveX = -1; moveX <= 1; moveX +=2)
		{		
			int newX = x + moveX;
			if (region.getCell(newX, y) == 0) {
				if (checkFill(region, newX, y, rightB, bottomB)) {
					region.setCell(newX, y, mNO_PENTS +2);
					return true;
				}
			}
			/** Returns true if cell has already been checked positively */
			if (region.getCell(newX, y) == mNO_PENTS+2) return true;
		}
		for (int moveY = -1; moveY <= 1; moveY += 2)
		{
			int newY = y + moveY;
			if (region.getCell(x, newY) == 0)
			{
				if (checkFill(region, x, newY, rightB, bottomB)) {
					region.setCell(x, newY, mNO_PENTS +2);
					return true;
				}
			}
			/** Returns true if cell has already been checked positively */
			if (region.getCell(x, newY) == mNO_PENTS+2) return true;
		}
		return false;
	}
	
	
	/** Checks if the pentomino fits into LeftTop and returns boolean */
	public boolean pentFits(Pentomino pent, int position) {	
		//System.out.println ("entering pentfits");
		//Gets X and Y coordinates from the exact position
		int x = getXCoor(position);
		int y = getYCoor(position);
		int xpent = -1;
		int ypent = -1;
		/** newX and newY are adjusted to the first filled cell of the pentomino */
		//Gets X and Y coordinates of Pentomino
		if (mMatrix.getCell(x, y) == 0) {
			xpent = pent.getXCoor(pent.getLeftTop());
			ypent = pent.getYCoor(pent.getLeftTop());
			//System.out.println("XPENT: " + xpent + " YPENT: " + ypent);
			//System.out.println("Width pent: " + pent.getWidth() + " Height pent: " + pent.getHeight());
		} else {
			//System.out.println ("Topleft cell " + x + "|" + y + " given is not empty");
			return false;
		}
		int newX = x - xpent;
		int newY = y - ypent;
		//System.out.println ("Got X = " + x + " y = " + y);
		//System.out.println("NewX: " + newX + " NewY: " + newY);
		/** Checks if pent is in boundaries */
		if (((newX+pent.getWidth()) > this.getWidth()) || ((newY+pent.getHeight()) > this.getHeight())) 
		{
			//System.out.println ("Pentomino matrix does not fit in board");
			return false;
		}
		
		/** Loops through pent and Board and checks if Board==0||Pent==0 */
		for (int i=0; i< pent.getWidth(); i++) {
			for (int j=0; j< pent.getHeight(); j++) {
				if (this.getElement(newX + i, newY + j) != 0 && pent.getElement(i, j) != 0) 
				{
					//System.out.println ("Non matching element at " + i + "|" + j + " on pentomino and at " + 
						//(newX + i) + "|" + (newY + j) + " on board");
					return false;
				}
			}
		}
		return true;				
	}
	
	/** 
	 * @param row The index of the row which will be set to the same as the row above
	 */
	public void moveRow(int row){
		mMatrix.moveRow(row);
	}
	
	/**
	 * @param row The index of the row which will be deleted
	 */
	public void clearRow(int row){
		mMatrix.clearRow(row);
	}
	
	/**
	 * @param row The index of the row that should be checked
	 * @return True if row is filled
	 */
	public boolean isRowFilled(int row) {
		return mMatrix.isRowFilled(row);
	}
	
	/**
	 * @param row The index of the column that should be checked
	 * @return True if column is filled
	 */
	public boolean isColFilled(int col){
		return mMatrix.isColFilled(col);
	}
	
	/**
	 * @param row The index of the row that should be checked
	 * @return True if row is empty
	 */
	public boolean isRowEmpty(int row) {
		return mMatrix.isŔowEmpty(row);
	}
	
	/**
	 * @param row The index of the column that should be checked
	 * @return True if column is empty
	 */
	public boolean isColEmpty(int col){
		return mMatrix.isColEmpty(col);
	}
	
	/**
	 * @param direc Which direction to look for the closest filled cell on the same row
	 * @param position The cell number (in position number format)
	 * @return Index of column the closest filled cell is in or -1 if there are none (borders = restrictions)
	 */
	public int getCloseRow(Direction direc,int position){
		return mMatrix.getCloseRow(direc, position);
	}
	
	/**
	 * @param direc Which direction to look for the closest filled cell on the same col
	 * @param position The cell coordinates which contain x and y
	 * @return Index of row the closest filled cell is in or -1 if there are none (borders = restrictions)
	 */
	public int getCloseCol(Direction direc, int position) {
		return mMatrix.getCloseCol(direc, position);
	}
	
	public boolean equals (Board compare)
	{
		return (this.mMatrix.equals(compare.mMatrix));	
	}
	
	public boolean isEmpty ()
	{
		for (int cCol = 0; cCol < this.getWidth(); cCol++)
		{
			for (int cRow = 0; cRow < this.getHeight(); cRow++)
			{
				if (this.getElement (cCol, cRow) != 0)
					return false;
			}
		}
		return true;
	}
			
	/** Returns the X position of the cell */
	public int getXCoor(int position) {
		int height = mMatrix.getHeight();
		int positionX = (int)Math.floor((position-1)/height);
		return positionX;
	}
	
	/** Returns the Y position of the cell */
	public int getYCoor(int position) {
		int height = mMatrix.getHeight();
		int positionY = (int)((position-1)%height);
		return positionY;
	}
		
	/** Takes the coordinates x and y and returns the value of that cell */
	public int getElement(int x, int y) {
		return mMatrix.getCell(x, y);
	}
	
	/** Returns a clone of mMatrix */
	public Board clone() {
		Board cloneBoard = new Board(mMatrix);
		return cloneBoard;		
	}
	
	/**determines best way to split uniformly boards*/ 
	public Board splitHalfFloor (int pentSquares)
	{
		//if width is larger than height and maximum-sized pentomino could still be placed inside the splitted Board
		if (this.getWidth() >= this.getHeight() && (int)Math.floor (this.getWidth() / 2) >= pentSquares)
			return new Board ((int)Math.floor (this.getWidth() / 2), this.getHeight());
		//opposite of previous case
		else if (this.getHeight() > this.getWidth() && (int)Math.floor (this.getHeight() / 2) >= pentSquares)
			return new Board (this.getWidth(), (int)Math.floor (this.getHeight() / 2));
		//if splitted Board would prevent program from finding all solutions
		return new Board (0, 0);
	}
	
	/**returns pentominos not used*/
	public ArrayList <Pentomino> unusedPents (ArrayList <Pentomino> totalPents)
	{
		ArrayList <Pentomino> unused = new ArrayList <Pentomino>();
		for (Pentomino check : totalPents)
		{
			int iPlaced = 0;
			while (iPlaced < mListPents.size() && mListPents.get (iPlaced).getId() != check.getId())
				++iPlaced;
			if (iPlaced >= mListPents.size())
				unused.add (check.clone());
		}
		return unused;
	}
	
	public void rotate()
	{
		mMatrix.rotate();	
	}
	
	public void flipVer()
	{
		mMatrix.flipVer();	
	}
	
	public void flipHor()
	{
		mMatrix.flipHor();	
	}
	
	public void printBoard() {
		System.out.println("Height: " + this.getHeight());
		System.out.println("Width: " + this.getWidth());
		for (int i=0; i<this.getHeight(); i++) {
			for (int j=0; j<this.getWidth(); j++) {
				System.out.print(mMatrix.getCell(j,i) + " ");
			}
			System.out.println();
		}
	}

	
	//Private 
//CHANGE
	/** This method gets the LeftTop cell and returns the position as an integer */
	public int getLeftTop() {
		int count = 0;
		for (int i=0; i<this.getWidth(); i++) {
			for (int j=0; j<this.getHeight(); j++) {
				count++;
				if (this.getElement(i,j) == 0) return count;
			}
		}
		return -1;
	}
	
	
		
		
	

	/** Matrix of type MatrixHandler */
	private MatrixHandler mMatrix;
	
	/** Arraylist of type Pentomino */
	private ArrayList<Pentomino> mListPents;
	
	private Direction direc;
}