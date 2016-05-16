package edu.wm.cs.cs301.slidingpuzzle;
import static org.junit.Assert.*;

import org.junit.Test;

import edu.wm.cs.cs301.slidingpuzzle.PuzzleState.Operation;


/**
 * Test cases for class SimplePuzzleState that implements PuzzleState.
 * @author Probir Roy, Peter Kemper
 *
 */
public class PuzzleStateTest {
		
	/**
	 * Wrapper to encapsulate constructor call for class that implements PuzzleState interface
	 * @param puzzle
	 * @return
	 */
	PuzzleState createPuzzleState(int[] puzzle) {
		PuzzleState pState= new SimplePuzzleState(); 
	    pState.setState(puzzle);
	    assertNotNull(pState.getState()) ; // object has state
	    assertNull(pState.getParent()) ; // object has no parent
	    assertNull(pState.getOperation()) ; // object has no operation
	    return pState ;
	}
	
	// a few particular puzzles to test
	// 2 8 3
	// 1 6 4
	// 7 5 0
	final int[] puzzleLowerRightCorner = {2,8,3,1,6,4,7,5,0};
	// 2 8 3
	// 1 6 4
	// 7 0 5	
	final int[] puzzleLastRowMiddlePosition = {2,8,3,1,6,4,7,0,5};
	// 2 8 3	
	// 1 0 4 
	// 7 6 5	
	final int[] puzzleCenterPosition = {2,8,3,1,0,4,7,6,5};
	// 2 0 3
	// 1 6 4 
	// 7 8 5
	final int[] puzzleFirstRowMiddlePosition = {2,0,3,1,6,4,7,8,5};
	// 2 0 3
	// 1 8 4 
	// 7 6 5
	final int[] puzzleFirstRowMiddlePosition2 = {2,0,3,1,8,4,7,6,5};
	// 2 8 3
	// 1 6 4
	// 0 7 5 
	final int[] puzzleLowerLeftCorner = {2,8,3,1,6,4,0,7,5} ;
	// 0 8 3
	// 1 6 4
	// 2 7 5 
	final int[] puzzleUpperLeftCorner = {0,8,3,1,6,4,2,7,5} ;
	// 8 3 0
	// 1 6 4
	// 2 7 5 	
	final int[] puzzleUpperRightCorner = {8,3,0,1,6,4,2,7,5} ;
	
	// some 2 x 2 puzzles
	// note that puzzles are not necessarily reachable from each other
	final int[] smallPuzzleUpperLeftCorner = {0,1,2,3};
	final int[] smallPuzzleLowerLeftCorner = {1,3,0,2};
	final int[] smallPuzzleLowerRightCorner = {3,1,2,0}; 
	final int[] smallPuzzleUpperRightCorner = {1,0,2,3};

	/**
	 * Produces a string with the content of a puzzle, used for error messages
	 * @param puzzle
	 * @return
	 */
	private String printPuzzle(int[] puzzle) {
		String result = "" ;
		for (int i = 0 ; i < puzzle.length ; i++) {
			result += " " + puzzle[i] ;
		}
		return result ;
	}
	/**
	 * Test case verifies the equals function of a Puzzle state. 
	 * Function must also handle Null puzzle state.
	 */
	@Test
	public void testEquals() {
		// create two puzzles of dimensions 3x3 that differ
		int[] puzzle1 = puzzleLastRowMiddlePosition.clone();
		int[] puzzle2 = puzzleLowerRightCorner.clone();
		// create 3 puzzle states to compare, 
	    PuzzleState pState = createPuzzleState(puzzle1) ;
	    PuzzleState pSame = createPuzzleState(puzzle1) ;
	    PuzzleState pDifferent= createPuzzleState(puzzle2) ;
	     
	    // check properties of isEqual method
	    // no state is equal to null
	    assertFalse(pState.equals(null));
	    assertFalse(pSame.equals(null));
	    assertFalse(pDifferent.equals(null));
	    // equality is reflexive: every state is equal to itself
	    assertTrue(pState.equals(pState));
	    assertTrue(pSame.equals(pSame));
	    assertTrue(pDifferent.equals(pDifferent));
	    // equality is symmetric
	    assertTrue(pState.equals(pSame));
	    assertTrue(pSame.equals(pState));
	    assertFalse(pState.equals(pDifferent));
	    assertFalse(pDifferent.equals(pState));
	    // equality is transitive: not checked
	}

