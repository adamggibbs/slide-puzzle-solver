
public class Main {
    public static void main(String[] args){

        Puzzle puzzle = new Puzzle(4,4);
        puzzle.shuffle(50);
        puzzle.print();
        puzzle = Solver.solve(puzzle);
        puzzle.print();
        Solver.printSolution(puzzle);
        
    }
}
