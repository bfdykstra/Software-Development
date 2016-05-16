/**
 * 
 */
package falstad;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases to test the MazeBuilder. 
 * Testing is complicated by the fact that the MazeBuilder operates mainly in a thread of its own 
 * which needs to be scheduled and needs time to terminate before we can test its outcome.
 * We make this class a subclass of Maze such that an instance of this class can serve as a mock object
 * of a Maze to cooperate with a MazeBuilder.
 * 
 * We test the maze generation for various sizes of mazes by varying the requested skill level.
 * For each generated maze, we test if the maze is enclosed by a border and has exactly one exit. 
 * We also test if there is a path from each cell in the maze towards the exit.
 * 
 * Not tested: correctness of BSP node data structure.
 * 
 * Status: working, can be used for grading project 2 to see if maze generation algorithm produces a correct maze
 * 
 * @author peterkemper
 *
 */
public class MazeBuilderTATest extends Maze {

	Maze maze = null ;
	BSPNode root  = null ;
	Cells mazecells = null ;
	int[][] dists = null ;
	int startx ;
	int starty ;
	int mazew ;
	int mazeh ;
	
	/**
	 * This method encapsulates the instantiation of an object to be tested. 
	 * Needs to be adjusted to produce a MazeBuilder or a MazeBuilderPrim object.
	 * @return a MazeBuilder object to generate a Maze
	 */
	public MazeBuilder createMazeBuilder()
	{
		// reset class attribute to be able to recognize failure of a thread
		maze = null ;
		root  = null ;
		mazecells = null ;
		dists = null ;
		startx = 0 ;
		starty = 0 ;
		// add exception handler to provide feedback if thread died prematurely
		Thread.setDefaultUncaughtExceptionHandler(
		        new Thread.UncaughtExceptionHandler() {
		            @Override public void uncaughtException(Thread t, Throwable e) {
		        		String msg = "Maze builder's internal worker thread died due to unhandled exception: " ;
		            	System.err.println(msg + t.getName()+": "+e) ;
		            	e.printStackTrace() ;
		            }
		        });

		//return new MazeBuilder() ; 
		//return new MazeBuilderPrim() ;
		//return new MazeBuilderKruskal() ;
		return new MazeBuilderEller() ;
		//return new MazeBuilderAldousBroder() ;
		//return new MazeBuilderWilson() ;
		
	}
	
	/**
	 * Method overwrites superclass method. It is used by the mazebuilder object to communicate
	 * back a finished maze. For testing purposes, we simply memorize the given bits and pieces
	 * of a maze;
	 * @param root of bsp nodes
	 * @param c cells encodes the computed maze
	 * @param dists is an array of same dimension as the maze and holds all distances to the exit for each cell
	 * @param startx is the x coordinate of the starting position
	 * @param starty is the y coordinate of the starting position
	 */
	public void newMaze(BSPNode root, Cells c, Distance dists, int startx, int starty) {
		//System.out.println("Trace: MazeBuilder calls back, cells:" + c);
		this.root = root ;
		mazecells = c ;
		this.dists = dists.getDists() ;
		this.startx = startx ;
		this.starty = starty ;
	}
	
