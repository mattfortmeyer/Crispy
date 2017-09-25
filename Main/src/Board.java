/**
 * Class to represent the current board state
 * with extra helper functions
 *
 * @author Matthew Matthew Craig
 */
public class Board {
    public double heuristicScore;
    public double utilityScore;
    /**
        its the zip zop zoopitybop board
        'x'  = black
        '0' = white
     */
    char[][] state = new char[15][15];



    /**
     *
     */
    public Board(char[][] state) {
       this.state = state;
    }

    /**
     *
     */
    public Board() {
        this.state = new char[15][15];
    }

    /**
     * Evaluates the current board state, representing how good the board is for the player
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
        //score it
        return 0.0;
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
            Board board = new Board(state);
            board.state[x][y] = player;
            return board;
        }
    }

}
