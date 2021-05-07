import java.util.*;

public class Solver {

    public static Puzzle solve(Puzzle initialPuzz){
      Queue<Puzzle> q = new LinkedList<>();
      q.add(initialPuzz);
      System.out.println(initialPuzz);
      while (!q.isEmpty()){
        Puzzle puzzle=q.remove();
        if (puzzle.isSolved()){
          return puzzle;
        }
        else{
          //System.out.println(puzzle.prevMoves.size());
          int last_move=puzzle.prevMoves.get(puzzle.prevMoves.size()-1);
          if (last_move!=1){
            Puzzle toAdd = Solver.copy(puzzle);
            if (toAdd.move_down()){
                q.add(toAdd);
            }
          }
          if (last_move!=0){
            Puzzle toAdd = Solver.copy(puzzle);
            if (toAdd.move_up()){
                q.add(toAdd);
            }

          }
          if (last_move!=3){
            Puzzle toAdd = Solver.copy(puzzle);
            if (toAdd.move_right()){
                q.add(toAdd);
            }
          }
          if (last_move!=2){
            Puzzle toAdd = Solver.copy(puzzle);
            if (toAdd.move_left()){
              System.out.println("Test");
                q.add(toAdd);
            }
          }
        }


      }
      return initialPuzz;
    }

    public static Puzzle copy(Puzzle puzzle){
      Puzzle newPuzz=new Puzzle(puzzle.puzzle.length,puzzle.puzzle[0].length);
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
