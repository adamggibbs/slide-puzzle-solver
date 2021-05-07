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

    // constructor 2
    public Puzzle(Puzzle initialPuzz){
      puzzle = initialPuzz.puzzle;

    }

    // Move Methods, refers to direction open space moves
    // return false if move isn't possible
    // performs move and returns true if possible
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
        int shuffles = 30;

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

        // for(int i = 0; i < 9; i++){
        //     this.move_right();
        //     this.move_down();
        // }

        prevMoves=new ArrayList<>();
        prevMoves.add(-1);
    }

    // shuffle() overload to allow shuffles to be given
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

        // for(int i = 0; i < 9; i++){
        //     this.move_right();
        //     this.move_down();
        // }

        prevMoves=new ArrayList<>();
        prevMoves.add(-1);
    }

    // isSolved()

    public boolean isSolved(){

        int last_piece = -1;
        for(int i = 0; i < puzzle.length; i++){
            for(int j = 0; j < puzzle[0].length; j++){
                if(puzzle[i][j] <= last_piece){
                    return false;
                } else {
                    last_piece = puzzle[i][j];
                }
            }
        }

        return true;

    }

    // print()

    public void print(){
        System.out.println(this);
    }

    // printPrevMoves()

    public void printPrevMoves(){

        if(prevMoves.size() == 0){
            System.out.println("There are no previous moves.");
        }

        System.out.print("[" + getDir(this.prevMoves.get(1)) + ", ");
        for(int i = 2; i < this.prevMoves.size()-1; i++){
            if(i % 20 == 0){
                String dir = getDir(this.prevMoves.get(i));
                System.out.println(dir + ",");
            } else {
                String dir = getDir(this.prevMoves.get(i));
                System.out.print(dir + ", ");
            }
        }
        System.out.println(getDir(this.prevMoves.get(prevMoves.size()-1)) + "]");
    }

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