	//////////////////////// test individual moves ////////////////////////////////
	/**
	 * Test case verifies moveRight returns null on invalid move.
	 */
	@Test
	public void testMoveRightExpectException(){
		// create an arbitrary puzzle and puzzle state
		PuzzleState pState2 = createPuzzleState(puzzleLowerRightCorner.clone()) ;
	    // perform a move that is not legal
	    // current state:
	    // 2 8 3
	    // 1 6 4 -> the 0 position can not move right or down
	    // 7 5 0
	    // 
		PuzzleState pStateNull = pState2.moveRight();
		assertNull(pStateNull);
	}
	/**
	 * Test case verifies moveRight function performs a valid right shift of specified block.
	 */
	@Test
	public void testMoveRightNoException(){
		// 2 8 3		2 8 3
		// 1 6 4  -> 	1 6 4
		// 7 0 5		7 5 0
		PuzzleState start = createPuzzleState(puzzleLastRowMiddlePosition.clone()) ;
		PuzzleState goal = createPuzzleState(puzzleLowerRightCorner.clone()) ;
		
		PuzzleState result = start.moveRight();
		assertTrue("Expected" + printPuzzle(goal.getState()) + ", computed: " + printPuzzle(result.getState()), 
					goal.equals(result));
		assertTrue("Expected" + printPuzzle(start.getState()) + ", computed: " + printPuzzle(result.getParent().getState()), 
				start.equals(result.getParent()));
		assertTrue("Expected" + Operation.MOVERIGHT + ", computed: " + result.getOperation(), 
				Operation.MOVERIGHT.equals(result.getOperation()));
			
	}
	/**
	 * Test case verifies moveDown returns null on invalid move.
	 */
	@Test
	public void testMoveDownExpectException(){
		PuzzleState start = createPuzzleState(puzzleLowerRightCorner.clone()) ;
	    // perform a move that is not legal
	    // current state:
	    // 2 8 3
	    // 1 6 4 -> the 0 position can not move right or down
	    // 7 5 0
	    // 
	    PuzzleState pStateNull = start.moveDown();
		assertNull(pStateNull);
	}
	/**
	 * Test case verifies moveDown function performs a valid down shift of specified block.
	 */
	@Test
	public void testMoveDownNoException(){
		// 2 8 3	2 8 3
		// 1 0 4 ->	1 6 4
		// 7 6 5	7 0 5
		PuzzleState start = createPuzzleState(puzzleCenterPosition.clone()) ;
		PuzzleState goal = createPuzzleState(puzzleLastRowMiddlePosition.clone()) ;
		
		PuzzleState result = start.moveDown();
		assertTrue("Expected" + printPuzzle(goal.getState()) + ", computed: " + printPuzzle(result.getState()), 
					goal.equals(result));
		assertTrue("Expected" + printPuzzle(start.getState()) + ", computed: " + printPuzzle(result.getParent().getState()), 
				start.equals(result.getParent()));
		assertTrue("Expected" + Operation.MOVEDOWN + ", computed: " + result.getOperation(), 
				Operation.MOVEDOWN.equals(result.getOperation()));
			
	}
	/**
	 * Test case verifies moveUp returns null on invalid move.
	 */	
	@Test
	public void testMoveUpExpectException(){
		PuzzleState start = createPuzzleState(puzzleFirstRowMiddlePosition.clone()) ;
	    PuzzleState pStateNull = start.moveUp();
	    assertNull(pStateNull);
	}
	/**
	 * Test case verifies moveUp function performs a valid up shift of specified block.
	 */	
	@Test
	public void testMoveUpNoException(){
		// 2 8 3	2 0 3	
		// 1 0 4 -> 1 8 4
		// 7 6 5	7 6 5
		PuzzleState start = createPuzzleState(puzzleCenterPosition.clone()) ;
		PuzzleState goal = createPuzzleState(puzzleFirstRowMiddlePosition2.clone()) ; // note: 2 puzzles of that kind, 2nd one is it
	
		PuzzleState result = start.moveUp();
		assertTrue("Expected" + printPuzzle(goal.getState()) + ", computed: " + printPuzzle(result.getState()), 
					goal.equals(result));
		assertTrue("Expected" + printPuzzle(start.getState()) + ", computed: " + printPuzzle(result.getParent().getState()), 
				start.equals(result.getParent()));
		assertTrue("Expected" + Operation.MOVEUP + ", computed: " + result.getOperation(), 
				Operation.MOVEUP.equals(result.getOperation()));
		
	}
	/**
	 * Test case verifies moveLeft returns null on invalid move.
	 */
	@Test
	public void testMoveLeftExpectException(){
		PuzzleState start = createPuzzleState(puzzleLowerLeftCorner.clone()) ;
		PuzzleState pStateNull = start.moveLeft();
	    assertNull(pStateNull);
	}
	/**
	 * Test case verifies moveLeft function performs a valid left shift of specified block.
	 */
	@Test
	public void testMoveLeftNoException(){
		PuzzleState start = createPuzzleState(puzzleLastRowMiddlePosition.clone()) ;
		PuzzleState goal = createPuzzleState(puzzleLowerLeftCorner.clone()) ;
	
		PuzzleState result = start.moveLeft();
		assertTrue("Expected" + printPuzzle(goal.getState()) + ", computed: " + printPuzzle(result.getState()), 
					goal.equals(result));
		assertTrue("Expected" + printPuzzle(start.getState()) + ", computed: " + printPuzzle(result.getParent().getState()), 
				start.equals(result.getParent()));
		assertTrue("Expected" + Operation.MOVELEFT + ", computed: " + result.getOperation(), 
				Operation.MOVELEFT.equals(result.getOperation()));
			
	}
	////////////////// test sequences of moves //////////////////////////////////////////////////
	/**
	 * Helper method that performs a move into a given direction and then back 
	 * such that the returned state equals the starting state if the movement is possible.
	 * @param start
	 * @param op the direction to move
	 * @return null if movement is not possible
	 */
	private PuzzleState moveBackAndForth(PuzzleState start, Operation op){
		PuzzleState result = start ;
		switch (op) {
		case MOVEUP :
			if(result!=null)
				result = result.moveUp() ;
			if(result!=null)
				result = result.moveDown() ;
			break ;
		case MOVEDOWN :
			if(result!=null)
				result = result.moveDown() ;
			if(result!=null)
				result = result.moveUp() ;
			break ;
		case MOVELEFT :
			if(result!=null)
				result = result.moveLeft() ;
			if(result!=null)
				result = result.moveRight() ;
			break ;
		case MOVERIGHT :
			if(result!=null)
				result = result.moveRight() ;
			if(result!=null)
				result = result.moveLeft() ;
			break ;
		default: 
			fail("Impossible input for parameter op: " + op) ;
		}
		return result ;
	}
	/**
	 * Test possible moves in all directions from the center position of a 3 x 3 puzzle.
	 * We use the invariant that a pair of right-left moves and up-down moves cancel out.
	 */
	@Test
	public void testMovesFromCenterPosition() {
		// create a puzzle with 0 at center position
		PuzzleState start = createPuzzleState(puzzleCenterPosition.clone()) ;
		PuzzleState end ;
		// test all movements and check invariant
			end = this.moveBackAndForth(start, Operation.MOVEUP) ;
			assertTrue(start.equals(end));
			end = this.moveBackAndForth(start, Operation.MOVEDOWN) ;
			assertTrue(start.equals(end));
			end = this.moveBackAndForth(start, Operation.MOVELEFT) ;
			assertTrue(start.equals(end));
			end = this.moveBackAndForth(start, Operation.MOVERIGHT) ;
			assertTrue(start.equals(end));
	}
	/**
	 * Helper method that performs a move into a given direction in a circular way 
	 * such that the returned state equals the starting state if the movement is possible.
	 * The cycle is minimal and performs on a 2 x 2 area.
	 * Clockwise on a 2 x 2 puzzle works for gap on upper left corner.
	 * Counterclockwise on a 2 x 2 puzzle works for gap on lower right corner.
	 * @param start
	 * @param op the direction to move
	 * @return null if movement is not possible
	 */
	private PuzzleState performSmallCycle(PuzzleState start, boolean clockwise){
		PuzzleState result = start ;
		for (int i = 1 ; i <= 3 ; i++) {
			result = (clockwise) ? doFourStepsClockwise(result) : doFourStepsCounterClockwise(result) ;
		}
		return result ;
	}
	private PuzzleState doFourStepsCounterClockwise(PuzzleState result) {
		if(result!=null)
			result = result.moveLeft() ;
		if(result!=null)
			result = result.moveDown() ;
		if(result!=null)
			result = result.moveRight() ;
		if(result!=null)
			result = result.moveUp() ;
		return result;
	}
	private PuzzleState doFourStepsClockwise(PuzzleState result) {
		if(result!=null)
			result = result.moveRight() ;
		if(result!=null)
			result = result.moveDown() ;
		if(result!=null)
			result = result.moveLeft() ;
		if(result!=null)
			result = result.moveUp() ;
		return result;
	}
	
