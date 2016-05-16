package edu.wm.cs.cs301.slidingpuzzle;


import java.util.Stack;
import java.util.HashSet;

import edu.wm.cs.cs301.slidingpuzzle.PuzzleState.Operation;

public class DFSSolver implements PuzzleSolver {
	
	private PuzzleState initState ; //field for the initial state
	private PuzzleState goalState ; //field for the goal state
	private HashSet<PuzzleState> known = new HashSet<PuzzleState>(); //field to store all of the known states that have been explored
	
	public DFSSolver() {
		initState = new SimplePuzzleState();
		goalState = new SimplePuzzleState();
		
	}

	@Override
	/**
	 * Configures the solver with initial and goal states. As interfaces 
	 * do not allow for the specification of a constructor. A default 
	 * constructor with no parameters is assumed and the configure method 
	 * is used to clarify what is necessary to properly initialize/configure 
	 * an object of a class that implements the PuzzleSolver interface. 
	 * The initial and goal states also implicitly carry information on 
	 * the puzzles dimension (i.e. number of rows). The states are provided 
	 * as an array of integer. For Instance, an one dimensional integer 
	 * array {0,1,3,2} represents a state of 2x2 puzzle with top row values 
	 * of 0 and 1, bottom row values of 3 and 2. The puzzle solver will be 
	 * provided with two one dimensional integer arrays, initial and goal, 
	 * representing the initial and goal state of the puzzle.
	 * @param initial Initial state represented as single dimensional 
	 * array(i.e. {0,1,3,2} for 2x2 puzzle).
	 * Blank state is represented as 0.
	 * @param goal Goal state represented as single dimensional array(i.e. {0,1,3,2} 
	 * for 2x2 puzzle).
	 * Blank state is represented as 0.
	 * @return Return true on successful configuration. false otherwise.
	 */
	public boolean configure(int[] initial, int[] goal) {
		
		if (initial == null || goal == null) {
			return false;
		}
		
		initState.setState(initial);
		goalState.setState(goal);
		return true; 
	}

	
	@Override
	/**
	 * Returns a list of operations representing the directions to move the blank space
	 *  in order to solve the puzzle. This method internally performs a search for a 
	 *  solution to a configured puzzle problem. If there is no solution movesToSolve must 
	 *  return null. For instance, {0,1,2,3} and {1,0,2,3} are the initial and goal state 
	 *  of a 2x2 puzzle. For this puzzle movesToSolve of the puzzle solver must return an 
	 *  array of operations containing only one element MOVERIGHT. For impossible puzzle 
	 *  states i.e. {0,1,2,3} and {3,1,2,0} as initial and goal state respectively there 
	 *  is no solution. movesToSolve of puzzle solver must return null for invalid puzzles.
	 *  @return An array of Operations to get to the goalState
	 */
	public Operation[] movesToSolve() {
		
		Stack<PuzzleState> toDo = new Stack<PuzzleState>();
		
		int max = this.getMaxSizeOfQueue(); 
		
		//adds initial State to known puzzles and toDo queue
		known.add(initState);
		toDo.push(initState);
		
	
	while( !toDo.isEmpty() ){
		
		//Impossible case check
		if( toDo.size() > max){
			return null;
		}
		
		PuzzleState child = new SimplePuzzleState(); 
		//PuzzleState curState = (PuzzleState) toDo.remove(toDo.size() - 1); //removes last candidate from toDo list
		//PuzzleState curState = (PuzzleState) toDo.pop();
		PuzzleState curState = (PuzzleState) toDo.pop();

		
		if(curState.equals(goalState)){
			Operation[] returnPath = new Operation[ curState.getDistance() ];
			
			if(returnPath.length > 0){
				int lastNdx = returnPath.length - 1;
				
				//last operation that was done, placed at the last ndx of the operation array
				returnPath[lastNdx] = curState.getOperation(); 
				curState = curState.getParent();
				
				//Gets the array of operations
				while( curState.getOperation() != null){
					lastNdx = lastNdx - 1; //
					returnPath[lastNdx] = curState.getOperation();
					curState = curState.getParent();
				}
				return returnPath;
			}
			else{
				//initial was goal, so no moves are needed
				return returnPath;
			}
		}
		
		child = curState.moveUp();
		if( child != null){
			this.addElement(known, child, toDo);
		}
		
		child = curState.moveDown();
		if(child != null){
			this.addElement(known, child, toDo);
		}
		
		child = curState.moveLeft();
		if(child != null){
			this.addElement(known, child, toDo);
		}
		
		child = curState.moveRight();
		if(child != null){
			this.addElement(known, child, toDo);
		}
	}
	return null;
}
	
	
	@Override
	/**
	 * Gets the initial state of the puzzle
	 */
	public PuzzleState getSolverInitialState() {
		
		return initState;
	}

	
	@Override
	/**
	 * Gets the final desired state
	 */
	public PuzzleState getSolverFinalState() {
		
		return goalState;
	}

	
	@Override
	/**
	 * Get the number of states explored. This number provides a 
	 * measure for the number of steps a search algorithm performed,
	 *  i.e. it is a measure of efficiency of a search algorithm. 
	 *  This method should only be called after a computation of a 
	 *  solution, i.e. movesToSolve(). The number is the number of 
	 *  different states that have been instantiated during the search.
	 *  @return the number of moves that have been performed
	 */
	public int getNumberOfStatesExplored() {
		// TODO Auto-generated method stub
		
		return known.size();
	}

	
	@Override
	/**
	 * Get the maximum number of elements in the queue of states 
	 * that need to be explored. The queue or todo-list is an internal data 
	 * structure to store intermediate solutions that need further exploration. 
	 * Its size provides a measure for the efficiency of the algorithm.
	 */
	public int getMaxSizeOfQueue() {
		int n = initState.getState().length;
	    int result = 1;
	    for (int i = 1; i <= n; i++) {
	    	result = result * i;
	    	}
		return result;
	}
	
	
	/**
	 * Adds child into the known hashSet and the toDo stack.
	 * @param known HashSet of known puzzles
	 * @param child the puzzle in question
	 * @param toDo the toDo stack
	 */
	private void addElement(HashSet<PuzzleState> known, PuzzleState child, Stack<PuzzleState> toDo){
		if((!known.contains(child))){
			known.add(child);
			toDo.push(child);
			//toDo.put(key, child);
		}
	}
}
