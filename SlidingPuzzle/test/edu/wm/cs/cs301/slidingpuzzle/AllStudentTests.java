package edu.wm.cs.cs301.slidingpuzzle;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PuzzleSolverTestAStar.class, PuzzleSolverTestBFS.class,
		PuzzleSolverTestDFS.class, PuzzleStateTest.class })
public class AllStudentTests {

}
