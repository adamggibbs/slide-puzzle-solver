import java.util.*;

public class Solver {

    //Uses a BFS of all possible puzzle states to find
    //the shortest possible moves to reach a solved puzzle
    public static Puzzle solve(Puzzle initialPuzz){

      Queue<Puzzle> q = new LinkedList<>();

      q.add(initialPuzz);
      int counter=0;
      while (!q.isEmpty()){

        Puzzle puzzle=q.remove();
        if (counter<10)
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

    public static Puzzle parallelSolve(Puzzle initialPuzz){
      int nThreads = Runtime.getRuntime().availableProcessors();
      ExecutorService pool = Executors.newFixedThreadPool(nThreads);

      Atomic Integer minSolution = Integer.MAX_VALUE;
      Atmoic boolean solved = false;

      // need to finish code here to create and execute tasks



    }

    //Prints the solved Puzzle and the moves using
    //the list of moves stored in each puzzle state
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

    //Creates a deep copy of the current puzzle to be
    //added to the queue for the BFS
    public static Puzzle copy(Puzzle puzzle){
      Puzzle newPuzz=new Puzzle(puzzle.puzzle[0].length, puzzle.puzzle.length);
      newPuzz.open_c=puzzle.open_c;
      newPuzz.open_r=puzzle.open_r;

      for (int i=0;i<puzzle.prevMoves.size();i++){
        newPuzz.prevMoves.add(puzzle.prevMoves.get(i));
      }
      for (int i=0;i<puzzle.puzzle.length;i++){
        for (int j=0;j<puzzle.puzzle[0].length;j++){
          newPuzz.puzzle[i][j]=puzzle.puzzle[i][j];
        }
      }
      return newPuzz;
    }
}
