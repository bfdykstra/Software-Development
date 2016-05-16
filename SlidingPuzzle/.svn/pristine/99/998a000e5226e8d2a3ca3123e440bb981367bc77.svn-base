package edu.wm.cs.cs301.slidingpuzzle;
/**
 * A puzzle state represents the current arrangement of tiles on a sliding puzzle.
 * The encoding assumes that the empty slot (or blank tile) is represented with a zero
 * number and that the empty slot is the one that moves right, left, up or down. 
 * 
 * The PuzzleStateInterface provides four move operations: up, down, left and right, by 
 * fixing a naming convention with an enum type Operation. 
 * An implementing class encapsulates the knowledge about the rules of the game, 
 * i.e. which move is possible in which state.
 * 
 * A puzzle state can be also used as a pure data container:
 * a) to store a tuple (state1,operation,state2) with the notion 
 * of a parent and operation. A particular state would store state2 as its state, state1 as 
 * its parent and operation as its operation. Get/set methods allow to set and retrieve this
 * data.
 * b) to store the distance between the initial state and the state
 * represented by the puzzle state. This is a straightforward by-product of the 
 * computation of moves and successor puzzle states as the distance of the 
 * successor is just an increment by one. The distance is useful for the A* Solver.
 * 
 * The foreseen usage of PuzzleState is in a Depth-First-Search (DFS) or 
 * Breadth-First-Search (BFS) or A* Algorithm 
 * where the search algorithm explores a directed
 * graph with puzzle states as nodes and move operations as edges. 
 * (state1,operation,state2) is an edge is this graph and the operation 
 * moves the puzzle from state1 to state2. Once the graph is explored
 * and the search ends at the goal or final state, the algorithm needs
 * to extract the sequence of moves or operations from starting state
 * to the final state. As this naturally happens in a backward manner
 * beginning at the final state, it is useful to obtain the starting state
 * in the (state1,operation,state2) with the getParent method, i.e.
 * state2.getParent() delivers state1, state2.getOperation() delivers 
 * the move operation.
 * 
 * @author Probir Roy, Peter Kemper
 *
 */
public interface PuzzleState {
	/**
	 * The puzzle allows for a four moves whose names are fixed with an enum type 
	 * Operation. Note that move right means that the empty slot (the blank tile) 
	 * moves to the right. Do not confuse this with the tiles that move in the actual game. 
	 * The interface follows the point of view that the empty slot is what moves around.
	 * The empty slot is encoded with a numerical value of 0.
	 */
	public enum Operation{ MOVERIGHT, MOVELEFT, MOVEUP, MOVEDOWN } ;
	/**
	 * Allows to configure the puzzle state to a particular game state. 
	 * The state is provided as an array of integer.
	 * For instance, an one dimensional integer array {0,1,3,2} represents a state of 2x2 puzzle
	 * with the first row carrying values 0 and 1, the second row carrying values 3 and 2.
	 * @param gameState provides a value for each tile in the current state of the game. 
	 */
	public void setState(int[] gameState);
	/**
	 * Get the current puzzle state in a single dimensional array (i.e. {0,1,3,2} for 2x2 puzzle). 
	 * Blank tile is represented as 0. The coding goes by rows, for the example the top row 
	 * contains values 0 and 1 from left to right. This method is symmetric to setState and
	 * returns an array of same content that was assigned with the setState method.
	 * @return Single dimensional array of integer representing puzzle state
	 */
	public int[] getState();
	/**
	 * Get the parent state which was previously set with the setParentState method.
	 * The parent state is useful if one wants to represent
	 * that a puzzle state A moves to puzzle state B with a particular move operation.
	 * For this example B.getParentState() returns A. Obtaining A is useful to extract
	 * a path from a starting state to a final state once the final state is reached.
	 * @return a previously stored parent state
	 */
	public PuzzleState getParent() ;
	/**
	 * Sets the parent state. 
	 * For the intended use, see comment for corresponding getParentState method.
	 * @param parentState
	 */
	public void setParent(PuzzleState parentState) ;
	/**
	 * Sets the operation by which this state is reached.
	 * @param op
	 */
	public void setOperation(Operation op) ;
	/**
	 * Retrieves the operation by which this state is reached. This just returns the
	 * operation that was previously stored with setOperation.
	 * @return previously stored Operation or null if there is none.
	 */
	public Operation getOperation() ;
	/**
	 * Set distance from initial state.
	 * The initial state has distance 0, states that are reached with one move have distance one and so forth.
	 */
	public void setDistance(int distance) ;
	/**
	 * Get distance from initial state.
	 * The initial state has distance 0, states that are reached with one move have distance one and so forth.
	 */
	public int getDistance() ;
	/**
	 * Compare other puzzle state to current puzzle state.
	 * @param other puzzle state to compare
	 * @return true if equal and false otherwise
	 */
	public boolean equals(PuzzleState other);
	/**
	 * Returns a new instance of PuzzleState where the gap and the value above it are switched.
	 * The returned PuzzleState is a newly created object where state, parent and operation
	 * attributes are properly set. The parent is this object, the operation is MOVEUP.
	 * @return new PuzzleState for legal move. For illegal move operation it return null.
	 */
	public PuzzleState moveUp();
	/**
	 * Returns a new instance of PuzzleState where the gap and the value below it are switched.
	 * The returned PuzzleState is a newly created object where state, parent and operation
	 * attributes are properly set. The parent is this object, the operation is MOVEDOWN.
	 * @return new PuzzleState for legal move. For illegal move operation it return null.
	 */
	public PuzzleState moveDown();
	/**
	 * Returns a new instance of PuzzleState where the gap and the value to its left are switched.
	 * The returned PuzzleState is a newly created object where state, parent and operation
	 * attributes are properly set. The parent is this object, the operation is MOVELEFT.
	 * @return new PuzzleState for legal move. For illegal move operation it return null.
	 */
	public PuzzleState moveLeft();
	/**
	 * Returns a new instance of PuzzleState where the gap and the value to its right are switched.
	 * The returned PuzzleState is a newly created object where state, parent and operation
	 * attributes are properly set. The parent is this object, the operation is MOVERIGHT.
	 * @return new PuzzleState for legal move. For illegal move operation it return null.
	 */
	public PuzzleState moveRight();
	

}
