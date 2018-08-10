package demo1;

import java.util.Arrays;
import java.util.HashSet;

public class Player {

    private HashSet<Ship.ShipType> survivedShips;
    private String username;
    private final static int BOARDSIZE = 10;
    private GridType[][] myBoard = new GridType[BOARDSIZE][BOARDSIZE];
//    private GridType[][] targetBoard = new GridType[BOARDSIZE][BOARDSIZE];
    private GameHandler.GameTurn turn;
//    private boolean[] survivedShipIds = new boolean[Ship.ShipType.values().length];
    // 1-5, shipId; 9, hit attempt; 8, hit target; 7, miss target

    public Player(String username, GameHandler.GameTurn gameTurn) {
        setUsername(username);
        setTurn(gameTurn);

        survivedShips = new HashSet<>();
        for (Ship.ShipType st: Ship.ShipType.values()) {
            survivedShips.add(st);
        }
//
//        for (GridType[] arr: targetBoard) {
//            for (GridType gt: arr) {
//                gt = GridStatus.Empty;
//            }
//        }
//        survivedShips = new HashMap<>();
//
//        for (Ship.ShipType st: Ship.ShipType.values()) {
//            survivedShips.put(Ship.getShipAttributes(st)[0], new Ship(st));
//        }
//
//        for (int i = 0; i < survivedShipIds.length; i++) {
//            survivedShipIds[i] = true;
//        }
    }

    public Player(String username) {
        setUsername(username);

        survivedShips = new HashSet<>();
        for (Ship.ShipType st: Ship.ShipType.values()) {
            survivedShips.add(st);
        }
//
//        for (GridType[] arr: targetBoard) {
//            for (GridType gt: arr) {
//                gt = GridStatus.Empty;
//            }
//        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public HashSet<Ship.ShipType> getSurvivedShips() {
        return survivedShips;
    }

    public void sinkShip(int id) {
        survivedShips.remove(id);
//        survivedShipIds[id - 1] = false;
    }

    public GridType[][] getMyBoard() {
        return myBoard;
    }

    public void setMyBoard(GridType[][] myBoard) {
        this.myBoard = myBoard;
    }
//
//    public void setTargetBoard(GridType[][] targetBoard) {
//        this.targetBoard = targetBoard;
//    }

    public GridType[] getHit(GridStatus gs, int row, int col) {
        GridType[] result = new GridType[3];

        if (myBoard[row][col] instanceof Ship.ShipType) {
            myBoard[row][col] = GridStatus.HIT;
            result[0] = GridStatus.HIT;
        } else if (myBoard[row][col] instanceof GridStatus) {
            if (myBoard[row][col].equals(GridStatus.Empty)) {
                myBoard[row][col] = GridStatus.MISS;
                result[0] = GridStatus.MISS;
            } else {
                throw new IllegalStateException("Already attacked");
            }
        }

        result[1] = checkSurvivedShip();

        if (!checkSurvival()) {
            System.out.println("Game over");
            result[2] = GridStatus.Empty;
        }

        return result;
    }
//
//    public GridType[][] getTargetBoard() {
//        return targetBoard;
//    }

//    public void hitOrMiss(GridStatus gs, int row, int col) {
//        if (targetBoard[row][col] == GridStatus.ATTEMPT ) {
//            this.targetBoard[row][col] = gs;
//        } else {
//            throw new IllegalArgumentException("Only Grids that are marked as ATTEMPT can be HIT or MISS.");
//        }
//    }

    public Ship.ShipType checkSurvivedShip() {
        HashSet<Ship.ShipType> localSurvivedShips = new HashSet<>();
        HashSet<Ship.ShipType> tempSet = survivedShips;

        printBoard(myBoard);

        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                if (myBoard[i][j] instanceof Ship.ShipType) {
                    localSurvivedShips.add((Ship.ShipType) myBoard[i][j]);
                }
            }
        }

        System.out.println("localSurvivedShips : " + Arrays.toString(localSurvivedShips.toArray()));
        System.out.println("tempSet : " + Arrays.toString(tempSet.toArray()));

        tempSet.removeAll(localSurvivedShips);

        if (tempSet.size() == 1) {
            Ship.ShipType dead = tempSet.iterator().next();
            System.out.println(tempSet.iterator().next().toString());
            return dead;
        } else if (tempSet.size() > 1) {
            try {
                throw new IllegalStateException("There're discrepancies between client side and server side.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        survivedShips = localSurvivedShips;
        return null;
    }

    public boolean checkSurvival() {

        return (!survivedShips.isEmpty());
    }
//
//    public int[] hitEnemy(int row, int col) {
//        if (targetBoard[row][col] == GridStatus.Empty || targetBoard[row][col] == null) {
//            targetBoard[row][col] = GridStatus.ATTEMPT;
//        }
//
//        return new int[]{row, col};
//    }
//
//    public void setGridStatus(GridStatus gs, int row, int col) {
//        myBoard[row][col] = gs;
//    }
//
//    public void setOpponentGridStatus(GridStatus gs, int row, int col) {
//        targetBoard[row][col] = gs;
//    }

    public GameHandler.GameTurn getTurn() {
        return turn;
    }

    public void setTurn(GameHandler.GameTurn turn) {
        this.turn = turn;
    }

    public static void printBoard(GridType[][] gt2d) {

        if (gt2d != null) {
            for (int i = 0; i < BOARDSIZE; i++) {
                for (int j = 0; j < BOARDSIZE; j++) {
                    System.out.print(gt2d[i][j] + " ");
                }
                System.out.println();
            }
        } else {
            System.out.println("Null board");
        }
    }

    public static void main(String[] args) {
        Player aa = new Player("aa", GameHandler.GameTurn.A);
        System.out.println(aa.getSurvivedShips());
        GridType[][] gt = new GridType[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gt[i][j] = GridStatus.Empty;
            }
        }
        gt[2][5] = Ship.ShipType.Cruiser;
        gt[1][2] = Ship.ShipType.Battleship;
        gt[3][4] = Ship.ShipType.Carrier;
        gt[3][5] = Ship.ShipType.Carrier;
        gt[4][1] = Ship.ShipType.Destroyer;
        gt[9][2] = Ship.ShipType.Submarine;
        gt[1][3] = GridStatus.MISS;

        Player.printBoard(gt);

        aa.setMyBoard(gt);
//        aa.hitEnemy(1, 5);
//        aa.hitEnemy(2, 5);
        aa.getHit(GridStatus.HIT, 1, 2);
        aa.getHit(GridStatus.HIT, 3, 4);
        aa.getHit(GridStatus.HIT, 2, 5);
        aa.getHit(GridStatus.HIT, 4, 1);
        aa.getHit(GridStatus.HIT, 9, 2);
        aa.getHit(GridStatus.HIT, 3, 5);

        Player.printBoard(gt);
    }
}
