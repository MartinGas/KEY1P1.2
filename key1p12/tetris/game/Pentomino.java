package key1p12.tetris.game;
import java.util.ArrayList;

public class Pentomino implements Cloneable
{ 

	/**Arraylist with the 12 original pentominoes */
	public static ArrayList<Pentomino> createsPentList() {

	ArrayList<Pentomino> pentList = new ArrayList<Pentomino>();
	int[][][] grid1 = new int[6][3][3];
	int[][][] grid2 = new int[2][2][3];
	int[][][] grid3 = new int[3][2][4];
	int[][][] grid4 = new int[1][1][5];
		
	//Create P1
		for (int j=0; j<grid2[0].length; j++) {
			for (int k=0; k<grid2[0][j].length; k++) {
				grid2[0][j][k] = 1;
			}
		}
		grid2[0][1][2] = 0;
		
		
		//Create X
		for (int j=0; j<grid1[0].length; j++) {
			for (int k=0; k<grid1[0][j].length; k++) {
				grid1[0][j][k] = 2;
			}
		}
		grid1[0][0][0] = 0;
		grid1[0][0][2] = 0;
		grid1[0][2][0] = 0;
		grid1[0][2][2] = 0;
		
		//Create F1
		for (int j=0; j<grid1[0].length; j++) {
			for (int k=0; k<grid1[0][j].length; k++) {
				grid1[1][j][k] = 3;
			}
		}
		grid1[1][0][0] = 0;
		grid1[1][0][2] = 0;
		grid1[1][2][1] = 0;
		grid1[1][2][2] = 0;
		
		//Create V1
		for (int j=0; j<grid1[0].length; j++) {
			for (int k=0; k<grid1[0][j].length; k++) {
				grid1[2][j][k] = 4;
			}
		}
		grid1[2][1][0] = 0;
		grid1[2][1][1] = 0;
		grid1[2][2][0] = 0;
		grid1[2][2][1] = 0;
		
		//Create W1
		for (int j=0; j<grid1[0].length; j++) {
			for (int k=0; k<grid1[0][j].length; k++) {
				grid1[3][j][k] = 5;
			}
		}
		grid1[3][0][2] = 0;
		grid1[3][1][0] = 0;
		grid1[3][2][0] = 0;
		grid1[3][2][1] = 0;
		
		//Create Y1
		for (int j=0; j<grid3[0].length; j++) {
			for (int k=0; k<grid3[0][j].length; k++) {
				grid3[0][j][k] = 6;
			}
		}
		grid3[0][0][0] = 0;
		grid3[0][0][2] = 0;
		grid3[0][0][3] = 0;
		
		//Create I1
		for (int k=0; k<grid4[0][0].length; k++) {
			grid4[0][0][k] = 7;
			}
		
		
		//Create T1
		for (int j=0; j<grid1[0].length; j++) {
			for (int k=0; k<grid1[0][j].length; k++) {
				grid1[4][j][k] = 8;
			}
		}
		grid1[4][0][1] = 0;
		grid1[4][0][2] = 0;
		grid1[4][2][1] = 0;
		grid1[4][2][2] = 0;
		
		//Create Z1
		for (int j=0; j<grid1[0].length; j++) {
			for (int k=0; k<grid1[0][j].length; k++) {
				grid1[5][j][k] = 9;
			}
		}
		grid1[5][0][1] = 0;
		grid1[5][0][2] = 0;
		grid1[5][2][0] = 0;
		grid1[5][2][1] = 0;
		
		//Create U1
		for (int j=0; j<grid2[0].length; j++) {
			for (int k=0; k<grid2[0][j].length; k++) {
				grid2[1][j][k] = 10;
			}
		}
		grid2[1][1][1] = 0;
		
		//Create N1
		for (int j=0; j<grid3[0].length; j++) {
			for (int k=0; k<grid3[0][j].length; k++) {
				grid3[1][j][k] = 11;
			}
		}
		grid3[1][0][0] = 0;
		grid3[1][1][2] = 0;
		grid3[1][1][3] = 0;
		
		//Create L1
		for (int j=0; j<grid3[0].length; j++) {
			for (int k=0; k<grid3[0][j].length; k++) {
				grid3[2][j][k] = 12;
			}
		}
		grid3[2][1][0] = 0;
		grid3[2][1][1] = 0;
		grid3[2][1][2] = 0;


		for (int i=0; i<grid1.length; i++) {
			Pentomino newPentomino = new Pentomino(grid1[i]);
			pentList.add(newPentomino);
		}
		for (int i=0; i<grid2.length; i++) {
			Pentomino newPentomino = new Pentomino(grid2[i]);
			pentList.add(newPentomino);
		}
		for (int i=0; i<grid3.length; i++) {
			Pentomino newPentomino = new Pentomino(grid3[i]);
			pentList.add(newPentomino);
		}
		for (int i=0; i<grid4.length; i++) {
			Pentomino newPentomino = new Pentomino(grid4[i]);
			pentList.add(newPentomino);
		}
		return pentList;
	}
	
