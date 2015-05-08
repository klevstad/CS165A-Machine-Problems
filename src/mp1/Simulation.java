package mp1;

import java.util.Arrays;

public class Simulation {
	
	private State _initialState;
	private State _currentState;
	private int _availableMoves;
	private int[][] _board;
	
	public Simulation(State init, int moves) {
		this.set_initialState(init);
		this._currentState = mapInitialToCurrent(init);
		this._availableMoves = moves;
		this._board = buildBoard(init.getQueens());
	}
	
	public void displayState(State state, long startTime) {
		System.out.println(state.getConflicts() + " conflicts");
		for(Position queen : state.getQueens()) {
			System.out.println(String.format((queen.get_yPosition() + 1) + " " + (queen.get_xPosition() + 1)));
		}
		long endTime = System.currentTimeMillis();
		System.out.println(((endTime - startTime)/1000.0) + " seconds");
		System.exit(1);
	}
	
	public void printBoard() {
		for(int[] row : this.get_board()){
			System.out.println(Arrays.toString(row));
		}
	}

	public State get_currentState() {
		return _currentState;
	}

	public void set_currentState(State _currentState) {
		this._currentState = _currentState;
	}


	public int get_availableMoves() {
		return _availableMoves;
	}

	public void set_availableMoves(int _availableMoves) {
		this._availableMoves = _availableMoves;
	}

	public int[][] get_board() {
		return _board;
	}

	public void set_board(int[][] _board) {
		this._board = _board;
	}

	public State get_initialState() {
		return _initialState;
	}
	private void set_initialState(State _initialState) {
		this._initialState = _initialState;
	}

	private State mapInitialToCurrent(State initial) {
		Position[] initialQueenPostions = new Position[initial.getQueens().length]; //initial.getQueens();
		int counter = 0;
		for(Position initialQueen : initial.getQueens()) {
			initialQueenPostions[counter] = new Position(initialQueen.get_xPosition(), initialQueen.get_yPosition());
			counter ++;
		}
		return new State(initialQueenPostions);
	}

	private int[][] buildBoard(Position[] initialPositions) {
		int[][] board = new int[initialPositions.length][initialPositions.length];
		for(Position queen : initialPositions) {
			int x = queen.get_xPosition();
			int y = queen.get_yPosition();
			board[x][y] = 1;
		}
		return board;
	}
}