	/** 
	 * Testcase: Correctness of the build method for maze of various sizes, skill level 0 - 5,
	 * existence of exit
	 * <p>
	 * Method under test: {@link falstad.MazeBuilder#build(falstad.Maze2, int, int, int, int)}
	 * <p>
	 * Correct behavior:
	 * The returned cells are surrounded by borders but for a single exit.
	 */
	@Test
	public final void testExistenceOfExitAcrossSkillLevels() {
		for (int skill = 0; skill <= 5 ; skill++) {
			mySetUp(skill);
			// check if the maze is correct
			// debug output for cells to allow for a manual inspection
			//dbg() ;
			// check if border settings are correct and there is an exit
			System.out.println("Testing existence of exit for skill level: " + skill 
					+ ", maze width " + mazew + ", maze height " + mazeh) ;
			checkBorder(mazecells, dists, mazew, mazeh) ;
		}
	}
	/** 
	 * Testcase: Correctness of the build method for maze of various sizes, skill level 0 - 5,
	 * exit is reachable
	 * <p>
	 * Method under test: {@link falstad.MazeBuilder#build(falstad.Maze2, int, int, int, int)}
	 * <p>
	 * Correct behavior:
	 * For each cell, there is a path to the exit. We test this by checking several 
	 * simple local condition which in conjunction allow us to conclude that a path exits:<br> 
	 * For each cell, i.e., position, in the maze, there is an adjacent cell that is accessible (no wall between them) and
	 * that is one step closer to the exit. The distance to the exit is given by the dists matrix. 
	 * If this is true for each cell, then a path via those neighbors must lead to the exit.
	 * Check existence of such a neighbor for each cell is sufficient to ensure existence of a path. 
	 */
	@Test
	public final void testReachabilityOfExitAcrossSkillLevels() {
		for (int skill = 0; skill <= 5 ; skill++) {
			mySetUp(skill);
			// check if the maze is correct
			// debug output for cells to allow for a manual inspection
			//dbg() ;
			// check if each cell has a neighbor that is reachable and a step closer to the exit
			System.out.println("Testing reachability of exit for skill level: " + skill  
					+ ", maze width " + mazew + ", maze height " + mazeh) ;
			checkNeighbors(mazecells, dists, mazew, mazeh) ;
			
		}
	}
	/** 
	 * Testcase: Correctness of the build method for maze of various sizes, skill level 2 - 5,
	 * sufficient amount of walls are still up
	 * <p>
	 * Method under test: {@link falstad.MazeBuilder#build(falstad.Maze2, int, int, int, int)}
	 * <p>
	 * Correct behavior:
	 * A maze requires a sufficient amount of walls to be interesting. 
	 * We check for at least 10% of all walls being up which is a reasonable minimum for mazes
	 * of at least skill level 3. 
	 * Check if the number of walls in horizontal and vertical direction exceed a minimum percentage of 10%
	 * to recognize trivial cases with extremely few walls left or cases where the algorithm knocks down
	 * all horizontal (or vertical) walls.
	 */
	@Test
	public final void testExistenceOfWallsAcrossSkillLevels() {
		for (int skill = 3; skill <= 5 ; skill++) {
			// check if there is a reasonable amount of walls, here 10 percent
			mySetUp(skill);
			float percentage = .10f ;
			System.out.println("Testing existence of walls for skill level: " + skill  
					+ ", maze width " + mazew + ", maze height " + mazeh) ;
			checkWalls(mazecells,mazew,mazeh,percentage) ; 
			// past experience with web-cat showed 
			// that randomness introduced artificial failures, so 10% may be too rigid.
			// I believe that this takes place only at small mazes where few random choices are made
			// such that otherwise rare situations can occur. 
			// A more solid way would be to exercise the algorithm several times and average values
			// to estimate the probabilities for choices
		}
	}

