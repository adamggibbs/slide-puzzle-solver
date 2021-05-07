
public class Main {
    public static void main(String[] args){
        Puzzle puzzle = new Puzzle(10,10);
        puzzle.shuffle();
        System.out.println(puzzle);
        puzzle=Solver.solve(puzzle);
        System.out.println(puzzle);
    }
}
