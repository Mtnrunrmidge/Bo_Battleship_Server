package demo1;

import demo1.message.*;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class GameHandler implements Runnable{

    public enum GameState {NOT_PLAYING, JOIN_PHASE, RUNNING}
    public enum GameTurn {A, B}
    private GameState currentState = GameState.JOIN_PHASE;
    private static GameTurn currentTurn  = GameTurn.A;
    private static ConcurrentSkipListMap<MessageHandler, Player> players = new ConcurrentSkipListMap<>();
//    private List<String> onlinePlayers = new ArrayList<>();
//    private Set<String> inGamePlayers = new HashSet<>();
    private final int BOARDSIZE = 10;
    private static GridType[][] playerBoardA;
    private static GridType[][] playerBoardB;
    private static final int GAMESIZE = 2;
    private static GameHandler game = new GameHandler();

//    public void joinGame() {
//        currentState = GameState.JOIN_PHASE;
//    }

    public static void shutdown() {
        game = new GameHandler();
    }

    public void startGame() {
        currentState = GameState.RUNNING;
        broadcast(MessageFactory.getStartMessage());
//        setPlayerBoardA(playerBoardA);
//        setPlayerBoardB(playerBoardB);
        new Thread(this).start();
    }

    public static void handleSystemMessage(SystemMessage sysMsg, MessageHandler mh) {
        if (sysMsg.getSystemResponse().equals(SystemMessage.SystemResponse.READY)) {
            if (players.size() < GAMESIZE) {
                game.join(mh);

                if (currentTurn == GameTurn.A) {
                    playerBoardA = sysMsg.getGt();
                    Player.printBoard(playerBoardA);
                    players.get(mh).setMyBoard(playerBoardA);

                } else {
                    playerBoardB = sysMsg.getGt();
                    Player.printBoard(playerBoardB);
                    players.get(mh).setMyBoard(playerBoardB);
                }


                nextGameTurn();

                if (players.size() == GAMESIZE) {
                    game.startGame();
                }
            } else {
                throw new IllegalStateException("Discrepancy! Only 2 persons are allowed in the one game.");
            }
        }
    }

    private static void nextGameTurn() {
        System.out.println(currentTurn);
        currentTurn = (currentTurn == GameTurn.A)? GameTurn.B: GameTurn.A;
        System.out.println(currentTurn);
        System.out.println();
    }

    public static void handleActionMessage(GridStatusMessage msg, MessageHandler mh) {
        if (players.size() != GAMESIZE) {
            throw new IllegalStateException("Two, and only two players");
        }

        // messageSender
        if (players.get(mh).getTurn().equals(currentTurn)) {

            Player messageSender = players.get(mh);
            ConcurrentSkipListMap<MessageHandler, Player> temp = players.clone();
            temp.remove(mh);
            Player theOpponent = temp.entrySet().iterator().next().getValue();

            nextGameTurn();
            if (msg.getGs().equals(GridStatus.ATTEMPT)) {
                System.out.println(msg.toString());
                GridType[] result = theOpponent.getHit(msg.getGs(), msg.getRow(), msg.getCol());

                // brief results send to the attacker
                mh.sendMessage(MessageFactory.getResultMessage(mh.getUsername(), result, msg.getRow(), msg.getCol()));
                // results with updated board send to the attackee
                mh.sendMessage(MessageFactory.getGameActionMessage(theOpponent.getUsername(), result, msg.getRow(), msg.getCol(),
                        players.get(mh).getMyBoard()));

                if (!theOpponent.checkSurvival()) {
                    game.currentState = GameState.NOT_PLAYING;
                    game.broadcast(MessageFactory.getAckMessage());
                }
            }
        } else {
            mh.sendMessage(MessageFactory.getDenyMessage());
        }
    }

    public void join(MessageHandler handler) {
        if (!currentState.equals(GameState.JOIN_PHASE)) {
            throw new IllegalStateException("It's " + currentState + ", not READY phase.");
        }

        players.put(handler, new Player(handler.getUsername(), currentTurn));
        System.out.println(handler.getUsername() + " " + currentTurn);
        System.out.println(currentState);
        if (players.size() > GAMESIZE) {
            throw new IllegalStateException("Discrepancy! Only 2 persons are allowed in the one game.");
        }
    }

    public String[] getPlayerName() {
        String[] names = new String[players.size()];

        int index = 0;
        for (MessageHandler player: players.keySet()) {
            names[index++] = player.getUsername();
        }

        return names;
    }

    private void broadcast(Message msg) {
        broadcast(msg, null);
    }

    private void broadcast(Message msg, String username) {
        for (MessageHandler player: players.keySet()) {
            if (username == null || !player.getUsername().equals(username)) {
                player.sendMessage(msg);
            }
        }
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public GameTurn getCurrentTurn() {
        return currentTurn;
    }

    public GridType[][] getPlayerBoardA() {
        return playerBoardA;
    }

    public void setPlayerBoardA(GridType[][] playerBoardA) {
        this.playerBoardA = playerBoardA;
    }

    public GridType[][] getPlayerBoardB() {
        return playerBoardB;
    }

    public void setPlayerBoardB(GridType[][] playerBoardB) {
        this.playerBoardB = playerBoardB;
    }

    @Override
    public void run() {
        if (players.size() >= GAMESIZE) {
            broadcast(MessageFactory.getStartMessage());
        } else {
            currentState = GameState.NOT_PLAYING;
            players.clear();
        }
    }

    public static void main(String[] args) {
        HashMap<String, String> demo = new HashMap<>();
        demo.put("1", "one");
        demo.put("2", "one");

        HashMap<String, String> temp = (HashMap<String, String>) demo.clone();

        System.out.println(demo.get("1"));
        System.out.println(temp.remove("1"));
        System.out.println(demo.keySet());
    }
}
