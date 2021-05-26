import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.ReentrantLock;

public class ProcessPruneStateTask implements Runnable{

    Puzzle puzzle;
    Queue<Puzzle> sharedQueue;
    int minSolution;
    AtomicReference<Puzzle> solved;
    AtomicBoolean isSolved;
    ConcurrentHashMap<String, Boolean> usedPuzzes;
    ReentrantLock l;

    public ProcessPruneStateTask(Puzzle puzzle, Queue<Puzzle> sharedQueue, int minSolution, AtomicReference<Puzzle> solved, AtomicBoolean isSolved, ConcurrentHashMap<String, Boolean> usedPuzzes){
        this.puzzle = puzzle;
        this.sharedQueue = sharedQueue;
        this.minSolution = minSolution;
        this.solved = solved;
        this.isSolved = isSolved;
        this.usedPuzzes = usedPuzzes;
        l = new ReentrantLock();
    }

    public void run(){
            if (puzzle.isSolved()){

                // need to create a lock method for getting and changing the shortest solution
                // I used a java implemented lock, I saw we use this until everything else is
                // working then we can try implementing our own
                l.lock();
                try {

                    /* NOTE TO BEN & SCOTT: do we need minSolution
                     * to be atomic since we're using a lock?
                     * with a lock only one thread can be in this critical
                     * section and so we don't need to worry about concurrent
                     * modification of minSolution
                     */ 
                    
                    if(puzzle.prevMoves.size() < minSolution){
                        isSolved.set(true);
                        minSolution = puzzle.prevMoves.size();
                        solved.set(puzzle);

                    }
                    
                } finally {
                    l.unlock();
                }
                

            } else if((puzzle.prevMoves.size()+1) < minSolution && alreadyChecked(puzzle)){
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

    private boolean alreadyChecked(Puzzle toCheck){
        int[][] puzz = toCheck.puzzle;
        String id = "";
        for(int i = 0; i < puzz.length; i++){
            for(int j = 0; j < puzz[0].length; j++){
                id += puzz[i][j];
            }
        }

        if(usedPuzzes.containsKey(id)){
            return true;
        } else {
            usedPuzzes.put(id, true);
            return false;
        }

    }


    // idea: we could make a Locks class where 
    // we have a couple diff locks implemented
    // and then we call Locks.PetersonLock() and
    // Locks.PetersonUnlock() but also have more
    // than just Peterson.
    // but this is just ambition, otherwise defining
    // methods here are fine and we can always move 
    // them later

    // another idea: let's use an already impelemented
    // java lock until everything else is working
    
    
    // lock method will go here
    public void lock(){

    }

    // unlock method will go here 
    public void unlock(){

    }

}