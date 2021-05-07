import java.util.*;

public class Solver {

    // public Puzzle Solve(Puzzle initialPuzz){
    //   Queue<Puzzle> q = new LinkedList<>();
    //   q.add(initialPuzz);
    //   while (!q.isEmpty()){
    //     Puzzle puzzle=q.remove();
    //     if (puzzle.isSolved()){
    //       return puzzle;
    //     }
    //     else{
    //       int last_move=puzzle.get(puzzle.prevMoves.size());
    //       if (last_move!=0 ){
    //         q.add();
    //       }
    //       if (last_move!=1){
    //         q.add(puzzle.move_up());
    //       }
    //         if (last_move!=2){
    //         q.add(puzzle.move_right());
    //       }
    //         if (last_move!=3){
    //         q.add(puzzle.move_left());
    //       }
    //     }
    //
    //
    //   }
    //
    // }

    public static Puzzle copy(Puzzle puzzle){
      Puzzle newPuzz=new Puzzle(puzzle.puzzle.length,puzzle.puzzle[0].length);
      for (int i=0;i<puzzle.puzzle.length;i++){
        for (int j=0;j<puzzle.puzzle[0].length;j++){
          newPuzz.puzzle[i][j]=puzzle.puzzle[i][j];
        }
      }
      return newPuzz;
    }
}
