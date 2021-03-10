package pkg8queens;
/**
 *
 * @author MattL
 */
public class Main {
    public static void main(String[] args) {// creates object, creates initial random board, then initiates state change
     EightQueens myBoard = new EightQueens( );
     myBoard.randomizeboard();
     myBoard.hillClimbing();
    }

}

