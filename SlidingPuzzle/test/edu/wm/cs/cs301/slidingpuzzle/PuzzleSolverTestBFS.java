package edu.wm.cs.cs301.slidingpuzzle;

import static org.junit.Assert.*;

import org.junit.Test;

public class PuzzleSolverTestBFS extends PuzzleSolverTest {

	/**
	 * Wrapper to encapsulate constructor call for class that implements PuzzleSolver interface
	 * @param puzzle1 for starting configuration, puzzle2 for goal configuration
	 * @return
	 */
	protected PuzzleSolver createPuzzleSolver(int[] puzzle1, int[] puzzle2) {
		PuzzleSolver pSolver = new BFSSolver();
    	pSolver.configure(puzzle1, puzzle2);
		return pSolver;
	}

}