	/** 
	 * Testcase: Correctness of the build method for maze of various sizes, skill level 2 - 5,
	 * maze should support at least one room for large enough mazes
	 * <p>
	 * Method under test: {@link falstad.MazeBuilder#build(falstad.Maze2, int, int, int, int)}
	 * <p>
	 * Correct behavior:
	 * A maze of size larger than skill level 0 should have at least one room, to be on the
	 * safe side for the random room generation we start with skill level 2 and stop at 5 to 
	 * limit the time for test. 
	 */
	@Test
	public final void testExistenceOfRoomsAcrossSkillLevels() {
		for (int skill = 2; skill <= 5 ; skill++) {
			mySetUp(skill);
			System.out.println("Testing existence of rooms for skill level: " + skill  
					+ ", maze width " + mazew + ", maze height " + mazeh) ;
			checkRooms(mazecells, dists, mazew, mazeh) ;
		}
	}
	/**
	 * Testcase: Maze generation process gets interrupted, check if mazebuilder reacts accordingly
	 * <p>
	 * Method under test:  {@link falstad.MazeBuilder#interrupt()}
	 * <p>
	 * Correct behavior: thread interrupts and does not call newMaze method.
	 * We use a high skill level to increase the likelihood that the thread stays busy long enough to have 
	 * it interrupted. As the thread does not finish the calculation, it does not call newMaze to provide results.
	 * So attributes such as mazecells, root, and dists will remain unchanged.
	 */
	@Test
	public final void testInterruptSignal() {
		int skill = 4 ;
		MazeBuilder mazebuilder = createMazeBuilder();
		// lets pick the reasonable configuration from Maze.java
		mazew = Constants.SKILL_X[skill] ; //skill_x[skill];
		mazeh = Constants.SKILL_Y[skill] ; // skill_y[skill];
		int roomcount = Constants.SKILL_ROOMS[skill] ; // skill_rooms[skill];
		int partc = Constants.SKILL_PARTCT[skill] ; // skill_partct[skill]
		mazecells = null ;
		root = null ; 
		dists = null ;
		mazebuilder.build(this, mazew, mazeh, roomcount, partc);
		// give mazebuilder thread a chance to proceed
		try {
		Thread.sleep(10) ; // give the worker thread a chance to start
		mazebuilder.interrupt() ;
		//mazebuilder.buildThread.join() ;
		Thread.sleep(10) ; // give worker thread a chance to recognize signal
		}
		catch (Exception ex) {
			fail("Test thread should not be interrupted!") ;
		}
		assertTrue(null == mazecells) ;
		assertTrue(null == root) ;
		assertTrue(null == dists) ;
	}
	////////////////////////////// internal methods /////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method checks if at least one room is present in the given maze.
	 * @param cells
	 * @param dists
	 * @param w
	 * @param h
	 */
	private void checkRooms(Cells cells, int[][] dists, int w, int h) {
		// check cells to see if there is a room, pick the top left corner
		int[] topLeftCorner = this.findTopLeftCornerOfARoom(cells, w, h) ;
		// a topLeftCorner must exist if there is at least a single room
		assertNotNull("At least one room should exist, should have a top left corner", topLeftCorner) ;
		int[] bottomRightCorner = this.findBottomRightCornerOfARoom(cells, w, h, topLeftCorner) ;
		// if there is topLeftCorner, there must be a bottom right corner with x,y coordinates greater than zero
		assertTrue("Room with top left corner at " + topLeftCorner[0] + "," + topLeftCorner[1] 
				+ " and bottom right corner"+ bottomRightCorner[0] + "," + bottomRightCorner[1] 
						+ "is wrong!",
						0 < bottomRightCorner[0] && 0 < bottomRightCorner[1]) ;
		
		// explore the room and see if its interior contains no walls for positions one step away from border
		for (int i = topLeftCorner[0]+1; i < bottomRightCorner[0]-1; i++ ){
			for (int j = topLeftCorner[1]+1 ; j < bottomRightCorner[1]-1; j++) {
				assertTrue("Position " + i + "," + j + " not inside room",  cells.isInRoom(i, j)); 
				assertTrue("Position " + i + "," + j + " unexpected wall on right side.", cells.hasNoWallOnRight(i, j)) ;
				assertTrue("Position " + i + "," + j + " unexpected wall on bottom side.", cells.hasNoWallOnBottom(i, j)) ;
				assertTrue("Position " + i + "," + j + " unexpected wall on left side.", cells.hasNoWallOnLeft(i, j)) ;
				assertTrue("Position " + i + "," + j + " unexpected wall on top side.", cells.hasNoWallOnTop(i, j)) ;

			}
		}
		// explore the borders of the room and check that not more than half of
		// the walls have been removed 
		int countDoors = 0 ;
		// top
		int other ;
		other = topLeftCorner[1];
		for (int i = topLeftCorner[0]; i <= bottomRightCorner[0]; i++ ) {
			if (cells.hasNoWallOnTop(i, other)) {
				countDoors++ ;
			}
			assertTrue("Position " + i + "," + other + " unexpected wall on bottom side.", 
					cells.hasNoWallOnBottom(i, other)) ;
			if (topLeftCorner[0] < i) {
				assertTrue("Position " + i + "," + other + " unexpected wall on left side.", 
						cells.hasNoWallOnLeft(i, other)) ;
			}
			if (i < bottomRightCorner[0]) {
				assertTrue("Position " + i + "," + other + " unexpected wall on right side.", 
						cells.hasNoWallOnRight(i, other)) ;
			}
		}
		// left
		other = topLeftCorner[0];
		for (int i = topLeftCorner[1]; i <= bottomRightCorner[1]; i++ ) {
			if (cells.hasNoWallOnLeft(other,i)) {
				countDoors++ ;
			}
			assertTrue("Position " + other + "," + i + " unexpected wall on right side.", 
					cells.hasNoWallOnRight(other,i)) ;
			if (topLeftCorner[1] < i) {
				assertTrue("Position " + other + "," + i + " unexpected wall on top side.", 
						cells.hasNoWallOnTop(other,i)) ;
			}
			if (i < bottomRightCorner[1]) {
				assertTrue("Position " + other + "," + i + " unexpected wall on bottom side.", 
						cells.hasNoWallOnBottom(other,i)) ;
			}
		}
		// right
		other = bottomRightCorner[0] ;
		for (int i = topLeftCorner[1]; i <= bottomRightCorner[1]; i++ ) {
			if (cells.hasNoWallOnRight(other,i)) {
				countDoors++ ;
			}
			assertTrue("Position " + other + "," + i + " unexpected wall on left side.", 
					cells.hasNoWallOnLeft(other,i)) ;
			if (topLeftCorner[1] < i) {
				assertTrue("Position " + other + "," + i + " unexpected wall on top side.", 
						cells.hasNoWallOnTop(other,i)) ;
			}
			if (i < bottomRightCorner[1]) {
				assertTrue("Position " + other + "," + i + " unexpected wall on bottom side.", 
						cells.hasNoWallOnBottom(other,i)) ;
			}
		}
		// bottom
		other = bottomRightCorner[1] ;
		for (int i = topLeftCorner[0]; i <= bottomRightCorner[0]; i++ ) {
			if (cells.hasNoWallOnBottom(i, other)) {
				countDoors++ ;
			}
			assertTrue("Position " + i + "," + other + " unexpected wall on top side.", 
					cells.hasNoWallOnTop(i, other)) ;
			if (topLeftCorner[0] < i) {
				assertTrue("Position " + i + "," + other + " unexpected wall on left side.", 
						cells.hasNoWallOnLeft(i, other)) ;
			}
			if (i < bottomRightCorner[0]) {
				assertTrue("Position " + i + "," + other + " unexpected wall on right side.", 
						cells.hasNoWallOnRight(i, other)) ;
			}
		}
		// check proportions: at most half of all walls have been taken away for doors
		int halfWalls = (bottomRightCorner[0] - topLeftCorner[0])+(bottomRightCorner[1] - topLeftCorner[1]) ;
		String str = "Maze has room with top left corner at (" + topLeftCorner[0] + "," + topLeftCorner[1] 
				+ ") and bottom right corner at ("+ bottomRightCorner[0] + "," + bottomRightCorner[1] 
						+ ") and " + countDoors + " doors.";
		assertTrue(str, 0 < countDoors) ;
		assertTrue(str + " and " + (2*halfWalls) + " walls", countDoors <= halfWalls) ;
		System.out.println(str) ;

	}
	
