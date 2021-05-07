
public class Main {
    public static void main(String[] args){
        Puzzle puzzle = new Puzzle(10,10);
        System.out.println(puzzle);
        System.out.println(puzzle.isSolved() + "\n");
        puzzle.shuffle();
        System.out.println(puzzle);
        System.out.println(puzzle.isSolved() + "\n");
        
    }
}
