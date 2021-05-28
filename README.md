# Slide Puzzle Solver
## COSC 273: Parallel & Distributed Computing
## 28 May 2021
## Ben Wadsworth, Scott Romeyn, Adam Gibbs

### GitHub Repository: https://github.com/adamggibbs/slide-puzzle-solver#slide-puzzle-solver
### Project submission Folder: https://drive.google.com/drive/u/1/folders/1gOcAh22pcXFj694W3M7YJadTHfI7e2iu


## Description:
This document contains the write up of our slide puzzle solver project. This README contains the following sections: our task, our implementations for solving that task, a description of our use of multithreading to improve performance, a comparison of our different implementations, a description of each piece of our code in the repository, and how to compile and run our programs.
## Our Task:
This Project aims to create a slide puzzle game and a brute force solver for the optimal solution to any <b>n x m</b> slide puzzle. We use parallel programming to speed up the brute force solver to be able to solve larger puzzles and puzzles with larger shuffles within a reasonable amount of time. We also have solutions that seek to limit memory use as the brute force solver uses lots of memory which causes issues when running these programs on small machines. A possible extension to this project would be implementing an approximate solver that finds near-optimal solutions and seeing how that compares to brute force and how/if that solver benefits from parallelization.
## Our implementations:

We had several implementations of slide puzzle solvers that each perform the task of finding the optimal solution for any given shuffled slide puzzle of size <b>n x m</b>. Some implementations are run sequentially while others are designed to be run in parallel. Below are descriptions of each implementation and the following sections explain and compare the use of multithreading in more depth.

### Sequential Solver
This solver uses breadth first search to find the optimal solution. The initial shuffled puzzle is added to a queue and then while the queue is not empty, we pop a puzzle off the queue and process it. Processing a puzzle state involves checking if it is solved, in which case it is returned as the solution, but if it is not solved it finds all possible next states and adds them all to the queue. Possible next states are the states that can be obtained from the current state by making one valid move (non valid moves would be moving the piece off the board in the case it was on the edge). We also don't go back to the parent state, so if the previous move on that state was down we wouldn't then move back up because we know that wouldn't be a solution. This process continues until a solution is found and returned. It returns a solved puzzle which contains an arraylist of the moves needed to solve that puzzle.

### Parallel State Solver
This solver uses the same approach as the sequential solver except the processing of each state is considered a task. So every time a state is popped off the queue, it is made into a task and sent to a thread pool to be completed. To make this happen, we used a concurrent queue so that multiple threads can add their children to the queue without contention causing errors. We also have an AtomicReference variable that is set to be null until a thread finds a solution and updates the AtomicReference to contain the solution found. At that point the while loop popping tasks off the queue stops since a solution has been found and it returns that solution. This solution almost always returns the optimal solution and we actually haven't had a test return a suboptimal solution yet. The only chance a suboptimal solution could occur is if a task with a solution crashes before updates the AtomicReference or if there are two solution each one move apart and the suboptimal task beats the optimal task to updating AtomicReference.
### Parallel Tree
This solver uses a similar approach to the sequential solver but splits the task into multiple sequential tasks. This solution we devised after noticing that the sequential solver actually beats the parallel state solver for most tests and this is because the task of checking a puzzle state is so quick that the overhead from creating a pool task counters the speed up from checking <i>nThreads</i> puzzle states in parallel. This solution works by getting the first <b><i>s</i></b> number of states where s is in (<i>nThreads-3</i>, <i>nThreads</i>). We choose this number of states because the way the BFS tree is structured, each parent has 2 or 3 states and if we reach nThreads-2 states, then we cannot pop off a state and add all its children. Otherwise we'd have more initial states than threads and that would be slow. Then we take all those puzzle states and run sequential solve with each puzzle state as the initial puzzle. These calls to sequential solve are done in parallel and when one finds a solution it updates an AtomicReference to store the solved puzzle. At this point, the solver shuts down the pool and all other calls to sequential solve get terminated. This solution can also return a suboptimal solution if one thread reaches a solution at a greater depth faster than another thread finds the optimal solution. This solution can also return suboptimal solutions if a thread crashes and the optimal solution was in its part of the tree. However, there is also overlap on the trees since we don't prune to eliminate already checked states. So an optimal solution may appear in multiple threads' searches. We can guarantee that the solution returned is at worst the <i>nThread-th</i> best solution, so in the case of 4 threads, the returned solution is definitely at worst the 4th best.
### Sequential Solver with Pruning
This sequential solver is very similar to the one described above. It is also a brute force breadth first search, however instead of using a queue this solver uses an ArrayList and does not remove any states that are generate from the list. When new states are generated, before adding to the list, a method is used to check if a puzzle state with an identical two dimensional int array already exists in the list. A state is only added to the list if it isn't already in the list. Another change is the `PuzzlePruning` class does not use an ArrayList to store previous moves. Instead each PuzzlePruning object stores a pointer to its parent state and the move that was made to generate its arrangement from the parent state.

