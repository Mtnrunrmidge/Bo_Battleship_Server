package demo1.message;

import demo1.GridStatus;
import demo1.GridType;
import demo1.Player;

public class SystemMessage extends Message{

    public enum SystemResponse {ACK, DENY, READY, START, OVER}
    private SystemResponse sr;
    private String winner;
    private GridType[][] gt;

    public SystemMessage(String username, SystemResponse sr) {
        super(null);
        this.sr = sr;
        setWinner(username);
    }

    public SystemMessage(SystemResponse sr) {
        super(null);
        this.sr = sr;
        setWinner(null);
    }

    public SystemMessage(SystemResponse sr, GridType[][] gt) {
        super(null);
        this.sr = sr;
        setWinner(null);
        this.gt = gt;
    }

    // start game signal from server to clients
    public static SystemMessage getReadyMessage(GridType[][] gt) {
        return new SystemMessage(SystemResponse.READY, gt);
    }

    // start game signal from server to clients
    public static SystemMessage getStartMessage() {
        return new SystemMessage(SystemResponse.START);
    }

    public static SystemMessage getAckMessage() {
        return new SystemMessage(SystemResponse.ACK);
    }

    public static SystemMessage getDenyMessage() {
        return new SystemMessage(SystemResponse.DENY);
    }

    public static SystemMessage getGameOverMessage(String username) {
        return new SystemMessage(username, SystemResponse.OVER);
    }

    public SystemResponse getSystemResponse() {
        return sr;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public GridType[][] getGt() {
        return gt;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.SYSTEM;
    }

    @Override
    public String toString() {
        return "SystemMessage{" +
                "sr=" + sr +
                ", winner='" + winner + '\'' +
                '}';
    }
}
