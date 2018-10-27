package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Implementation of the Stack ADT that auto-removes the furthest back if the maxSize limit is exceeded.
 */
public class LimitedStack implements Serializable {
    /**
     * The maximum size of contents, null if unlimited.
     */
    private Integer maxSize;
    /**
     * The contents of the stack.
     */
    private ArrayList<BoardManager> contents;

    /**
     * @param maxSize: the maximum size of contents
     */
    public LimitedStack(int maxSize) {
        this.maxSize = maxSize;
        contents = new ArrayList<>(maxSize);
    }

    public LimitedStack() {
        this.maxSize = null;
        contents = new ArrayList<>();
    }

    /**
     * Add a move to the front of the Stack.
     *
     * @param board: the most recent move
     */
    public void add(BoardManager board) {
        contents.add(board);
        if (maxSize != null && contents.size() > maxSize) {
            contents.remove(0);
        }
    }

    /**
     * Return the last item in the Stack, else null is empty.
     *
     * @return the last item in the Stack, else null if empty.
     */
    public BoardManager remove() {
        if (!contents.isEmpty()) {
            return contents.remove(contents.size() - 1);
        }
        return null;
    }
}
