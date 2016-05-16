package edu.wm.cs.cs301.BenDykstra.falstad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.os.Handler;
import android.util.Log;
import edu.wm.cs.cs301.BenDykstra.falstad.Robot.Turn;
import edu.wm.cs.cs301.BenDykstra.falstad.Robot.Direction;

public class CuriousMouse extends WallFollower implements RobotDriver {
	/**
	 * A class that randomly searches a maze. The mouse has a preference for routes that are less. Uses a hashmap to
	 * keep track of known cells and how many times they have been visited.
	 */
	
	HashMap<Integer, Integer> known; //hashmap with unique cell integer as key and # of visits as value
	int[][] cellArray;
	ArrayList<Direction> dirList; 
	int[] EAST = {1, 0};
	int[] WEST = {-1, 0};
	int[] SOUTH = {0,-1};
	int[] NORTH = {0, 1};
	SingleRandom random;
	Direction prevMove;
	int[] prevCell;
	//Handler handler = new Handler();
	
	public CuriousMouse() {
		super();
		known = new HashMap<Integer, Integer>();
		
		dirList = new ArrayList<Direction>();
		dirList.add(Direction.BACKWARD);
		dirList.add(Direction.FORWARD);
		dirList.add(Direction.LEFT);
		dirList.add(Direction.RIGHT);
		random = SingleRandom.getRandom();
		
	}
	
