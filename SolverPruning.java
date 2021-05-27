import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SolverPruning {

    // arraylist that holds all the generated states for pruning in sequential solve
    static ArrayList<PuzzlePruning> q = new ArrayList<>();

    //Uses a BFS of all possible puzzle states to find
    //the shortest possible moves to reach a solved puzzle
    public static PuzzlePruning solve(PuzzlePruning initialPuzz){

        //
        q.add(initialPuzz);
        int index = 0;
        while (!q.isEmpty()){
            PuzzlePruning puzzlePruning = q.get(index++);
            if (puzzlePruning.isSolved()){

                return puzzlePruning;
            } else{
                int last_move = puzzlePruning.preMove;
                /* each of these if statements produces the puzzle state only if it isn't the reverse of the previous move
                *  and it only adds it to the arraylist if it isn't already in the array list
                * */
                if (last_move!=1){
                    if(puzzlePruning.open_r != puzzlePruning.puzzle.length-1){

                        PuzzlePruning toAdd = new PuzzlePruning(puzzlePruning, 0);
                        toAdd.move_down();
                        if(notInQueue(toAdd)) {
                            q.add(toAdd);
                        }
                    }
                }
                if (last_move!=0){
                    if(puzzlePruning.open_r != 0){
                        PuzzlePruning toAdd = new PuzzlePruning(puzzlePruning, 1);
                        toAdd.move_up();
                        if(notInQueue(toAdd)) {
                            q.add(toAdd);
                        }
                    }
                }
                if (last_move!=3){
                    if(puzzlePruning.open_c != puzzlePruning.puzzle[0].length-1){
                        PuzzlePruning toAdd = new PuzzlePruning(puzzlePruning, 2);
                        toAdd.move_right();
                        if(notInQueue(toAdd)) {
                            q.add(toAdd);
                        }
                    }
                }
                if (last_move!=2){
                    if(puzzlePruning.open_c != 0){
                        PuzzlePruning toAdd = new PuzzlePruning(puzzlePruning, 3);
                        toAdd.move_left();
                        if(notInQueue(toAdd)) {
                            q.add(toAdd);
                        }
                    }
                }
            }
        }
        return initialPuzz;
    }

    // parallel solver with one combiner and multiple threads producing next states
    public static PuzzlePruning parallelSolve(PuzzlePruning initialPuzz){

        // if the puzzle is solved return
        if(initialPuzz.isSolved()){
           return initialPuzz;
        }

        int nThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(nThreads);

        // biglist is the equivalent of the arraylist in the single thread version
        // it stores all the previous moves generated
        // it is only used in this main thread and the combiner thread
        ConcurrentHashMap<Integer, PuzzlePruning> bigList = new ConcurrentHashMap<>();

        // for this map each key is a thread ID and the hashset is the next states that the thread produces
        ConcurrentHashMap<Integer, HashSet<PuzzlePruning>> map = new ConcurrentHashMap<>();

        // this boolean stops the thread constructor once a thread finds a solution
        // this means no new threads will be created but all the other
        AtomicBoolean solutionFound = new AtomicBoolean(false);

        // this is the reference to the first solution that the combiner comes to which due to the use of IDs will be the shortest solution
        AtomicReference<PuzzlePruning> solution = new AtomicReference<>();

        // add first state to the 'queue' its not a queue because its going by increasing ID through a hashmap
        bigList.put(0,initialPuzz);

        // start the combiner task
        CombinerTask combiner = new CombinerTask(bigList, map, solution);
        pool.execute(combiner);

        // the ID is used to go through the bigList in the correct order
        int ID = 0;

        while(!solutionFound.get()){
            // go through the list in the correct order -> the order the states are generated in
            while(!bigList.containsKey(ID)) {
                //wait
            }

            // start next task, each task gets the atomic boolean, its ID,
            NextStatesTaskPruning task = new NextStatesTaskPruning(solutionFound, ID, bigList.get(ID), map);
            pool.execute(task);
            ID++;
        }
        // wait until the combiner finds the earliest solution in its list
        while(solution.get() == null){
            //wait
        }
        pool.shutdownNow();
        return solution.get();

    }

    // parallel solver with two combiners and multiple threads producing next states
    public static PuzzlePruning parallelSolve_V2(PuzzlePruning initialPuzz){

        // if the puzzle is solved return
        if(initialPuzz.isSolved()){
            return initialPuzz;
        }

        int nThreads = Runtime.getRuntime().availableProcessors();
        // this solver doesn't
        if(nThreads < 3){
            System.out.println("ParallelSolve_V2 can't be run with this number of available threads");
            return initialPuzz;
        }
        ExecutorService pool = Executors.newFixedThreadPool(nThreads);

        // biglist is the equivalent of the arraylist in the single thread version
        // it stores all the previous moves generated
        // it is only used in this main thread and the combiner threads
        ConcurrentHashMap<Integer, PuzzlePruning> bigList = new ConcurrentHashMap<>();

        // for this map each key is a thread ID and the hashset is the next states that the thread produces
        ConcurrentHashMap<Integer, HashSet<PuzzlePruning>> map = new ConcurrentHashMap<>();

        // this boolean stops the thread constructor once a thread finds a solution
        // this means no new threads will be created but all the other
        AtomicBoolean solutionFound = new AtomicBoolean(false);

        // this is the reference to the first solution that the combiner comes to which due to the use of IDs will be the shortest solution
        AtomicReference<PuzzlePruning> solutionOdd = new AtomicReference<>();
        AtomicReference<PuzzlePruning> solutionEven = new AtomicReference<>();
        // to make sure the shorter solution between the two combiners is returned
        // the values odd and even will be set to the threadID number that has the earlier solution
        // also the values odd and even tell each thread which threadIDs to check from the map
        AtomicInteger odd = new AtomicInteger(1);
        AtomicInteger even = new AtomicInteger(0);

        // add first state to the 'queue' its not a queue because its going by increasing ID through a hashmap
        bigList.put(0,initialPuzz);

        // start the combiner tasks
        CombinerTaskParallelV2 combinerOdd = new CombinerTaskParallelV2(bigList, map, solutionOdd, odd);
        CombinerTaskParallelV2 combinerEven = new CombinerTaskParallelV2(bigList, map, solutionEven, even);
        pool.execute(combinerOdd);
        pool.execute(combinerEven);

        int ID = 0;

        while(!solutionFound.get()){
            // go through the list in the correct order -> the order the states are generated in
            while(!bigList.containsKey(ID)) {
                //wait
            }

            // start next task, each task gets the atomic boolean, its ID,
            NextStatesTaskPruning task = new NextStatesTaskPruning(solutionFound, ID, bigList.get(ID), map);
            pool.execute(task);
            ID++;
        }

        // wait until the combiner finds the earliest solution in its list
        while(solutionOdd.get() == null && solutionEven.get() == null){
            //wait
        }
        pool.shutdownNow();

        // check to return the solution that comes first in the search tree
        // this is not entirely correct but it is an attempt to deal with the problem of
        // what happens if both threads find a solution at the same time but one solution has fewer moves
        if(solutionOdd.get() != null && solutionEven.get() != null){
            if(odd.get() < even.get()){
                return solutionOdd.get();
            }
            return solutionEven.get();
        }
        if(solutionOdd.get() == null){
            return solutionEven.get();
        }


        return solutionOdd.get();
    }



    //Prints the solved Puzzle and the moves using
    //the list of moves stored in each puzzle state
    public static void printSolution(PuzzlePruning puzzlePruning){
        if(!puzzlePruning.isSolved()){
            System.out.println("Puzzle is not solved.");
        } else if(puzzlePruning.preMove == -1){
            System.out.println("Puzzle has not been shuffled");
        } else {
            puzzlePruning.printPrevMoves();
        }
    }

    // this method iterates through the arrayList in the sequential version in the single thread solve
    // and puzzle states are only added to the arraylist if they aren't already in the arraylist
    // can't use arraylist.contains(puzzle) because two puzzle states could have the same arrangment but different parent states
    public static boolean notInQueue(PuzzlePruning check){
        for(PuzzlePruning p : q){
            if(Arrays.deepEquals(p.puzzle, check.puzzle)){
                return false;
            }
        }
        return true;
    }

}
