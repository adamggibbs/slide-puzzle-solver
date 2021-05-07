
public class Main {
    public static void main(String[] args){
        
	// create a puzzle
        Puzzle puzzle = new Puzzle(5,5);

	// shuffle the puzzle
        puzzle.shuffle();
        puzzle.print();

	// solve the puzzle
        puzzle = Solver.solve(puzzle);
        puzzle.print();
        Solver.printSolution(puzzle);

	System.out.println();
        
    }
}
