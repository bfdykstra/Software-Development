package falstad;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MazeBuilderEllerTest {
	
	MazeStub mazeStub; //// reference to the mazeStub that is constructed, results are returned by calling mazeStub.newMaze(..)
	
	
	/**
	 * Create a fully initialized maze with everything in it generated. 
	 * @throws InterruptedException if interupped
	 */
	@Before
	public void setUp() throws InterruptedException{
		
		mazeStub = new MazeStub();
		//TODO Modify difficulty of build, will generate some different stuff
		mazeStub.buildWithEller(1); //this then calls Mazebuilder.build() which then calls init and initializes builder attributes
		//run() is also called and sets up a maze
		mazeStub.mazebuilder.buildThread.join(); //we wait for this thread to die before we continue with test
		
	}
	
	@Test
	/**
	 * Test to make sure all of the fields were properly initialized
	 */
	public void configTest() {
		//MazeBuilder m1 = new MazeBuilder(true);
		assertNotNull(mazeStub.mazebuilder.cells);
		assertNotNull(mazeStub.mazebuilder.dists);
		assertNotNull(mazeStub.mazebuilder.height);
		assertNotNull(mazeStub.mazebuilder.width);
		assertNotNull(mazeStub.mazebuilder.startx);
		assertNotNull(mazeStub.mazebuilder.starty);
		
	}

	@Test
	/**
	 * Asserts that there is the entrance is the same one that is called by the getStartPosition in Distance class
	 */
	public void entranceTest(){
		
		//tests start
		assertNotNull(mazeStub.mazebuilder.dists.getStartPosition());
		int[] start = mazeStub.mazebuilder.dists.getStartPosition();
		
		int xStart = start[0];
		int yStart = start[1];
		assertTrue(mazeStub.mazebuilder.startx == xStart);
		assertTrue(mazeStub.mazebuilder.starty == yStart);
		
		
	}
	
	@Test
	/**
	 * Asserts that there is the exit is the same one that is called by the getExitPosition in Distance class
	 */
	public void exitTest(){
		//tests exit
		assertNotNull(mazeStub.mazebuilder.dists.getExitPosition());
		int[] exit = mazeStub.mazebuilder.dists.getExitPosition();
		
		int xExit = exit[0];
		int yExit = exit[1];
		assertTrue(mazeStub.mazebuilder.dists.isExitPosition(xExit, yExit));
		assertTrue(mazeStub.mazebuilder.cells.isExitPosition(xExit, yExit));
	}
	
	
	@Test
	/**
	 * Iterates over the entire array and checks if any of the distances are infinity. Furthermore, 
	 * it makes sure that at least one cell surrounding the current one is closer.
	 */
	public void pathwayTest(){
		//If none of the distances are INFINITY, there should be a path between them.
		// In the computeDistances method of Distance, sets all values of distance array to infinity,
		// so if a value wasn't reset, then it was not reachable. This indicates that there were no isolated
		// rooms and that all cells were reached.
		int[][] distArray = mazeStub.mazebuilder.dists.getDists();
		int i = 0;
		int j = 0;
		int maxNdxWidth = mazeStub.mazebuilder.width - 1;
		int maxNdxHeight = mazeStub.mazebuilder.height - 1;
		for(i = 0; i < mazeStub.mazebuilder.width; i++){
			for(j = 0; j < mazeStub.mazebuilder.height; j++){
				int refCell = distArray[i][j];
				boolean testBool = false;
				//check if exit position
				if(mazeStub.mazebuilder.dists.isExitPosition(i, j)){
					testBool = true;
				}
				//index out of bounds
				if((i + 1) <= maxNdxWidth ){
					int northCell = distArray[i+1][j];
					//check if cell is less than reference cell
					if(northCell < refCell){
						testBool = true;
					}
				}
				
				//index out of bounds
				if((j + 1) <= maxNdxHeight ){
					int eastCell = distArray[i][j+1];
					//check if cell is less than reference cell
					if(eastCell < refCell){
						testBool = true;
					}
				}
				if((i - 1) >= 0 ){
					int southCell = distArray[i-1][j];
					//check if cell is less than reference cell
					if(southCell < refCell){
						testBool = true;
					}
				}
				if((j - 1) >= 0 ){
					int westCell = distArray[i][j-1];
					//check if cell is less than reference cell
					if(westCell < refCell){
						testBool = true;
					}
				}
				
			
				assertTrue(testBool);
				assertFalse(distArray[i][j] == mazeStub.mazebuilder.dists.INFINITY);
				assertFalse(distArray[i][j] < 0);
			}
		}
		
	}

}
