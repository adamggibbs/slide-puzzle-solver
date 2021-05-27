import java.util.Queue;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.ReentrantLock;

public class ProcessStateTask implements Runnable{

    // instance variables
    Puzzle puzzle;
    Queue<Puzzle> sharedQueue;
    int minSolution;
    AtomicReference<Puzzle> solved;
    ReentrantLock l;

    // constructor
    public ProcessStateTask(Puzzle puzzle, Queue<Puzzle> sharedQueue, int minSolution, AtomicReference<Puzzle> solved){
        this.puzzle = puzzle;
        this.sharedQueue = sharedQueue;
        this.minSolution = minSolution;
        this.solved = solved;
        l = new ReentrantLock();
    }

    public void run(){
            if (puzzle.isSolved()){

                // lock the next section so two threads can't add a solution 
                // at the same time
                // unlock once done
                l.lock();
                try {
                    
                    // if the solution is less than minSolution we have,
                    // set solved puzzle Atomic Reference to point to 
                    // new solved puzzle
                    // update minSolution
                    if(puzzle.prevMoves.size() < minSolution){

                        minSolution = puzzle.prevMoves.size();
                        solved.set(puzzle);

                    }
                    
                } finally {
                    // unlock once critical section has been completed
                    l.unlock();
                }
                

            } 
            // else add next possible states to the queue
            // this code is identical to that in sequential solver Solver.solve()
            // see those comments for explaination
            else if((puzzle.prevMoves.size()+1) < minSolution){

                int last_move=puzzle.prevMoves.get(puzzle.prevMoves.size()-1);

                if (last_move!=1){
                    Puzzle toAdd = Solver.copy(puzzle);
                    if (toAdd.move_down()){
                        sharedQueue.add(toAdd);
                    }
                }

                if (last_move!=0){
                    Puzzle toAdd1 = Solver.copy(puzzle);
                    if (toAdd1.move_up()){
                        sharedQueue.add(toAdd1);
                    }

                }

                if (last_move!=3){
                    Puzzle toAdd2 = Solver.copy(puzzle);
                    if (toAdd2.move_right()){
                        sharedQueue.add(toAdd2);
                    }
                }

                if (last_move!=2){
                    Puzzle toAdd3 = Solver.copy(puzzle);
                    if (toAdd3.move_left()){
                        sharedQueue.add(toAdd3);
                    }
                }
            }

    }

}