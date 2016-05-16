package edu.wm.cs.cs301.slidingpuzzle;
import static org.junit.Assert.*;

import java.util.Random;
import java.util.logging.Logger;

import org.junit.Test;

import edu.wm.cs.cs301.slidingpuzzle.PuzzleState.Operation;


/**
 * Test cases for class SimplePuzzleSolver that implements PuzzleSolver.
 * @author Probir Roy, Peter Kemper
 *
 */
public class PuzzleSolverTest {
	
	/**
	 * Wrapper to encapsulate constructor call for class that implements PuzzleState interface
	 * @param puzzle
	 * @return
	 */
	PuzzleState createPuzzleState(int[] puzzle) {
		PuzzleState pState= new SimplePuzzleState();
	    pState.setState(puzzle);
	    return pState ;
	}
	/**
	 * Wrapper to encapsulate constructor call for class that implements PuzzleSolver interface
	 * @param puzzle1 for starting configuration, puzzle2 for goal configuration
	 * @return
	 */
	protected PuzzleSolver createPuzzleSolver(int[] puzzle1, int[] puzzle2) {
		//PuzzleSolver pSolver = new SimplePuzzleSolver();
		//PuzzleSolver pSolver = new DFSSolver();
		//PuzzleSolver pSolver = new BFSSolver();
		/* Working version
		PuzzleSolver pSolver = new AStarSolver() ;
    	pSolver.configure(puzzle1, puzzle2);
		return pSolver;
		*/
		// modified because subclasses test individual solver
		fail("Error: test class needs to overwrite method createPuzzleSolver") ;
		return null ;
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



	///////////////////// tests for the solver ////////////////////////////
	/**
	 * This test case verifies that solvers configuration function, getSolverState and getSolverFinalState function.
	 * getSolverState must return the current state. Initially, before solving the puzzle, the initial 
	 * state and current state of solver must be same. getSolverState and getSolverFinalState must return 
	 * initial state and goal state respectively.
	 * This test case configure the solver with valid 2x2 initial and goal state.
	 */	
	@Test
	public void testSolverConfigure(){
		int[] puzzle1 = smallPuzzleUpperLeftCorner.clone();
		int[] puzzle2 = smallPuzzleLowerLeftCorner.clone();
	    // note puzzle1 and puzzle2 describe different configurations
		// set up solver
		PuzzleSolver pSolver = createPuzzleSolver(puzzle1, puzzle2);
		// test if initial and final state are initialized correctly
    	PuzzleState psInitial = pSolver.getSolverInitialState();
    	PuzzleState psGoal = pSolver.getSolverFinalState();
    	
		PuzzleState pState1 = createPuzzleState(puzzle1) ;
	    PuzzleState pState2 = createPuzzleState(puzzle2) ;

	    assertTrue(pState1.equals(psInitial));
	    assertTrue(pState2.equals(psGoal));
	}




	/**
	 * This test case verifies that solver can find route from initial state to goal state.
	 * This test case configure the solver with valid 2x2 initial and goal state. The movesToSolve function 
	 * is called to verify the list moves the solver found to reach the goal state.
	 */	
	@Test
	public void testSolver2x2(){
		int[] puzzle1 = smallPuzzleUpperLeftCorner.clone();
		int[] puzzle2 = smallPuzzleLowerLeftCorner.clone();
		// note puzzle1 and puzzle2 describe different configurations
		// set up solver		
		PuzzleSolver pSolver = createPuzzleSolver(puzzle1, puzzle2);
		
	    PuzzleState goal = createPuzzleState(puzzle2) ;

	    checkThatComputedSolutionIsCorrect(pSolver, goal);
	}
	/**
	 * For a given, already configured solver, this method checks if the solver finds a solution and
	 * if that solution leads to the given goal
	 * @param pSolver
	 * @param goal
	 */
	private void checkThatComputedSolutionIsCorrect(PuzzleSolver pSolver, PuzzleState goal) {
		Operation[] moveList = pSolver.movesToSolve();
    	PuzzleState start = pSolver.getSolverInitialState();
		assertNotNull("Solver fails to find solution for starting state: " + 
				printPuzzle(start.getState())  + " towards " + printPuzzle(goal.getState()), 
				moveList) ;
    	PuzzleState result = performSequencesOfMoves(moveList, start);
 		assertTrue(goal.equals(result));
	}
	/**
	 * This test case verifies that solver can find route from initial state to goal state 
	 * and also traverse from goal state to initial state. This test case configure the solver with valid 2x2 initial 
	 * and goal state.
	 */
	@Test
	public void testSolverBothDirection2x2(){
		int[] puzzle1 = smallPuzzleUpperLeftCorner.clone();
		int[] puzzle2 = smallPuzzleLowerLeftCorner.clone();
	    
		PuzzleSolver pSolver ;
		PuzzleState goal ;

		// forward direction
		// set up solver		
    	pSolver = createPuzzleSolver(puzzle1, puzzle2);
    	goal = createPuzzleState(puzzle2);
		checkThatComputedSolutionIsCorrect(pSolver, goal) ;
    	
    	// search reverse path by switching start and goal
		// set up solver		
    	pSolver = createPuzzleSolver(puzzle2, puzzle1);
    	goal = createPuzzleState(puzzle1) ;
    	checkThatComputedSolutionIsCorrect(pSolver, goal) ;
	}

	/**
	 * Performs a sequences of moves starting from the given state and returns the resulting state.
	 * @param moveList
	 * @param pState
	 * @return resulting state
	 */
	private PuzzleState performSequencesOfMoves(Operation[] moveList, PuzzleState pState) {	
		for(int i=0;i<moveList.length;i++)
		{
			switch (moveList[i]){
				case MOVEUP:
					if(pState!=null)
						pState = pState.moveUp();
					break;
				case MOVEDOWN:
					if(pState!=null)
						pState = pState.moveDown();
					break;
				case MOVELEFT:
					if(pState!=null)
						pState = pState.moveLeft() ;
					break;
				case MOVERIGHT:
					if(pState!=null)
						pState = pState.moveRight();
					break;
				default:
					fail("List of moves contained an unknown type element: " + moveList[i] + " at position " + i) ;
					break;
			}
			if(pState==null)
			{
				fail("List of moves contained illegal move at position: " + i) ;
			}
		}
		return pState;
	}
	/**
	 * This test case verifies solvers capability of handling impossible cases. It configures solver with 2x2 puzzle.
	 * In the test case goal state is impossible to reach from initial puzzle state. movesToSolve function of solver 
	 * is called to verify the response. movesToSolve function must return null for puzzles that are impossible to solve.
	 */
	@Test
	public void testSolver2x2ImpossibleCase(){
		int[] puzzle1 = smallPuzzleUpperLeftCorner.clone();
		int[] puzzle2 = smallPuzzleLowerRightCorner.clone();
	    
		PuzzleSolver pSolver = createPuzzleSolver(puzzle1, puzzle2);
		
	    Operation[] moveList = pSolver.movesToSolve();
    	
    	assertNull(moveList);
	}

	/**
	 * Configure solver with automatically generated 2x2 initial and goal puzzle states.
	 * movesToSolve function is called to verify the moves from initial state to goal state.
	 * movesToSolve function must return a list of moves for this test case.
	 */
	@Test
	public void testSolver2x2AutoGeneratePuzzle(){
		int[] puzzle1 = smallPuzzleUpperLeftCorner.clone();
		int[] puzzle2 = smallPuzzleLowerRightCorner.clone();
		
		// get to randomly generated puzzles
		Random rand = new Random();
		performRandomSwitches(puzzle1, rand, 4);
		performRandomSwitches(puzzle2, rand, 4);
		
		PuzzleSolver pSolver = createPuzzleSolver(puzzle1, puzzle2);	
	    PuzzleState goal = createPuzzleState(puzzle2) ;
	    Operation[] moveList = pSolver.movesToSolve();
	    // only if a solution is found, we can check something
    	if(null!=moveList) {
    		PuzzleState start = pSolver.getSolverInitialState();
        	PuzzleState result = performSequencesOfMoves(moveList, start);
        	assertTrue(result.equals(goal));
    	}
	}
	/**
	 * Perform a number of random switches between values in the given puzzle
	 * @param puzzle
	 * @param rand stream of random numbers
	 * @param total number of switches
	 * @return
	 */
	private void performRandomSwitches(int[] puzzle, Random rand, int total) {
		int count = 0 ;
		int max = puzzle.length ;
		int a ;
		int b ;
		while(count != total)
		{
			a =	rand.nextInt(max);
			b =	rand.nextInt(max);
			
			if(a!=b)
			{
				switchPlacesInPuzzle(puzzle, a, b);
				count++;
			}
		}
	}	/**
	 * Exchanges values at positions a and b within given array
	 * @param puzzle
	 * @param a
	 * @param b
	 */
	private void switchPlacesInPuzzle(int[] puzzle, int a, int b) {
		int temp = puzzle[a];
		puzzle[a] = puzzle[b];
		puzzle[b] = temp;
	}
	/**
	 * Test 3x3 puzzles that are solvable and generated with a random sequence of moves
	 * for a given starting state and a maximum length for the sequence of moves.
	 * This test performs a sequence of such tests for a growing maximum length from 1 to 10.
	 */
	@Test
	public void testSolver3x3AutoGeneratePuzzle(){
		for (int i = 1; i <= 10; i++) {
			checkSolvable3x3Game(puzzleUpperLeftCorner.clone(), i);
		}
	}
	/**
	 * Configure solver with automatically generated 3x3 initial and goal puzzle states.
	 * movesToSolve function is called to verify the moves from initial state to goal state.
	 * movesToSolve function must return a list of moves for this test case.
	 * @param start is the starting state
	 * @param total is the maximum number of steps between start and goal state
	 */
	private void checkSolvable3x3Game(int[] puzzle1, int total) {
		// obtain 2nd puzzle from first by performing a random sequence of moves
		PuzzleState start = createPuzzleState(puzzle1) ;
		PuzzleState goal = performRandomMoves(start, new Random(), total) ;
		int[] puzzle2 = goal.getState() ;
		//System.out.println("start: " + printPuzzle(start.getState()) + " goal: " + printPuzzle(goal.getState()) + " depth: " + total) ;
		// set up solver	
		PuzzleSolver pSolver = createPuzzleSolver(puzzle1, puzzle2);
		// calculate solution
	    Operation[] moveList = pSolver.movesToSolve();
	    // check that solution exists and that it is correct.
	    assertTrue(null != moveList) ;
	    // only if a solution is found, we can check something
    	if(null!=moveList) {
    		// starting state is as given
    		PuzzleState start2 = pSolver.getSolverInitialState();
    		assertTrue(start2.equals(start));
    		// sequence of moves can be performed
        	PuzzleState goal2 = performSequencesOfMoves(moveList, start);
        	// final state is as given
        	assertTrue(goal2.equals(goal));
    	}
	}
	/**
	 * Performs random a sequence of random moves on a given puzzle state.
	 * @param p starting state
	 * @param rand random number stream
	 * @param total number of moves performed (upper bound)
	 * @return final state
	 */
	private PuzzleState performRandomMoves(PuzzleState p, Random rand, final int total) {
		int count = 0 ;
		PuzzleState p2 = null ;
		int a = 0 ;
		int log = 0 ;
		while(count != total)
		{
			a = rand.nextInt(4) ;
			switch(a) {
			case 0: 
				p2 = p.moveDown() ;
				break ;
			case 1: 
				p2 = p.moveRight() ;
				break ;
			case 2: 
				p2 = p.moveLeft() ;
				break ; 
			case 3: 
				p2 = p.moveUp() ;
				break ;
			default : // do nothing, impossible according to spec of nextInt
				break ;
			}
			// if we made a move, update the current state p
			if (null != p2) {
				p = p2 ; 
				count++ ; // progress towards termination
			}
			// note: if move methods constantly return null, 
			// loop can not reach termination!
			log++ ;
			if (log > 10*total)
				fail("Move methods do not produce anything else but null, stopped after " + log + " iterations.") ;
		}
		return p ;
	}



}
