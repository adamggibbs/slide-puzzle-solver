//Main Method
public class Main {
    public static void main(String[] args){


      // default setting for n x m
      int n = 4;
      int m = 4;

      // default number of times to shuffle puzzle
      int shuffles=40;

      // see if user specified preferences for dimensions of puzzle
      if(args.length > 0){
          if(args.length == 1 || args.length > 3){
              System.err.println("Incorrect number of command line arguments [" + args.length + "]");
              System.err.println("\tPlease enter no command line arguments to use defualt puzzle dimensions, ");
              System.err.println("\tor enter 3 integers as command line arguments (n and m and s) to specify a n x m puzzle with s shuffles.");
              System.exit(1);
          }

          try{
              n = Integer.parseInt(args[0]);
              m = Integer.parseInt(args[1]);
              if (args.length==3){
                shuffles = Integer.parseInt(args[2]);
              }

          } catch(NumberFormatException e){
              System.err.println("Command line arguments passed are not integers.");
              System.err.println("\tPlease enter three integers to specify puzzle dimensions and number of shuffles, ");
              System.err.println("\tor enter no command line arguments to use default puzzle dimensions.");
              System.exit(2);
          }

      }

	// create a n x m puzzle
        Puzzle puzzle = new Puzzle(n,m);

        // CODE BEYOND HERE CAN BE ALTERED FOR TESTING AND EXPLORATION:

	// shuffle the puzzle
        puzzle.shuffle(shuffles);
        System.out.println("Puzzle to be solved:");
        puzzle.print();

	// solve the puzzle

        long startTime = System.nanoTime();
        Puzzle puzzle3 = Solver.parallelTreeSolve(puzzle);
        long endTime = System.nanoTime();
        puzzle3.print();
        Solver.printSolution(puzzle3);
        long treeTime = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("It took parallel tree " + (treeTime / 1000000.0) + "ms to solve.");

        startTime = System.nanoTime();
        Puzzle puzzle1 = Solver.parallelSolve(puzzle);
        endTime = System.nanoTime();
        puzzle1.print();
        Solver.printSolution(puzzle1);
        long parallelTime = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("It took parallel " + (parallelTime / 1000000) + "ms to solve.");

        startTime = System.nanoTime();
        Puzzle puzzle2 = Solver.solve(puzzle);
        endTime = System.nanoTime();
        puzzle2.print();
        Solver.printSolution(puzzle2);
        long sequentialTime = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("It took sequential " + (sequentialTime / 1000000.0) + "ms to solve.");

        // prints a summary of the tests
        double speedup = (sequentialTime / 1000000.0) / (parallelTime / 1000000.0);
        System.out.println();
        System.out.println("Solution summary for puzzle shuffled "+shuffles+" times:");
        System.out.printf( "\n size | shuffles | solve type | avg solve time (ms) | avg speedup | # moves \n"
               + "------+----------+------------+---------------------+-------------+--------\n");
        System.out.printf("%5s |%9d |%11s |%20f |%12.2f |%6d \n", n+"x"+m, shuffles, "sequential", (sequentialTime / 1000000.0), 1.00, puzzle2.prevMoves.size()-1);
        System.out.println("------+----------+------------+---------------------+-------------+--------\n");
        System.out.printf("%5s |%9d |%11s |%20f |%12.2f |%6d \n", n+"x"+m, shuffles, "parallel", (parallelTime / 1000000.0), speedup, puzzle1.prevMoves.size()-1);
        speedup =  (sequentialTime / 1000000.0) / (treeTime / 1000000.0);
        System.out.println("------+----------+------------+---------------------+-------------+--------\n");
        System.out.printf("%5s |%9d |%11s |%20f |%12.2f |%6d \n", n+"x"+m, shuffles, "tree solve", (treeTime / 1000000.0), speedup, puzzle3.prevMoves.size()-1);
        System.out.println("------+----------+------------+---------------------+-------------+--------\n");


        // Print an empty line before next terminal prompt for readability
	       System.out.println();

    }
}
