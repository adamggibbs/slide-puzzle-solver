import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class ParallelPruneSequentialTask implements Runnable{

    // instance variables
    Puzzle initialPuzz;
    AtomicReference<Puzzle> solved;
    HashSet<String> checkedPuzzles;

    // constructor
    public ParallelPruneSequentialTask(Puzzle initialPuzz, AtomicReference<Puzzle> solved){
        this.initialPuzz = initialPuzz;
        this.solved = solved;
        this.checkedPuzzles = new HashSet<String>();
    }

    // run() method
    // run sequential solve starting at given puzzle
    // when solve() returns a puzzle, check is thread has been interrupted
    // if it hasn't, return solved puzzle
    public void run(){
        Puzzle solvedPuzzle = solve(initialPuzz);
        if(!Thread.currentThread().isInterrupted()){
            solved.set(solvedPuzzle);
        }
    }

    // solve() method
    // nearly identical to sequential solve in Solver.solve()
    // but checks to see if thread has been interrupted
    public Puzzle solve(Puzzle initialPuzz){

        // create a queue to store states needed to be checked
        Queue<Puzzle> q = new LinkedList<>();

        // add initial puzzle to the queue
        q.add(initialPuzz);

        // while there are states to be checked and the pool has not been shutdown
        while (!q.isEmpty() && !Thread.currentThread().isInterrupted()){

          // get the next state to be checked
          Puzzle puzzle=q.remove();

          if(alreadyChecked(puzzle)){
            continue;
          }

          // see is state is solved
          if (puzzle.isSolved()){
            return puzzle;
          }
          // otherwise, add all possible next states
          // identical to Solver.solve code()
          // see Solver.solve() for explainations in comments
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

        // if no solution is found return initial puzzle
        // this is reached if thread is interrupted
        return initialPuzz;

    }

    private boolean alreadyChecked(Puzzle toCheck){

        int[][] puzzle = toCheck.puzzle;

        String id = "";

        for(int i = 0; i < puzzle.length; i++){
            for(int j = 0; j < puzzle[0].length; j++){
                id += puzzle[i][j];
            }
        }

        if(checkedPuzzles.contains(id)){
            return true;
        } else {
            checkedPuzzles.add(id);
            return false;
        }
        
    }


}
