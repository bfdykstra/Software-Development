package edu.wm.cs.cs301.BenDykstra.falstad;

import android.os.Handler;
import edu.wm.cs.cs301.BenDykstra.falstad.Robot.Turn;
import edu.wm.cs.cs301.BenDykstra.ui.PlayActivity;

/**
 * A Driver that operates on a Robot
 * @author Benjamin Dykstra
 *
 */
public class ManualDriver implements RobotDriver {
	
	Robot robot;
	int width;
	int height;
	Distance distance;
	int numSteps = 0;
	PlayActivity play;
	Handler handler = new Handler();
	boolean isPaused = false;
	
	public Runnable mShowMaze = new Runnable(){
		public void run(){
			play.getMaze().showMaze();
		}
		
	};
	

	public ManualDriver() {
		
	}

	@Override
	public void setRobot(Robot r) {
		this.robot = (BasicRobot) r;
	}

	@Override
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	@Override
	public boolean drive2Exit() throws Exception {
		return true;
	}

	@Override
	public float getEnergyConsumption() {
		float consumption = 2500 - robot.getBatteryLevel();
		return consumption;
	}

	@Override
	public int getPathLength() {
		return this.numSteps;
	}
	
	/**
	 * called in SimpleKeyListener of maze for certain keys, only the arrow keys specifically
	 */
	protected void passToRobot(int key){
		//System.out.println("In passToRobot, using this key: " + key);
		switch(key){
		case 104:
			try {
				robot.rotate(Turn.LEFT);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 108:
			try {
				robot.rotate(Turn.RIGHT);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 107:
			try {
				robot.move(1);
				this.numSteps ++;
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 106:
			try{
				robot.rotate(Turn.AROUND);
				robot.move(1);
				this.numSteps++;
			} catch(Exception e){
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void setPlayAct(PlayActivity play) {
		this.play = play;
		
	}

	@Override
	public Handler getHandler() {
		
		return this.handler;
	}
	
	/**
	 * toggles the paused flag
	 */
	public void pause(){
		isPaused = !isPaused;
		try {
			drive2Exit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