	/**
	 * Find the top left corner of a room
	 * @param cells
	 * @param w
	 * @param h
	 * @return
	 */
	private int[] findTopLeftCornerOfARoom(Cells cells, int w, int h) {
		int[] result = null ;
		for (int i = 0; i < w; i++ ){
			for (int j = 0 ; j < h; j++) {
				if (cells.isInRoom(i, j)) {
					result = new int[2] ;
					result[0] = i;
					result[1] = j;
					return result ;
				}
			}
		}
		return result ;
	}
	/**
	 * Find the bottom right corner of a room for given top left corner
	 * @param cells
	 * @param w
	 * @param h
	 * @param topleftcorner
	 * @return
	 */
	private int[] findBottomRightCornerOfARoom(Cells cells, int w, int h, int[] topLeftCorner) {
		int[] result = new int[2] ;
		for (int i = topLeftCorner[0]; i < w && cells.isInRoom(i, topLeftCorner[1]); i++ ){
			result[0] = i;
		}
		for (int j = topLeftCorner[1]; j < h && cells.isInRoom(topLeftCorner[0],j); j++ ){
			result[1] = j;
		}
		return result ;
	}
	/**
	 * This method checks if there are reasonable percentage of walls up to avoid trivial cases.
	 * We count walls on inner cells which can have up to 4 walls and exclude cells that are on the border to the
	 * outside of the maze. We count horizontal and vertical walls separately and expect for each category to have 
	 * at least the given percentage to be up.
	 * @param cells of maze
	 * @param w width
	 * @param h height
	 * @param requiredPercentage
	 */
	private void checkWalls(Cells cells, int w, int h, float requiredPercentage) {
		final int total = (w-2) * (h-2) ; // total number of internal cells to consider
		// we need at least a 2 x 3 maze to have more than zero internal cells
		if (total <= 0)
			return ;
		// check all inner cells and count their horizontal and vertical walls
		int vertical = 0 ;
		int horizontal = 0 ;
		for (int i = 1; i < w-1 ; i++) {
			for (int j = 1 ; j < h-1 ; j++) {
				if (cells.hasWallOnBottom(i, j))
					horizontal++ ;
				if (cells.hasWallOnTop(i, j))
					horizontal++ ;
				if (cells.hasWallOnLeft(i, j))
					vertical++ ;
				if (cells.hasWallOnRight(i, j))
					vertical++ ;
			}
		}
		// per cell we have at most 4 walls (2 horizontal, 2 vertical), so max # walls is 2 x total
		// check if required percentage per inner cell is met
		float percent = ((float)horizontal) / (float)(2 * total) ;
		assertTrue("Too few horizontal walls (in %):" + percent,percent >= requiredPercentage) ;
		percent = ((float)vertical) / (float)(2 * total) ;
		assertTrue("Too few vertical walls (in %):" + percent,percent >= requiredPercentage) ;
		
	}

