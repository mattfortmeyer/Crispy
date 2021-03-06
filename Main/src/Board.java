import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Class to represent the current board spaces
 * with extra helper functions
 *
 * @author Matthew Matthew Craig
 */
public class Board {

    public double heuristicScore;
    public double utilityScore;
    public boolean whiteTurn;
    /**
     * board variable
     * 'x'  = black
     * '0' = white
     */
    public char[][] spaces = new char[15][15];
    private ArrayList<Board> children;
    private int moveX;
    private int moveY;


    /**
     * Char constructor
     * @param spaces the board to create
     * @param turn whose turn it is
     */
    public Board(char[][] spaces, Boolean turn) {
        this.spaces = spaces;
        this.children = new ArrayList<Board>();
        this.whiteTurn = turn;
    }

    /**
     * Empty constructor, just in case
     */
    public Board() {
        this.spaces = new char[15][15];
        this.children = new ArrayList<Board>();
    }

    /**
     * Evaluates the current board spaces, representing how good the board is for the player
     *
     * @return board goodness
     */
    public double utility() {
        char[][] newSpaces = new char[15][15];
        for (int k = 0; k < 15; k++) {
            newSpaces[k] = Arrays.copyOf(this.spaces[k], 15);
        }

        double whiteScore = this.scorePlayer(newSpaces, 'O');
        double blackScore = this.scorePlayer(newSpaces, 'X');
        this.utilityScore = whiteScore - blackScore;
        return this.utilityScore;
    }

