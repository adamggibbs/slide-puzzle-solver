import java.util.*;

public class Puzzle{

    // puzzle itself
    int[][] puzzle;
    // store index of row and col of open space
    int open_r;
    int open_c;

    //stores prvious moves to get to current state
    ArrayList<Integer> prevMoves = new ArrayList<>();

    // Constructor
    public Puzzle(int width, int height){
        puzzle = new int[height][width];
        open_c=0;
        open_r=0;

        int count = 0;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                puzzle[i][j] = count;
                count++;
            }
        }
    }

    // Move Methods, refers to direction open space moves
    // return false if move isn't possible
    // performs move and returns true if possible
    //move_down
    public boolean move_down(){

        if(open_r == puzzle.length-1){
            return false;
        } else {
            puzzle[open_r][open_c] = puzzle[open_r+1][open_c];
            puzzle[open_r+1][open_c] = 0;
            open_r += 1;
            prevMoves.add(0);
            return true;
        }
    }

    //move_up
    public boolean move_up(){

        if(open_r == 0){
            return false;
        } else {
            puzzle[open_r][open_c] = puzzle[open_r-1][open_c];
            puzzle[open_r-1][open_c] = 0;
            open_r -= 1;
            prevMoves.add(1);
            return true;
        }
    }

    //move_right
    public boolean move_right(){

        if(open_c == puzzle[0].length-1){
            return false;
        } else {
            puzzle[open_r][open_c] = puzzle[open_r][open_c+1];
            puzzle[open_r][open_c+1] = 0;
            open_c += 1;
            prevMoves.add(2);
            return true;
        }
    }

    //move_left
    public boolean move_left(){

        if(open_c == 0){
            return false;
        } else {
            puzzle[open_r][open_c] = puzzle[open_r][open_c-1];
            puzzle[open_r][open_c-1] = 0;
            open_c -= 1;
            prevMoves.add(3);
            return true;
        }
    }

    // shuffle()
    public void shuffle(){

        // default number of shuffles
        int shuffles = 50;

        // for each shuffle, randomly pick a move
        // and perform it
        for(int i = 0; i < shuffles; i++){
            int move = (int)(Math.random()*4);

            if(move == 0){
                this.move_down();
            } else if(move == 1){
                this.move_up();
            } else if(move == 2){
                this.move_right();
            } else if(move == 3){
                this.move_left();
            }
        }

        // make sure prevMoves arraylist is clean
        prevMoves=new ArrayList<>();
        // add -1 as first move, denotes a newly shuffled puzzle
        prevMoves.add(-1);
    }

    // shuffle() overload to allow shuffles to be given
    // works just as other shuffle method
    public void shuffle(int shuffles){

        for(int i = 0; i < shuffles; i++){
            int move = (int)(Math.random()*4);

            if(move == 0){
                this.move_down();
            } else if(move == 1){
                this.move_up();
            } else if(move == 2){
                this.move_right();
            } else if(move == 3){
                this.move_left();
            }
        }

        prevMoves=new ArrayList<>();
        prevMoves.add(-1);
    }

    // isSolved()
    // loop through array to see if puzzle is solved
    // if previous element is ever greater than current
    // element then puzzle is not solved
    public boolean isSolved(){

        // make first check -1 since first piece should be 0
        int last_piece = -1;
        // loop through puzzle to check each piece
        for(int i = 0; i < puzzle.length; i++){
            for(int j = 0; j < puzzle[0].length; j++){
                if(puzzle[i][j] <= last_piece){
                    // return false immediately if a current piece
                    // is less than previous piece
                    return false;
                } else {
                    last_piece = puzzle[i][j];
                }
            }
        }

        // if all pieces were in increasing order, return true
        return true;

    }

    // print()
    // create quick print method for puzzle
    public void print(){
        System.out.println(this);
    }

    // printPrevMoves()
    // prints previous moves taken on puzzle
    public void printPrevMoves(){

        // if there are no previous moves, say that
        if(prevMoves.size() == 0){
            System.out.println("There are no previous moves.");
        }

        // skip first move since it is set to be -1 by shuffle
        // print open bracket with first move
        System.out.print("[" + getDir(this.prevMoves.get(1)) + ", ");
        // print 20 moves per line
        for(int i = 2; i < this.prevMoves.size()-1; i++){
            if(i % 20 == 0){
                String dir = getDir(this.prevMoves.get(i));
                System.out.println(dir + ",");
            } else {
                String dir = getDir(this.prevMoves.get(i));
                System.out.print(dir + ", ");
            }
        }
        // print last move with closed bracket 
        System.out.println(getDir(this.prevMoves.get(prevMoves.size()-1)) + "]");
    }

    // getDir()
    // private method that converts ints 0-3 
    // to their corresponding move direction
    // to print
    private String getDir(int move){

        String dir = "";
        if(move == 0){
            dir = "down";
        } else if(move == 1){
            dir = "up";
        } else if(move == 2){
            dir = "right";
        } else if(move == 3){
            dir = "left";
        }
        return dir;

    }

    // toString() method to print puzzle
    // currently works on up to 10x10
    public String toString(){

        String toReturn = "";

        for(int i = 0; i < puzzle.length; i++){
            for(int j = 0; j < puzzle[0].length; j++){
                toReturn += puzzle[i][j];
                if(puzzle[i][j] < 10){
                    toReturn += "  ";
                } else {
                    toReturn += " ";
                }
            }
            toReturn += "\n";
        }

        return toReturn;

    }

}
