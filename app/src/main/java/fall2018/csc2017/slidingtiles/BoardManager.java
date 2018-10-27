package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class BoardManager implements Serializable {

    /**
     * The board being managed.
     */
    private Board board;

    /**
     * A LimitedStack that stores moves that can be undone.
     */
    private LimitedStack undoStack;

    /**
     * Number of moves currently made
     */
    private int moves = 0;

    /**
     * The number of  moves after which the game is saved.
     */
    //TODO: SEE IF THIS NEEDS TO BE CHANGEABLE
    private final int SAVE_MOVE_LIMIT = 3;

    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */
    BoardManager(Board board) {
        this.board = board;
    }

    /**
     * Return the current board.
     */
    Board getBoard() {
        return board;
    }

    /**
     * Manage a new shuffled board.
     */
    BoardManager(int rows, int cols) {
        List<Tile> tiles = new ArrayList<>();

        // TODO: IMPLEMENT HOW THE UNDO WILL TAKE THE MAX SIZE
        undoStack = new LimitedStack();
        final int numTiles = rows * cols;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum, Integer.toString(rows)));
        }

        Collections.shuffle(tiles);
        this.board = new Board(tiles, rows, cols);
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    boolean puzzleSolved() {
        boolean solved = true;
        ArrayList<Integer> tileIds = new ArrayList<>();

        fillListWithInts(tileIds, 1, board.getNumRows() * board.getNumCols());
        removeOrderedTiles(tileIds);

        if (!tileIds.isEmpty()) {
            solved = false;
        }
        return solved;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {

        int row = position / board.getNumCols();
        int col = getColumn(position);
        int blankId = board.numTiles();
        // Are any of the 4 the blank tile?
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == board.getNumRows() - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == board.getNumCols() - 1 ? null : board.getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    void touchMove(int position) {

        int blankId = board.numTiles();
        board.swapTiles(getRow(position), getColumn(position), getRowOfBlank(position, blankId), getColOfBlank(position, blankId));
        moves++;
    }

    /**
     * Remove all tiles that are in correct order on the grid from passed array list
     *
     * @param tileIds array list of correctly arranged tile ids
     */
    private void removeOrderedTiles(ArrayList<Integer> tileIds) {
        int j = 0;
        for (Tile t : board) {
            if (t.getId() == tileIds.get(j)) {
                tileIds.remove(j);
            } else {
                j++;
            }
        }
    }

    /**
     * Fill the passed array list with integers from min to max inclusive
     *
     * @param tileIds array list to be filled
     * @param min     smallest value in the list
     * @param max     largest value in the list
     */
    private void fillListWithInts(ArrayList<Integer> tileIds, int min, int max) {
        for (int i = min; i <= max; i++) {
            tileIds.add(i);
        }
    }

    /**
     * Return the row of the blank tile adjacent to the tile at position
     *
     * @param position position of tile
     * @param blankId  id of blank tile
     * @return row of blank tile
     */
    private int getRowOfBlank(int position, int blankId) {
        if (isBlankTop(position, blankId)) {
            return getRow(position) - 1;
        } else if (isBlankBottom(position, blankId)) {
            return getRow(position) + 1;
        }
        return getRow(position);
    }

    /**
     * Return whether the tile on top of tile at position is blank
     *
     * @param position position of tile
     * @param blankId  id of blank tile
     * @return whether the tile on top of tile at position is blank
     */
    private boolean isBlankTop(int position, int blankId) {
        if (getRow(position) - 1 >= 0) {
            return isBlankTile(getRow(position) - 1, getColumn(position), blankId);
        }
        return false;
    }

    /**
     * Return whether the tile at the bottom of tile at position is blank
     *
     * @param position position of tile
     * @param blankId  id of blank tile
     * @return whether the tile at the bottom of tile at position is blank
     */
    private boolean isBlankBottom(int position, int blankId) {
        if (getRow(position) + 1 < board.getNumRows()) {
            return isBlankTile(getRow(position) + 1, getColumn(position), blankId);
        }
        return false;
    }

    /**
     * Find the column of the blank tile adjacent to tile at position
     *
     * @param position position of the tile
     * @param blankId  id of blank tile
     * @return the column location of the blank tile adjacent to tile at position
     */
    private int getColOfBlank(int position, int blankId) {
        if (isBlankLeft(position, blankId)) {
            return getColumn(position) - 1;
        } else if (isBlankRight(position, blankId)) {
            return getColumn(position) + 1;
        }
        return getColumn(position);
    }

    /**
     * Return whether tile at (row, col) is blank
     *
     * @param row     row of tile to check
     * @param col     column of tile to check
     * @param blankId id of blank tile
     * @return whether the tile at row and col on the grid is blank
     */
    private boolean isBlankTile(int row, int col, int blankId) {
        return board.getTile(row, col).getId() == blankId;
    }

    /**
     * Return whether the tile to the left of the tile at position is blank
     *
     * @param position position of tile
     * @param blankId  id of blank tile
     * @return whether the tile to the left of tile at position is blank
     */
    private boolean isBlankLeft(int position, int blankId) {
        if (getColumn(position) - 1 >= 0) {
            return isBlankTile(getRow(position), getColumn(position) - 1, blankId);
        }
        return false;
    }

    /**
     * Return whether the tile to the right of tile at position is blank
     *
     * @param position position of tile
     * @param blankId  id of blank tile
     * @return whether the tile to the right of tile at position is blank
     */
    private boolean isBlankRight(int position, int blankId) {
        if (getColumn(position) + 1 < board.getNumCols()) {
            return isBlankTile(getRow(position), getColumn(position) + 1, blankId);
        }
        return false;
    }

    /**
     * Return the column of tile at position
     *
     * @param position position of tile
     * @return the column of the tile at position
     */
    private int getColumn(int position) {
        return position % board.getNumCols();
    }

    /**
     * Return the row of tile at position
     *
     * @param position position of tile
     * @return the row of the tile at position
     */
    private int getRow(int position) {
        return position / board.getNumRows();
    }

    /**
     * Return the number of moves made in this session of sliding tiles
     *
     * @return number of moves made
     */
    public int getMoves() {
        return moves;
    }

    /**
     * Return the number of moves that needs to be made before the game is saved.
     *
     * @return current SAVE_MOVE_LIMIT
     */
    public int getSAVE_MOVE_LIMIT() {
        return SAVE_MOVE_LIMIT;
    }
}
