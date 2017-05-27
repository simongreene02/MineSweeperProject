import java.util.Optional;
import java.util.Random;

/**
 * This class represents the model for a game of MineSweeper. It has a
 * constructor that takes a preset boolean 2D array where true means there is a
 * mine. This first constructor (you'll need 2) is for testing the methods of
 * this class.
 * 
 * The second constructor that takes the number of rows, the number of columns,
 * and the number of mines to be set randomly in that sized mine field. Do this
 * last.
 * 
 * @author YOUR NAME
 */
public class MineSweeper implements MineSweeperModel {

  private class GameSquare {

    private boolean isMine;
    private int row;
    private int col;
    private boolean isVisible;
    private boolean isFlagged;
    private int mineNeighbors;

    // Construct a GameSquare object with all values initialized except
    // mineNeighbors, which is an instance variables that can only be set after
    // all
    // GameSquare objects have been constructed in the 2D array.
    public GameSquare(boolean isMine, int row, int col) {
      this.isMine = isMine;
      this.row = row;
      this.col = col;
      isVisible = false; // Default until someone starts clicking
      isFlagged = false; // Default until someone starts clicking
      // call setAdjacentMines() from both constructors
      // to set this for each new GameSquare.
      mineNeighbors = 0;
    }
  }

  // The instance variable represents all GameSquare objects where each knows its row,
  // column, number of mines around it, if it is a mine, flagged, or visible
  private GameSquare[][] board;

  /**
   * Construct a MineSweeper object using a given mine field represented by an
   * array of boolean values: true means there is mine, false means there is not
   * a mine at that location.
   * 
   * @param mines
   *          A 2D array to represent a mine field so all methods can be tested
   *          with no random placements.
   */
  public MineSweeper(boolean[][] mines) {
    board = new GameSquare[mines.length][mines[0].length];
    
    for (int i = 0; i < mines.length; i++) {
    	for (int j = 0; j < mines[0].length; j++) {
    		board[i][j] = new GameSquare(mines[i][j], i, j);
    		if (board[i][j].isMine) {
    			System.out.print("+");
    		} else {
    			System.out.print("-");
    		}
    	}
    	System.out.println();
    }

    // Example construction of one GameSquare stored in row 2, column 4:
    // /// board[2][4] = new GameSquare(mines[2][4], 2, 4);
    // Use a nested for loop to change all board array elements
    // from null to a new GameSquare

    // You will need to call private void setAdjacentMines() to set
    // mineNeighbors for all GameSquare objects because each GameSquare object
    // must first know if it is a mine or not. Set mineNeighbors for each.
    setAdjacentMines();
  }

  /**
   * Use the almost initialized 2D array of GameSquare objects to set the
   * instance variable mineNeighbors for every 2D array element (even if that
   * one GameSquare has a mine). This is similar to GameOfLife neighborCount.
   */
  private void setAdjacentMines() {
    for (int i = 0; i < board.length; i++) {
    	for (int j = 0; j < board[0].length; j++) {
    		board[i][j].mineNeighbors = getAdjacentMines(i, j);
    		System.out.print(board[i][j].mineNeighbors);
    	}
    	System.out.println();
    }
  }

  /**
   * This method returns the number of mines surrounding the requested
   * GameSquare (the mineNeighbors value of the square). A square with a mine
   * may return the number of surrounding mines, even though it will never
   * display that information.
   * 
   * @param row
   *          - An int value representing the row in board.
   * @param column
   *          - An int value representing the column in board.
   * @return The number of mines surrounding to this GameSquare (mineNeighbors)
   * 
   *         Must run O(1)
   */
  public int getAdjacentMines(int row, int column) {
	int sum = 0;
    for (int i = row-1; i <= row+1; i++) {
    	for (int j = column-1; j <= column+1; j++) {
    		//System.out.println(i + " " + j + " " + row + " " + column);
    		if (!board[row][column].isMine || i != row || j != column) {
    			if (i >= 0 && i < board.length && j >= 0 && j < board[0].length) {
    				if (board[i][j].isMine == true) {sum++;}
    			}
    		}
    	}
    }
    return Math.min(8, sum);
  }

  /**
   * Construct a MineSweeper of any size that has numberOfMines randomly set so
   * we get different games.
   * 
   * @param rows
   *          Height of the board
   * @param columns
   *          Width of the board
   * @param numberOfMines
   *          How m any mines are to randomly placed
   */
  public MineSweeper(int rows, int columns, int numberOfMines) {
       Random generator = new Random();
       int x;
       int y;
       boolean[][] mines = new boolean[columns][rows];
       for (int i = 0; i < Math.min(numberOfMines, columns*rows); i++) {
    	   do {
    		   x = generator.nextInt(rows);
    		   y = generator.nextInt(columns);
    	   } while (mines[x][y]);
    	   mines[y][x] = true;
       }
       
       
       
       board = new GameSquare[mines.length][mines[0].length];
       
       for (int i = 0; i < mines.length; i++) {
       	for (int j = 0; j < mines[0].length; j++) {
       		board[i][j] = new GameSquare(mines[i][j], i, j);
       		if (board[i][j].isMine) {
       			System.out.print("+");
       		} else {
       			System.out.print("-");
       		}
       	}
       	System.out.println();
       }
       
       setAdjacentMines();
       
  }

