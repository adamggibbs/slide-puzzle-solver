import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ParallelSequentialTask implements Runnable{

    Puzzle initialPuzz;
    AtomicBoolean isSolved; 
    AtomicReference<Puzzle> solved;

    public ParallelSequentialTask(Puzzle initialPuzz, AtomicBoolean isSolved, AtomicReference<Puzzle> solved){
        this.initialPuzz = initialPuzz;
        this.isSolved = isSolved;
        this.solved = solved;
    }

    public void run(){
        Puzzle solvedPuzzle = solve(initialPuzz);
        if(!Thread.currentThread().isInterrupted()){
            solved.set(solvedPuzzle);
            isSolved.set(true);
        }
    }

    public static Puzzle solve(Puzzle initialPuzz){

        Queue<Puzzle> q = new LinkedList<>();
  
        q.add(initialPuzz);
        while (!q.isEmpty() && !Thread.currentThread().isInterrupted()){
            
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
    
}