package mp1;

import java.util.ArrayList;

public class ConflictSupport {
	
	public int getTotalConflictsOnBoard(int[][] board, Position[] positions) {
		int total = 0;
		for(Position queen : positions) {
			total += calculateConflictsForQueen(board, queen);
		}
		return total / 2;
	}
	
	public Position pickInitialQueenAtRandom(Position[] positions) {
		double ran = Math.random() * positions.length;
		return positions[(int) ran];
	}
	
	public Position findQueenWithMostConflicts(int[][] board, Position[] positions) {
		int highest = 0;
		ArrayList<Position> asd = new ArrayList<Position>();
		
		for(Position queen : positions) {
			int th = calculateConflictsForQueen(board, queen);
			if(th > highest){
				asd = new ArrayList<Position>();
				asd.add(queen);
				highest = th;
			}
			else if(th == highest) {
				asd.add(queen);
			}
		}
		
		if(asd.size() == 1) {
			return asd.get(0);
		}
		else {
			double ran = Math.random() * asd.size();
			return asd.get((int) ran);
		}
		
	}
	
	public int[] calculateConflictPositionsInSelectedQueensColumn(int[][] board, Position queen) {
		int[] verticalConflicts = findVerticalConflictsForRow(board, queen.get_xPosition());
		int[] diagonalConflicts = findDiagonalConflictsForRow(board, queen.get_xPosition());
		int[] total = calculateTotalConflictsForRow(verticalConflicts, diagonalConflicts);
		return total;
	}
	
	private int calculateConflictsForQueen(int[][] board, Position queen) {
		int x = queen.get_xPosition();
		int y = queen.get_yPosition();
		int conflicts = 0;
		
		for(int row = 0; row < board.length; row ++) {
			if (row == x) {
				continue;
			}
			if(board[row][y] == 1) {
				conflicts += 1;
			}
		}

		int startRow = x;
		int startCol = y;
		
		while((startRow - 1) >= 0 && (startCol - 1) >= 0) {
			startRow--;
			startCol--;
		}
		
		while(startRow < board.length && startCol < board.length) {
			if(board[startRow][startCol] == 1 && startRow != x ) {
				conflicts += 1;
			}
			startRow++;
			startCol++;
		}
		
		startRow = x;
		startCol = y;
		
		while((startRow - 1) >= 0 && (startCol + 1) < board.length) {
			startRow--;
			startCol++;
		}
		
		while(startRow < board.length && startRow >= 0 && startCol >= 0 && startCol < board.length) {
			if(board[startRow][startCol] == 1 && startRow != x) {
				conflicts += 1;
			}
			startRow++;
			startCol--;
		}
		return conflicts;
	}

	private int[] calculateTotalConflictsForRow(int[] vertical, int[] diagonal) {
		int[] total = new int[vertical.length];
		for(int i = 0; i < vertical.length; i ++) {
			total[i] = vertical[i] + diagonal[i];
		}
		return total;
	}
	
	private int[] findVerticalConflictsForRow(int[][] board, int rrow) {
		int[] conflicts = new int[board.length];
		for(int row = 0; row < board.length; row++) {
			if(row == rrow) {
				continue;
			}
			for(int column = 0; column < board.length; column ++) {
				if(board[row][column] == 1){
					conflicts[column] += 1;
				}
			}
		}
		return conflicts;
	}
		
	private int[] findDiagonalConflictsForRow(int[][] board, int rrow) {
		int[] conflicts = new int[board.length];
		
		for(int i = 0; i < board.length; i ++) {
			
			int startRow = rrow;
			int startCol = i;
			
			while((startRow - 1) >= 0 && (startCol - 1) >= 0) {
				startRow--;
				startCol--;
			}
			
			while(startRow < board.length && startCol < board.length) {
				if(board[startRow][startCol] == 1 && startRow != rrow ) {
					conflicts[i] += 1;
				}
				startRow++;
				startCol++;
			}
			
			startRow = rrow;
			startCol = i;
			
			while((startRow - 1) >= 0 && (startCol + 1) < board.length) {
				startRow--;
				startCol++;
			}
			
			while(startRow < board.length && startRow >= 0 && startCol >= 0 && startCol < board.length) {

				if(board[startRow][startCol] == 1 && startRow != rrow) {
					conflicts[i] += 1;
				}
				startRow++;
				startCol--;
			}
		}
		return conflicts;
	}
}