### Parallel with One Combiner
This solver uses a thread pool with one thread combining the puzzle states generated by the remaining threads. The data structure that is shared with the producer threads is a Concurrent HashMap, and the combiner reads from this HashMap and adds puzzle states to another Concurrent HashMap if they aren't already contained in the HashMap. One problem with this solution is the combiner thread will eventually fall behind the producer threads, and the main thread that gives the producer puzzle states to generate from will have to wait. This waiting essentially slows the solver down to the speed of one producer and one combiner.

### Parallel with Two Combiners
This solver uses the same producer threads as the parallel solution with one combiner, but attempts to solve the waiting problem described by having two combiners. However, having two combiners creates different problems including the need for more shared objects and the need to continually check if the thread pool has been shutdown.

## Performance Optimization via Multithreading
Our brute force sequential solver is really good at solving small puzzles with small shuffles, but once solutions became more complex, the BFS tree got so large that getting to depth 25 takes a very long time. To improve performance via multithreading we tried multiple methods for creating a parallel program for our BFS. The first method we employed sent each puzzle state to a thread pool to be processed. This method ended up being slower than the sequential solver for most puzzles and only became an improvement on more complex puzzles. This method struggled because the task of processing one puzzle state is very quick, so taking the time to create the task, send it to the pool, schedule it, and execute it ended up cancelling out the improvements from checking multiple puzzles in parallel. After this observation that the sequential search is actually pretty quick, we decided to split up the search tree into <b><i>nThreads</i></b> disjoint subtrees and run sequential search on each. This ended up giving us a runtime improvement of around <b><i>nThreads</i></b> times faster. This creates almost a hybrid of BFS and DFS since each sequential solver is running BFS but since each tree is smaller than the global one, we reach greater depths much quicker. This solution allowed us to solve larger puzzles and more complex puzzles as well. It should be noted that the two parallel solutions do not guarantee optimal solutions anymore. However, in every one of our trials the parallel solutions have returned the optimal solution to the puzzle. This is because as the depths gets greater in the trees, the number of puzzle states at each depth grows exponentially and the probability that one thread is checking a greater depth for a significant amount of time before other threads finish checking the shallower depths decreases. Our next solutions involved pruning. These solutions run significantly slower than the rest but they check far fewer puzzles. Theoretically, using pruning uses less memory, however the pruning solutions run significantly slower and testing the puzzle cases that cause memory errors on the earlier solutions would take many hours to run. Our non pruning solutions will get memory errors after a few minutes of runtime but the pruned solutions do not. In these solutions the two combiner solution is quicker than the one combiner which is quicker than the sequential solver, but all of them are slower than the non pruning solutions. This optimization becomes more important if you're using a laptop with small memory. When we run on remus and romulus the memory error is almost never an issue.

## Comparison of Solver Performance
We assess performance on two accounts: runtime and memory use. In terms of runtime the parallel tree solver is by far the fastest solver. The sequential and parallel solvers are the second and third fastest solver. The sequential solver beats the parallel solver for small puzzles and the parallel solver only starts to beat the sequential solver for much larger solutions. The pruning methods are then much slower than the non pruning methods but for their runtime performance, the two combiner solution is fastest, the one combiner solution is second, and the sequential method is the slowest. The pruning methods theoretically perform well when we consider memory use. The non pruning methods run out of memory since the queues get too large for complex solutions but the pruning methods don't have this issue for most puzzles. They did not have memory issues for any of the puzzles that we tested.