  /**
   * This method returns the number of mines found in the game board.
   * 
   * @return The number of mines.
   */
  public int getTotalMineCount() {
    int sum = 0;
    for (int i = 0; i < board.length; i++) {
    	for (int j = 0; j < board[0].length; j++) {
    		if (board[i][j].isMine) {sum++;}
    	}
    }
    return sum;
  }

  /**
   * This method returned whether or not the square has been flagged by the
   * user. Flags are a tool used by players to quickly tell which squares they
   * think contain mines as well as prevent accidental clicking on those
   * squares.
   * 
   * @param row
   *          - An int value representing the row (x) value in the game board.
   * @param column
   *          - An int value representing the column (y) value in the game
   *          board.
   * @return A boolean value representing the flagged state of this location.
   */
  public boolean isFlagged(int row, int column) {
    return board[row][column].isFlagged;
  }

  public void toggleFlagged(int row, int column) {
    board[row][column].isFlagged = !isFlagged(row, column);
  }

  /**
   * This method determines if the square in question is a mine.
   * 
   * @param row
   *          - An int value representing the row (x) value in the game board.
   * @param column
   *          - An int value representing the column (y) value in the game
   *          board.
   * @return A boolean representing the mine status of the square.
   */
  public boolean isMine(int row, int column) {
    return board[row][column].isMine;
  }

  /**
   * This method gets the visibility of the square in question. Visibilty is
   * initially defined for all squares to be false and uncovered when the click
   * method checks the square.
   * 
   * @param row
   *          - An int value representing the row (x) value in the game board.
   * @param column
   *          - An int value representing the column (y) value in the game
   *          board.
   * @return A boolean representing whether or not the square is set to be
   *         visible.
   */
  public boolean isVisible(int row, int column) {
    return board[row][column].isVisible;
  }

  /**
   * This method determines if the player has lost on the current board. A
   * player loses if and only if they have clicked on a mine.
   * 
   * @return A boolean representing player failure.
   */
  private boolean lost;
  public boolean lost() {
    return lost;
  }

  /**
   * Returns a textual representation of the GameBoard. 
   * 
   * @return A String representation of the game board data.
   */
  public String toString() {
    // TODO: Implement this method. And make sure you call it once in your test
    return "Under construction";
  }

  /**
   * This method determines if a player has won the game. Winning means all
   * non-mine squares are visible and no mines have been detonated.
   * 
   * @return A boolean representing whether or not the player has won.
   */
  public boolean won() {
	 for (int i = 0; i < board.length; i++) {
	   	for (int j = 0; j < board[0].length; j++) {
    		if (!(board[i][j].isMine ^ board[i][j].isVisible)) {return false;} // ^ is XOR
    	}
	 }
    return true;
  }

  /**
   * This method alerts the Game Board the user has clicked on the square at the
   * given row/column. There are five possibilities for updating the board
   * during the click messages to your MineSweeper. The GameSquare object stored
   * at the just clicked row and column
   * 
   * 1. is a mine (player looses)
   * 
   * 2. is visible already (do nothing)
   * 
   * 3. is flagged (do nothing)
   * 
   * 4. has mineNeighbors >- 1 (simply mark that visible)
   * 
   * 5. is not adjacent to any mines with mineNeighbors == 0 (mark many visible)
   * 
   * Because MineSweeper automatically clears all squares adjacent to any blank
   * square connected to the square clicked, a special algorithm is needed to
   * set the proper part of the board visible. This pseudo-code shows the
   * suggested algorithm.
   */
  // Check special cases first, there may be nothing to do or the user clicked a mine 
  // if the clicked GameSquare is a mine 
  //    change the state of this object to lost (when this click method finishes, the GUI will refresh with a loss)
  // else if the clicked GameSquare is flagged or the clicked GameSquare isVisible
  //    return, there is nothing to do
  // else if the clicked GameSquare has 1 or more neighboring mines 
  //    set the square to be visible, which applies only when mineNeighbors is 1..8
  // else clear all possible GameSquares up to the border or GameSquares with numbers 1..8
  //    mark the clicked GameSquare as visible
  //    push the GameSquare onto the stack
  //    while the stack is not empty:
  //       pop the stack and mark GameSquare as the current GameSquare
  //       if the current square must has no neighboring mines (not 1..8)
  //         for each adjacent square
  //           if it's not visible and not flagged and mines == 0
  //              push adjacent GameSquare on stack and set isVisible to true    
  /**
   * @param row
   *          - An int value representing the row (x) value in the game board.
   * @param column
   *          - An int value representing the column (y) value in the game
   *          board.
   */
  public void click(int row, int column) {
    GameSquare tile = board[row][column];
    if (tile.isVisible || tile.isFlagged) {
    	return;
    }
    if (tile.isMine) {
    	lost = true;
    }
    rClick(row, column);
  }
  public void rClick(int row, int column) {
	GameSquare tile = board[row][column];
	if (tile.isVisible) {return;}
	tile.isVisible = true;
	if (tile.mineNeighbors == 0) {
	    if (row+1 < board.length) {rClick(row+1, column);}
	    if (column+1 < board[0].length) {rClick(row, column+1);}
	    if (row-1 > -1) {rClick(row-1, column);}
	    if (column-1 > -1) {rClick(row, column-1);}
	}
	if (tile.isMine) {tile.isVisible = false;}
  }
}