package falstad;

import java.util.ArrayList;

import falstad.Robot.Direction;
import falstad.Robot.Turn;

public class Wizard extends ManualDriver implements RobotDriver {
	
	int[] EAST = {1, 0};
	int[] WEST = {-1, 0};
	int[] SOUTH = {0,-1};
	int[] NORTH = {0, 1};
	

	public Wizard() {
		super();	
	}

	@Override
	public boolean drive2Exit()  {
		while(!robot.isAtGoal() ){
			try {
				move();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		goThroughExit();
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
			
		} else if(exitRight == Integer.MAX_VALUE){
			try {
				robot.rotate(Turn.RIGHT);
				robot.move(1);
				this.numSteps++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(exitBackwards == Integer.MAX_VALUE){
			try {
				robot.rotate(Turn.AROUND);
				robot.move(1);
				this.numSteps++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
	
	/**
	 * moves the robot to the cell that is the shortest distance away
	 * @throws Exception
	 */
	private void move() throws Exception{
		
		//returns the direction of the cell that is the shortest distance
		Direction shortDist = distCheck();
		
		//need to move in that direction, based off of absolute direction		
		if(shortDist == Direction.FORWARD){
			robot.move(1);
			this.numSteps++;
			return;
		} else if(shortDist == Direction.BACKWARD){
			robot.rotate(Turn.AROUND);
			robot.move(1);
			this.numSteps++;
			return;
		} else if(shortDist == Direction.LEFT){
			robot.rotate(Turn.LEFT);
			robot.move(1);
			this.numSteps++;
			return;
		} else if(shortDist == Direction.RIGHT){
			robot.rotate(Turn.RIGHT);
			robot.move(1);
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
