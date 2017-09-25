import java.util.*;



/**
 *  Main class to run Crispy, an AI for playing GoMoku
 *
 * @author Matthew, Matthew, Craig
 *
 */
public class Main {
    static TreeMap currentState;
    static TreeMap nextMove;
    static java.util.Timer timer;

    public static void main(String[] args){
        //do file IO and add new turns to our current model
        //initialize timer
        //start expanding like crazy
        //wait for the interrupt
        //make our move (more file IO)
        //wait for new turns
        //maybe calculate trees in the meantime?
        //update on opponent moves
    }


    /**
     * Order the tree using the heuristic
     * @param tree
     * @return an ordered tree
     */
    TreeMap minMax(TreeMap tree) {
        mm();
        //scores children using utility function and picks a new current move
        //expands children of current move, then recursively calls
        return tree;
    }

    //TODO: Expand?!?
    /**
     * Expand Wong
     */
    public TreeMap expand() {
        return null;
        //adds a bunch of new nodes, runs heuristic on each, and then puts in sorted order
    }

    /**
     * mm
     * @return mm
     */
    boolean mm() {
        while(mm()){
            mm();
        }
        return mm();
    }
}
