
public class Main {
    public static void main(String[] args){
        Puzzle puzzle = new Puzzle(10,10);
        puzzle.shuffle();
        System.out.println(puzzle);
        puzzle=Solver.solve(puzzle);
        System.out.println(puzzle);
        System.out.println("Moves to Solve the Puzzle:");
        for (int i=1;i<puzzle.prevMoves.size();i++){
            if (puzzle.prevMoves.get(i)==0){
              System.out.print("Down");
            }
            if (puzzle.prevMoves.get(i)==1){
              System.out.print("Up");
            }
            if (puzzle.prevMoves.get(i)==2){
              System.out.print("Right");
            }
            if (puzzle.prevMoves.get(i)==3){
              System.out.print("Left");
            }
            if (i<puzzle.prevMoves.size()-1){
              System.out.print(", ");
            }
        }
    }
}
