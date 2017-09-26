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
    private ArrayList<Board> children;
    public boolean whiteTurn;
    /**
        its the zip zop zoopitybop board
        'x'  = black
        '0' = white
     */
    public char[][] spaces = new char[15][15];



    /**
     *
     */
    public Board(char[][] spaces, Boolean turn){
       this.spaces = spaces;
       this.children = new ArrayList<Board>();
       this.whiteTurn = turn;
    }

    /**
     *
     */
    public Board() {
        this.spaces = new char[15][15];
        this.children = new ArrayList<Board>();
    }

    /**
     * Evaluates the current board spaces, representing how good the board is for the player
     * @return board goodness
     */
    public double utility(){
        return utility();
    }

    /**
     * Decides expansion order
     * @return float representing its evaluation priority
     */
    double heuristic() {
        Random r = new Random();
        double rand = 100 * r.nextDouble();
        this.heuristicScore = rand;
        return rand;
    }

    /**
     *
     */
    public Board move(char player, int x, int y) {
        if(x<0||y<0||y>15||x>15) {
            //dont do this please
            throw new RuntimeException("Bad move place. x:"+ x + ", y:"+ y);
        } else {
            //initialize variables and other constructor stuff
            //givin birth
            Board board = new Board(spaces, !this.whiteTurn);
            board.spaces[x][y] = player;
            return board;
        }
    }

    /**
     *  Adds all possible children to the tree
     * @return number of children added
     */
    public ArrayList<Board> addChildren(){
        int numChildren = 0;

        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(!(this.spaces[i][j] == 'X' || this.spaces[i][j] == 'O')) {
                    char[][] newSpaces = new char[15][15];
                    for (int k = 0; k < 15; k++) {
                        newSpaces[k] = Arrays.copyOf(this.spaces[k], 15);
                    }
                    Board newBoard = new Board(newSpaces, !this.whiteTurn);
                    double h = newBoard.heuristic();
                    if(whiteTurn){
                        newBoard.spaces[i][j] = 'O';
                    } else{
                        newBoard.spaces[i][j] = 'X';
                    }
                    this.children.add(newBoard);
                    newBoard.printBoard();
                    numChildren++;
                }
            }
        }
        return this.children;
    }


    public void printBoard(){
        for(int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if(this.spaces[i][j] == 'X'){
                    System.out.print("[X]");
                } else if(this.spaces[i][j] == 'O'){
                    System.out.print("[O]");
                } else {
                    System.out.print("[ ]");
                }
            }
            System.out.print("\n");
        }
        System.out.print(this.heuristicScore + "\n");
    }
}
