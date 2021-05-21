java.util.concurrent.atomic;

public class ProcessStateTask implements Runnable{

    Puzzle puzzle;
    Queue<Puzzle> sharedQueue;
    AtomicInteger minSolution;
    AtmoicReference<Puzzle> solved;

    public procssStateTask(Puzzle puzzle, Queue<Puzzle> sharedQueue, AtomicInteger minSolution, AtmoicRefernce<Puzzle> solved){
        this.puzzle = puzzle;
        this.sharedQueue = sharedQueue;
        this.minSolution = minSolution;
        this.solved = solved;
    }

    public void run(){
            if (puzzle.isSolved()){

                int curr_min = minSolution.get();
                if(puzzle.prevMoves.size() < curr_min){
                    // need to create a lock method for getting and changing the shortest solution
                    minSolution.compareAndSet(curr_min, puzzle.prevMoves.size());
                    solved.set(Puzzle);
                }

            } else if(minSolution.get() == -1 || puzzle.prevMoves.size()+1 < minSolution){
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

    // lock method will go here

}