
public class MainPruning {
    public static void main(String[] args){

        // default setting for n x m
        int n = 4;
        int m = 4;
        int shuffles = 30;

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
                shuffles = Integer.parseInt(args[2]);

            } catch(NumberFormatException e){
                System.err.println("Command line arguments passed are not integers.");
                System.err.println("\tPlease enter three integers to specify puzzle dimensions and number of shuffles, ");
                System.err.println("\tor enter no command line arguments to use default puzzle dimensions.");
                System.exit(2);
            }

        }


        // create a n x m puzzle
        PuzzlePruning puzzlePruning = new PuzzlePruning(n,m);

        // shuffle the puzzle
        puzzlePruning.shuffle(shuffles);

        long startTime;
        long endTime;
        long duration;
        puzzlePruning.print();

        // start sequential solve
        startTime = System.nanoTime();
        PuzzlePruning puzzlePruning1 = SolverPruning.solve(puzzlePruning);
        endTime = System.nanoTime();
        //puzzle1.print(); // can be used to check to make sure the puzzle is in the solved position
        SolverPruning.printSolution(puzzlePruning1);
        duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("It took sequential with pruning " + (duration / 1000000) + "ms to solve.\n");

        //start parallel solve with one combiner
        startTime = System.nanoTime();
        PuzzlePruning puzzlePruning2 = SolverPruning.parallelSolve(puzzlePruning);
        endTime = System.nanoTime();
        //puzzle2.print(); // can be used to check to make sure the puzzle is in the solved position
        SolverPruning.printSolution(puzzlePruning2);
        duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("It took parallel with pruning and combiner " + (duration / 1000000) + "ms to solve.\n");

        // start parallel solve with two combiners
        startTime = System.nanoTime();
        PuzzlePruning puzzlePruning3 = SolverPruning.parallelSolve_V2(puzzlePruning);
        endTime = System.nanoTime();
        //puzzle2.print(); // can be used to check to make sure the puzzle is in the solved position
        SolverPruning.printSolution(puzzlePruning3);
        duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("It took parallel_V2 with pruning and two combiners " + (duration / 1000000) + "ms to solve.");

        // Print a line before next terminal prompt for readability
        System.out.println("-------------------------------------------------");
    }
}
