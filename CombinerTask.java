import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class CombinerTask implements Runnable{

    // the combiner sends puzzle state to bigList if they aren't already in it
    ConcurrentHashMap<Integer, PuzzlePruning> bigList;
    // map is all the states each thread sends to the combiner
    ConcurrentHashMap<Integer, HashSet<PuzzlePruning>> map;
    // if a combiner finds a solution this reference is set to that solution
    AtomicReference<PuzzlePruning> solution;

    public CombinerTask(ConcurrentHashMap<Integer, PuzzlePruning> bigList, ConcurrentHashMap<Integer, HashSet<PuzzlePruning>> map, AtomicReference<PuzzlePruning> solution) {
        this.bigList = bigList;
        this.map = map;
        this.solution = solution;
    }

    public void run() {
        // puzzleID is used in the bigList of all the puzzle states generated so the threads get the puzzle states in order
        int puzzleID = 1;
        // threadID tells the combiner which thread to find
        int threadID = 0;
        // when the combiner finds a solution it will exit the while loop with this boolean
        boolean notDone = true;
        while(notDone){
            //System.out.println("\t\t\t\t\t\t\t\t\t\t waiting for thread " + threadID);
            // if the map of states produced by the threads doesn't have the next thread  wait until it does
            // this way all the states are checked in order
            while(!map.containsKey(threadID)) {
                //wait
            }
            //System.out.println("\t\t\t\t\t\t\t\t\t\t\t   reading from " + threadID);
            //set of puzzle states that came from one thread
            // technically speaking after the combiner reads from the map that threadID can be deleted
            // but because it is a concurrent Hashmap, its possible that not deleting things is faster
            // but that means the puzzle states aren't being pruned and they are staying in the Hashmap, so removing them would use less memory
            HashSet<PuzzlePruning> toAdd = map.get(threadID);

            for(PuzzlePruning p : toAdd){
                if(p.isSolved()){
                    //System.out.println("||||||||||||||||||| SOLUTION FOUND |||||||||||||||||||");
                    // if we have a solution
                    solution.set(p);
                    notDone = false;
                    break;
                }
                // add puzzle states to the 'queue' if they aren't in it already
                if(notInQueue(p)){
                    bigList.put(puzzleID,p);
                    puzzleID++;
                }
            }
            threadID++;
        }
    }

    // similar method to the single thread version -> iterates over all the puzzle states in the map
    // if the puzzle state is in the map already don't add it
    public boolean notInQueue(PuzzlePruning check){
        for (PuzzlePruning p : bigList.values()) {
            if(Arrays.deepEquals(p.puzzle, check.puzzle)){
                return false;
            }
        }
        return true;
    }

}