	/**
	 * This method creates a maze for a given skill level with the help of the maze builder.
	 * The method waits for the maze builder thread to terminate before it proceeds. 
	 */
	private void mySetUp(int skill) {
		MazeBuilder mazebuilder = createMazeBuilder();
		// lets pick the smallest configuration from Maze.java
		mazew = Constants.SKILL_X[skill] ; //skill_x[skill];
		mazeh = Constants.SKILL_Y[skill] ; // skill_y[skill];
		int roomcount = Constants.SKILL_ROOMS[skill] ; // skill_rooms[skill];
		int partc = Constants.SKILL_PARTCT[skill] ; // skill_partct[skill]
		mazebuilder.build(this, mazew, mazeh, roomcount, partc);
		// give mazebuilder thread a chance to proceed
		try {
			// Variant 1: wait long enough for thread to finish, 
			// problem: no good way to estimate computation time for thread
			//Thread.currentThread().sleep(100);
			// Variant 2: wait for thread to terminate
			mazebuilder.buildThread.join() ;
		} catch (Exception e) { 
			fail("mySetup(): join synchronization with builder thread should not lead to an exception") ;
		}
		// sanity check to recognize gross errors early:
		// see if the maze builder computed anything
		String msg = "MazeBuilder thread did not deliver results, check for unhandled exception: " ;
		assertTrue(msg + " no cells", mazecells != null) ;
		assertTrue(msg + " no bspnode", root != null) ;
		assertTrue(msg + " no distance matrix", dists != null) ;
		assertTrue("Starting position out of bounds", 0 <= startx && startx <= mazew) ;
		assertTrue("Starting position out of bounds", 0 <= starty && starty <= mazeh) ;
	}
	/**
	 * Debug output method to dump distances and borders to system out
	 */
	private void dbg() {
		System.out.println(mazecells.toString()) ;
		for (int i = 0 ; i < mazew ; i++)
			for (int j = 0 ; j < mazeh ; j++)
				System.out.println("dist " + i + ", " + j + ": " + dists[i][j]);
		for (int i = 0 ; i < mazew ; i++)
		{
			for (int j = 0 ; j < mazeh ; j++)
			System.out.println("Borders: " + i + ", " + j + ": b:" + mazecells.hasWallOnBottom(i, j)+ " l:" + mazecells.hasWallOnLeft(i, j) + " r:" + mazecells.hasWallOnRight(i, j)+ " t:" + mazecells.hasWallOnTop(i, j));
		}
	}
	/**
	 * This method evaluates several conditions for the correct setup of each individual cell in the maze.
	 * For each cell: <p>
	 * Check if a cell has at least one neighbor that it connects to.<br>
	 * Check if a cell has a neighbor that is closer to the exit.<br> 
	 * Check if walls are symmetric in the sense that if a wall prohibits a movement from cell A to cell B
	 * then there must also be a wall that prohibits a movement from cell B to cell A.<br>
	 * Check if there is no wall between A and B, then there is also none between B and A.
	 * 
	 * Note that if each cell has a neighbor cell that is closer to the exit, i.e., its distance is 
	 * one less, then one could successively follow a path along those neighbors to an exit. 
	 * This establishes an argument for existence of a path without performing an explicit search
	 * through the maze. 
	 */
	private void checkNeighbors(Cells c, int[][] dists, int w, int h)
	{
		int dx ; 
		int dy ;
		int bitmask ;
		int opposite ;
		int nx ; // neighbor x coordinate
		int ny ; //neighbor y coordinate
		boolean hasNeighbor = false ;
		boolean hasSolution = false ;
		final int[] masks = Constants.MASKS ;
		// consider all cells
		for (int i = 0 ; i < w ; i++)
			for (int j = 0 ; j < h ; j++)
			{
				hasNeighbor = false ;
				hasSolution =  (dists[i][j] == 1) ? true : false ;
				// for every direction
				for (int k=0 ; k < 4 ; k++)
				{
					dx = Constants.DIRS_X[k] ;
					dy = Constants.DIRS_Y[k] ;
					bitmask = masks[k] ;
					nx = i + dx ;
					ny = j + dy ;
					// Symmetry condition of walls
					// check if there is a wall on either side of this pair
					if (c.hasMaskedBitsTrue(i, j, bitmask))
					{
						// if neighbor is within legal range of values, i.e. it is on the board
						if ((0 <= nx) && (nx < w) && (0 <= ny) && (ny < h))
						{
							opposite = masks[(k+2) & 3] ; // get the opposite direction
							assertTrue("Position " + i + "," + j + " Neighbor:" + nx + "," + ny, c.hasMaskedBitsTrue(nx, ny, opposite)) ;
						}
					}
					// Symmetry condition for absence of walls
					// if there is no wall, then there is none on both sides
					if (c.hasMaskedBitsFalse(i, j, bitmask))
					{
						// if neighbor is within legal range of values, i.e. it is on the board
						if ((0 <= nx) && (nx < w) && (0 <= ny) && (ny < h))
						{
							opposite = masks[(k+2) & 3] ; // get the opposite direction
							assertTrue("Position " + i + "," + j + " Neighbor:" + nx + "," + ny, c.hasMaskedBitsFalse(nx, ny, opposite)) ;
							// Keep track if cell has a neighbor and if it gets you closer to the exit
							hasNeighbor = true ; // no walls in between so this node has a neighbor
							if (!hasSolution && (dists[nx][ny] < dists[i][j]))
								hasSolution = true ; // no solution yet, but a neighbor is closer to the exit
						}
					}
				}
				// we tried all directions, so we must have found a neighbor and a step towards a solution
				assertTrue("No neighbor at position " + i + "," + j,hasNeighbor) ;
				assertTrue("No neighbor towards exit at position " + i + "," + j, hasSolution) ;
			}
	}
	/**
	 * Check border of the grid to see if the maze is surrounded by walls but for the one and only exit position.
	 * The exit position is recognized by dists[x][y]=1 and does not have a border to exit the maze.
	 * All other cells have dists[x][y] > 1 and must have border/wall on its corresponding side.<p>
	 * x = 0        => wall on left <br>
	 * x = width-1  => wall on right <br>
	 * y = 0        => wall on top <br>
	 * y = height-1 => wall on bottom <br>
	 */
	private void checkBorder(Cells c, int[][] dists, int w, int h)
	{
		int exitx = 0 ; // x coord
		int exity = 0 ; // y coord
		int exitc = 0 ; // counter
		for (int i = 0 ; i < w ; i++)
		{
			if (dists[i][0] != 1)
				assertTrue("Position " + i + "," + "0",c.hasWallOnTop(i, 0)) ;
			else {
				// check if exit is on one of 2 corners
				if (i == 0)
					assertTrue("Position 0,0" ,c.hasNoWallOnTop(0, 0)||c.hasNoWallOnLeft(0, 0)) ;
				else if (i == w-1)
					assertTrue("Position "+ (w-1) + ",0" ,c.hasNoWallOnTop(w-1, 0)||c.hasNoWallOnRight(w-1, 0)) ;
				else
					assertFalse("Position " + i + "," + "0",c.hasWallOnTop(i, 0)) ;
				exitc++ ;
				exitx = i ;
				exity = 0 ;
			}
			if (dists[i][h-1] != 1)
				assertTrue("Position " + i + "," + (h-1),c.hasWallOnBottom(i, h-1)) ;
			else {
				// check if exit is on one of 2 corners
				if (i == 0)
					assertTrue("Position " + 0 + "," + (h-1),c.hasNoWallOnLeft(0, h-1)||c.hasNoWallOnBottom(0, h-1)) ;
				else
				if (i == w-1)
					assertTrue("Position " + (w-1) + "," + (h-1),c.hasNoWallOnBottom(w-1, h-1)||c.hasNoWallOnRight(w-1, h-1)) ;
				else
					assertFalse("Position " + i + "," + (h-1),c.hasWallOnBottom(i, h-1)) ;
				exitc++ ;
				exitx = i ;
				exity = h-1 ;
			}
		}
		// note: all four corners have already been checked, so we can skip those cases below 
		for (int j = 1 ; j < h-1 ; j++)
		{
			if (dists[0][j] != 1)
				assertTrue("Position " + "0" + "," + j,c.hasWallOnLeft(0, j)) ;
			else {
				// check if exit is on one of 2 corners
				/*
				if (j == 0)
					assertTrue("Position 0,0" ,c.hasNoWallOnTop(0, j)||c.hasNoWallOnLeft(0, j)) ;
				else if (j == h-1)
					assertTrue("Position 0,"+j ,c.hasNoWallOnBottom(0, j)||c.hasNoWallOnLeft(0, j)) ;
				else
					assertFalse("Position " + "0," + j,c.hasWallOnLeft(0, j)) ;
					*/
				assertFalse("Position " + "0," + j,c.hasWallOnLeft(0, j)) ;
				exitc++ ;
				exitx = 0 ;
				exity = j ;
			}
			if (dists[w-1][j] != 1)
				assertTrue("Position " + (w-1) + "," + j,c.hasWallOnRight(w-1, j)) ;
			else {
				// check if exit is on one of 2 corners
				/*
				if (j == 0)
					assertTrue("Position " + (w-1)  + "," + j,c.hasNoWallOnTop(w-1 , j)||c.hasNoWallOnRight(w-1 , j)) ;
				else if (j == h-1)
					assertTrue("Position " + (w-1) + "," + j,c.hasNoWallOnBottom(w-1, j)||c.hasNoWallOnRight(w-1, j)) ;
				else
					assertFalse("Position " + (w-1) + "," + j,c.hasWallOnRight(w-1, j)) ;
					*/
				assertFalse("Position " + (w-1) + "," + j,c.hasWallOnRight(w-1, j)) ;
				exitc++ ;
				exitx = w-1 ;
				exity = j ;
			}
		}
		// we assume that there is only one exit
		assertTrue(1 == exitc) ;
		System.out.println("Exit position is at " + exitx + " " + exity);
	}
}

