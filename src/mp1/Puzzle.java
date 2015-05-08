package mp1;

public class Puzzle {
	
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("You didn't specify a configuration file.");
			System.out.println("Usage: 'java Puzzle <configuration file>' ");
		}
		else if(args.length == 1) {
			Simulator simulator = new Simulator();
			simulator.setup(args[0], false);
			simulator.run();
		}
		else if(args.length == 2 && (args[1].equals("true") || args[1].equals("false"))) {
			Simulator simulator = new Simulator();
			simulator.setup(args[0], Boolean.valueOf(args[1]));
			simulator.run();
		}
		else {
			System.out.println("Hold your horses! Only one file, please.");
			System.out.println("Usage: 'java Puzzle <configuration file>' ");
		}
	}
}