	/**
	 * @param pent pentomino which should be rotated
	 * postcondition: pent is not included in ArrayList returned
	 * @return all rotated versions of pent
	 */
	public static ArrayList <Pentomino> generateRotations (Pentomino pent)
	{
		ArrayList<Pentomino> storeRotations = new ArrayList<Pentomino>();
		Pentomino clone = pent.clone();
		do 
		{	
			storeRotations.add (clone);
			clone = clone.clone();
			clone.rotate();
		} while  (!storeRotations.get (0).equals (clone));
		return storeRotations;
	}
	
	
	public static ArrayList <Pentomino> generateFlips (Pentomino pent)
	{
		ArrayList <Pentomino> storeFlips = new ArrayList <Pentomino>();
		Pentomino clone = pent.clone();
		do
		{
			storeFlips.add (clone);
			clone = clone.clone();
			clone.flip();
		} while (!storeFlips.get (0).equals (clone));
		return storeFlips;
	}

	/** We rotated and fliped all the pentominoes and stored them in the arrayList */
	public static ArrayList<ArrayList<Pentomino>> generateVersions(ArrayList<Pentomino> listofPent) {
		ArrayList<ArrayList<Pentomino>> versions = new ArrayList<ArrayList<Pentomino>>();
		for (int i=0; i<listofPent.size();i++) {
			ArrayList<Pentomino> storeRotations = new ArrayList<Pentomino>();
			Pentomino pents = listofPent.get(i);
			
			do {
				
				Pentomino clonePento = pents.clone();
				storeRotations.add(clonePento);
				Pentomino flipClonePento = pents.clone();
				flipClonePento.flip();
				if (!flipClonePento.equals(pents)){
						storeRotations.add(flipClonePento);
				}
				pents.rotate();
				
				
			} while  (storeRotations.size()==0 || !storeRotations.get(0).equals(pents));
			
			versions.add(storeRotations);
		}
	return versions;
	} 
	
	/**searches matrix for value != 0 => ID*/
	public static int getIdFromMatrix (MatrixHandler matrix)
	{
		int cCol = 0, cRow = 0;
		while (matrix.getCell (cCol, cRow) == 0 && cCol < matrix.getWidth() && cRow < matrix.getHeight())
		{
			cRow++;
			if (cRow == matrix.getHeight())
			{
				cRow = 0;
				cCol++;
			}
		}
		if (cCol < matrix.getWidth() && cRow < matrix.getHeight())
			return matrix.getCell (cCol, cRow);
		else
			return 0;
	}

	/** first constructor */
	public Pentomino(int[][] arPent) {
		mMatrix = new MatrixHandler(arPent);
		mId = getIdFromMatrix (mMatrix);
	}

	/** second constructor */
	public Pentomino(MatrixHandler matrix) {
		mMatrix = matrix.clone();
		mId = getIdFromMatrix (mMatrix);
	}
 
 	/** method to clone pentominoes */
	public Pentomino clone() {

		Pentomino newPentomino = new Pentomino(this.mMatrix);
			return newPentomino;
	}

	/** test for flip */
	public void flip() {
		mMatrix.flipVer();
	}

	/** test for rotation */
	public void rotate() {
		mMatrix.rotate();
	}
	
	public int getId()
	{
		return mId;	
	}

	public int getWidth() {
		return mMatrix.getWidth();
	} 

	public int getHeight() {
		return mMatrix.getHeight();
	}

	public int getElement(int x, int y) {
		return  mMatrix.getCell(x, y);

	}
	
	/** Returns the X position of the cell */
	public int getXCoor(int position) {
		int height = this.getHeight();
		int positionX = (int)Math.floor((position-1)/height);
		return positionX;
	}
	
	/** Returns the Y position of the cell */
	public int getYCoor(int position) {
		int height = this.getHeight();
		int positionY = (int)((position-1)%height);
		return positionY;
	}

	/** search for the fisrt used cell starting from the left top corner */
	public int getLeftTop() {
		int cntr = 0;
		for (int i=0; i<getWidth(); i++) {
			for (int j=0; j<getHeight(); j++) {
				cntr++;
				if (mMatrix.getCell(i, j) != 0)
					return cntr;
			}
		}
				return -1;
	}

 	/** find out where the filled cell is located */
	public int wposition(int cntr) {
		int w =(int) Math.floor((cntr-1)/getHeight());
			return w;
	}

	public int hposition(int cntr) {
		int h = ((cntr-1)%getHeight());
			return h;
	}
	
	public boolean equals(Pentomino pent) {
		return this.mMatrix.equals((pent.mMatrix));
	}
	
	public void debugPrint(){
		mMatrix.print();
	}
	
	private MatrixHandler mMatrix;
	private int mId;
}




