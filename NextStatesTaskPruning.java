import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class NextStatesTaskPruning implements Runnable{

    // thread id
    int id;
    // shared map of all the puzzle states produced by the threads that is sent to the combiner
    ConcurrentHashMap<Integer,HashSet<PuzzlePruning>> map;
    // the state 'popped' from the 'queue' of states that have been generated and the state this thread will produce states from
    PuzzlePruning state;
    // this boolean is used to stop starting threads in the main thread that is assigning threads puzzle state to generate next states from
    AtomicBoolean solutionFound;

    public NextStatesTaskPruning(AtomicBoolean solutionFound, int id, PuzzlePruning state, ConcurrentHashMap<Integer, HashSet<PuzzlePruning>> map) {
        this.id = id;
        this.state = state;
        this.map = map;
        this.solutionFound = solutionFound;
    }

    public void run() {
        HashSet<PuzzlePruning> set = new HashSet<>();
        int last_move = state.preMove;
        /* each of these if statements produces the puzzle state only if it isn't the reverse of the previous move
         *  and if it is a solution, set the atomic boolean to true to stop creating tasks
         * */
        if (last_move!=1){
            if(state.open_r != state.puzzle.length-1){
                PuzzlePruning toAdd = new PuzzlePruning(state, 0);
                toAdd.move_down();
                if(toAdd.isSolved()){
                    solutionFound.set(true);
                }
                set.add(toAdd);
            }
        }
        if (last_move!=0){
            if(state.open_r != 0){
                PuzzlePruning toAdd = new PuzzlePruning(state, 1);
                toAdd.move_up();
                if(toAdd.isSolved()){
                    solutionFound.set(true);
                }
                set.add(toAdd);
            }
        }
        if (last_move!=3){
            if(state.open_c != state.puzzle[0].length-1){
                PuzzlePruning toAdd = new PuzzlePruning(state, 2);
                toAdd.move_right();
                if(toAdd.isSolved()){
                    solutionFound.set(true);
                }
                set.add(toAdd);
            }
        }
        if (last_move!=2){
            if(state.open_c != 0){
                PuzzlePruning toAdd = new PuzzlePruning(state, 3);
                toAdd.move_left();
                if(toAdd.isSolved()){
                    solutionFound.set(true);
                }
                set.add(toAdd);
            }
        }
        map.put(id,set);
    }
}
