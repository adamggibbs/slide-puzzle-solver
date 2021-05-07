
public class Main {
    public static void main(String[] args){
        Puzzle puzzle = new Puzzle(10,10);
        System.out.println(puzzle);
        puzzle.move_right();
        puzzle.prevMoves.clear();
        puzzle.prevMoves.add(0);
        Solver.solve(puzzle);
    }
}
