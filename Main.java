
public class Main {
    public static void main(String[] args){
        Puzzle puzzle = new Puzzle(6,6);
        puzzle.shuffle();
        puzzle.print();
        puzzle=Solver.solve(puzzle);
        puzzle.print();
        puzzle.printPrevMoves();
    }
}