    /**
     * Decides expansion order
     *
     * @return float representing its evaluation priority
     */
    double heuristic() {
        double numTouching = 0;

        if((this.getMoveY() > 0 && this.getMoveY() < 14) && (this.getMoveX() > 0 && this.getMoveX() < 14)){
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (this.spaces[this.getMoveY() - 1 + i][this.getMoveX() - 1 + j] == 'X' ||
                            this.spaces[this.getMoveY() - 1 + i][this.getMoveX() - 1 + j] == 'O') {
                        numTouching++;
                    }
                }
            }
        }
        return numTouching;
    }


    /**
     * Adds all possible children to the tree
     *
     * @return number of children added
     */
    public ArrayList<Board> addChildren() {
        int numChildren = 0;

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (!(this.spaces[i][j] == 'X' || this.spaces[i][j] == 'O')) {
                    char[][] newSpaces = new char[15][15];
                    for (int k = 0; k < 15; k++) {
                        newSpaces[k] = Arrays.copyOf(this.spaces[k], 15);
                    }

                    Board newBoard = new Board(newSpaces, !this.whiteTurn);
                    newBoard.setMoveX(j);
                    newBoard.setMoveY(i);
                    double h = newBoard.heuristic();
                    if (whiteTurn) {
                        newBoard.spaces[i][j] = 'O';
                    } else {
                        newBoard.spaces[i][j] = 'X';
                    }
                    this.children.add(newBoard);
                    numChildren++;
                }
            }
        }
        return this.children;
    }

    /**
     * Prints the board into the console
     */
    public void printBoard() {
        System.out.println("   A  B  C  D  E  F  G  H  I  J  K  L  M  N  O");

        //Print digits <10 with a leading space
        for (int i = 0; i < 9; i++) {
            System.out.print(" " + (i + 1));
            printRow(this.spaces[i]);
            System.out.print("\n");
        }

        for (int i = 9; i < 15; i++) {
            System.out.print(i + 1);
            printRow(this.spaces[i]);
            System.out.print("\n");
        }
        System.out.print(this.utility() + "\n");
    }

    public void printRow(char[] space) {
        for (int j = 0; j < 15; j++) {
            if (space[j] == 'X') {
                System.out.print("[X]");
            } else if (space[j] == 'O') {
                System.out.print("[O]");
            } else {
                System.out.print("[ ]");
            }
        }
    }


    public int getMoveX() {
        return this.moveX;
    }

    public void setMoveX(int x) {
        this.moveX = x;
    }

    public int getMoveY() {
        return this.moveY;
    }

    public void setMoveY(int y) {
        this.moveY = y;
    }

    /**
     * Calculate a score for a player based on chain lengths
     *
     * @param spaces board to be assesed
     * @param player person who the assessment is for
     * @return double score based on chain lengths
     */
    public double scorePlayer(char[][] spaces, char player){
        double score = 0;

        char[][] newSpaces = new char[15][15];
        for (int k = 0; k < 15; k++) {
            newSpaces[k] = Arrays.copyOf(spaces[k], 15);
        }

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if(newSpaces[i][j] == player){
                    int chainLength = 1;
                    newSpaces[i][j] = ' ';
                    for(int m = j; m < 14; m++){
                        if(newSpaces[i][m + 1] == player){
                            chainLength++;
                            newSpaces[i][m + 1] = ' ';
                        } else{
                            break;
                        }
                    }
                    if(chainLength == 2){
                        score += 2;
                    } else  if(chainLength == 3){
                        score += 5;
                    } else  if(chainLength == 4){
                        score += 20;
                    } else  if(chainLength == 5){
                        score += 5000;
                    }
                }
            }
        }

        newSpaces = new char[15][15];
        for (int k = 0; k < 15; k++) {
            newSpaces[k] = Arrays.copyOf(spaces[k], 15);
        }

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if(newSpaces[i][j] == player){
                    int chainLength = 1;
                    newSpaces[i][j] = ' ';
                    for(int m = i; m < 14; m++){
                        if(newSpaces[m + 1][j] == player){
                            chainLength++;
                            newSpaces[m + 1][j] = ' ';
                        } else{
                            break;
                        }
                    }
                    if(chainLength == 2){
                        score += 2;
                    } else  if(chainLength == 3){
                        score += 5;
                    } else  if(chainLength == 4){
                        score += 20;
                    } else  if(chainLength == 5){
                        score += 5000;
                    }
                }
            }
        }

        newSpaces = new char[15][15];
        for (int k = 0; k < 15; k++) {
            newSpaces[k] = Arrays.copyOf(spaces[k], 15);
        }

        //top half
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j <= i; j++) {
                int k = i - j;
                if(newSpaces[k][j] == player){
                    int chainLength = 1;
                    newSpaces[k][j] = ' ';
                    for(int m = j; m < i; m++){
                        if(newSpaces[i - m - 1][m + 1] == player){
                            chainLength++;
                            newSpaces[i - m - 1][m + 1] = ' ';
                        } else{
                            break;
                        }
                    }
                    if(chainLength == 2){
                        score += 2;
                    } else  if(chainLength == 3){
                        score += 5;
                    } else  if(chainLength == 4){
                        score += 20;
                    } else  if(chainLength == 5){
                        score += 5000;
                    }
                }
            }
        }

        //bottom half
        for (int i = 13 ; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                int k = i - j;
                if(newSpaces[14 - j][14 - k] == player){
                    int chainLength = 1;
                    newSpaces[14 - j][14 - k] = ' ';
                    for(int m = j; m < i; m++){
                        if(newSpaces[i - m + 1][m + 3] == player){
                            chainLength++;
                            newSpaces[i - m + 1][m + 3] = ' ';
                        } else{
                            break;
                        }
                    }
                    if(chainLength == 2){
                        score += 2;
                    } else  if(chainLength == 3){
                        score += 5;
                    } else  if(chainLength == 4){
                        score += 20;
                    } else  if(chainLength == 5){
                        score += 5000;
                    }
                }
            }
        }

        //Flip the board to calculate diagonal chains again
        newSpaces = new char[15][15];
        for (int k = 0; k < 15; k++) {
            for(int m = 0; m < 15; m++){
                newSpaces[k][14 - m] = spaces[k][m];
            }
        }

        //top half
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j <= i; j++) {
                int k = i - j;
                if(newSpaces[k][j] == player){
                    int chainLength = 1;
                    newSpaces[k][j] = ' ';
                    for(int m = j; m < i; m++){
                        if(newSpaces[i - m - 1][m + 1] == player){
                            chainLength++;
                            newSpaces[i - m - 1][m + 1] = ' ';
                        } else{
                            break;
                        }
                    }
                    if(chainLength == 2){
                        score += 2;
                    } else  if(chainLength == 3){
                        score += 5;
                    } else  if(chainLength == 4){
                        score += 20;
                    } else  if(chainLength == 5){
                        score += 5000;
                    }
                }
            }
        }

        //bottom half
        for (int i = 13 ; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                int k = i - j;
                if(newSpaces[14 - j][14 - k] == player){
                    int chainLength = 1;
                    newSpaces[14 - j][14 - k] = ' ';
                    for(int m = j; m < i; m++){
                        if(newSpaces[i - m + 1][m + 3] == player){
                            chainLength++;
                            newSpaces[i - m + 1][m + 3] = ' ';
                        } else{
                            break;
                        }
                    }
                    if(chainLength == 2){
                        score += 2;
                    } else  if(chainLength == 3){
                        score += 5;
                    } else  if(chainLength == 4){
                        score += 20;
                    } else  if(chainLength == 5){
                        score += 5000;
                    }
                }
            }
        }
        return score;
    }
}