	/**
	 * Test if we move on a 2 x 2 puzzle in a circle, if we get back to the 
	 * initial state. We try both directions, clockwise and counterclockwise.
	 */
	@Test
	public void testCycleOn2x2Clockwise() {
		PuzzleState start = createPuzzleState(smallPuzzleUpperLeftCorner.clone()) ;
		PuzzleState goal = start ;
		
		PuzzleState result = performSmallCycle(start, true) ;
		assertTrue("Expected" + printPuzzle(goal.getState()) + ", computed: " + printPuzzle(result.getState()), 
					goal.equals(result));
	}
	@Test
	public void testCycleOn2x2Counterclockwise() {
		PuzzleState start = createPuzzleState(smallPuzzleUpperRightCorner.clone()) ;
		PuzzleState goal = start ;
		
		PuzzleState result = performSmallCycle(start, false) ;
		assertTrue("Expected" + printPuzzle(goal.getState()) + ", computed: " + printPuzzle(result.getState()), 
					goal.equals(result));
	}
	/**
	 * Performs a move into a given direction in a circular way 
	 * such that the returned state equals the starting state if the movement is possible.
	 * The cycle performs on a 3 x 3 area.
	 * Clockwise on a 3 x 3 puzzle works for gap on upper left corner.
	 * Counterclockwise on a 3 x 3 puzzle works for gap on lower right corner.
	 * Note that more turns are necessary to get back to the initial state since not
	 * only the gap moves around.
	 * @param start
	 * @param op the direction to move
	 * @return null if movement is not possible
	 */
	private PuzzleState performMediumCycle(PuzzleState start, boolean clockwise){
		PuzzleState result = start ;
		for (int i = 1 ; i <= 7 ; i++) {
			result = (clockwise) ? doEightStepsClockwise(result) : doEightStepsCounterClockwise(result) ;
		}
		return result ;
	}
	/**
	 * Helper method for test cases that check cycles in 3 x 3 puzzles
	 * @param result
	 * @return
	 */
	private PuzzleState doEightStepsCounterClockwise(PuzzleState result) {
		if(result!=null)
			result = result.moveLeft() ;
		if(result!=null)
			result = result.moveLeft() ;
		if(result!=null)
			result = result.moveDown() ;
		if(result!=null)
			result = result.moveDown() ;
		if(result!=null)
			result = result.moveRight() ;
		if(result!=null)
			result = result.moveRight() ;
		if(result!=null)
			result = result.moveUp() ;
		if(result!=null)
			result = result.moveUp() ;
		return result;
	}
	/**
	 * Helper method for test cases that check cycles in 3 x 3 puzzles
	 * @param result
	 * @return
	 */
	private PuzzleState doEightStepsClockwise(PuzzleState result) {
		if(result!=null)
			result = result.moveRight() ;
		if(result!=null)
			result = result.moveRight() ;
		if(result!=null)
			result = result.moveDown() ;
		if(result!=null)
			result = result.moveDown() ;
		if(result!=null)
			result = result.moveLeft() ;
		if(result!=null)
			result = result.moveLeft() ;
		if(result!=null)
			result = result.moveUp() ;
		if(result!=null)
			result = result.moveUp() ;
		return result;
	}
	/**
	 * Test if we can move on a 3 x 3 puzzle in a circle, if we get back to the 
	 * initial state. We try both directions, clockwise and counterclockwise.
	 */
	@Test
	public void testCycleOn3x3Clockwise() {
		PuzzleState start = createPuzzleState(puzzleUpperLeftCorner.clone()) ;
		PuzzleState goal = start ;
		
		PuzzleState result = performMediumCycle(start, true) ;
		assertTrue("Expected" + printPuzzle(goal.getState()) + ", computed: " + printPuzzle(result.getState()), 
					goal.equals(result));
	}
	@Test
	public void testCycleOn3x3Counterclockwise() {
		PuzzleState start = createPuzzleState(puzzleUpperRightCorner.clone()) ;
		PuzzleState goal = start ;
		
		PuzzleState result = performMediumCycle(start, false) ;
		assertTrue("Expected" + printPuzzle(goal.getState()) + ", computed: " + printPuzzle(result.getState()), 
					goal.equals(result));
	}

}
