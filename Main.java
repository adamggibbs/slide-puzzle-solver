
public class Main {
    public static void main(String[] args){
        Puzzle puzzle = new Puzzle(6,6);
        puzzle.shuffle();
        puzzle.print();
        puzzle=Solver.solve(puzzle);
<<<<<<< HEAD
        puzzle.print();
        puzzle.printPrevMoves();
=======
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
>>>>>>> d557570d0f89b959450e8da7c92bf9b56c97b9ae
    }
}
