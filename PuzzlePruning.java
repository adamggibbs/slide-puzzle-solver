public class PuzzlePruning {

    //pointer to the parent state that produced this state and the move it took to produce it
    PuzzlePruning parent;
    int preMove = -1;


    // puzzle itself
    int[][] puzzle;
    // store index of row and col of open space
    int open_r;
    int open_c;


    // Constructor for first puzzle
    public PuzzlePruning(int width, int height){
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

    //constructor for when a thread produces a puzzle from a parent
    public PuzzlePruning(PuzzlePruning parent, int preMove){
        puzzle = new int[parent.puzzle.length][parent.puzzle[0].length];
        open_c = parent.open_c;
        open_r = parent.open_r;
        this.parent = parent;
        this.preMove = preMove;

        for (int i=0; i < puzzle.length; i++){
            for (int j=0; j < puzzle[0].length; j++){
                puzzle[i][j]=parent.puzzle[i][j];
            }
        }
    }

    // Move Methods, refers to direction open space moves
    // return false if move isn't possible
    // performs move and returns true if possible
    //move_down
    public void move_down(){
        puzzle[open_r][open_c] = puzzle[open_r+1][open_c];
        puzzle[open_r+1][open_c] = 0;
        open_r += 1;
    }

    //move_up
    public void move_up(){
        puzzle[open_r][open_c] = puzzle[open_r-1][open_c];
        puzzle[open_r-1][open_c] = 0;
        open_r -= 1;
    }

    //move_right
    public void move_right(){
        puzzle[open_r][open_c] = puzzle[open_r][open_c+1];
        puzzle[open_r][open_c+1] = 0;
        open_c += 1;
    }

    //move_left
    public void move_left(){
        puzzle[open_r][open_c] = puzzle[open_r][open_c-1];
        puzzle[open_r][open_c-1] = 0;
        open_c -= 1;
    }


    // shuffle() shuffles the board
    public void shuffle(int shuffles){

        for(int i = 0; i < shuffles; i++){
            int move = (int)(Math.random()*4);

            if(move == 0){
                if(open_r != puzzle.length-1){
                    puzzle[open_r][open_c] = puzzle[open_r+1][open_c];
                    puzzle[open_r+1][open_c] = 0;
                    open_r += 1;
                }
            } else if(move == 1){
                if(open_r != 0){
                    puzzle[open_r][open_c] = puzzle[open_r-1][open_c];
                    puzzle[open_r-1][open_c] = 0;
                    open_r -= 1;
                }
            } else if(move == 2){
                if(open_c != puzzle[0].length-1){
                    puzzle[open_r][open_c] = puzzle[open_r][open_c+1];
                    puzzle[open_r][open_c+1] = 0;
                    open_c += 1;
                }
            } else if(move == 3){
                if(open_c != 0){
                    puzzle[open_r][open_c] = puzzle[open_r][open_c-1];
                    puzzle[open_r][open_c-1] = 0;
                    open_c -= 1;
                }
            }
        }

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
        System.out.println("calculating moves");

        String toPrint = "";
        int count = 0;

        // string constructor that starts with the solved puzzle and adds the previous move
        // then uses the parent pointer to back track and continually add each parents previous move at the beginning of the string

        PuzzlePruning backTrack = this;
        while(backTrack.preMove != -1){
            count++;
            String temp = toPrint;
            toPrint = getDir(backTrack.preMove) + ", ";
            toPrint += temp;
            backTrack = backTrack.parent;
        }
        System.out.println("It took " + count + " moves to solve the puzzle.");
        System.out.println("The moves were: ");
        System.out.println("[" + toPrint + "]");
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
