
public class Main {
    public static void main(String[] args){
        Puzzle puzzle = new Puzzle(5,5);
        puzzle.shuffle();
        puzzle.print();
        puzzle = Solver.solve(puzzle);
        puzzle.print();
        Solver.printSolution(puzzle);
        
    }
}
