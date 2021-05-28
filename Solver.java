import java.util.concurrent.atomic.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Solver {

    
    // SEQUENTIAL SOLVE
    /* Use Breadth First Search to find the optimal solution to slide puzzle
     * Sequentially checks every puzzle state and if it's not a solution, 
     *    adds all next possible states to a queue to be checked later
     * Returns the solved puzzle with an Arraylist of the moves taken
     */
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Puzzle solve(Puzzle initialPuzz){

      // queue to store puzzle states needed to be checked
      Queue<Puzzle> q = new LinkedList<>();

      // add initial puzzle to the queue
      q.add(initialPuzz);

      // while there are puzzles to be checked,
      // keep processing the states
      while (!q.isEmpty()){

        // remove next puzzle in the queue to be processed
        Puzzle puzzle=q.remove();

        // check is puzzle is solved,
        // if so, return it
        if (puzzle.isSolved()){
          return puzzle;
        }

        // otherwise, add all possible next states to the queue
        // don't add state we just came from 
        // (i.e. don't move up if previous move was move down)
        // don't do impossible moves, when empty piece is on an edge
        else{
          // get the previous move so we don't go back
          int last_move=puzzle.prevMoves.get(puzzle.prevMoves.size()-1);

          // if last move wasn't up,
          // see if you can move down
          // if so, add next state to queue
          if (last_move!=1){
            Puzzle toAdd = Solver.copy(puzzle);
            if (toAdd.move_down()){
                q.add(toAdd);
            }
          }

          // if last move wasn't down,
          // see if you can move up
          // if so, add next state to queue
          if (last_move!=0){
            Puzzle toAdd1 = Solver.copy(puzzle);
            if (toAdd1.move_up()){
                q.add(toAdd1);
            }

          }

          // if last move wasn't left,
          // see if you can move right
          // if so, add next state to queue
          if (last_move!=3){
            Puzzle toAdd2 = Solver.copy(puzzle);
            if (toAdd2.move_right()){
                q.add(toAdd2);
            }
          }

          // if last move wasn't right,
          // see if you can move left
          // if so, add next state to queue
          if (last_move!=2){
            Puzzle toAdd3 = Solver.copy(puzzle);
            if (toAdd3.move_left()){
                q.add(toAdd3);
            }
          }
        }

      }

      // if no solution is ever found return the initial puzzle
      // this should not be reached
      return initialPuzz;

    }


    // PARALLEL SOLVE
    /* Use Breadth First Search to find the optimal solution to slide puzzle
     * Checks every puzzle state in parallel and if it's not a solution, 
     *    adds all next possible states to a queue to be checked later
     * Each check is added to a thread pool with the task of checking if
     *    current state is a solution and then adding children to concurrent queue
     * So, each puzzle state becomes its own pool task
     * Returns the solved puzzle with an Arraylist of the moves taken
     */
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Puzzle parallelSolve(Puzzle initialPuzz){

      // get number of available threads and create a pool of those threads
      int nThreads = Runtime.getRuntime().availableProcessors();
      ExecutorService pool = Executors.newFixedThreadPool(nThreads);

      // store minSolution as max integer value
      int minSolution = Integer.MAX_VALUE;
      // create Atomic variable to store solved puzzle object
      AtomicReference<Puzzle> solved = new AtomicReference<>();

      // create concurrent queue to store puzzle states to be checked
      // must be concurrent because multiple threads are adding to it
      ConcurrentLinkedQueue<Puzzle> q = new ConcurrentLinkedQueue<Puzzle>();

      // add initial puzzle to the queue
      q.add(initialPuzz);

      // while we don't have a solution, 
      // keep creating new tasks to check states in queue
      while (solved.get() == null){
        // only add a task if the queue isn't empty
        // have to do this cause queue will be empty while initial puzz
        // is being processed before task adds it's children
        if(!q.isEmpty()){

          // create a new task and execute it
          ProcessStateTask nextStateTask = new ProcessStateTask(q.remove(), q, minSolution, solved);
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
    /* Use Breadth First Search to find the optimal solution to slide puzzle
     * Splits search tree into (approximately) nThreads subtrees and performs
     *    the sequential BFS search on each subtree 
     * In each subtree it sequentially checks every puzzle state and if it's 
     *    not a solution, adds all next possible states to a queue to be checked later
     * Returns the solved puzzle with an Arraylist of the moves taken
     */
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Puzzle parallelTreeSolve(Puzzle initialPuzz){

      // get number of available threads and create a pool of them
      int nThreads = Runtime.getRuntime().availableProcessors();
      ExecutorService pool = Executors.newFixedThreadPool(nThreads);

      // create Atomic varibale to store solved puzzle
      AtomicReference<Puzzle> solved = new AtomicReference<>();

      // create a queue to store initial states
      Queue<Puzzle> q = new LinkedList<Puzzle>();

      // add initial puzzle to the queue
      q.add(initialPuzz);

      // perform sequential solve as in Solver.solve() until
      // size of queue is grater than nThreads-3
      // we go to nThreads-3 becuase at most 3 children are
      // added from each state and we can't have more than nThreads
      // states in the queue
      while (q.size() <= (nThreads-3)){

        // this code is identical to that in Solver.solve()
        // see Solver.solve() for explainations in comments
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

      // for each state in the queue, create a task that performs
      // sequential solve starting at that puzzle state
      while(!q.isEmpty()){

        ParallelSequentialTask task = new ParallelSequentialTask(q.remove(), solved);
        pool.execute(task);

      }

      // constantly check is a solution has been reached
      // when there is a solution, break to shutdown pool and return
      while(true){
        if(solved.get() != null){
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

    // PARALLEL PRUNE TREE SOLVE
    /* Use Breadth First Search to find the optimal solution to slide puzzle
     * Splits search tree into (approximately) nThreads subtrees and performs
     *    the sequential BFS search on each subtree 
     * In each subtree it sequentially checks every puzzle state and if it's 
     *    not a solution, adds all next possible states to a queue to be checked later
     * Keeps a HashSet in each thread so threads 
     * Returns the solved puzzle with an Arraylist of the moves taken
     */
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Puzzle parallelPruneTreeSolve(Puzzle initialPuzz){

      // get number of available threads and create a pool of them
      int nThreads = Runtime.getRuntime().availableProcessors();
      ExecutorService pool = Executors.newFixedThreadPool(nThreads);

      // create Atomic varibale to store solved puzzle
      AtomicReference<Puzzle> solved = new AtomicReference<>();

      // create a queue to store initial states
      Queue<Puzzle> q = new LinkedList<Puzzle>();

      // add initial puzzle to the queue
      q.add(initialPuzz);

      // perform sequential solve as in Solver.solve() until
      // size of queue is grater than nThreads-3
      // we go to nThreads-3 becuase at most 3 children are
      // added from each state and we can't have more than nThreads
      // states in the queue
      while (q.size() <= (nThreads-3)){

        // this code is identical to that in Solver.solve()
        // see Solver.solve() for explainations in comments
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

      // for each state in the queue, create a task that performs
      // sequential solve starting at that puzzle state
      while(!q.isEmpty()){

        ParallelPruneSequentialTask task = new ParallelPruneSequentialTask(q.remove(), solved);
        pool.execute(task);

      }

      // constantly check is a solution has been reached
      // when there is a solution, break to shutdown pool and return
      while(true){
        if(solved.get() != null){
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

    // Prints the solved Puzzle and the moves using
    // the list of moves stored in each puzzle state
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

    // Creates a deep copy of the current puzzle to be
    // added to the queue for the BFS
    public static Puzzle copy(Puzzle puzzle){

      // create a new puzzle for old puzzle to be copied to
      Puzzle newPuzz=new Puzzle(puzzle.puzzle[0].length, puzzle.puzzle.length);

      // transfer pointer to open space over to new puzzle
      newPuzz.open_c=puzzle.open_c;
      newPuzz.open_r=puzzle.open_r;

      // copy over the prevMoves array
      for (int i=0;i<puzzle.prevMoves.size();i++){
        newPuzz.prevMoves.add(puzzle.prevMoves.get(i));
      }

      // copy over each element from old puzzle array
      // to the new puzzle array
      for (int i=0;i<puzzle.puzzle.length;i++){
        for (int j=0;j<puzzle.puzzle[0].length;j++){
          newPuzz.puzzle[i][j]=puzzle.puzzle[i][j];
        }
      }

      // return new copied puzzle
      return newPuzz;
      
    }
}
