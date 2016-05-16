package edu.wm.cs.cs301.slidingpuzzle;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

import edu.wm.cs.cs301.slidingpuzzle.PuzzleState.Operation;

public class AStarSolver implements PuzzleSolver {

	
	private static PuzzleState initState ; //field for the initial state
	private static PuzzleState goalState ; //field for the goal state
	private HashSet<PuzzleState> known = new HashSet<PuzzleState>();
	
	
	public AStarSolver() {
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
	public boolean configure(int[] initial, int[] goal){
		
		if (initial == null || goal == null) {
			return false;
		}
		
		initState.setState(initial);
		goalState.setState(goal);
		return true; 
	}
	
	/**
	 * Comparator class that calculates a metric composed of the sum of
	 * the manhattan Distance between two states and the amount of moves it took 
	 * to get to another state. The priority queue ranks based off of which one 
	 * has the lower metric (meaning closer to the goal).
	 */
	public static Comparator<PuzzleState> Distance = new Comparator<PuzzleState>(){
		
		/**
		 * gets the row index in 2d view 
		 * @param index the index from the 1d array in question
		 * @return y the y-coordinate 
		 */
		private int getRowNdx(int index, int width) {
			
			int y = index/width;
			
			return y;		
		}
		
		/**
		 * gets the column index in 2d view
		 * @param index the index from the 1d array in question
		 * @return x the x-coordinate 
		 */
		private int getColNdx(int index, int width) {
			
			int x = index % width;
			
			return x;		
		}
		
		/** Gets what would be the width of a square grid.
		 * Does so by taking the square root of the length of the array.
		 * 
		 * @return width of 2d array
		 */
		private int getWidth(int[] puz) {
			
			double l = puz.length;
			int width = (int) Math.sqrt(l);
			return width;
			
		}
		
		
		/**
		 * Computes the Manhattan distance. First finds the row and column indices of both iState
		 * and goalState. Then finds the absolute difference between the two for each value of the
		 * iState array. 
		 * @param iState the initial PuzzleState
		 * @param goalState the GoalState field
		 * @return the Manhattan distance between iState and goalState
		 */
		private int manhattanDist(PuzzleState iState, PuzzleState goalState){
			int[] i = iState.getState();
			int[] g = goalState.getState();
			
			int totalDist = 0;
			int width = this.getWidth(i);
			
			int f = 0;
			int h = 0;
			for(h = 0; h < i.length; h++){
				//find value in initial and its corresponding value in goal
				// h is index value in initial, f is index value in goal
				int firstVal = i[h];
				for (f = 0; f < i.length; f++){
					if (g[f] == firstVal){
						
						//need to calculate row and col ndx now
						int rowInit = this.getRowNdx(h, width);
						int rowGoal = this.getRowNdx(f, width);
						
						int colInit = this.getColNdx(h, width);
						int colGoal = this.getColNdx(f, width);
						
						int valDist = Math.abs(rowInit - rowGoal) + Math.abs(colInit - colGoal);
						
						totalDist = valDist + totalDist;
					}
				}
			}
			return totalDist;
			
		}
		
		
		@Override
		/**
		 * Overrides the Comparator's compare method such that if iState has a greater sum of
		 * Manhattan distance and moves, then it will be a lower priority than gState.
		 * @param iState the initial state to get distance to gState
		 * @param gState the goal state to find distance to
		 * @returns 1 if iState is greater than gState, 0 if they are equal, 
		 * -1 if iState is less than gState.
		 */
		public int compare( PuzzleState iState, PuzzleState gState){
			int c1 = this.manhattanDist(iState, goalState) + iState.getDistance();
			int c2 = this.manhattanDist(gState, goalState) + gState.getDistance();
			
			if( c1 > c2){
				return 1;
			}
			
			if( c1 == c2){
				return 0;
			}
			
			if( c1 < c2){
				return -1;
			}
			return 0;
		}
	};
	
	
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
		
		PriorityQueue<PuzzleState> toDo = new PriorityQueue<PuzzleState>(1, Distance);
		
		int max = this.getMaxSizeOfQueue(); 
		toDo.add(initState);
		known.add(initState);
		
		while( !toDo.isEmpty() ){
			
			//Impossible case check
			if( toDo.size() > max){
				return null;
			}
			
			PuzzleState child = new SimplePuzzleState(); 
			PuzzleState curState = (PuzzleState) toDo.remove();
			
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
	 * Adds child into the known hashSet and the toDo priority queue.
	 * @param known HashSet of known puzzles
	 * @param child the puzzle in question
	 * @param toDo the toDo priority queue
	 */
	private void addElement(HashSet<PuzzleState> known, PuzzleState child, PriorityQueue<PuzzleState> toDo){
		if((!known.contains(child))){
			known.add(child);
			toDo.add(child);
			
		}
	}
	

}
