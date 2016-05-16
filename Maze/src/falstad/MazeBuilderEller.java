package falstad;


import java.util.ArrayList;
import java.util.Hashtable;

/**
 * An implementation of Eller's algorithm using a 2d array and a hash table
 * @author benjamindykstra
 *
 */

public class MazeBuilderEller extends MazeBuilder {
	
	int[] curRow;  //the current row that the algorithm is operating on
	Hashtable<Integer, Integer> sets = new Hashtable<Integer, Integer>(); //a hashtable with the cell value as a key, and the "set number" as a value
											
	int[][] cellValArray; //an array with unique values that will mirror the cell object array
	int rowNum = 0; //THE ROW NUMBER WE ARE AT
	
	public MazeBuilderEller() {
		
		super();
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}

	public MazeBuilderEller(boolean deterministic) {
		super(deterministic);
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze. Using deterministic maze");
		
	}
	

	@Override
	protected void generatePathways() {
		
		this.curRow = new int[this.maze.mazew]; //makes a blank array of size width
		//this.curRow = new int[this.width];
		this.cellValArray = new int[this.height][this.width];
		populateCellValArray(); //fills cellValArray with sequential values
		populateRooms(); //makes rooms all into their own respective sets
		
		while(rowNum < this.height - 1){
			this.curRow = new int[this.width]; //THE NEW LINE
			this.setCurRow(rowNum);
			int[] cur = getCurRow();
			this.generateSets(cur); // puts the unique set values into the hashtable
			
			//STEP 1: Randomly join adjacent cells if they are not in the same set
			setStrings();
			randJoin(cur, sets);
			setStrings();
			
			
			//STEP 2: Randomly determine vertical connections, with at least 1 per set
			
			//get each unique set in curRow
			ArrayList<Integer> setIDs = new ArrayList<Integer>();
			for(int i = 0; i < curRow.length; i++){
				int refSetID = sets.get(curRow[i]);
				if(!(setIDs.contains(refSetID)) ){
					setIDs.add(refSetID);
				}
				
			}
			
			//Randomly determine vertical connections, with at least 1 per set
			for(int j = 0; j < setIDs.size(); j++){
				this.verticalConnect(setIDs.get(j));	
			}
			setStrings();
			
			rowNum++; //increment row up one to get ready for next loop
			
		}
		this.setCurRow(rowNum);
		this.generateSets(curRow); 
		
		// STEP 3: Join any remaining sets together
		this.lastRow();
		setStrings();
	}

	
	/**
	 * sets and returns the curRow
	 * @param cellRow the row from Cells that is being inspected
	 * @return int[] with values of the row from cells array
	 */
	protected void setCurRow(int cellRow){
		for(int i = 0; i < width; i++){
			curRow[i] = cellValArray[cellRow][i];
		}
	}
	
