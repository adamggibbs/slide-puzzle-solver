import java.util.concurrent.atomic.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Solver {

    //Uses a BFS of all possible puzzle states to find
    //the shortest possible moves to reach a solved puzzle
    // SEQUENTIAL SOLVE
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Puzzle solve(Puzzle initialPuzz){

      Queue<Puzzle> q = new LinkedList<>();

      q.add(initialPuzz);
      while (!q.isEmpty()){

        Puzzle puzzle=q.remove();
        if (puzzle.isSolved()){
          return puzzle;
        }
        else{
          int last_move=puzzle.prevMoves.get(puzzle.prevMoves.size()-1);
          if (last_move!=1){
            Puzzle toAdd = Solver.copy(puzzle);
            if (toAdd.move_down()){
                q.add(toAdd);
            }
          }
          if (last_move!=0){
            Puzzle toAdd1 = Solver.copy(puzzle);
            if (toAdd1.move_up()){
                q.add(toAdd1);
            }

          }
          if (last_move!=3){
            Puzzle toAdd2 = Solver.copy(puzzle);
            if (toAdd2.move_right()){
                q.add(toAdd2);
            }
          }
          if (last_move!=2){
            Puzzle toAdd3 = Solver.copy(puzzle);
            if (toAdd3.move_left()){
                q.add(toAdd3);
            }
          }
        }

      }

      return initialPuzz;

    }


    // PARALLEL SOLVE
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Puzzle parallelSolve(Puzzle initialPuzz){

      int nThreads = Runtime.getRuntime().availableProcessors();
      ExecutorService pool = Executors.newFixedThreadPool(nThreads);

      int minSolution = Integer.MAX_VALUE;
      AtomicBoolean isSolved = new AtomicBoolean(false);
      AtomicReference<Puzzle> solved = new AtomicReference<>();

      // need to finish code here to create and execute tasks
      ConcurrentLinkedQueue<Puzzle> q = new ConcurrentLinkedQueue<Puzzle>();

      q.add(initialPuzz);

      // while we don't have a solution we have to keep looking
      // PROBLEM: I THINK THIS READ OP IS WICKED SLOW
      while (solved.get() == null){
        // only add a task if the queue isn't empty
        // have to do this cause queue will be empty while initial puzz
        // is being processed before task adds it's children

        // Will it make a difference if we create this variable, then
        // pass it to the task
        // or if we call q.remove() as a parameter
        if(!q.isEmpty()){
          Puzzle nextState = q.remove();
          ProcessStateTask nextStateTask = new ProcessStateTask(nextState, q, minSolution, solved, isSolved);
          // ProcessStateTask nextStateTask = new ProcessStateTask(q.remove(), q, minSolution, solved);
          pool.execute(nextStateTask);
        }

      }

      // shutdown the pool!
      pool.shutdown();

      // solved will have a puzzle
      // while loop cannot end until it isn't null
      // returns a solved puzzle
      return solved.get();

    }

    // PARALLEL PRUNED SOLVE - NOT FUNCTIONAL
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Puzzle parallelPrunedSolve(Puzzle initialPuzz){

      int nThreads = Runtime.getRuntime().availableProcessors();
      ExecutorService pool = Executors.newFixedThreadPool(nThreads);

      ConcurrentHashMap<String, Boolean> usedPuzzes = new ConcurrentHashMap<>();

      int minSolution = Integer.MAX_VALUE;
      AtomicBoolean isSolved = new AtomicBoolean(false);
      AtomicReference<Puzzle> solved = new AtomicReference<>();

      // need to finish code here to create and execute tasks
      ConcurrentLinkedQueue<Puzzle> q = new ConcurrentLinkedQueue<Puzzle>();

      q.add(initialPuzz);

      // while we don't have a solution we have to keep looking
      // PROBLEM: I THINK THIS READ OP IS WICKED SLOW
      while (solved.get() == null){
        // only add a task if the queue isn't empty
        // have to do this cause queue will be empty while initial puzz
        // is being processed before task adds it's children

        // Will it make a difference if we create this variable, then
        // pass it to the task
        // or if we call q.remove() as a parameter
        if(!q.isEmpty()){
          Puzzle nextState = q.remove();
          ProcessPruneStateTask nextStateTask = new ProcessPruneStateTask(nextState, q, minSolution, solved, isSolved, usedPuzzes);
          pool.execute(nextStateTask);
        }

      }

      // shutdown the pool!
      pool.shutdown();

      // solved will have a puzzle
      // while loop cannot end until it isn't null
      // returns a solved puzzle
      return solved.get();

    }

    // PARALLEL TREE SOLVE
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Puzzle parallelTreeSolve(Puzzle initialPuzz){

      int nThreads = Runtime.getRuntime().availableProcessors();
      ExecutorService pool = Executors.newFixedThreadPool(nThreads);

      AtomicBoolean isSolved = new AtomicBoolean(false);
      AtomicReference<Puzzle> solved = new AtomicReference<>();

      // need to finish code here to create and execute tasks
      Queue<Puzzle> q = new LinkedList<Puzzle>();

      q.add(initialPuzz);

      while (q.size() <= (nThreads-4)){

        Puzzle puzzle=q.remove();
        if (puzzle.isSolved()){
          return puzzle;
        }
        else{
          int last_move=puzzle.prevMoves.get(puzzle.prevMoves.size()-1);
          if (last_move!=1){
            Puzzle toAdd = Solver.copy(puzzle);
            if (toAdd.move_down()){
                q.add(toAdd);
            }
          }
          if (last_move!=0){
            Puzzle toAdd1 = Solver.copy(puzzle);
            if (toAdd1.move_up()){
                q.add(toAdd1);
            }

          }
          if (last_move!=3){
            Puzzle toAdd2 = Solver.copy(puzzle);
            if (toAdd2.move_right()){
                q.add(toAdd2);
            }
          }
          if (last_move!=2){
            Puzzle toAdd3 = Solver.copy(puzzle);
            if (toAdd3.move_left()){
                q.add(toAdd3);
            }
          }
        }

      }

      while(!q.isEmpty()){

        ParallelSequentialTask task = new ParallelSequentialTask(q.remove(), isSolved, solved);
        pool.execute(task);

      }

      while(true){
        if(isSolved.get()){
          break;
        }
      }

      // shutdown the pool!
      pool.shutdownNow();

      // solved will have a puzzle
      // while loop cannot end until it isn't null
      // returns a solved puzzle
      return solved.get();

    }

    // ADDITIONAL METHODS
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Prints the solved Puzzle and the moves using
    //the list of moves stored in each puzzle state
    public static void printSolution(Puzzle puzzle){
      if(!puzzle.isSolved()){
        System.out.println("Puzzle is not solved.");
      } else if(puzzle.prevMoves.isEmpty()){
        System.out.println("Puzzle has not been shuffled");
      } else {

        System.out.println("It took " + (puzzle.prevMoves.size()-1) + " moves to solve the puzzle.");
        System.out.println("The moves were: ");
        puzzle.printPrevMoves();

      }
    }

    //Creates a deep copy of the current puzzle to be
    //added to the queue for the BFS
    public static Puzzle copy(Puzzle puzzle){
      Puzzle newPuzz=new Puzzle(puzzle.puzzle[0].length, puzzle.puzzle.length);
      newPuzz.open_c=puzzle.open_c;
      newPuzz.open_r=puzzle.open_r;

      for (int i=0;i<puzzle.prevMoves.size();i++){
        newPuzz.prevMoves.add(puzzle.prevMoves.get(i));
      }
      for (int i=0;i<puzzle.puzzle.length;i++){
        for (int j=0;j<puzzle.puzzle[0].length;j++){
          newPuzz.puzzle[i][j]=puzzle.puzzle[i][j];
        }
      }
      return newPuzz;
    }
}
