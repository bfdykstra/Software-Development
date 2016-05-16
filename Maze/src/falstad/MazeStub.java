package falstad;

public class MazeStub extends Maze {
	
	
	
	/**
	 * Method obtains a new Mazebuilder and has it compute new maze, 
	 * it is only used in keyDown()
	 * @param skill level determines the width, height and number of rooms for the new maze
	 */
	protected void build(int skill) {
		
		mazebuilder = new MazeBuilder(true);
		
		// adjust settings and launch generation in a separate thread
		mazew = Constants.SKILL_X[skill];
		mazeh = Constants.SKILL_Y[skill];
		mazebuilder.build(this, mazew, mazeh, Constants.SKILL_ROOMS[skill], Constants.SKILL_PARTCT[skill]);
		
		//mazebuilder performs in a separate thread and calls back by calling newMaze() (below) to return newly generated maze
	}
	
	protected void buildWithEller(int skill){
		mazebuilder = new MazeBuilderEller(true);
		
		mazew = Constants.SKILL_X[skill];
		mazeh = Constants.SKILL_Y[skill];
		mazebuilder.build(this, mazew, mazeh, Constants.SKILL_ROOMS[skill], Constants.SKILL_PARTCT[skill]);
		
	}
	
	@Override
	public void newMaze(BSPNode root, Cells c, Distance dists, int startx, int starty) {
		if (Cells.deepdebugWall)
		{   // for debugging: dump the sequence of all deleted walls to a log file
			// This reveals how the maze was generated
			c.saveLogFile(Cells.deepedebugWallFileName);
		}
		// adjust internal state of maze model
		
		mazecells = c ;
		mazedists = dists;
		rootnode = root ;
		setCurrentPosition(startx,starty) ;
		
		
	}
	
	
	protected void setCurrentPosition(int x, int y)
	{
		px = x ;
		py = y ;
	}
	
	
	
}
