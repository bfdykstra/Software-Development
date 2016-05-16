package edu.wm.cs.cs301.BenDykstra.falstad;

import java.util.ArrayList;

import android.os.Handler;
import android.util.Log;
import edu.wm.cs.cs301.BenDykstra.falstad.Robot.Direction;
import edu.wm.cs.cs301.BenDykstra.falstad.Robot.Turn;
import edu.wm.cs.cs301.BenDykstra.ui.PlayActivity;

public class Wizard extends ManualDriver implements RobotDriver {
	
	int[] EAST = {1, 0};
	int[] WEST = {-1, 0};
	int[] SOUTH = {0,-1};
	int[] NORTH = {0, 1};
	//Handler handler  = new Handler();
	
	

	public Wizard() {
		super();	
	}
	

	@Override
	public boolean drive2Exit()  {
		Log.v("!!!!", "in wizard drive2Exit");
		
		if(isPaused){
			return true;
		}else{
			if(!robot.isAtGoal()){
				try {
					move();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}else{
				goThroughExit();
				return true;
			}
		}
		
		
		
		return true;
	}
	
	/**
	 * For use once the robot gets to the exit cell. Goes through the exit
	 */
	protected void goThroughExit(){
		int exitForward = robot.distanceToObstacle(Direction.FORWARD);
		int exitLeft = robot.distanceToObstacle(Direction.LEFT);
		int exitRight = robot.distanceToObstacle(Direction.RIGHT);
		int exitBackwards = robot.distanceToObstacle(Direction.BACKWARD);
		if(exitForward == Integer.MAX_VALUE){
			try {
				//robot.move(1);
				handler.postDelayed(mForwardFinish, 200);
				this.numSteps++;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(exitLeft == Integer.MAX_VALUE){
			try {
				//robot.rotate(Turn.LEFT);
				//robot.move(1);
				//play.handler.postDelayed(mRotateLeft, 200);
				handler.postDelayed(mLeftFinish, 200);
				this.numSteps++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if(exitRight == Integer.MAX_VALUE){
			try {
				//robot.rotate(Turn.RIGHT);
				//robot.move(1);
				//play.handler.postDelayed(mRotateRight, 200);
				handler.postDelayed(mRightFinish, 200);
				this.numSteps++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(exitBackwards == Integer.MAX_VALUE){
			try {
				//robot.rotate(Turn.AROUND);
				//robot.move(1);
				//play.handler.postDelayed(mRotateAround, 200);
				handler.postDelayed(mBackwardFinish, 200);
				this.numSteps++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//play.shortcut(numSteps, (int) this.getEnergyConsumption(), this.robot.hasStopped());
		//handler.postDelayed(mSwitchToFinish, 200);
	}
	
	
	/**
	 * a method to find the distances around the wizard
	 * @throws Exception
	 * @returns the direction relative to the robot that the shortest distance is.
	 */
	private Direction distCheck() throws Exception{
		int[][] distArray = this.distance.getDists();
		int i = this.robot.getCurrentPosition()[0];
		int j = this.robot.getCurrentPosition()[1];
		
		int maxNdxWidth = this.width - 1;
		int maxNdxHeight = this.height - 1;
		int refCell = distArray[i][j];
		
		ArrayList<Integer> distList = new ArrayList<Integer>();
		ArrayList<Direction> directionList = new ArrayList<Direction>();
		
		//get initial distances
		if((j + 1) <= maxNdxHeight ){
			
			int northCell = distArray[i][j+1];
			if(northCell < refCell){
				//need to determine which way we're facing so that robot can turn and move the correct way
				int[] curDir = this.robot.getCurrentDirection();
				//facing north
				if(this.arrayEquals(curDir, NORTH)){
					int check = this.robot.distanceToObstacle(Direction.FORWARD);
					if(check != 0){
						directionList.add(Direction.FORWARD);
					}
					
				}
				//facing east
				else if(this.arrayEquals(curDir, EAST)){
					int check = this.robot.distanceToObstacle(Direction.LEFT);
					if(check != 0){
						directionList.add(Direction.LEFT);
					}	
				} 
				//facing west
				else if(this.arrayEquals(curDir, WEST)){
					int check = this.robot.distanceToObstacle(Direction.RIGHT);
					if(check != 0){
						directionList.add(Direction.RIGHT);
					}	
				}
				//facing south
				else if(this.arrayEquals(curDir, SOUTH)){
					int check = this.robot.distanceToObstacle(Direction.BACKWARD);
					if(check != 0){
						directionList.add(Direction.BACKWARD);
					}	
				}
				//mostly for checking at this point
				distList.add(northCell);
			}
			
		}
		if((i + 1) <= maxNdxWidth ){
			int eastCell = distArray[i+ 1][j];
			
			if(eastCell < refCell){
				//need to determine which way we're facing so that robot can turn and move the correct way
				int[] curDir = this.robot.getCurrentDirection();
				//facing north
				if(this.arrayEquals(curDir, NORTH)){
					int check = this.robot.distanceToObstacle(Direction.RIGHT);
					if(check != 0){
						directionList.add(Direction.RIGHT);
					}	
				}
				//facing east
				else if(this.arrayEquals(curDir, EAST)){
					int check = this.robot.distanceToObstacle(Direction.FORWARD);
					if(check != 0){
						directionList.add(Direction.FORWARD);
					}	
				} 
				//facing west
				else if(this.arrayEquals(curDir, WEST)){
					int check = this.robot.distanceToObstacle(Direction.BACKWARD);
					if(check != 0){
						directionList.add(Direction.BACKWARD);
					}	
				}
				//facing south
				else if(this.arrayEquals(curDir, SOUTH)){
					int check = this.robot.distanceToObstacle(Direction.LEFT);
					if(check != 0){
						directionList.add(Direction.LEFT);
					}	
				}
				distList.add(eastCell);
			}
			
		}
		if((j - 1) >= 0 ){
			int southCell = distArray[i][j-1];
			if(southCell < refCell){
				//need to determine which way we're facing so that robot can turn and move the correct way
				int[] curDir = this.robot.getCurrentDirection();
				//facing north
				if(this.arrayEquals(curDir, NORTH)){
					int check = this.robot.distanceToObstacle(Direction.BACKWARD);
					if(check != 0){
						directionList.add(Direction.BACKWARD);
					}	
				}
				//facing east
				else if(this.arrayEquals(curDir, EAST)){
					int check = this.robot.distanceToObstacle(Direction.RIGHT);
					if(check != 0){
						directionList.add(Direction.RIGHT);
					}	
				} 
				//facing west
				else if(this.arrayEquals(curDir, WEST)){
					int check = this.robot.distanceToObstacle(Direction.LEFT);
					if(check != 0){
						directionList.add(Direction.LEFT);
					}	

				}
				//facing south
				else if(this.arrayEquals(curDir, SOUTH)){
					int check = this.robot.distanceToObstacle(Direction.FORWARD);
					if(check != 0){
						directionList.add(Direction.FORWARD);
					}	
				}
				distList.add(southCell);	
			}	
		}
		if((i - 1) >= 0 ){
			int westCell = distArray[i-1][j];
			if(westCell < refCell){
				//need to determine which way we're facing so that robot can turn and move the correct way
				int[] curDir = this.robot.getCurrentDirection();
				//facing north
				if(this.arrayEquals(curDir, NORTH)){
					int check = this.robot.distanceToObstacle(Direction.LEFT);
					if(check != 0){
						directionList.add(Direction.LEFT);
					}	
				}
				//facing east
				else if(this.arrayEquals(curDir, EAST)){
					int check = this.robot.distanceToObstacle(Direction.BACKWARD);
					if(check != 0){
						directionList.add(Direction.BACKWARD);
					}	
				} 
				//facing west
				else if(this.arrayEquals(curDir, WEST)){
					int check = this.robot.distanceToObstacle(Direction.FORWARD);
					if(check != 0){
						directionList.add(Direction.FORWARD);
					}	
				}
				//facing south
				else if(this.arrayEquals(curDir, SOUTH)){
					int check = this.robot.distanceToObstacle(Direction.RIGHT);
					if(check != 0){
						directionList.add(Direction.RIGHT);
					}
				}
				distList.add(westCell);
			}	
		}
		return directionList.get(0);
		
	}
	
	Runnable mForward = new Runnable(){
		public void run(){
			try {
				Log.v("!!!!!!", "In mMove called from the move method of wizard");
				robot.move(1);
				drive2Exit();
				//play.handler.postDelayed(this, 200);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//play.shortcut(numSteps, (int) getEnergyConsumption(), robot.hasStopped());
			}
		}
	};
	
	Runnable mBackward = new Runnable(){
		public void run(){
			try {
				robot.rotate(Turn.AROUND);
				robot.move(1);
				drive2Exit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//play.shortcut(numSteps, (int) getEnergyConsumption(), robot.hasStopped());
			}
			
		}
	};
	
	Runnable mRight = new Runnable(){
		public void run(){
			try {
				robot.rotate(Turn.RIGHT);
				robot.move(1);
				drive2Exit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//play.shortcut(numSteps, (int) getEnergyConsumption(), robot.hasStopped());
			}
			
		}
	};
	
	Runnable mLeft = new Runnable(){
		public void run(){
			try {
				robot.rotate(Turn.LEFT);
				robot.move(1);
				drive2Exit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//play.shortcut(numSteps, (int) getEnergyConsumption(), robot.hasStopped());
			}
			
		}
	};
	
	Runnable mForwardFinish = new Runnable(){
		public void run(){
			try {
				Log.v("!!!!!!", "In mMove called from the move method of wizard");
				robot.move(1);
				//play.handler.postDelayed(this, 200);
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
	
	Runnable mRightFinish = new Runnable(){
		public void run(){
			try {
				robot.rotate(Turn.RIGHT);
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
	
	
	
	/**
	 * moves the robot to the cell that is the shortest distance away
	 * @throws Exception
	 */
	private void move() throws Exception{
		
		//returns the direction of the cell that is the shortest distance
		Direction shortDist = distCheck();
		
		//need to move in that direction, based off of absolute direction		
		if(shortDist == Direction.FORWARD){
			//robot.move(1);
			Log.v("!!!!!", "In direction forward of wizard.move");
			handler.postDelayed(mForward, 50);
			//play.handler.postDelayed(mMove, 200);
			this.numSteps++;
			return;
		} else if(shortDist == Direction.BACKWARD){
			//robot.rotate(Turn.AROUND);
			//robot.move(1);
			Log.v("!!!!!", "In direction backward of wizard.move");
			handler.postDelayed(mBackward, 50);
			this.numSteps++;
			return;
		} else if(shortDist == Direction.LEFT){
			//robot.rotate(Turn.LEFT);
			//robot.move(1);
			Log.v("!!!!!", "In direction left of wizard.move");
			handler.postDelayed(mLeft, 50);
			this.numSteps++;
			return;
		} else if(shortDist == Direction.RIGHT){
			//robot.rotate(Turn.RIGHT);
			//robot.move(1);
			Log.v("!!!!!", "In direction right of wizard.move");
			handler.postDelayed(mRight, 50);
			this.numSteps++;
			return;
		}
		
	}
	
	/**
	 * tests whether two arrays are equal
	 * @param a1
	 * @param a2
	 * @return
	 */
	protected boolean arrayEquals(int[] a1, int[] a2){
		if(a1.length != a2.length){
			return false;
		}
		for(int i = 0; i < a1.length; i++){
			if( a1[i] != a2[i]){
				return false;
			}
		}
		return true;
	}
}
