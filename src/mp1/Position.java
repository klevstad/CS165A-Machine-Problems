package mp1;

public class Position {
	
	private int _xPosition;
	private int _yPosition;
	private int _conflicts;
	
	public Position(int x, int y) {
		this._xPosition = x;
		this._yPosition = y;
		this._conflicts = 0;
	}

	public int get_xPosition() {
		return _xPosition;
	}

	public void set_xPosition(int _xPosition) {
		this._xPosition = _xPosition;
	}

	public int get_yPosition() {
		return _yPosition;
	}

	public void set_yPosition(int _yPosition) {
		this._yPosition = _yPosition;
	}

	public int get_conflicts() {
		return _conflicts;
	}

	public void set_conflicts(int _conflicts) {
		this._conflicts = _conflicts;
	}
}