public class Gameboard {

    private int gameBoard[][];
    private final static int BOARDSIZE = 10;

    public Gameboard(int gameBoard[][]) {

        this.gameBoard = gameBoard;

    }


    public int[][] getGameBoard() {

        return gameBoard;
    }

    public void setGameBoard(int gameBoard[][]) {

        this.gameBoard = gameBoard ;
    }

    public static void toString(int[][] board) {

        if (board != null) {
            for (int row = 0; row < BOARDSIZE; row++) {
                for (int column = 0; column < BOARDSIZE; column++) {
                    System.out.print(board[row][column] + " ");
                }
                System.out.println();
            }
        } else {
            System.out.println("Null board");
        }
    }

}
