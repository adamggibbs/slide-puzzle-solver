
public class Main {
    public static void main(String[] args){
        Puzzle puzzle = new Puzzle(10,10);
        puzzle.shuffle();
        Puzzle puzzle2=Solver.copy(puzzle);
        System.out.println(puzzle);
        System.out.println(puzzle2);

    }
}