## Components of the Repository
A list of all java classes in the repository and a description of their contents and functionality. A brief explanation of how to use these classes is also included.
(To Ben and Scott: Write a description of your files and programs here. I say we do somewhat of a bulleted format but that's not 100% necessary)
#### Puzzle.java
  - Holds the 2D array with position of puzzle pieces
  - Methods for moving pieces on the board. All moves are relative to the open spot
    so "move up" means the open space moves up even tho this visually appears as the
    physical piece above the open space moving down into the open space.
  - Method to shuffle the board to begin solving the puzzle
  - Methods to print the board and the previous moves that have been done to the board

#### Solver.java
  - Contains methods involved with solving the puzzle
  - Has method that solves the puzzle with a brute force method
  - Has method that solves the puzzle with a brute force method in parallel with small parallel tasks
  - Has method that solves the puzzle with a brute force method by dividing the BFS tree into multiple smaller trees to be run in parallel
  - Has method that prints the moves to the optimal solution of the puzzle after being solved
  - All methods are static so they can be called without an instance of the Solver class via Solver.<i>solve-method()</i>

#### ProcessStateTask.java
  - Runnable task for parallel solver
  - Used in Solver.parallelSolve() as its parallel task
  - Gets a puzzle and checks if it's solved, then adds its children to the queue of states to be checked

#### ParallelSequentialTask.java
  - Runnable task for parallel tree solver
  - Used in Solver.parallelTreeSolve as its parallel task
  - Takes in a puzzle and runs sequential solve with the given puzzle as the initial puzzle
  - Either sets found solution as the global solved puzzle or thread is interrupted and it does nothing

#### Main.java
  - Contains only a main method to test out the functionality of
    the sequential solver, the parallel solver, the parallel tree solver,
    and the pruned tree solver
  - Prints solution to a shuffled puzzle along with information about
    speed of each solver


#### PuzzlePruning.java
  - Holds the 2D array with position of puzzle pieces
  - Holds a pointer to the parent state to this puzzle state and the move it took to produce this state from the parent state
  - Methods for moving pieces on the board identical to the Puzzle.java class for the sequential solution without pruning.
  - Method to shuffle the board to begin solving the puzzle
  - Methods to print the board and the previous moves that have been done to the board

#### SolverPruning.java
  - Contains methods involved with solving the puzzle
  - Has method that solves the puzzle with a brute force method with pruning
  - Has method that solves the puzzle using a thread pool with one combiner and multiple producers
  - Has method that solves the puzzle with a thread pool with two combiners and multiple producers
  - Has method that prints the moves to the optimal solution of the puzzle after being solved

#### NextStateTaskPruning.java
  - Producer task for the parallel solver with pruning
  - Used in SolverPruning.parallelSolve() and SolverPruning.parallelSolve_V2() as its parallel task
  - Gets a puzzle and produces the next states. If a child state is solved it updates a shared boolean to tell the main thread a solution has been found. Regardless of whether a state is a solution or not this task adds all its children to the Concurrent HashMap that will be checked by the combiner task(s).

#### CombinerTask.java
  - This task goes through each threadID in order in the concurrent HashMap `map`, checks to see if any of the puzzle states produced are solved.
  - If a state is solved it updates the AtomicReference to reference the solved puzzle and end the program.
  - If a state isn't solved and isn't in the concurrent HashMap `bigList` it adds the state.

#### CombinerTaskParallelV2.java
  - This task is very similar to the `CombinerTask`, however it as the added AtomicInteger `oddOReven` so one combiner task will check even threads and one will check odd.
  - The even combiner will send even puzzle states and the odd combiner odd puzzle states vie the concurrent HashMap `biglist` again.

#### MainPruning.java
  - Contains the main method to test the same puzzle solve for each type of solution: sequential with pruning, parallel with one combiner, and parallel with two combiners.

#### 3x3InitialTester.sh
  - A script which runs the baseline `Main` on 3x3 puzzles multiple times
    with different shuffles

#### 4x4InitialTester.sh
  - A script which runs the baseline `Main` on 4x4 puzzles multiple times
    with different shuffles

#### PruningTester.sh
  - Shell script that runs trials for 3x3 and 4x4 puzzles with 30 and 40 shuffles and prints results to `outputPruningTest.txt`.

#### Output Files for Non-Pruning Solutions
    - Output for non-pruning solutions include sequential, parallel, and parallel tree solutions
    - Note: future outputs will contain results for pruned tree solution
    - 3x3Output files include shuffles of 100 and 1000
    - 4x4Output files include shuffles of 30 and 40

#### Output Files for Pruning
    - Tests run for pruning files comparing all methods including some comparisons to the brute force sequential solver with out pruning.


## How to compile and run on the command line:
#### To Compile:
  - <i>$</i> javac *.java
#### To run:
  - To use default puzzle dimensions (4x4):

    <i>$</i> java Main
    *or for pruning solutions:*
    <i>$</i> java MainPruning

  - To use user defined puzzle dimensions and default 40 shuffles:

    <i>$</i> java Main <i>n m</i>

    where <b>m, n</b> are integers specifying puzzle dimensions

  - To use user defined puzzle dimensions and number of shuffles:

    <i>$</i> java Main <i>n m s</i>
    *or for pruning solutions:*
    <i>$</i> java MainPruning <i>n m s</i>

    where <i>n, m</i> are integers specifying puzzle dimensions and <i>s</i> is the number of shuffles you would like to perform

  - To run tester for MainPruning.java (compile and then):

    <i>$</i> bash PruningTester.sh

  - To run 3x3 tester for Main.java (compile and then):

    <i>$</i> bash 3x3InitialTester.sh

  - To run 4x4 tester for Main.java (compile and then):

    <i>$</i> bash 4x4InitialTester.sh