	@Override
	/**
	 * instantiates the cell array to be used as keys in the known set. Populates the cell array with
	 * unique values.
	 */
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
		this.cellArray = new int[width][height];
		populateCellArray();
	}
	
	/**
	 * populates the cell array and the known list with every cell having been visited zero times
	 */
	private void populateCellArray(){
		int num = 0;
		// (i,j) = row, column
		//width is number of colums, height is number of rows
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				this.cellArray[i][j] = num;
				known.put(num, 0);
				num++;
			}
		}
	}
	
	/**
	 * cycles through all possible directions and gets ones that are available to move to
	 * @return a list of directions that are possible moves
	 */
	private ArrayList<Direction> checkOptions(){
		ArrayList<Direction> possDirs = new ArrayList<Direction>();
		for(int i = 0; i < 4; i++){
			int dist = robot.distanceToObstacle(dirList.get(i));
			if(dist != 0){
				possDirs.add(dirList.get(i));
			}
		}
		return possDirs;
	}
	
	
	/**
	 * checks the moves to assess whether mouse has already visited cell. If the cell is not known, 
	 * then it is more likely to be picked
	 * @return a sorted list of directions to randomly choose
	 * @throws Exception 
	 */
	private ArrayList<Direction> checkKnown(ArrayList<Direction> possMoves) throws Exception{
		ArrayList<Direction> movesToChoose = new ArrayList<Direction>();
		int[] curDir = this.robot.getCurrentDirection();
		
		//current position janx to base directions off of
		int x = this.robot.getCurrentPosition()[0];
		int y = this.robot.getCurrentPosition()[1];
		
		int maxNdxWidth = this.width - 1;
		int maxNdxHeight = this.height - 1;
		
		if(this.arrayEquals(curDir, EAST)){
			
			ArrayList<Integer> numVisits = new ArrayList<Integer>(); //number of times cell has been visited
			
			for(int i = 0; i < possMoves.size(); i++){
				//The move that will be added to choose array
				Direction move = possMoves.get(i);
				
				//check all the possible move that it could be and check if that cell is already known.
				if(move == Direction.FORWARD){
					int eastCell = cellArray[x+1][y]; //key of cell
					int visits = known.get(eastCell);
					numVisits.add(visits);
				} else if(move == Direction.LEFT){
					int northCell = cellArray[x][y+1];
					int visits = known.get(northCell);
					numVisits.add(visits);
				} else if(move == Direction.BACKWARD){
					int westCell = cellArray[x - 1][y];
					int visits = known.get(westCell);
					numVisits.add(visits);
				} else if( move == Direction.RIGHT){
					int southCell = cellArray[x][y - 1];
					int visits = known.get(southCell);
					numVisits.add(visits);
				}
			}
			//iterate over val array and get lowest val and add that direction in to moves to choose
			Collections.sort(numVisits);
			//compares the number of visits for each cell. the moves to choose will be ordered
			for(int i = 0; i < numVisits.size(); i++){
				//compares the number of visits 
				if(x+1 <= maxNdxWidth){
					if(numVisits.get(i) == known.get(cellArray[x+1][y]) && !movesToChoose.contains(Direction.FORWARD) && possMoves.contains(Direction.FORWARD)){
						movesToChoose.add(Direction.FORWARD);
					}
				}
				if(y+ 1 <= maxNdxHeight){
					if(numVisits.get(i) == known.get(cellArray[x][y+1]) && !movesToChoose.contains(Direction.LEFT) && possMoves.contains(Direction.LEFT)){
						movesToChoose.add(Direction.LEFT);
					}
				}
				if(x-1 >= 0){
					if(numVisits.get(i) == known.get(cellArray[x-1][y]) && !movesToChoose.contains(Direction.BACKWARD) && possMoves.contains(Direction.BACKWARD)){
						movesToChoose.add(Direction.BACKWARD);
					}
				}
				if(y-1 >= 0){
					if(numVisits.get(i) == known.get(cellArray[x][y-1]) && !movesToChoose.contains(Direction.RIGHT) && possMoves.contains(Direction.RIGHT)){
						movesToChoose.add(Direction.RIGHT);
					}
					
				}
			}
			return movesToChoose;
			
		} else if(this.arrayEquals(curDir, NORTH)){
			ArrayList<Integer> numVisits = new ArrayList<Integer>(); //number of times cell has been visited
			
			for(int i = 0; i < possMoves.size(); i++){
				//The move that will be added to choose array
				Direction move = possMoves.get(i);
				
				//check all the possible move that it could be and check if that cell is already known.
				if(move == Direction.FORWARD){
					int cell = cellArray[x][y + 1];
					int visits = known.get(cell);
					numVisits.add(visits);
				} else if(move == Direction.LEFT){
					int cell = cellArray[x - 1][y];
					int visits = known.get(cell);
					numVisits.add(visits);
				} else if(move == Direction.BACKWARD){
					int cell = cellArray[x][y - 1];
					int visits = known.get(cell);
					numVisits.add(visits);
				} else if( move == Direction.RIGHT){
					int cell = cellArray[x + 1][y];
					int visits = known.get(cell);
					numVisits.add(visits);
				}
			}
			
			//iterate over val array and get lowest val and add that direction in to moves to choose
			Collections.sort(numVisits);
			//compares the number of visits for each cell. the moves to choose will be ordered
			for(int i = 0; i < numVisits.size(); i++){
				if(x+1 <= maxNdxWidth){
					if(numVisits.get(i) == known.get(cellArray[x+1][y]) && !movesToChoose.contains(Direction.RIGHT) && possMoves.contains(Direction.RIGHT)){
						movesToChoose.add(Direction.RIGHT);
					}	
				}
				if(y+1 <= maxNdxHeight){
					if(numVisits.get(i) == known.get(cellArray[x][y+1]) && !movesToChoose.contains(Direction.FORWARD) && possMoves.contains(Direction.FORWARD)){
						movesToChoose.add(Direction.FORWARD);
					}
				}
				if(x-1 >= 0){
					if(numVisits.get(i) == known.get(cellArray[x-1][y]) && !movesToChoose.contains(Direction.LEFT) && possMoves.contains(Direction.LEFT)){
						movesToChoose.add(Direction.LEFT);
					}
				}
				if(y-1 >= 0){
					if(numVisits.get(i) == known.get(cellArray[x][y-1]) && !movesToChoose.contains(Direction.BACKWARD) && possMoves.contains(Direction.BACKWARD)){
						movesToChoose.add(Direction.BACKWARD);
					}	
				}
			}
			return movesToChoose;
			
		} else if(this.arrayEquals(curDir, WEST)){
			ArrayList<Integer> numVisits = new ArrayList<Integer>(); //number of times cell has been visited
			for(int i = 0; i < possMoves.size(); i++){
				//The move that will be added to choose array
				Direction move = possMoves.get(i);
				
				//check all the possible move that it could be and check if that cell is already known.
				if(move == Direction.FORWARD){
					int cell = cellArray[x - 1][y];
					int visits = known.get(cell);
					numVisits.add(visits);
				} else if(move == Direction.LEFT){
					int cell = cellArray[x][y - 1];
					int visits = known.get(cell);
					numVisits.add(visits);
				} else if(move == Direction.BACKWARD){
					int cell = cellArray[x + 1][y];
					int visits = known.get(cell);
					numVisits.add(visits);
				} else if( move == Direction.RIGHT){
					int cell = cellArray[x][y + 1];
					int visits = known.get(cell);
					numVisits.add(visits);
				}
			}
			//iterate over val array and get lowest val and add that direction in to moves to choose
			Collections.sort(numVisits);
			//compares the number of visits for each cell. the moves to choose will be ordered
			for(int i = 0; i < numVisits.size(); i++){
				if(x+1 <= maxNdxWidth){
					if(numVisits.get(i) == known.get(cellArray[x+1][y]) && !movesToChoose.contains(Direction.BACKWARD) && possMoves.contains(Direction.BACKWARD)){
						movesToChoose.add(Direction.BACKWARD);
					}
				}
				if(y+1 <= maxNdxHeight){
					if(numVisits.get(i) == known.get(cellArray[x][y+1]) && !movesToChoose.contains(Direction.RIGHT) && possMoves.contains(Direction.RIGHT)){
					movesToChoose.add(Direction.RIGHT);
					}
				}
				if(x-1 >= 0){
					if(numVisits.get(i) == known.get(cellArray[x-1][y]) && !movesToChoose.contains(Direction.FORWARD) && possMoves.contains(Direction.FORWARD)){
					movesToChoose.add(Direction.FORWARD);
					}
				}
				if(y-1 >= 0){
					if(numVisits.get(i) == known.get(cellArray[x][y-1]) && !movesToChoose.contains(Direction.LEFT) && possMoves.contains(Direction.LEFT)){
					movesToChoose.add(Direction.LEFT);
					}
				}
			}
			return movesToChoose;
		} else if(this.arrayEquals(curDir, SOUTH)){
			ArrayList<Integer> numVisits = new ArrayList<Integer>(); //number of times cell has been visited
			for(int i = 0; i < possMoves.size(); i++){
				//The move that will be added to choose array
				Direction move = possMoves.get(i);
				
				//check all the possible move that it could be and check if that cell is already known.
				if(move == Direction.FORWARD){
					int cell = cellArray[x][y - 1];
					int visits = known.get(cell);
					numVisits.add(visits);
				} else if(move == Direction.LEFT){
					int cell = cellArray[x + 1][y];
					int visits = known.get(cell);
					numVisits.add(visits);
				} else if(move == Direction.BACKWARD){
					int cell = cellArray[x][y + 1];
					int visits = known.get(cell);
					numVisits.add(visits);
				} else if( move == Direction.RIGHT){
					int cell = cellArray[x - 1][y];
					int visits = known.get(cell);
					numVisits.add(visits);
				}
			}
			//iterate over val array and get lowest val and add that direction in to moves to choose
			Collections.sort(numVisits);
			//compares the number of visits for each cell. the moves to choose will be ordered
			for(int i = 0; i < numVisits.size(); i++){
				if(x + 1 <= maxNdxWidth){
					if(numVisits.get(i) == known.get(cellArray[x+1][y]) && !movesToChoose.contains(Direction.LEFT) && possMoves.contains(Direction.LEFT)){
						movesToChoose.add(Direction.LEFT);
					}
				}
				if(y+1 <= maxNdxHeight){
					if(numVisits.get(i) == known.get(cellArray[x][y+1]) && !movesToChoose.contains(Direction.BACKWARD) && possMoves.contains(Direction.BACKWARD)){
					movesToChoose.add(Direction.BACKWARD);
					}
				}
				if(x -1 >= 0){
					if(numVisits.get(i) == known.get(cellArray[x-1][y]) && !movesToChoose.contains(Direction.RIGHT) && possMoves.contains(Direction.RIGHT)){
					movesToChoose.add(Direction.RIGHT);
					}
				}
				if(y -1 >= 0){
					if(numVisits.get(i) == known.get(cellArray[x][y-1]) && !movesToChoose.contains(Direction.FORWARD) && possMoves.contains(Direction.FORWARD)){
					movesToChoose.add(Direction.FORWARD);
					}
				}
			}
			return movesToChoose;
			
		}
		return null;
	}
	
	/**
	 * executes the randomly selected move based on the given direction
	 * @param theMove the direction in which to move.
	 * @throws Exception if robot cannot move that way or runs out of battery.
	 */
	private void randMove(Direction theMove) throws Exception{
		
		if(theMove == Direction.FORWARD){
			//robot.move(1);
			handler.postDelayed(mForward, 200);
			//this.numSteps++;
			return;
		} else if(theMove == Direction.BACKWARD){
			//robot.rotate(Turn.AROUND);
			//robot.move(1);
			handler.postDelayed(mBackward, 200);
			//this.numSteps++;
			return;
		} else if(theMove == Direction.LEFT){
			//robot.rotate(Turn.LEFT);
			//robot.move(1);
			handler.postDelayed(mLeft, 200);
			//this.numSteps++;
			return;
		} else if(theMove == Direction.RIGHT){
			//robot.rotate(Turn.RIGHT);
			//robot.move(1);
			handler.postDelayed(mRight, 200);
			//this.numSteps++;
			return;
		}
	}
	
	@Override
	/**
	 * Randomly drives the mouse to the exit. If the mouse is in a room, will follow the left wall until it gets out of the room.
	 */
	public boolean drive2Exit() throws Exception {
		
		Log.v("!!!!!", "In CuriousMouse drive2Exit");
		
		if(isPaused){
			return true;
		}else{
			if(! this.robot.isAtGoal()){
				
				//1. check options
				ArrayList<Direction> possMoves = checkOptions();
				
				//2. check known cells
				ArrayList<Direction> movesToChoose = checkKnown(possMoves);
				
				//3. choose a random direction out of those possible candidates
				if(movesToChoose.size() == 1){
					Direction theMove = movesToChoose.get(0);
					randMove(theMove);
				}else if(movesToChoose.size() == 2){
					Direction theMove;
					int randNdx = random.nextIntWithinInterval(0, 9);
					// 80% chance for less visited one, 20% for other
					if(randNdx <= 7){
						theMove = movesToChoose.get(0);
					}else{
						theMove = movesToChoose.get(1);
					}
					randMove(theMove);
				}else if(movesToChoose.size() == 3){
					Direction theMove;
					int randNdx = random.nextIntWithinInterval(0, 9);
					//%80 chance for less visited one, 10% each for others
					if(randNdx <= 7){
						theMove = movesToChoose.get(0);
					}else if(randNdx == 8){
						theMove = movesToChoose.get(1);
					}else{
						theMove = movesToChoose.get(2);
					}
					randMove(theMove);
				}else if(movesToChoose.size() == 4){
					Direction theMove;
					int randNdx = random.nextIntWithinInterval(0, 9);
					//%70 chance for less visited one, 10% each for others
					if(randNdx <= 6){
						theMove = movesToChoose.get(0);
					}else if(randNdx == 7){
						theMove = movesToChoose.get(1);
					}else if(randNdx == 8){
						theMove = movesToChoose.get(2);
					}else {
						theMove = movesToChoose.get(3);
					}
					randMove(theMove);
				}
				
				int x = this.cellArray[this.robot.getCurrentPosition()[0]][this.robot.getCurrentPosition()[1]];
				
				int numTimesVisited = known.get(x); 
				numTimesVisited++;
				known.put(x, numTimesVisited );	
				
			}else{
				goThroughExit();
			}
		}
		
		
		
		return true;
	}
	
	/**
	 * Orients the mouse in a room so that a wall is always on the left side.
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
	 * Calls moveAlongWall while the mouse is still inside a room.
	 * @throws Exception
	 */
	private void mouseFollowWall() throws Exception{
		
		while(robot.isInsideRoom()){
			moveAlongWall();
		}
	}
	
	/**
	 * moves along a wall so that the wall is always on the left side
	 * @throws Exception if robot runs into a wall or runs out of battery
	 */
	private void moveAlongWall() throws Exception{
		//get forward dist to obstacle.
		orient();
		int forwardDist = robot.distanceToObstacle(Direction.FORWARD);
		int left = 0;
		int x = 0;
		while(left == 0 && x < forwardDist && robot.isInsideRoom()){
			robot.move(1);
			this.numSteps++;
			x++;
			left = robot.distanceToObstacle(Direction.LEFT);
		}
	}
	
	
	/**
	 * For use once the robot gets to the exit cell. Finds the direction that the exit is in and goes through it.
	 */
	protected void goThroughExit(){
		int exitForward = robot.distanceToObstacle(Direction.FORWARD);
		int exitLeft = robot.distanceToObstacle(Direction.LEFT);
		int exitRight = robot.distanceToObstacle(Direction.RIGHT);
		int exitBackwards = robot.distanceToObstacle(Direction.BACKWARD);
		if(exitForward == Integer.MAX_VALUE){
			try {
				//robot.move(1);
				handler.postDelayed(mForward, 200);
				this.numSteps++;
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		} else if(exitLeft == Integer.MAX_VALUE){
			try {
				//robot.rotate(Turn.LEFT);
				//robot.move(1);
				handler.postDelayed(mLeft, 200);
				this.numSteps++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if(exitRight == Integer.MAX_VALUE){
			try {
				//robot.rotate(Turn.RIGHT);
				//robot.move(1);
				handler.postDelayed(mRight, 200);
				this.numSteps++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(exitBackwards == Integer.MAX_VALUE){
			try {
				//robot.rotate(Turn.AROUND);
				//robot.move(1);
				handler.postDelayed(mBackward, 200);
				this.numSteps++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	Runnable mForward = new Runnable(){
		public void run(){
			try {
				Log.v("!!!!!!", "In mMove called from the move method of wizard");
				robot.move(1);
				numSteps++;
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
				numSteps++;
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
				numSteps++;
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
				numSteps++;
				drive2Exit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//play.shortcut(numSteps, (int) getEnergyConsumption(), robot.hasStopped());
			}
			
		}
	};

}
