
public class Main {
    public static void main(String[] args){

        // default setting for n x m
        int n = 4;
        int m = 4;

        // see if user specified preferences for dimensions of puzzle
        if(args.length > 0){
                if(args.length == 1 || args.length > 2){
                        System.err.println("Incorrect number of command line arguments [" + args.length + "]");
                        System.err.println("\tPlease enter no command line arguments to use defualt puzzle dimensions, ");
                        System.err.println("\tor enter 2 integers as command line arguments (n and m) to specify a n x m puzzle.");
                        System.exit(1);
                }

                try{
                        n = Integer.parseInt(args[0]);
                        m = Integer.parseInt(args[1]);
                } catch(NumberFormatException e){
                        System.err.println("Command line arguments passed are not integers.");
                        System.err.println("\tPlease enter two integers to specify puzzle dimensions, ");
                        System.err.println("\tor enter no command line arguments to use default puzzle dimensions.");
                        System.exit(2);
                }

        }


	// create a n x m puzzle
        Puzzle puzzle = new Puzzle(n,m);

        // CODE BEYOND HERE CAN BE ALTERED FOR TESTING AND EXPLORATION:

	// shuffle the puzzle
        puzzle.shuffle(40);
        puzzle.print();

	// solve the puzzle
        puzzle = Solver.solve(puzzle);
        puzzle.print();
        Solver.printSolution(puzzle);

        // END OF CODE TESTING AND EXPLORATION CHUNK

        // Print an empty line before next terminal prompt for readability
	System.out.println();

    }
}
