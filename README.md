# Slide Puzzle Solver
## COSC 273: Parallel & Distributed Computing
## 19 May 2021
## Ben Wadsworth, Scott Romeyn, Adam Gibbs
### Project submission Folder: https://drive.google.com/drive/u/1/folders/1gOcAh22pcXFj694W3M7YJadTHfI7e2iu


### Description:
This Project aims to create a slide puzzle game and a brute force solver for the optimal
solution to any n x m slide puzzle. We will utilize parallel programming to speed up
the brute force solver to be able to solve larger puzzles and puzzles with larger shuffles
within a reasonable amount of time. Possible extensions to this project would be 
implementing a approximate solver that finds near-optimal solutions and seeing
how that compares to brute force and how/if that solver benefits from parallization. Also,
adding visualizations such as watching the slide puzzle get solved optimally after the 
Solver finds the optimal solution or a program that converts am image into a n x m puzzle
and unscrambles it after finding the solution. 

We will be testing the performance of the multithreaded version with a concurrent queue by 
running the solver on many different n x m puzzles with many runs of each and tracking the 
runtime of each solution. We will also compare the solution of the sequential brute force 
and multithreaded brute force to ensure that the optimal solution is being found (or at 
least one isn't finding shorter paths than the other one). The use of bredth vs search in
the sequential algorithm ensures an optimal solution is found. We will need to reason through
how to ensure the optimal solution is found with multithreading because there is a chance one
thread finds a non optimal solution before the thread about to find the optimal solution finds
the optimal solution. If we end up making an approximation algorithm we will perform the same 
analysis of sequential approximation vs multithreaded approximation but also analyze the 
runtime/memory use vs optimality of the solution. 


### Currently implemented functionality:
1. Puzzle class with methods for moving pieces, printing, and shuffling
2. A sequential brute-force solver that can solve small puzzles and small shuffles
   a. "small puzzles" means n and m less than 10 or so
   b. "small shuffles" means less than 30 or so moves to shuffle
   c. smaller puzzles, like 3x3, can tolerate larger shuffles up to 1000 but even the 
      4x4 has to have 50 or fewer shuffles


### This repository contains:
#### Puzzle.java
  - Holds the 2D array with position of puzzle pieces
  - Methods for moving pieces on the board. All moves are realtive to the open spot
    so "move up" means the open space moves up even tho this visually appears as the 
    physical piece above the open space moving down into the open space.
  - Method to shuffle the board to begin solving the puzzle
  - Methods to print the board and the previous moves that have been done to the board
  
  
#### Solver.java
  - Contains methods involved with solving the puzzle
  - Has method that solves the puzzle with a brute force method
  - Has method that prints the moves to the optimal solution of 
    the puzzle after being solved
    
#### Main.java
  - Contains only a main method to test out the functionality of 
    Puzzle and Solver
    
    
### How to run code to solve a puzzle:
1. Create a puzzle by creating a Puzzle object that takes a width and a height
   into the constructor
2. Shuffle the puzzle with Puzzle.shuffle(). This takes in no parameters
3. Solve the puzzle with Puzzle = Solver.sovle(Puzzle). Solver.solve returns
   the solved puzzle which contains all the moves of the optimal solution.
4. Use Solver.printSolution(Puzzle) to see the moves of the optimal solution.

### How to compile and run on the command line:
  -To Compile: use javac Main.java
  -To run: 
    -to use default puzzle dimensions (4x4): 'java Main'
    - to use user defined puzzle dimensions: 'java Main n m' 
      where n and m are integers specifying puzzle dimensions 

**Note you can use Puzzle.print ( or System.out.println(Puzzle) ) at any time
  to print the current state of the puzzle
**Also note the Main.java file should show how to use most of the methods.
