import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CombinerTaskParallelV2 implements Runnable{

    // the combiner sends puzzle state to bigList if they aren't already in it
    ConcurrentHashMap<Integer, PuzzlePruning> bigList;
    // map is all the states each thread sends to the combiner
    ConcurrentHashMap<Integer, HashSet<PuzzlePruning>> map;
    // if a combiner finds a solution this reference is set to that solution
    AtomicReference<PuzzlePruning> solution;
    // sets whether the combiner deals with odd or even thread IDs and puzzle IDs
    AtomicInteger oddOReven;

    public CombinerTaskParallelV2(ConcurrentHashMap<Integer, PuzzlePruning> bigList,
                                  ConcurrentHashMap<Integer, HashSet<PuzzlePruning>> map, AtomicReference<PuzzlePruning> solution, AtomicInteger oddOReven) {
        this.bigList = bigList;
        this.map = map;
        this.solution = solution;
        this.oddOReven = oddOReven;
    }

    public void run() {
        // puzzleID is used in the bigList of all the puzzle states generated so the threads get the puzzle states in order
        int puzzleID = 1 + oddOReven.get();
        int threadID = oddOReven.get();
        // threadID tells the combiner which thread to find
        // when the combiner finds a solution it will exit the while loop with this boolean
        boolean notDone = true;
        while(notDone && !Thread.currentThread().isInterrupted()){
            // if the map of states produced by the threads doesn't have the next thread  wait until it does
            // this way all the states are checked in order
            // also just in case the thread is interrupted this will take you out of the waiting loop
            // this is okay because if the threadID isn't contained that means it is a later solution than the one that has already beenfound
            while(!map.containsKey(threadID) && !Thread.currentThread().isInterrupted()) {
                //wait
            }
            // if the thread has been interrupted break becuase the threadID might not be contained so it will through
            // a null pointer error
            if(Thread.currentThread().isInterrupted()) break;

            //set of puzzle states that came from one thread
            // same as single combiner: technically speaking after the combiner reads from the map that threadID can be deleted
            // but because it is a concurrent Hashmap, its possible that not deleting things is faster
            // but that means the puzzle states aren't being pruned and they are staying in the Hashmap, so removing them would use less memory
            HashSet<PuzzlePruning> toAdd = map.get(threadID);

            for(PuzzlePruning p : toAdd){
                if(p.isSolved()){
                    // if we have a solution
                    solution.set(p);
                    notDone = false;
                    oddOReven.set(threadID);
                    break;
                }
                // add puzzle states to the 'queue' if they aren't in it already
                if(notInQueue(p)){
                    bigList.put(puzzleID,p);
   // the puzzleIDs and threadIDs increment by two so odd and even don't add or check the same threads or puzzles
                    puzzleID+=2;
                }
            }
            threadID+=2;
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

