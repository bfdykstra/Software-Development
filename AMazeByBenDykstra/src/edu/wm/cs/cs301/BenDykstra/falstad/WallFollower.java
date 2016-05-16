package edu.wm.cs.cs301.BenDykstra.falstad;

import android.os.Handler;
import android.util.Log;
import edu.wm.cs.cs301.BenDykstra.falstad.Robot.Direction;
import edu.wm.cs.cs301.BenDykstra.falstad.Robot.Turn;

public class WallFollower extends ManualDriver implements RobotDriver {
	
	//Handler handler = new Handler();

	public WallFollower() {
		super();
		
	}
	
	
	Runnable mLeft = new Runnable(){
		public void run(){
			try {
				robot.rotate(Turn.LEFT);
				robot.move(1);
				numSteps++;
				drive2Exit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//play.shortcut(numSteps, (int) getEnergyConsumption(), robot.hasStopped());
			}
			
		}
	};
	
	@Override
	/**
	 * Drives the robot to the exit by following the wall on the left
	 */
	public boolean drive2Exit() throws Exception {
		//check each sensor and find one that is zero. rotate so that wall is on left side. If none are zero
		// go to the shortest one away and get wall on left side
		Log.v("!!!!!", "In WallFollower drive2Exit");
		
		
		if(isPaused){
			return true;
		}else{
			if(!robot.isAtGoal()){
				int left = robot.distanceToObstacle(Direction.LEFT);
				int forward = robot.distanceToObstacle(Direction.FORWARD);
				if(left == 0 && forward == 0){
					handler.postDelayed(mRotateRight, 200);
				}else if(forward == 0 && left != 0){
					handler.postDelayed(mLeft, 200);
				}else if(forward != 0 && left != 0){
					handler.postDelayed(mLeft, 200);
				}else if(forward != 0 && left == 0){
					handler.postDelayed(mMoveForward, 200);
				}
			}else{
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
				//robot.move(1);
				handler.postDelayed(mForwardFinish, 200);
				//this.numSteps++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(exitLeft == Integer.MAX_VALUE){
			try {
				//robot.rotate(Turn.LEFT);
				//robot.move(1);
				handler.postDelayed(mLeftFinish, 200);
				//this.numSteps++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else{
			try {
				//robot.rotate(Turn.AROUND);
				//robot.move(1);
				handler.postDelayed(mRotateRight, 200);
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
	/*
	protected void orient() throws Exception{
		int left = robot.distanceToObstacle(Direction.LEFT);
		int forward = robot.distanceToObstacle(Direction.FORWARD);
		//if left == 0 dont do anything. if forward == 0 turn left. if forward and left = 0, turn right
		if(left == 0 && forward == 0){
			//robot.rotate(Turn.RIGHT);
			handler.postDelayed(mRotateRight, 200);
		} else if( forward == 0 && (left != 0)){
			//robot.rotate(Turn.LEFT);
			handler.postDelayed(mRotateLeft, 200);
		} else if(forward != 0 && left != 0){
			handler.postDelayed(mRotateLeft, 200);
		}
	}*/
	
	/**
	 * follows the wall on the left until robot reaches the exit
	 * @return true if robot reaches exit, false otherwise.
	 * @throws Exception if robot runs out of battery or hits a wall
	 */
	protected boolean followWall() throws Exception{
		
		//orient();
		//get forward dist to obstacle.
		int forwardDist = robot.distanceToObstacle(Direction.FORWARD);
		//move forward a step at a time and check isAtGoal each step
		/*
		int x = 0;
		int left = 0;
		if(left == 0 && x < forwardDist){
			boolean goal = robot.isAtGoal();
			if(goal){
				return true;
			}
			robot.move(1);
			this.numSteps++;
			x++;
			left = robot.distanceToObstacle(Direction.LEFT);
		}*/
		if(forwardDist != 0){
			handler.postDelayed(mMoveForward, 200);
			this.numSteps++;
			if(robot.isAtGoal()){
				return true;
			}
		}else{
			followWall();
		}
		
		/*
		while(left == 0 && x < forwardDist){
			boolean goal = robot.isAtGoal();
			if(goal){
				return true;
			}
			robot.move(1);
			this.numSteps++;
			x++;
			left = robot.distanceToObstacle(Direction.LEFT);
		}*/
		return false;
	}
	
	Runnable mRotateRight = new Runnable(){
		public void run(){
			try {
				robot.rotate(Turn.RIGHT);
				drive2Exit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	
	
	Runnable mMoveForward = new Runnable(){
		public void run(){
			try {
				robot.move(1);
				numSteps++;
				drive2Exit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	Runnable mForwardFinish = new Runnable(){
		public void run(){
			try {
				robot.move(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//play.shortcut(numSteps, (int) getEnergyConsumption(), robot.hasStopped());
			}
		}
	};
	
	Runnable mBackwardFinish = new Runnable(){
		public void run(){
			try {
				robot.rotate(Turn.AROUND);
				robot.move(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//play.shortcut(numSteps, (int) getEnergyConsumption(), robot.hasStopped());
			}
			
		}
	};
	
	
	
	Runnable mLeftFinish = new Runnable(){
		public void run(){
			try {
				robot.rotate(Turn.LEFT);
				robot.move(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//play.shortcut(numSteps, (int) getEnergyConsumption(), robot.hasStopped());
			}
			
		}
	};
	
	
	
	

}
