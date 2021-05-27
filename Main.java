//Main Method
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
        //Timers to be printed for avg solution time
        long parallelTime=0;
        long treeTime=0;
        long sequentialTime=0;

        //puzzles to check in avg tester
        int puzzlesToCheck=10;

        //numshuffles
        int shuffles=30;

        //passed booleans indicate if solves equal sequential
        boolean parallelPassed=true;
        boolean treePassed=true;

        //Runs through our generic tester multiple times to determine solve time avgs
        for (int i=0; i<puzzlesToCheck; i++){
          Puzzle puzzle = new Puzzle(n,m);
          puzzle.shuffle(shuffles);

          long startTime = System.nanoTime();
          Puzzle puzzle1 = Solver.parallelSolve(puzzle);
          long endTime = System.nanoTime();
          parallelTime+= (endTime - startTime);

          startTime = System.nanoTime();
          Puzzle puzzle2 = Solver.parallelTreeSolve(puzzle);
          endTime = System.nanoTime();
          treeTime += (endTime - startTime);  //divide by 1000000 to get milliseconds.

          startTime = System.nanoTime();
          Puzzle puzzle3 = Solver.solve(puzzle);
          endTime = System.nanoTime();
          sequentialTime += (endTime - startTime);  //divide by 1000000 to get milliseconds.

          //If the length of solve by parallelTreeSolve is worse than sequential
          // change passed to false and print out puzzle for testing
          if (puzzle1.prevMoves.size()!=puzzle2.prevMoves.size()){
            System.out.println("The following puzzle in tree solution failed");
            treePassed=false;
            puzzle.print();
            Solver.printSolution(puzzle1);
            Solver.printSolution(puzzle2);

          }
          //If the length of solve by parallelSolve is worse than sequential
          // change passed to false and print out puzzle for testing
          if (puzzle1.prevMoves.size()!=puzzle3.prevMoves.size()){
            System.out.println("The following puzzle in parallel solution failed");
            parallelPassed=false;
            puzzle.print();
            Solver.printSolution(puzzle1);
            Solver.printSolution(puzzle3);
          }
        }

        double speedup = (sequentialTime / puzzlesToCheck / 1000000.0) / (parallelTime / puzzlesToCheck / 1000000.0);

        //prints average speedups for a certain number of test puzzles
        System.out.println("Average solve time chart:");
        System.out.printf( "\n size | shuffles | solve type | avg solve time (ms) | avg speedup | passed \n"
               + "------+----------+------------+---------------------+-------------+--------\n");
        System.out.printf("%5s |%9d |%11s |%20f |%12.2f |%6b \n", n+"x"+m, shuffles, "sequential", (sequentialTime / puzzlesToCheck / 1000000.0), 1.00, true);
        System.out.println("------+----------+------------+---------------------+-------------+--------\n");
        System.out.printf("%5s |%9d |%11s |%20f |%12.2f |%6b \n", n+"x"+m, shuffles, "parallel", (parallelTime / puzzlesToCheck / 1000000.0), speedup, parallelPassed);
        speedup =  (sequentialTime / puzzlesToCheck / 1000000.0) / (treeTime / puzzlesToCheck / 1000000.0);
        System.out.println("------+----------+------------+---------------------+-------------+--------\n");
        System.out.printf("%5s |%9d |%11s |%20f |%12.2f |%6b \n", n+"x"+m, shuffles, "tree solve", (treeTime / puzzlesToCheck / 1000000.0), speedup, treePassed);
        System.out.println("------+----------+------------+---------------------+-------------+--------\n");


        // System.out.println("It took parallel on average " + (parallelTime / puzzlesToCheck / 1000000) + "ms to solve.");
        // System.out.println("It took parallelTreeSolve on average " + (treeTime / puzzlesToCheck / 1000000) + "ms to solve.");
        // System.out.println("It took sequential on average " + (sequentialTime / puzzlesToCheck / 1000000) + "ms to solve.");

	// create a n x m puzzle
        Puzzle puzzle = new Puzzle(n,m);

        // CODE BEYOND HERE CAN BE ALTERED FOR TESTING AND EXPLORATION:

	// shuffle the puzzle
        puzzle.shuffle(shuffles);
        System.out.println("An example puzzle:");
        puzzle.print();

	// solve the puzzle
        long startTime = System.nanoTime();
        Puzzle puzzle1 = Solver.solve(puzzle);
        long endTime = System.nanoTime();
        puzzle1.print();
        Solver.printSolution(puzzle1);
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("It took sequential " + (duration / 1000000) + "ms to solve.");



        // Print an empty line before next terminal prompt for readability
	System.out.println();

    }
}
