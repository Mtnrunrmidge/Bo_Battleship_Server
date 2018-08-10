package demo1.message;

import demo1.GridType;

import java.util.Arrays;

public class GameActionMessage extends ResultMessage {

    private GridType[][] board;

    public GameActionMessage(String username, GridType[] gt, int row, int col, GridType[][] board) {
        super(username, gt, row, col);
        this.board = board;
    }

    public GridType[][] getBoard() {
        return board;
    }

    public void setBoard(GridType[][] board) {
        this.board = board;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.GAME_ACTION;
    }

    @Override
    public String toString() {
        return "GameActionMessage{" +
                ", hitMiss=" + hitMiss +
                ", sinkShip=" + sinkShip +
                ", isSurvival=" + isSurvival +
                '}';
    }
}
