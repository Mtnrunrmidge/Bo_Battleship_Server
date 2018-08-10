package demo1;

import demo1.message.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


public class TestingClientV2 {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private TestGUI cg;
    private Timer timerChat = new Timer();
    private Timer timerAction = new Timer();
    private String username;
    private boolean firstMsg = false;
    private final int BOARDSIZE = 10;
    GridType[][] gt = new GridType[10][10];
    private Player player;
    private int[][] myBoard = new int[BOARDSIZE][BOARDSIZE];

    public static void main(String[] args) throws IOException {

        TestingClientV2 client = new TestingClientV2();

        client.init();
    }

    private void init() throws IOException {
//        String host = "ec2-52-91-58-173.compute-1.amazonaws.com";
        String host = "127.0.0.1";
        InetAddress ia = InetAddress.getByName(host);
        socket = new Socket(ia, 8080);
        cg = new TestGUI();
        username = cg.getUsername();
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());

        readThread(socket, ois);

        player = new Player(username);

        login();


        while (socket.isConnected() && !socket.isClosed()) {
            sendMsg();
            sendGameAction();
        }
    }

    private void initBoard() {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gt[i][j] = GridStatus.Empty;
            }
        }

        gt[2][5] = Ship.ShipType.Cruiser;
        gt[2][6] = Ship.ShipType.Cruiser;
        gt[1][2] = Ship.ShipType.Battleship;
        gt[2][2] = Ship.ShipType.Battleship;
        gt[3][4] = Ship.ShipType.Carrier;
        gt[3][5] = Ship.ShipType.Carrier;
        gt[3][6] = Ship.ShipType.Carrier;
        gt[3][7] = Ship.ShipType.Carrier;
        gt[3][8] = Ship.ShipType.Carrier;
        gt[4][1] = Ship.ShipType.Destroyer;
        gt[5][1] = Ship.ShipType.Destroyer;
        gt[9][2] = Ship.ShipType.Submarine;
        gt[9][3] = Ship.ShipType.Submarine;

        player.setMyBoard(gt);

        GridStatus[][] opponent = new GridStatus[BOARDSIZE][BOARDSIZE];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                opponent[i][j] = GridStatus.Empty;
            }
        }

//        player.setTargetBoard(opponent);
    }

    private void readThread(Socket socket, ObjectInputStream ois) {
        Thread receiveMessage = new Thread(() -> {
            try {
                while (socket.isConnected() && !socket.isClosed()) {
                    readMessage(ois);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        receiveMessage.start();
    }

    private void readMessage(ObjectInputStream ois) throws IOException {
        try {
            Message read = (Message) ois.readObject();

            if (read != null) {
                if (read.getUsername() == null) {
                    cg.showMsgln("System message: ");
                }
                if (read instanceof ChatMessage) {
                    handleChat(read);
                } else if (read instanceof GameActionMessage) {
                    handleGridStatus((GameActionMessage) read);
                } else if (read instanceof ResultMessage) {
                    handleResultMessage((ResultMessage) read);
                } else  {
                    cg.showMsgln("username: " + read.getUsername());

                    cg.showMsgln("type: " + ackDeny(read) + "\n");
                }
            }
        } catch (Exception e) {
            disConnect(socket, oos, ois);

            System.exit(1);
        }
    }

    private void writeMINE() {
        printBoard(player.getMyBoard());
    }

    private void writeOPPONENT() {
//        printBoard(player.getTargetBoard());
    }

    private void handleResultMessage(ResultMessage read) {
        cg.showMsgln(read.toString());

    }

    private void handleGridStatus(GameActionMessage read) {
        cg.showMsgln(read.toString());
        gt = read.getBoard();
        cg.showMsgln("Your board: ");
        printBoard(gt);
    }

    public void printBoard(GridType[][] gt) {

        if (gt != null) {
            for (int i = 0; i < BOARDSIZE; i++) {
                for (int j = 0; j < BOARDSIZE; j++) {
                    cg.showMsg(String.format("%-12s", gt[i][j].toString()));
                }
                cg.showMsg("\n");
            }
        } else {
            cg.showMsgln("Null board");
        }
    }

    private String ackDeny(Message read) {
        String type = read.getMessageType().toString();
        if (type.equals("ACK")) {
            type = "Positive.";
        } else if (type.equals("DENY")) {
            type = "Negative.";
        }
        return type;
    }

    private void handleChat(Message read) {
        cg.showMsgln("username: " + read.getUsername());
        cg.showMsgln("type: " + read.getMessageType());
        cg.showMsgln("Text: " + ((ChatMessage) read).getText() + "\n");
    }
//
//    private void handleStatusMsg(Message read) {
//        if (!firstMsg) {
//            firstMsg = true;
//            if (read.getMessageType().toString().equals("ACK")) {
//                cg.showMsgln("    Connected to the game server successfully!\n");
//            } else {
//                cg.showMsgln("    Connection was refused by the game server!\n");
//            }
//        } else {
//            cg.showMsgln("    " + read.getClass().getSimpleName() + ": " + ackDeny(read) + "\n");
//        }
//    }

    private void login() throws IOException {
        oos.writeObject(MessageFactory.getLoginMessage(username));
        oos.flush();
    }

    private void writeMsg(String msg) throws IOException {
        oos.writeObject(MessageFactory.getChatMessage(msg, username));
        oos.flush();
    }


    private void writeHit() throws IOException {
        int[] cells;
        cells = cg.inputCell();
        oos.writeObject(MessageFactory.getAttemptMessage(username, cells[0], cells[1]));
        oos.flush();
    }

    private void writeJoin() throws IOException {
        initBoard();

        oos.writeObject(MessageFactory.getReadyMessage(gt));
        oos.flush();
    }


    private void sendMsg() {

        timerChat.schedule(new TimerTask() {

            @Override
            public void run() {
                String msg = cg.getMsg();

                if (msg != null && !msg.trim().equals("")) {
                    try {

                        writeMsg(msg);
                        cg.setMsg(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 500);
    }

    private void sendGameAction() {
        timerAction.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (cg.getGameAction() != null) {
                        switch (cg.getGameAction()) {
                            case HIT:
                                writeHit();
                                break;
                            case JOIN:
                                writeJoin();
                                break;
                            case MINE:
                                writeMINE();
                                break;
                            case OPPONENT:
                                writeOPPONENT();
                                break;
                            case EXIT:
                                disConnect(socket, oos, ois);
                                break;
                        }

                        cg.setGameAction(null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    private void disConnect(Socket s, ObjectOutputStream oos, ObjectInputStream ois) throws IOException {
        cg.exitWarning();
        oos.close();
        ois.close();
        s.close();
        System.exit(0);
    }
}
