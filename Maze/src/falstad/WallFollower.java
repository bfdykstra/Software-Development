package falstad;

import falstad.Robot.Direction;
import falstad.Robot.Turn;

public class WallFollower extends ManualDriver implements RobotDriver {

	public WallFollower() {
		super();
		
	}
	
	@Override
	/**
	 * Drives the robot to the exit by following the wall on the left
	 */
	public boolean drive2Exit() throws Exception {
		//check each sensor and find one that is zero. rotate so that wall is on left side. If none are zero
		// go to the shortest one away and get wall on left side
		
		boolean initGoal = robot.isAtGoal();
		if(initGoal){
			return true;
		}
		boolean thereYet = false;
		while(!thereYet){
			boolean drive = followWall();
			if(drive){
				thereYet = true;
				goThroughExit();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * For use once the robot gets to the exit cell. Finds the direction of the exit and drives the robot through. 
	 */
	protected void goThroughExit(){
		int exitForward = robot.distanceToObstacle(Direction.FORWARD);
		int exitLeft = robot.distanceToObstacle(Direction.LEFT);
		if(exitForward == Integer.MAX_VALUE){
			try {
				robot.move(1);
				this.numSteps++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(exitLeft == Integer.MAX_VALUE){
			try {
				robot.rotate(Turn.LEFT);
				robot.move(1);
				this.numSteps++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else{
			try {
				robot.rotate(Turn.AROUND);
				robot.move(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Orients the robot so that a wall is always on the left.
	 * @throws Exception if robot runs out of battery
	 */
	protected void orient() throws Exception{
		int left = robot.distanceToObstacle(Direction.LEFT);
		int forward = robot.distanceToObstacle(Direction.FORWARD);
		//if left == 0 dont do anything. if forward == 0 turn left. if forward and left = 0, turn right
		if(left == 0 && forward == 0){
			robot.rotate(Turn.RIGHT);
		} else if( forward == 0 && (left != 0)){
			robot.rotate(Turn.LEFT);
		} else if(forward != 0 && left != 0){
			robot.rotate(Turn.LEFT);
		}
	}
	
	/**
	 * follows the wall on the left until robot reaches the exit
	 * @return true if robot reaches exit, false otherwise.
	 * @throws Exception if robot runs out of battery or hits a wall
	 */
	protected boolean followWall() throws Exception{
		
		orient();
		//get forward dist to obstacle.
		int forwardDist = robot.distanceToObstacle(Direction.FORWARD);
		//move forward a step at a time and check isAtGoal each step
		int x = 0;
		
		int left = 0;
		while(left == 0 && x < forwardDist){
			boolean goal = robot.isAtGoal();
			if(goal){
				return true;
			}
			robot.move(1);
			this.numSteps++;
			x++;
			left = robot.distanceToObstacle(Direction.LEFT);
		}
		return false;
	}

}
