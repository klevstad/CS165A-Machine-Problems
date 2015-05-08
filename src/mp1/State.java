package mp1;

public class State {
	
	private int conflicts;
	private Position[] queens;
	
	public State(Position[] initial) {
		setQueens(initial);
	}
	
	public State(int conf, Position[] posi) {
		setConflicts(conf);
		setQueens(posi);
	}

	public int getConflicts() {
		return conflicts;
	}

	public void setConflicts(int conflicts) {
		this.conflicts = conflicts;
	}

	public Position[] getQueens() {
		return queens;
	}

	public void setQueens(Position[] queens) {
		this.queens = queens;
	}
}