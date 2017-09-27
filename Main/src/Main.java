import java.io.*;
import java.util.*;

import static java.lang.Thread.sleep;


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
    static double currentMax = 0;
    static java.util.Timer timer;
    static int movesMade = 0;
    static boolean yourTurn = false;
    static boolean over = false;
    static boolean areWhite = false;
    static boolean turn0 = true;

    private static long CLOSE_DELAY = 5000;

    public static void main(String[] args) {
        Board currentBoard = new Board(new char[15][15], true);
        //currentBoard.spaces[10][6] = 'O';
        //currentBoard.spaces[10][3] = 'O';
        currentState = currentBoard;

        //main loop
        while (true) {
            //if(over) return;
            //wait until it's your turn
            while (!yourTurn) {
                //if(over) return;
                //its your turn if go file exists
                File f = new File("Crispy.go");
                yourTurn = f.exists();

                File endGame = new File("end_game");
                //if end_game exists, the game ends
                if (endGame.exists()) {
                    return;
                }

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                File move = new File("move_file");
                //if move file exists then try to read it in
                if(!(move.length() == 0)) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader("move_file"));
                        String line = br.readLine();
                        String[] curLine = line.split("\\s");
                        if (areWhite && (!curLine[0].equals("Crispy"))) {
                            currentState.spaces[Integer.parseInt(curLine[2]) - 1][fromLetter(curLine[1].charAt(0)) - 1] = 'X';
                        } else if (!areWhite && (!curLine[0].equals("Crispy"))) {
                            currentState.spaces[Integer.parseInt(curLine[2]) - 1][fromLetter(curLine[1].charAt(0)) - 1] = 'O';
                        }
                    } catch (IOException e) {
                        System.out.println("I/O exception");
                        e.printStackTrace();
                    }
                }
            }

            //start the timer, its your turn
            Timer timer = new Timer(); //set timer
            timer.schedule(new TimerTask() {
                public void run() {
                    //schedule move to file, flip flags, start waiting again after .9s
                    makeMove(nextMovex, nextMovey);  //write move to file
                    System.out.println("timeout");
                    over = true;
                    yourTurn = false;
                    timer.cancel();
                }
            }, CLOSE_DELAY);

            //check the move file and set who you are
            File move = new File("move_file");
            if(turn0) {
                areWhite = (move.length() == 0);
                turn0 = false;
            }
            if(areWhite){
                System.out.println("I'm white!");
            } else{
                System.out.println("I'm black!");
            }

            over = false;
            //if(over) return;

            //generate all possible moves
            ArrayList<Board> children = currentBoard.addChildren();

            //sort those children using the utility function
            Collections.sort(children, Comparator.comparing(s -> s.utility()));

            if(areWhite) Collections.reverse(children);
            //?

            //currentBoard is always root of the tree
            currentBoard = children.get(0);
            //currentBoard.printBoard();

            //indexing fix (?) should probably be moved to getMoveX/Y
            nextMovex = currentBoard.getMoveX() + 1;
            nextMovey = currentBoard.getMoveY() + 1;

            //keep track of current max for minMax
            currentMax = currentBoard.utilityScore;
            currentState = currentBoard;

            //
            System.out.println("New Move:" + currentMax + " " + nextMovex + " " + nextMovey);
            movesMade++;

            //
            for(Board b : children){
                if (over) break;

                //expand a child
                ArrayList<Board> twoChildren = b.addChildren();

                Collections.sort(twoChildren, Comparator.comparing(s -> s.utility()));

                //since utility is black biased, we need to flip it. Potential fix: utility function taking player
                if(!areWhite) Collections.reverse(twoChildren);

                //current board becomes the most promising of the children
                currentBoard = twoChildren.get(0);
                //currentBoard.printBoard();
                movesMade++;

                //expand to a third child
                int localMoveX = 0;
                int localMoveY = 0;
                double localMin = 0;
                for(Board c : twoChildren){
                    if (over) break;
                    ArrayList<Board> threeChildren = c.addChildren();
                    Collections.sort(threeChildren, Comparator.comparing(s -> s.utility()));
                    if(areWhite) Collections.reverse(threeChildren);
                    currentBoard = threeChildren.get(0);
                    //currentBoard.printBoard();
                    if((c.equals(twoChildren.get(0))) || currentBoard.utilityScore < localMin){
                        localMoveX = b.getMoveX() + 1;
                        localMoveY = b.getMoveY() + 1;
                        localMin = currentBoard.utilityScore;
                    }
                    movesMade++;
                }
                if(b.equals(children.get(0)) || (localMin > currentMax)){
                    nextMovex = localMoveX;
                    nextMovey = localMoveY;
                    currentMax = localMin;
                    currentState = b;
                    //make a new move if we find a better one
                    System.out.println("New Move:" + currentMax + " " + nextMovex + " " + nextMovey);
                }
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
    }


    /**
     * Writes a move to the move file
     * @param x column of move to write
     * @param y row of move to write
     */
    public static void makeMove(int x, int y){
        try (PrintWriter pw = new PrintWriter("move_file")) {
            pw.write("Soggy " + toLetter(nextMovex) + " " + nextMovey);
            pw.close();
            //BufferedWriter bw = new BufferedWriter(new FileWriter("move_file", false);
            //bw.write("Soggy " + toLetter(nextMovex) + " " + nextMovey);
            //bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        try {
            File moveFile = new File("move_file");
            if(moveFile.exists()) {

                FileWriter moveWriter = new FileWriter(moveFile, false);
                moveWriter.write("Crispy " + toLetter(nextMovex) + " " + nextMovey);
                moveWriter.close();
            }
            yourTurn = false;
            currentState.printBoard();
        } catch(IOException e){
            System.out.println("I/O exception");
            e.printStackTrace();
        }*/
    }

    /**
     * Converts x to letters, as is the convention for file writing
     *
     * @param x row to be letterized
     * @return letter that x represents
     */
    public static char toLetter(int x){
        if(x == 1){
            return 'A';
        } else if(x == 2){
            return 'B';
        } else if(x == 3){
            return 'C';
        } else if(x == 4){
            return 'D';
        } else if(x == 5){
            return 'E';
        } else if(x == 6){
            return 'F';
        } else if(x == 7){
            return 'G';
        } else if(x == 8){
            return 'H';
        } else if(x == 9){
            return 'I';
        } else if(x == 10){
            return 'J';
        } else if(x == 11){
            return 'K';
        } else if(x == 12){
            return 'L';
        } else if(x == 13){
            return 'M';
        } else if(x == 14){
            return 'N';
        } else if(x == 15){
            return 'O';
        }
        return 'Z';
    }

    /**
     * Read letter into row, for reading from move file
     *
     * @param x letter to convert to row
     * @return
     */
    public static char fromLetter(int x){
        if(x == 'A'){
            return 1;
        } else if(x == 'B'){
            return 2;
        } else if(x == 'C'){
            return 3;
        } else if(x == 'D'){
            return 4;
        } else if(x == 'E'){
            return 5;
        } else if(x == 'F'){
            return 6;
        } else if(x == 'G'){
            return 7;
        } else if(x == 'H'){
            return 8;
        } else if(x == 'I'){
            return 9;
        } else if(x == 'J'){
            return 10;
        } else if(x == 'K'){
            return 11;
        } else if(x == 'L'){
            return 12;
        } else if(x == 'M'){
            return 13;
        } else if(x == 'N'){
            return 14;
        } else if(x == 'O'){
            return 15;
        }
        return 'Z';
    }

}
