package mp1;

import java.io.BufferedReader;
import java.io.FileReader;

public class Simulator {
	
	final long simulationTime = 60000; //1800000; // 30 minutes
	
	int K;
	State initialState;
	State bestState;
	Simulation sim;
	int loopCount;
	long startTime;
	boolean useLoopCounter;
	
	ConflictSupport conflictSupport;
	BoardAnalyzer boardAnalyzer;
	
	public void setup(String filename, boolean loop) {
		Position[] init = createEnvironment(filename);
		initialState = new State(init);
		bestState = new State(1000, null);
		conflictSupport = new ConflictSupport();
		boardAnalyzer = new BoardAnalyzer();
		useLoopCounter = loop;
	}
	
	public void run() {
		startTime = System.currentTimeMillis();
		boolean victory = false;
		while(!victory){
			if(abort(startTime)) {
				System.out.println("Problem took to long to solve. Aborting...");
				break;
			}
			sim = new Simulation(initialState, K);
			simulate();
		}
		if(!victory) {
			System.out.println("The most optimal solution the program managed to produce was: ");
			sim.displayState(bestState, startTime);
		}
	}
	
	public void simulate() {
	
		State currentState = sim.get_currentState();
		int[][] simulationBoard = sim.get_board();
		int availMoves = sim.get_availableMoves();
		loopCount = 0;
		boolean isFirst = true;

		while(availMoves > 0) {
			Position queen;
			if(isFirst) {
				queen = conflictSupport.pickInitialQueenAtRandom(currentState.getQueens());
				isFirst = false;
			}
			else {
				queen = conflictSupport.findQueenWithMostConflicts(simulationBoard, currentState.getQueens());			
			}
			
			int[] conflictArrayForSelectedQueenColumn = conflictSupport.calculateConflictPositionsInSelectedQueensColumn(simulationBoard, queen);
			Position destination = boardAnalyzer.findBestDestination(conflictArrayForSelectedQueenColumn, queen, availMoves);
			availMoves = move(simulationBoard, queen, destination, availMoves);
			
			int currentConflicts = conflictSupport.getTotalConflictsOnBoard(simulationBoard, currentState.getQueens());
			currentState.setConflicts(currentConflicts);
			
			if(currentConflicts == 0) {
				State win = new State(0, currentState.getQueens());
				System.out.println("Found an optimal solution to the problem!");
				sim.displayState(win, startTime);
			}
		}
		
		int currentConflicts = conflictSupport.getTotalConflictsOnBoard(simulationBoard, currentState.getQueens());
		if( currentConflicts < bestState.getConflicts() ){
			bestState = new State(currentConflicts, currentState.getQueens());
		}
	}
	
	public int move(int[][] board, Position current, Position destination, int availMoves) {
		if(current.get_yPosition() != destination.get_yPosition()) {
			board[current.get_xPosition()][current.get_yPosition()] = 0;
			board[current.get_xPosition()][destination.get_yPosition()] = 1;
			availMoves -= (Math.abs(current.get_yPosition() - destination.get_yPosition()));
			current.set_yPosition(destination.get_yPosition());
		}
		else {
			double escapeProbability = Math.random() * 10;
			
			if((int) escapeProbability < 2){
				int escape = 0;
				try {
					escape = current.get_yPosition() + 1;
					board[current.get_xPosition()][current.get_yPosition()] = 0;
					board[current.get_xPosition()][escape] = 1;
					availMoves -= (Math.abs(current.get_yPosition() - escape));
					current.set_yPosition(escape);
				} catch(IndexOutOfBoundsException e) {
					escape = current.get_yPosition() - 1;
					board[current.get_xPosition()][current.get_yPosition()] = 0;
					board[current.get_xPosition()][escape] = 1;
					availMoves -= (Math.abs(current.get_yPosition() - escape));
					current.set_yPosition(escape);	
				}
			}
			else {
				loopCount += 1;
			}
		}
		
		if(loopCount == initialState.getQueens().length * initialState.getQueens().length && useLoopCounter == true) {
			int currentConflicts = conflictSupport.getTotalConflictsOnBoard(sim.get_board(), sim.get_currentState().getQueens());
			if( currentConflicts < bestState.getConflicts() ){
				bestState = new State(currentConflicts, sim.get_currentState().getQueens());
			}
			System.out.println("The loop counter suggested that this isn't going anywhere.");
			sim.displayState(bestState, startTime);
		}
		return availMoves;
	}
	
	private Position[] createEnvironment(String filename){
		Position[] initialPositions = null;
	    try {
	        BufferedReader br = new BufferedReader(new FileReader(filename));
	        int counter = 0;
	        String line;
	        while ((line = br.readLine()) != null) {
	        	
	        	if(counter == 0){
	        		int N = Integer.parseInt(line);
	        		initialPositions = new Position[N];
	        	}
	        	else if(counter == 1){
	        		K = Integer.parseInt(line);
	        	}
	        	else{
	        		String[] posi = line.split(" ");
	        		int row = Integer.parseInt(posi[0]);
	        		int column = Integer.parseInt(posi[1]);
	        		initialPositions[counter-2] = new Position(column-1, row-1);
	        	}
	        	counter ++;
	        }
	        br.close();
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
	    
	    return initialPositions;
	}
	
	private boolean abort(long startTime){
		long currentTime = System.currentTimeMillis();
		return (currentTime - startTime > simulationTime) ? true : false;
	}
}