package pkg8queens;
/**
 *
 * @author MattL
 */
public class EightQueens {
    final private int[][] board = new int[8][8];
    final private int[][] testBoard = new int[8][8];
    private int heuristic = 0;
    private int numQueens = 0;
    private int restarts = 0;
    private int moves = 0;
    private int neighbors = 8;

    public EightQueens() { //creates a new, empty board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 0;
            }
        }
    }

    public void randomizeboard() { //randomizes the board
        while (numQueens < 8) {
            for (int i = 0; i < 8; i++) {
                board[(int) (Math.random() * 8)][i] = 1;
                numQueens++;
            }
        }
        heuristic = heuristic(board);
    }

    public boolean findRowAttack(int[][] test, int a) { //determines if there is an attack in a row
        boolean exFound = false;
        int attacks = 0;

        for (int i = 0; i < 8; i++) {
            if (test[i][a] == 1) {
                attacks++;
            }
        }
        if (attacks > 1) {
            exFound = true;
        }
        return exFound;
    }

    public boolean findColAttack(int[][] test, int j) { //determines if there is an attack in a column
        boolean exFound = false;
        int attacks = 0;
        for (int i = 0; i < 8; i++) {
            if (test[j][i] == 1) {
                attacks++;
            }
        }
        if (attacks > 1) {
            exFound = true;
        }
        return exFound;
    }

    public boolean findDiagAttack(int[][] test, int a, int b) {//determines if there is an attack on a diagonal
        boolean diagFound = false;

        for (int i = 1; i < 8; i++) {
            if (diagFound) {
                break;
            }

            if ((a + i < 8) && (b + i < 8)) {
                if (test[a + i][b + i] == 1) {
                    diagFound = true;
                }
            }
            if ((a - i >= 0) && (b - i >= 0)) {
                if (test[a - i][b - i] == 1) {
                    diagFound = true;
                }
            }
            if ((a + i < 8) && (b - i >= 0)) {
                if (test[a + i][b - i] == 1) {
                    diagFound = true;
                }
            }
            if ((a - i >= 0) && (b + i < 8)) {
                if (test[a - i][b + i] == 1) {
                    diagFound = true;
                }
            }
        }
        return diagFound;
    }

    public int heuristic(int[][] test) { //calculates the number of queens in conflict
        int count = 0;
        boolean rowAttack;
        boolean colAttack;
        boolean diaAttack;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (test[i][j] == 1) {
                    rowAttack = findRowAttack(test, j);
                    colAttack = findColAttack(test, i);
                    diaAttack = findDiagAttack(test, i, j);
                    
                    if (rowAttack || colAttack || diaAttack) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void hillClimbing() { //moves a queen and determines whether to continue to a new state or restart or to summarize solution
        int[][] hillArray = new int[8][8];
        int colCount;
        int minCol;
        int minRow;
        int prevQueenCol = 0;

        while (true) {
            colCount = 0;

            for (int i = 0; i < 8; i++) {
                System.arraycopy(board[i], 0, testBoard[i], 0, 8);
            }
            while (colCount < 8) {
                for (int i = 0; i < 8; i++) {
                    testBoard[i][colCount] = 0;
                }
                for (int i = 0; i < 8; i++) {
                    if (board[i][colCount] == 1) {
                        prevQueenCol = i;
                    }
                    testBoard[i][colCount] = 1;
                    hillArray[i][colCount] = heuristic(testBoard);
                    testBoard[i][colCount] = 0;
                }
                testBoard[prevQueenCol][colCount] = 1;
                colCount++;
            }

            if (determineRestart(hillArray)) {
                numQueens = 0;
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        board[i][j] = 0;
                    }
                }
                randomizeboard();
                System.out.println("RESTART");
                restarts++;
            }

            minCol = findMinCol(hillArray);
            minRow = findMinRow(hillArray);

            for (int i = 0; i < 8; i++) {
                board[i][minCol] = 0;
            }

            board[minRow][minCol] = 1;
            moves++;
            heuristic = heuristic(board);

            if (heuristic(board) == 0) {
                System.out.println("\nCurrent State");
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        System.out.print(board[i][j] + " ");
                    }
                    System.out.print("\n");
                }
                System.out.println("Solution Found!");
                System.out.println("State changes: " + moves);
                System.out.println("Restarts: " + restarts);
                break;
            }

            System.out.println("\n");
            System.out.println("Current h: " + heuristic);
            System.out.println("Current State");
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.print("\n");
            }
            System.out.println("Neighbors found with lower h: " + neighbors);
            System.out.println("Setting new current State");
        }
    }

    public int findMinCol(int[][] test) { //finds column of minimum neighbor state
        int minCol = 8;
        int minVal = 8;
        int count = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (test[i][j] < minVal) {
                    minVal = test[i][j];
                    minCol = j;
                }
                if (test[i][j] < heuristic) {
                    count++;
                }
            }
        }
        neighbors = count;
        return minCol;
    }

    public int findMinRow(int[][] test) { //finds row of minimum neighbor state
        int minRow = 8;
        int minVal = 8;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (test[i][j] < minVal) {
                    minVal = test[i][j];
                    minRow = i;
                }
            }
        }
        return minRow;
    }

    public boolean determineRestart(int[][] test) {// decides if a local maximum is reached
        int minVal = 8;
        boolean restart = false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (test[i][j] < minVal) {
                    minVal = test[i][j];
                }
            }
        }
        if (neighbors == 0) {
            restart = true;
        }
        return restart;
    }
}
