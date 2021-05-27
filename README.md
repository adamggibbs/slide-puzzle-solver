# Slide Puzzle Solver
## COSC 273: Parallel & Distributed Computing
## 19 May 2021
## Ben Wadsworth, Scott Romeyn, Adam Gibbs
### Project submission Folder: https://drive.google.com/drive/u/1/folders/1gOcAh22pcXFj694W3M7YJadTHfI7e2iu


## Description:
This document contains the write up of our slide puzzle solver project. This README contains the following sections: our task, our implementations for solving that task, a description of our use of multithreading to improve performance, a comaprison of our different implementations, a description of each piece of our code in the repository, and how to compile and run our programs.
## Our Task:
This Project aims to create a slide puzzle game and a brute force solver for the optimal
solution to any <b>n x m</b> slide puzzle. We will utilize parallel programming to speed up
the brute force solver to be able to solve larger puzzles and puzzles with larger shuffles
within a reasonable amount of time. Possible extensions to this project would be 
implementing a approximate solver that finds near-optimal solutions and seeing
how that compares to brute force and how/if that solver benefits from parallization. Also,
adding visualizations such as watching the slide puzzle get solved optimally after the 
Solver finds the optimal solution or a program that converts am image into a <b>n x m</b> puzzle
and unscrambles it after finding the solution. 
## Our implementations:

We had several implementations of slide puzzle sovlers that each perform the task of finding the optimal solution for any given shuffled slide puzzle of size <b>n x m</b>. Some implementations are run sequentially while others are designed to be run in parallel. Below are descriptions of each implementation and the following sections explain and compare the use of multithreading in more depth. 

### Sequential Solver (Adam will do this)

### Parallel State Solver (Adam will do this)

### Parallel Tree (Adam will do this)

### Scott Sequential (Change this name)

### Scott Combiner 1 (Change this name too)

### Scott Combiner 2 (CHange this name two too)

## Performance Optimization via Multithreading (Adam will do this)

## Comparison of Solver Performance (Adam will do this)



## Componenets of the Repository (Add other files I don't have listed with explainations)
A list of all java classes in the repository and a description of their contents and functionality. A brief explaination of how to use these classes is also included.
(To Ben and Scott: Write a description of your files and programs here. I say we do somewhat of a bulleted format but that's not 100% necessary)
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

#### ProcessStateTask.java
  - Runnable task for parallel solver

#### ParallelSequentialTask.java
  - Runnable task for parallel tree solver
#### Main.java (Ben will do this)
  - Contains only a main method to test out the functionality of 
    Puzzle and Solver
    
    
## How to compile and run on the command line: (Ben will do this)
  -To Compile: use javac Main.java
  -To run: 
    -to use default puzzle dimensions (4x4): 'java Main'
    - to use user defined puzzle dimensions: 'java Main n m' 
      where n and m are integers specifying puzzle dimensions 