	/**
	 * gets the current row that the algorithm is operating on
	 * @return
	 */
	protected int[] getCurRow(){
		return curRow;
	}
	
	
	/**
	 * Randomly join adjacent cells if they are not part of the same set.
	 * @param row The row from cellValArray
	 * @param sets A Hashtable containing the set numbers as values
	 */
	protected void randJoin(int[] row, Hashtable<Integer, Integer> sets){
	
		boolean sameSet;
		for(int i = 0; i < row.length; i++){
			if(i + 1 < row.length){
				sameSet = this.areSameSet(row[i], row[i + 1]); //tests if adjacent cells are part of same set
				if(!sameSet){
					
					//0 = join, 1 = don't join
					//int randChance = this.random.nextIntWithinInterval(1, 5);
					int join = this.random.nextIntWithinInterval(0, 1);
					
					if(join == 0){
						
						//checks if there is a boundary between cell and cell next to it
						boolean notBound = this.cells.canGo(i, this.rowNum, 1, 0);
						if(notBound){
							//deletes wall to the right
							this.cells.deleteWall(i, this.rowNum, 1, 0); 
							
							//add them to the same set
							int refCell = row[i];
							int adjCell = row[i+1];
							this.joinSets(refCell, adjCell);
						}
						
					}
				}
			}
			//cell at end of row
			else{ 
				sameSet = this.areSameSet(row[i], row[i-1]);
				if(!sameSet){
					
					//0 = join, 25% chance of joining
					int randChance = this.random.nextIntWithinInterval(1, 5);
					int join = this.random.nextIntWithinInterval(0, randChance);
					
					if(join == 0){
						//checks if there is a boundary between cell and cell next to it
						boolean notBound = this.cells.canGo(i, this.rowNum, 1, 0);
						
						if(notBound){
							//deletes wall to the left
							this.cells.deleteWall(i, this.rowNum, -1, 0); 
							
							//add them to the same set
							int refCell = row[i];
							int adjCell = row[i - 1];
							this.joinSets(refCell, adjCell);
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Randomly determine vertical connections, with at least one per set. Called on a set by set basis
	 * @param setID the set number that we are determing connections for
	 */
	private void verticalConnect(int setID){
		
		//gives us the maximum number of connections
		int maxNumConnects = getNumCellsSet(setID); 
		
		//chooses a random number of connections, with bottom limit of 1
		int numConnects = this.random.nextIntWithinInterval(1, maxNumConnects);
		
		int[] nextRow = new int[this.width];
		for(int i = 0; i < this.width; i ++){
			nextRow[i] = this.cellValArray[rowNum +1][i]; 
		}
		
		
		//find upper and lower index bounds of set in row
		//iterate of curRow, find key(index) that equals setID, keep going until it no longer does, return upper and lower
		int lowBound = 0;
		int upBound = 0;
		for(int j = 0; j< width ; j++){
			if(sets.get(curRow[j]) == setID){
				lowBound = j;
				
				int x = j;
				while(x + 1 < width){
					if(sets.get(curRow[j + 1]) == setID ){
						j++;
					}
					x++;
				}
				upBound = j ;
				break;
			}
		}
		
		
		//randomly chooses numConnects indices between lower and upper bounds
		ArrayList<Integer> randomNdxs = new ArrayList<Integer>();
		for(int i = 0; i < numConnects; i++){
			//randomNdxs[i] = rand.nextInt((upBound - lowBound) + 1) + lowBound;
			int theRandomNdx = this.random.nextIntWithinInterval(lowBound, upBound);
			if(!(randomNdxs.contains(theRandomNdx))){
				randomNdxs.add(theRandomNdx);
			}
			
		}
		
		//Get cells that were randomly chosen between upper and lower bounds using randomNdxs.
		//Delete wall below it and join the cell to the sets
		boolean gotIn = false;
		for(int i = 0; i< randomNdxs.size(); i++){
			
			//the randomly chosen cell within the boundary!!
			int refCell = curRow[randomNdxs.get(i)];
			
			//cell below refCell
			int southCell = this.cellValArray[rowNum + 1][randomNdxs.get(i)];
			
			//Checks if cell below ref cell is separated by boundary
			boolean bound = this.cells.canGo(randomNdxs.get(i), this.rowNum, 0, 1);
			
			//Check for single cell in set bc MUST make at least one connection, regardless if there is a boundary there or not
			if(maxNumConnects == 1){
				bound = true;
			}
			
			if(bound){
				
				gotIn = true;
				//breaking into a room through a door, already checked for boundary
				boolean inside = sets.containsKey(southCell);
				if(inside){
					this.joinSets(refCell, southCell);
				}
				
				//delete wall below refCell-- randNdxs.get(j) is the index of the randomly chosen cell and -1 specifies the one below it
				this.cells.deleteWall(randomNdxs.get(i), this.rowNum, 0, 1);
				
				//put southCell into the set of refCell's set
				int refCellSet = sets.get(refCell);
				sets.put(southCell, refCellSet);
				
				//happens if maxNumConnects != 1, but all vertical connections blocked by boundary
			} else if(gotIn == false){
				boolean inside = sets.containsKey(southCell);
				if(inside){
					this.joinSets(refCell, southCell);
				}
				
				//delete wall below refCell-- randNdxs.get(j) is the index of the randomly chosen cell and -1 specifies the one below it
				this.cells.deleteWall(randomNdxs.get(i), this.rowNum, 0, 1);
				
				//put southCell into the set of refCell's set
				int refCellSet = sets.get(refCell);
				sets.put(southCell, refCellSet);
			}
		}
	}
	
	
	/**
	 * same as randJoin() minus the randomness
	 */
	private void lastRow(){
		
		boolean sameSet;
		for(int i = 0; i < curRow.length; i++){
			if(i + 1 < curRow.length){
				//tests if adjacent cells are part of same set
				sameSet = this.areSameSet(curRow[i], curRow[i + 1]); 
				if(!sameSet){
					//deletes wall to the right
					this.cells.deleteWall(i, this.rowNum, 1, 0); 
					
					//add them to the same set
					int refCell = curRow[i];
					int adjCell = curRow[i+1];
					this.joinSets(refCell, adjCell);
					
					
				}
			}
			
			//cell at end of row
			else{ 
				sameSet = this.areSameSet(curRow[i], curRow[i-1]);
				if(!sameSet){
					
					this.cells.deleteWall(i, this.rowNum, -1, 0); //deletes wall to the left
					
					//add them to the same set
					int refCell = curRow[i];
					int adjCell = curRow[i - 1];
					this.joinSets(refCell, adjCell);
					
				}
			}
		}
	}
	
	
	/**
	 * merges sets of cell2 into cell1
	 * @param cell1
	 * @param cell2
	 */
	private void joinSets(int cell1, int cell2){
		//cell1 is refCell,
		//cell2 is adjCell
		int setOfRefCell = sets.get(cell1);
		
		//get all the other cells that are in same set as cell2
		int setOfAdjCell = sets.get(cell2);
		
		for(int i = 0; i < rowNum; i++){
			for(int j = 0; j < width; j++){
				int tempSet = sets.get(this.cellValArray[i][j]); //gets the set of cell i,j
				//check if same set as adjCell
				if(setOfAdjCell == tempSet){
					sets.put(cellValArray[i][j], setOfRefCell);
					
				}
			}
		}
		sets.put(cell2, setOfRefCell);
	}
	
	
	/**
	 * gets the number of cells in a set FOR A SINGLE ROW
	 * @param set the set of interest
	 * @return number of cells in a given set
	 */
	private int getNumCellsSet(int set){
		
		int setOfCellVal;
		
		int setCount = 0;
		for(int i = 0; i < this.width; i++){
			setOfCellVal = sets.get(curRow[i]);
			if(setOfCellVal == set){
				setCount++;
			}
		}
		return setCount;
	}
	
	
	/**
	 * checks if two cells are part of the same set
	 * @param cell1 The first cell of interest
	 * @param cell2 The second cell of interest
	 * @return true if cells are part of the same set, false otherwise
	 */
	private boolean areSameSet(int cell1, int cell2){
		//the set that cell1 belongs too
		int set1 = sets.get(cell1); 
		//the set that cell2 belongs too
		int set2 = sets.get(cell2); 
		
		if(set1 == set2){
			return true;
		}
		return false;
	}
	
	
	/**
	 * generates a unique set number for each cell in the cellValArray
	 * @param row A row of cells
	 */
	private void generateSets(int[] row){
		
		for(int i = 0; i < row.length; i++){
			
			int num = cellValArray[rowNum][i];
			if(!sets.containsKey(row[i])){
				sets.put(row[i], num);
			}
			
		}
	}
	
	
	/**
	 * populates the cellValArray with consecutive integers
	 */
	private void populateCellValArray(){
		int num = 0;
		// (i,j) = row, column
		//width is number of colums, height is number of rows
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				this.cellValArray[i][j] = num;
				num++;
			}
		}
	}
	
	
	/**
	 * puts rooms into their own set
	 * @prerequisite populateCellValArray must be called before this method
	 */
	private void populateRooms(){
		
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				
				boolean cellInRoom = this.cells.isInRoom(j, i);
				boolean found = sets.containsKey(cellValArray[i][j]); //key will be in the hashTable if it was already part of room
				if(cellInRoom && (!found) ){
					int xLow = j;
					int yLow = i;
					int xUp = compXbounds(xLow, yLow);
					int yUp = compYbounds(xLow, yLow);
					setRoom(xLow, yLow, xUp, yUp);
				}
			}
		}
	}
	
	
	/**
	 * Given upper and lower boundaries of a room, makes all of the cells in that room the same set
	 * @prerequisite populateCellValArray must have been called first, will throw nullPointer if not
	 * @param xLow lower x bound
	 * @param yLow lower y bound
	 * @param xUp upper x bound
	 * @param yUp upper y bound
	 */
	private void setRoom(int xLow, int yLow, int xUp, int yUp){
		int setID = this.cellValArray[yLow][xLow]; //the set that all the cells in this room will be set too
		for(int i = yLow; i <= yUp; i ++){
			for(int j = xLow; j <= xUp; j ++){
				int tempCell = this.cellValArray[i][j];
				sets.put(tempCell, setID);
				
			}
		}
	}
	
	
	/**
	 * prints out a visiulizations of all the sets 
	 */
	private void setStrings(){
		for(int i = 0; i < this.height; i++){
			System.out.println();
			for(int j = 0; j< this.width; j++){
				//System.out.print(sets.get(cellValArray[i][j]) + "  " );
				System.out.printf("%-5d", sets.get(cellValArray[i][j]));
				
			}
		}
		System.out.println();
		System.out.println();
		
	}
	
	
	/**
	 * determine the x value extent of a room
	 * @param xlow The lower x boundary of a room
	 * @param ylow The lower y boundary of a room
	 * @return xUp The upper x boundary of a room
	 */
	private int compXbounds(int xlow, int ylow){
		int x = xlow;
		boolean entered = false;
		//while(! (this.cells.canGo(i, ylow, 0, -1)) ){
		while( this.cells.isInRoom(x, ylow)){
			x++;
			entered = true;
		}
		if(entered){
			int xUp = x - 1;
			return xUp;
		}
		int xUp = x;
		return xUp;
	}
	
	
	/**
	 * determine the y value extent of a room
	 * @param xlow The lower x boundary of a room
	 * @param ylow The lower y boundary of a room
	 * @return yUp The upper y boundary of a room
	 */
	private int compYbounds(int xlow, int ylow){
		int y = ylow;
		boolean entered = false;
		while( this.cells.isInRoom(xlow, y)){
			y++;
			entered = true;
		}
		if(entered){
			int yUp = y - 1;
			return yUp;
		}
		//if room is only one cell wide
		int yUp = y;
		return yUp;
	}
	
	
}
