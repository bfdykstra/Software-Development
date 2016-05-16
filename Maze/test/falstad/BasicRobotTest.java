package falstad;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import falstad.Robot.Direction;
import falstad.Robot.Turn;

public class BasicRobotTest {
	BasicRobot myRobot; //robot that is being tested
	MazeApplication a;
	@Before
	public void setUp() throws Exception{
		SingleRandom.setSeed(17);
		myRobot = new BasicRobot();
		a = new MazeApplication("Eller");
		a.repaint();
		//0 = 48, 1 = 49
		a.maze.testing = true;
		a.maze.keyDown(49);
		a.maze.mazebuilder.buildThread.join();
		myRobot.setMaze(a.maze);
		a.maze.robot = myRobot;
		a.maze.setCurrentPosition(0, 0);
		
	}

	
	@Test 
	/**
	 * tests robot.rotate()
	 * correct behavior: should set the current direction to reflect rotations of robot. 
	 */
	public void testRotate()  {
		
			//Always facing east at start
			int[] targetDir = myRobot.getCurrentDirection();
			myRobot.setBatteryLevel(18);
			
			try {
				myRobot.rotate(Turn.LEFT);
				myRobot.rotate(Turn.RIGHT);
				myRobot.rotate(Turn.AROUND);
				myRobot.rotate(Turn.LEFT);
				myRobot.rotate(Turn.LEFT);
				System.out.println("current battery level: " + myRobot.getBatteryLevel());
				int[] newCurDir = myRobot.getCurrentDirection();
				boolean areTheyEqual = myRobot.arrayEquals(targetDir, newCurDir);
				assertTrue(areTheyEqual);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//test exception handling
			try {
				myRobot.rotate(Turn.RIGHT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Test
	/**
	 * Tests robot.move()
	 * correct behavior: hasStopped == False if robot hits wall, but does not run out of battery
	 * hasStopped == True if robot runs out of battery
	 */
	public void testMove() {
		
		try {
			myRobot.move(2); //brings up right to end of hall
			myRobot.move(1); //hits wall
			assertFalse(myRobot.hasStopped());
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		myRobot.setBatteryLevel(4);
		try {
			myRobot.move(1);
		} catch (Exception e) {
			assertTrue(myRobot.hasStopped());
			e.printStackTrace();
		}
		
	}


	@Test
	public void testGetCurrentPosition() {
		
		int[] curPos = myRobot.getCurrentPosition();
		assertNotNull(curPos);
		
	}

	
	@Test
	/**
	 * tests robot.CanSeeGoal()
	 * correct behavior: returns true if in the specified direction relative to the robots direction there is the exit
	 */
	public void testCanSeeGoal() {
		
		try {
			myRobot.move(2);
			myRobot.rotate(Turn.AROUND);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//facing west
		boolean canSeeForwardW = myRobot.canSeeGoal(Direction.FORWARD);
		assertTrue(canSeeForwardW);
		boolean canSeeBackwardW = myRobot.canSeeGoal(Direction.BACKWARD);
		assertFalse(canSeeBackwardW);
		boolean canSeeLeftW = myRobot.canSeeGoal(Direction.LEFT);
		assertFalse(canSeeLeftW);
		boolean canSeeRightW = myRobot.canSeeGoal(Direction.RIGHT);
		assertFalse(canSeeRightW);
		
		try {
			myRobot.rotate(Turn.LEFT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//facing south
		boolean canSeeForwardS = myRobot.canSeeGoal(Direction.FORWARD);
		assertFalse(canSeeForwardS);
		boolean canSeeBackwardS = myRobot.canSeeGoal(Direction.BACKWARD);
		assertFalse(canSeeBackwardS);
		boolean canSeeLeftS = myRobot.canSeeGoal(Direction.LEFT);
		assertFalse(canSeeLeftS);
		boolean canSeeRightS = myRobot.canSeeGoal(Direction.RIGHT);
		assertTrue(canSeeRightS);
		
		
		try {
			myRobot.rotate(Turn.LEFT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//facing east
		boolean canSeeForwardE = myRobot.canSeeGoal(Direction.FORWARD);
		assertFalse(canSeeForwardE);
		boolean canSeeBackwardE = myRobot.canSeeGoal(Direction.BACKWARD);
		assertTrue(canSeeBackwardE);
		boolean canSeeLeftE = myRobot.canSeeGoal(Direction.LEFT);
		assertFalse(canSeeLeftE);
		boolean canSeeRightE = myRobot.canSeeGoal(Direction.RIGHT);
		assertFalse(canSeeRightE);
		
		
		try {
			myRobot.rotate(Turn.LEFT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//facing North
		boolean canSeeForwardN = myRobot.canSeeGoal(Direction.FORWARD);
		assertFalse(canSeeForwardN);
		boolean canSeeBackwardN = myRobot.canSeeGoal(Direction.BACKWARD);
		assertFalse(canSeeBackwardN);
		boolean canSeeLeftN = myRobot.canSeeGoal(Direction.LEFT);
		assertTrue(canSeeLeftN);
		boolean canSeeRightN = myRobot.canSeeGoal(Direction.RIGHT);
		assertFalse(canSeeRightN);
		
		
		//battery exception check
		myRobot.setBatteryLevel(0);
		myRobot.canSeeGoal(Direction.LEFT);
		assertTrue(myRobot.hasStopped());
		
		
	}

	@Test
	public void testIsInsideRoom() {
		
		try {
			myRobot.move(2);
			myRobot.rotate(Turn.LEFT);
			myRobot.move(1);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		assertTrue(myRobot.isInsideRoom());
		
	}


	@Test
	public void testGetEnergyForFullRotation() {
		
		assertTrue(12 == myRobot.getEnergyForFullRotation());
	}

	@Test
	public void testGetEnergyForStepForward() {
		
		assertTrue(myRobot.getEnergyForStepForward() == 5);
	}

	@Test
	/**
	 * tests robot.hasStopped()
	 * correct behavior: hasStopped should be false if robot just hits wall and does not run out of battery
	 * hasStopped should be true if robot runs out of battery
	 */
	public void testHasStopped() {
		
		try {
			myRobot.move(3);
		} catch (Exception e) {
			assertFalse(myRobot.hasStopped());
			e.printStackTrace();
		}
		
		
		try {
			myRobot.rotate(Turn.LEFT); //turn the robot so that its not facing wall anymore
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		myRobot.setBatteryLevel(0);
		try {
			
			myRobot.move(1);
		} catch (Exception e) {
			assertTrue(myRobot.hasStopped());
			e.printStackTrace();
		}
		
		myRobot.distanceToObstacle(Direction.FORWARD);
		assertTrue(myRobot.hasStopped());
		myRobot.canSeeGoal(Direction.BACKWARD);
		assertTrue(myRobot.hasStopped());
		
		
		
	}

	@Test
	/**
	 * tests robot.DistanceToObstacle
	 * correct behavior: returns number of steps towards obstacle if obstacle is visible in a straight line of sight, Integer.MAX_VALUE otherwise
	 * (facing the exit)
	 */
	public void testDistanceToObstacle() {
		int x = myRobot.distanceToObstacle(Direction.BACKWARD);
		assertTrue(x == Integer.MAX_VALUE);
		try {
			myRobot.move(2);
			myRobot.rotate(Turn.LEFT);
			myRobot.move(2);
			myRobot.rotate(Turn.LEFT);
			myRobot.move(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//forward dist = 0, left dist = 1; idk what left and right are yet
		int f1 = myRobot.distanceToObstacle(Direction.FORWARD);
		if(f1 != 0){
			fail("didn't measure forward distance correctly");
		}
		int l1 = myRobot.distanceToObstacle(Direction.LEFT);
		if(l1 != 1){
			fail("didn't measure left distance correctly");
		}
		int b1 = myRobot.distanceToObstacle(Direction.BACKWARD);
		if(b1 != 2){
			fail("didn't measure backward distance correctly");
		}
		int r1 = myRobot.distanceToObstacle(Direction.RIGHT);
		if(r1 != 4){
			fail("didn't measure right distance correctly");
		}
		
		//Now we rotate and check other directions to make sure they all work correctly
		try {
			myRobot.rotate(Turn.RIGHT);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		//forward dist = 4, left dist = 0; backwards = 1; right = 2
		int f2 = myRobot.distanceToObstacle(Direction.FORWARD);
		if(f2 != 4){
			fail("didn't measure forward distance correctly");
		}
		int l2 = myRobot.distanceToObstacle(Direction.LEFT);
		if(l2 != 0){
			fail("didn't measure left distance correctly");
		}
		int b2 = myRobot.distanceToObstacle(Direction.BACKWARD);
		if(b2 != 1){
			fail("didn't measure backward distance correctly");
		}
		int r2 = myRobot.distanceToObstacle(Direction.RIGHT);
		if(r2 != 2){
			fail("didn't measure right distance correctly");
		}
		
		//next rotation
		try {
			myRobot.rotate(Turn.RIGHT);
		} catch (Exception e) {
				e.printStackTrace();
		}
		//forward dist = 4, left dist = 0; backwards = 1; right = 2
		int f3 = myRobot.distanceToObstacle(Direction.FORWARD);
		if(f3 != 2){
			fail("didn't measure forward distance correctly");
		}
		int l3 = myRobot.distanceToObstacle(Direction.LEFT);
		if(l3 != 4){
			fail("didn't measure left distance correctly");
		}
		int b3 = myRobot.distanceToObstacle(Direction.BACKWARD);
		if(b3 != 0){
			fail("didn't measure backward distance correctly");
		}
		int r3 = myRobot.distanceToObstacle(Direction.RIGHT);
		if(r3 != 1){
			fail("didn't measure right distance correctly");
		}
		
		//last rotation
		try {
			myRobot.rotate(Turn.RIGHT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//forward dist = 4, left dist = 0; backwards = 1; right = 2
		int f4 = myRobot.distanceToObstacle(Direction.FORWARD);
		if(f4 != 1){
			fail("didn't measure forward distance correctly");
		}
		int l4 = myRobot.distanceToObstacle(Direction.LEFT);
		if(l4 != 2){
			fail("didn't measure left distance correctly");
		}
		int b4 = myRobot.distanceToObstacle(Direction.BACKWARD);
		if(b4 != 4){
			fail("didn't measure backward distance correctly");
		}
		int r4 = myRobot.distanceToObstacle(Direction.RIGHT);
		if(r4 != 0){
			fail("didn't measure right distance correctly");
		}
				
	}
	
	
	@Test
	/**
	 * tests robot.isAtGoal()
	 * correct behavior: return true if robot is at the exit of the maze, false otherwise
	 */
	public void testIsAtGoal(){
		assertTrue(myRobot.isAtGoal());
		try {
			myRobot.move(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(myRobot.isAtGoal());
	}
	
	@Test
	public void testHasRoomSensor(){
		assertTrue(myRobot.hasRoomSensor());
	}
	
	@Test
	public void testHasDistanceSensor(){
		assertTrue(myRobot.hasDistanceSensor(Direction.BACKWARD));
		assertTrue(myRobot.hasDistanceSensor(Direction.FORWARD));
		assertTrue(myRobot.hasDistanceSensor(Direction.LEFT));
		assertTrue(myRobot.hasDistanceSensor(Direction.RIGHT));
	}
}
