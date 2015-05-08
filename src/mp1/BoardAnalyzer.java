package mp1;

import java.util.ArrayList;

public class BoardAnalyzer {
	
	public Position findBestDestination(int[] conflictColumn, Position currentPosition, int availMoves) {
		int smallest = conflictColumn.length * conflictColumn.length;
		ArrayList<Position> asd = new ArrayList<Position>();
		for(int i = 0; i < conflictColumn.length; i ++) {
			
			Position destination = new Position(currentPosition.get_xPosition(), i);
			if(conflictColumn[i] < smallest) {

				if(withinReach(currentPosition, destination, availMoves)) {
					asd = new ArrayList<Position>();
					smallest = conflictColumn[i];
					asd.add(destination);
				}
			}
			else if(conflictColumn[i] == smallest && withinReach(currentPosition, destination, availMoves)) {
				Position a = asd.get(0);
				asd.remove(0);
				asd.add(pickClosest(currentPosition, a, destination));
			}
		}
		return asd.get(0);
	}
	
	private Position pickClosest(Position currentPosition, Position positionA, Position positionB) {
		
		int currentToA = Math.abs(currentPosition.get_yPosition() - positionA.get_yPosition());
		int currentToB = Math.abs(currentPosition.get_yPosition() - positionB.get_yPosition());
		
		if (currentToA < currentToB) {
			double ran = Math.random() * 10; // introduced to escape local minimum
			return ((int) ran > 2) ? positionA : positionB;
		}
		else if(currentToB < currentToA) {
			double ran = Math.random() * 10; 
			return ((int) ran > 2) ? positionB : positionA;
		}
		else {
			double rand = Math.random() * 2;
			if ((int) rand == 0) {
				return positionA;
			}
			return positionB;
		}
	}
	
	private boolean withinReach(Position current, Position destination, int availMoves) {
		return Math.abs(current.get_yPosition() - destination.get_yPosition()) <= availMoves;
	}
}