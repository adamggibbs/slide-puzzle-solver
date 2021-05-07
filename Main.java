
public class Main {
    public static void main(String[] args){
        Puzzle puzzle = new Puzzle(10,10);
        puzzle=Solver.solve(puzzle);
        System.out.println(puzzle);
        puzzle.move_right();
        System.out.println(puzzle);
        //puzzle.prevMoves.clear();
        puzzle=Solver.solve(puzzle);
        System.out.println(puzzle);
        System.out.println(puzzle.prevMoves.get(0));
    }
}
