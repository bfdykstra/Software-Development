package falstad;



import falstad.GraphicsWrapper.gwColor;
//import java.awt.Graphics;

public class MazeView extends DefaultViewer {

	Maze maze ; // need to know the maze model to check the state 
	// and to provide progress information in the generating state
	
	public MazeView(Maze m) {
		super() ;
		maze = m;
	}

	@Override
	public void redraw(GraphicsWrapper gc, int state, int px, int py, int view_dx,
			int view_dy, int walk_step, int view_offset, RangeSet rset, int ang, boolean stopped) {
		//dbg("redraw") ;
		switch (state) {
		case Constants.STATE_TITLE:
			redrawTitle(gc);
			break;
		case Constants.STATE_GENERATING:
			redrawGenerating(gc);
			break;
		case Constants.STATE_PLAY:
			// skip this one
			break;
		case Constants.STATE_FINISH:
			redrawFinish(gc);
			break;
		case Constants.STATE_LOST:
			redrawLose(gc);
			break;
		}
	}
	
	private void dbg(String str) {
		System.out.println("MazeView:" + str);
	}
	
	/**
	 * Helper method for redraw to draw the title screen, screen is hardcoded
	 * @param  gc graphics is the off screen image
	 */
	void redrawTitle(GraphicsWrapper gc) {
		gc.setColor(gwColor.white);
		gc.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		//gc.setSpecFont("Large");
		//FontMetrics fm = gc.getFontMetrics();
		gc.setColor(gwColor.red);
		gc.centerString(gc, "MAZE", 100, GraphicsWrapper.largeBannerFont);
		gc.setColor(gwColor.blue);
		//gc.setSpecFont("Small");
		gc.centerString(gc, "by Paul Falstad", 160, GraphicsWrapper.smallBannerFont);
		gc.centerString(gc, "www.falstad.com", 190, GraphicsWrapper.smallBannerFont);
		gc.setColor(gwColor.black);
		gc.centerString(gc, "To start, select a skill level.", 250, GraphicsWrapper.smallBannerFont);
		gc.centerString(gc,  "(Press a number from 0 to 9,", 300, GraphicsWrapper.smallBannerFont);
		gc.centerString(gc, "or a letter from A to F)", 320, GraphicsWrapper.smallBannerFont);
		gc.centerString(gc,  "v1.2", 350, GraphicsWrapper.smallBannerFont);
	}
	/**
	 * Helper method for redraw to draw final screen, screen is hard coded
	 * @param gc graphics is the off screen image
	 */
	void redrawFinish(GraphicsWrapper gc) {
		gc.setColor(gwColor.blue);
		gc.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		//gc.setSpecFont("Large");
		//FontMetrics fm = gc.getFontMetrics();
		gc.setColor(gwColor.yellow);
		gc.centerString(gc, "You won!", 100, GraphicsWrapper.largeBannerFont);
		gc.setColor(gwColor.orange);
		//gc.setSpecFont("Small");
		//fm = gc.getFontMetrics();
		gc.centerString(gc, "Congratulations!", 160, GraphicsWrapper.smallBannerFont);
		gc.centerString(gc, "Robot battery is: " + maze.robot.getBatteryLevel(), 180, GraphicsWrapper.smallBannerFont);
		gc.centerString(gc, "Robot took: " + maze.robot.getNumSteps() + " steps.", 200, GraphicsWrapper.smallBannerFont);
		gc.setColor(gwColor.white);
		gc.centerString(gc, "Hit any key to restart", 300, GraphicsWrapper.smallBannerFont);
	}
	
	/**
	 * Helper method for redraw to draw screen if robot stops, screen is hard coded, dynamic part it the battery and path length
	 * @param gc graphics is the off screen image
	 */
	void redrawLose(GraphicsWrapper gc) {
		gc.setColor(gwColor.blue);
		gc.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		//gc.setSpecFont("Large");
		//FontMetrics fm = gc.getFontMetrics();
		gc.setColor(gwColor.yellow);
		gc.centerString(gc, "You lost! :(", 100, GraphicsWrapper.largeBannerFont);
		gc.setColor(gwColor.orange);
		//gc.setSpecFont("Small");
		//fm = gc.getFontMetrics();
		//centerString(gc, fm, "Congratulations!", 160);
		gc.centerString(gc, "Robot battery is: " + maze.robot.getBatteryLevel(), 160, GraphicsWrapper.smallBannerFont);
		gc.centerString(gc, "Robot took: " + maze.robot.getNumSteps() + " steps.", 180, GraphicsWrapper.smallBannerFont);
		gc.setColor(gwColor.white);
		gc.centerString(gc, "Hit any key to restart", 300, GraphicsWrapper.smallBannerFont);
	}

	/**
	 * Helper method for redraw to draw screen during phase of maze generation, screen is hard coded
	 * only attribute percentdone is dynamic
	 * @param gc graphics is the off screen image
	 */
	void redrawGenerating(GraphicsWrapper gc) {
		gc.setColor(gwColor.yellow);
		gc.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		//gc.setSpecFont("Large");
		//FontMetrics fm = gc.getFontMetrics();
		gc.setColor(gwColor.red);
		gc.centerString(gc, "Building maze", 150, GraphicsWrapper.largeBannerFont);
		//gc.setSpecFont("Small");
		//fm = gc.getFontMetrics();
		gc.setColor(gwColor.black);
		gc.centerString(gc, maze.getPercentDone()+"% completed", 200, GraphicsWrapper.smallBannerFont);
		gc.centerString(gc, "Hit escape to stop", 300, GraphicsWrapper.smallBannerFont);
	}
	
	
	
}
