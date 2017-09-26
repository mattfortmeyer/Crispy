import java.io.*;
import java.util.*;



/**
 *  Main class to run Crispy, an AI for playing GoMoku
 *
 * @author Matthew, Matthew, Craig
 *
 */
public class Main {
    static Board currentState;
    static int nextMovex = 0;
    static int nextMovey = 0;
    static java.util.Timer timer;
    static int movesMade = 0;
    static boolean yourTurn = false;

    static int DEPTH_LIMIT = 2;

    private static long CLOSE_DELAY = 5000;

    public static void main(String[] args){
        //wait until it's your turn
        while(!yourTurn){
            File f = new File("Crispy.go");
            yourTurn = f.exists();
        }

        Timer timer = new Timer(); //set timer
        timer.schedule(new TimerTask(){
            public void run() {
                makeMove(nextMovex, nextMovey);  //write move to file
                System.out.println("hi");
                timer.cancel();
            }
        }, CLOSE_DELAY);

        Board currentBoard = new Board(new char[15][15], true);
        currentBoard.printBoard();
        for(int i = 0; i < DEPTH_LIMIT; i++){
            ArrayList<Board> children = currentBoard.addChildren();
            Collections.sort(children, Comparator.comparing(s -> s.heuristicScore));
            currentBoard = children.get(0);
            currentBoard.printBoard();
            nextMovex = currentBoard.getMoveX() + 1;
            nextMovey = currentBoard.getMoveY() + 1;

            children = currentBoard.addChildren();
            Collections.sort(children, Comparator.comparing(s -> s.heuristicScore));
            currentBoard = children.get(0);
            currentBoard.printBoard();
        }

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
        //scores children using utility function and picks a new current move
        //expands children of current move, then recursively calls
        return tree;
    }

    //TODO: Expand?!?
    /**
     * Expand Wong
     */
    public Board expand(Board board) {
        return null;
        //adds a bunch of new nodes, runs heuristic on each, and then puts in sorted order
    }

    public static void makeMove(int x, int y){
        try {
            File moveFile = new File("move_file.txt");
            FileWriter moveWriter = new FileWriter(moveFile, false);
            moveWriter.write("Crispy " + nextMovex + " " + nextMovey);
            moveWriter.close();
            yourTurn = false;
        } catch(IOException e){
            System.out.println("I/O exception");
            e.printStackTrace();
        }
    }
}
