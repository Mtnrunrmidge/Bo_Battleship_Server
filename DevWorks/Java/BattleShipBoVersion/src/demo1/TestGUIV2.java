package demo1;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.TreeMap;


public class TestGUIV2 extends JFrame{

    private JTextArea taChat;
    private JTextArea taConsole;
    private String msg = "";
    private String username = "";
    private GameAction gameAction;
    public enum GameAction {HIT, JOIN, EXIT, MINE, OPPONENT}
    public Map<String, Color> shipColor = new TreeMap<>();
    public Map<Integer, String> shipID = new TreeMap<>();
    private static Color currentShip = Color.WHITE;
    public static final int BOARDSIZE = 10;
    private static int[][] myBoard = new int[BOARDSIZE][BOARDSIZE];
    private static int[][] targetBoard = new int[BOARDSIZE][BOARDSIZE];
    private JButton[][] myCells = new JButton[BOARDSIZE][BOARDSIZE];
    private JButton[][] targetCells = new JButton[BOARDSIZE][BOARDSIZE];
    private final int CELLSIZE = 49;
    private final int WIDTH = 510;

    TestGUIV2() {
        setMinimumSize(new Dimension(1000, 700));

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{0, 0};
        gbl_panel.rowHeights = new int[]{0, 0, 0};
        gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE, 1.0};
        panel.setLayout(gbl_panel);

        JScrollPane spConsole = new JScrollPane();
        spConsole.setBorder(new EmptyBorder(10, 10, 10, 10));
        spConsole.setMinimumSize(new Dimension(WIDTH, 200));
        spConsole.setPreferredSize(new Dimension(WIDTH, 200));
        spConsole.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints gbc_spConsole = new GridBagConstraints();
        gbc_spConsole.insets = new Insets(0, 0, 0, 0);
        gbc_spConsole.fill = GridBagConstraints.BOTH;
        gbc_spConsole.gridx = 1;
        gbc_spConsole.gridy = 1;
        panel.add(spConsole, gbc_spConsole);

        JPanel jpMyBoard = new JPanel();
        jpMyBoard.setMinimumSize(new Dimension(WIDTH, 506));
        jpMyBoard.setPreferredSize(new Dimension(WIDTH, 506));
        GridBagConstraints gbc_jpBoard = new GridBagConstraints();
        gbc_jpBoard.insets = new Insets(5, 0, 5, 0);
        gbc_jpBoard.fill = GridBagConstraints.BOTH;
        gbc_jpBoard.gridx = 0;
        gbc_jpBoard.gridy = 0;
        panel.add(jpMyBoard, gbc_jpBoard);

        AssignShip();

        jpMyBoard.setLayout(null);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                myCells[i][j] = new JButton();
                myCells[i][j].setMinimumSize(new Dimension(CELLSIZE, CELLSIZE));
                myCells[i][j].setMaximumSize(new Dimension(CELLSIZE, CELLSIZE));
                myCells[i][j].setPreferredSize(new Dimension(CELLSIZE, CELLSIZE));
                myCells[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                myCells[i][j].setBounds(5 + i * (CELLSIZE + 1), 5 + j * (CELLSIZE + 1), CELLSIZE, CELLSIZE);
                myCells[i][j].setBackground(Color.WHITE);
                jpMyBoard.add(myCells[i][j]);

                // more than 100 different ActionListener? But it seems to be a must
                myCells[i][j].addActionListener(new ShipActionListener(myCells[i][j], i, j));
            }
        }

        JPanel jpTargetBoard = new JPanel();
        jpTargetBoard.setMinimumSize(new Dimension(WIDTH, 506));
        jpTargetBoard.setPreferredSize(new Dimension(WIDTH, 506));
        GridBagConstraints gbc_jpTargetBoard = new GridBagConstraints();
        gbc_jpTargetBoard.insets = new Insets(5, 0, 5, 0);
        gbc_jpTargetBoard.fill = GridBagConstraints.BOTH;
        gbc_jpTargetBoard.gridx = 1;
        gbc_jpTargetBoard.gridy = 0;
        panel.add(jpTargetBoard, gbc_jpTargetBoard);

        jpTargetBoard.setLayout(null);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                targetCells[i][j] = new JButton();
                targetCells[i][j].setMinimumSize(new Dimension(CELLSIZE, CELLSIZE));
                targetCells[i][j].setMaximumSize(new Dimension(CELLSIZE, CELLSIZE));
                targetCells[i][j].setPreferredSize(new Dimension(CELLSIZE, CELLSIZE));
                targetCells[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                targetCells[i][j].setBounds(5 + i * (CELLSIZE + 1), 5 + j * (CELLSIZE + 1), CELLSIZE, CELLSIZE);
                targetCells[i][j].setBackground(Color.WHITE);
                jpTargetBoard.add(targetCells[i][j]);

                // more than 100 different ActionListener? But it seems to be a must
                myCells[i][j].addActionListener(new ShipActionListener(myCells[i][j], i, j));
            }
        }

        JScrollPane spChat = new JScrollPane();
        spChat.setBorder(new EmptyBorder(10, 10, 10, 10));
        spChat.setMinimumSize(new Dimension(WIDTH, 200));
        spChat.setPreferredSize(new Dimension(WIDTH, 200));
        spChat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints gbc_spChat = new GridBagConstraints();
        gbc_spChat.insets = new Insets(0, 0, 0, 0);
        gbc_spChat.fill = GridBagConstraints.BOTH;
        gbc_spChat.gridx = 0;
        gbc_spChat.gridy = 1;
        panel.add(spChat, gbc_spChat);

        taChat = new JTextArea();
        taChat.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        spChat.setViewportView(taChat);
        taChat.requestFocus();
        taChat.setLineWrap(true);

        taChat.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getModifiersEx() == InputEvent.CTRL_DOWN_MASK) {
                    sendText();
                }
            }
        });

        taConsole = new JTextArea();
        taConsole.setEditable(false);
        taConsole.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        spConsole.setViewportView(taConsole);
        taConsole.setLineWrap(true);
        taConsole.setFont(new Font("courier", Font.PLAIN, 12));

        JPanel panel_btn = new JPanel();
        panel_btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc_panel_btn = new GridBagConstraints();
        gbc_panel_btn.fill = GridBagConstraints.BOTH;
        gbc_panel_btn.gridx = 0;
        gbc_panel_btn.gridy = 2;
        panel.add(panel_btn, gbc_panel_btn);
        GridBagLayout gbl_panel_btn = new GridBagLayout();
        gbl_panel_btn.columnWidths = new int[]{0, 0, 0, 0};
        gbl_panel_btn.rowHeights = new int[]{0, 0};
        gbl_panel_btn.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panel_btn.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panel_btn.setLayout(gbl_panel_btn);

        JButton btnSendMessage = new JButton("Chat");
        btnSendMessage.setPreferredSize(new Dimension(80, 25));
        btnSendMessage.setMinimumSize(new Dimension(80, 25));
        btnSendMessage.setMaximumSize(new Dimension(80, 25));
        GridBagConstraints gbc_btnSendMessage = new GridBagConstraints();
        gbc_btnSendMessage.gridx = 1;
        gbc_btnSendMessage.gridy = 0;
        panel_btn.add(btnSendMessage, gbc_btnSendMessage);

        // Finish inputting the ships
        JButton btnReady = new JButton("Ready");
        btnReady.setPreferredSize(new Dimension(80, 25));
        btnReady.setMinimumSize(new Dimension(80, 25));
        btnReady.setMaximumSize(new Dimension(80, 25));
        GridBagConstraints gbc_btnReady = new GridBagConstraints();
        gbc_btnReady.gridx = 1;
        gbc_btnReady.gridy = 1;
        panel_btn.add(btnReady, gbc_btnReady);
        btnReady.addActionListener(e -> join());

//        JButton btnStart = new JButton("Start");
//        btnStart.setPreferredSize(new Dimension(80, 25));
//        btnStart.setMinimumSize(new Dimension(80, 25));
//        btnStart.setMaximumSize(new Dimension(80, 25));
//        GridBagConstraints gbc_btnStart = new GridBagConstraints();
//        gbc_btnStart.insets = new Insets(0, 70, 0, 0);
//        gbc_btnStart.gridx = 2;
//        gbc_btnStart.gridy = 0;
//        panel_btn.add(btnStart, gbc_btnStart);

        JButton btnJoin = new JButton("Join");
        btnJoin.setPreferredSize(new Dimension(80, 25));
        btnJoin.setMinimumSize(new Dimension(80, 25));
        btnJoin.setMaximumSize(new Dimension(80, 25));
        GridBagConstraints gbc_btnJoin = new GridBagConstraints();
        gbc_btnJoin.gridx = 3;
        gbc_btnJoin.gridy = 0;
        panel_btn.add(btnJoin, gbc_btnJoin);

        JButton btnHit = new JButton("Hit");
        btnHit.setPreferredSize(new Dimension(80, 25));
        btnHit.setMinimumSize(new Dimension(80, 25));
        btnHit.setMaximumSize(new Dimension(80, 25));
        GridBagConstraints gbc_btnHit = new GridBagConstraints();
        gbc_btnHit.gridx = 4;
        gbc_btnHit.gridy = 0;
        panel_btn.add(btnHit, gbc_btnHit);

        JButton btnMine = new JButton("Mine");
        btnMine.setPreferredSize(new Dimension(80, 25));
        btnMine.setMinimumSize(new Dimension(80, 25));
        btnMine.setMaximumSize(new Dimension(80, 25));
        GridBagConstraints gbc_btnMine = new GridBagConstraints();
        gbc_btnMine.insets = new Insets(10, 0, 10, 0);
        gbc_btnMine.gridx = 1;
        gbc_btnMine.gridy = 1;
        panel_btn.add(btnMine, gbc_btnMine);

        JButton btnOpponent = new JButton("Opp");
        btnOpponent.setPreferredSize(new Dimension(80, 25));
        btnOpponent.setMinimumSize(new Dimension(80, 25));
        btnOpponent.setMaximumSize(new Dimension(80, 25));
        GridBagConstraints gbc_btnOpponent = new GridBagConstraints();
        gbc_btnOpponent.insets = new Insets(10, 70, 10, 0);
        gbc_btnOpponent.gridx = 2;
        gbc_btnOpponent.gridy = 1;
        panel_btn.add(btnOpponent, gbc_btnOpponent);
//
//        JButton btnBust = new JButton("Bust");
//        btnBust.setPreferredSize(new Dimension(80, 25));
//        btnBust.setMinimumSize(new Dimension(80, 25));
//        btnBust.setMaximumSize(new Dimension(80, 25));
//        GridBagConstraints gbc_btnBust = new GridBagConstraints();
//        gbc_btnBust.insets = new Insets(10, 0, 10, 0);
//        gbc_btnBust.gridx = 3;
//        gbc_btnBust.gridy = 1;
//        panel_btn.add(btnBust, gbc_btnBust);

        JButton btnExit = new JButton("Exit");
        btnExit.setPreferredSize(new Dimension(80, 25));
        btnExit.setMinimumSize(new Dimension(80, 25));
        btnExit.setMaximumSize(new Dimension(80, 25));
        GridBagConstraints gbc_btnExit = new GridBagConstraints();
        gbc_btnExit.insets = new Insets(10, 0, 10, 0);
        gbc_btnExit.gridx = 4;
        gbc_btnExit.gridy = 1;
        panel_btn.add(btnExit, gbc_btnExit);



        btnSendMessage.addActionListener(e -> sendText());
//        btnStart.addActionListener(e -> setGameAction(GameAction.START));
//        btnBust.addActionListener(e -> setGameAction(GameAction.BUST));
        btnMine.addActionListener(e -> setGameAction(GameAction.MINE));
        btnJoin.addActionListener(e -> setGameAction(GameAction.JOIN));
        btnOpponent.addActionListener(e -> setGameAction(GameAction.OPPONENT));
        btnHit.addActionListener(e -> setGameAction(GameAction.HIT));
        btnExit.addActionListener(e -> setGameAction(GameAction.EXIT));

        btnSendMessage.setBackground(Color.BLACK);
//        btnStart.setBackground(Color.BLACK);
//        btnBust.setBackground(Color.BLACK);
        btnMine.setBackground(Color.BLACK);
        btnJoin.setBackground(Color.BLACK);
        btnOpponent.setBackground(Color.BLACK);
        btnHit.setBackground(Color.BLACK);
        btnExit.setBackground(Color.BLACK);
        btnReady.setBackground(Color.BLACK);

        btnSendMessage.setForeground(Color.WHITE);
//        btnStart.setForeground(Color.WHITE);
//        btnBust.setForeground(Color.WHITE);
        btnMine.setForeground(Color.WHITE);
        btnJoin.setForeground(Color.WHITE);
        btnOpponent.setForeground(Color.WHITE);
        btnHit.setForeground(Color.WHITE);
        btnExit.setForeground(Color.WHITE);
        btnReady.setForeground(Color.WHITE);

        JPanel jpButton = new JPanel();
        GridBagLayout gbl_jpButton = new GridBagLayout();
        gbl_jpButton.columnWidths = new int[]{0, 0};
        gbl_jpButton.rowHeights = new int[]{0, 0, 0};
        gbl_jpButton.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_jpButton.rowWeights = new double[]{1.0, Double.MIN_VALUE, 1.0};
        jpButton.setLayout(gbl_jpButton);
        GridBagConstraints gbc_jpButton = new GridBagConstraints();
        gbc_jpButton.insets = new Insets(0, 0, 5, 0);
        gbc_jpButton.fill = GridBagConstraints.BOTH;
        gbc_jpButton.gridx = 1;
        gbc_jpButton.gridy = 2;
        panel.add(jpButton, gbc_jpButton);

        JButton btnCarrier = new JButton("Carrier");
        btnCarrier.setPreferredSize(new Dimension(120, 25));
        btnCarrier.setMinimumSize(new Dimension(120, 25));
        btnCarrier.setMaximumSize(new Dimension(120, 25));
        btnCarrier.setBackground(Color.BLACK);
        btnCarrier.setForeground(Color.WHITE);
        GridBagConstraints gbc_btnCarrier = new GridBagConstraints();
        gbc_btnCarrier.insets = new Insets(10, 0, 10, 5);
        gbc_btnCarrier.gridx = 0;
        gbc_btnCarrier.gridy = 0;
        jpButton.add(btnCarrier, gbc_btnCarrier);

        JButton btnDestroyer = new JButton("Destroyer");
        btnDestroyer.setPreferredSize(new Dimension(120, 25));
        btnDestroyer.setMinimumSize(new Dimension(120, 25));
        btnDestroyer.setMaximumSize(new Dimension(120, 25));
        btnDestroyer.setBackground(Color.BLUE);
        btnDestroyer.setForeground(Color.WHITE);
        GridBagConstraints gbc_btnDestroyer = new GridBagConstraints();
        gbc_btnDestroyer.insets = new Insets(10, 0, 10, 20);
        gbc_btnDestroyer.gridx = 1;
        gbc_btnDestroyer.gridy = 0;
        jpButton.add(btnDestroyer, gbc_btnDestroyer);

        JButton btnBattleship = new JButton("Battleship");
        btnBattleship.setPreferredSize(new Dimension(120, 25));
        btnBattleship.setMinimumSize(new Dimension(120, 25));
        btnBattleship.setMaximumSize(new Dimension(120, 25));
        btnBattleship.setBackground(Color.GREEN);
        btnBattleship.setForeground(Color.BLACK);
        GridBagConstraints gbc_btnBattleship = new GridBagConstraints();
        gbc_btnBattleship.insets = new Insets(10, 0, 10, 20);
        gbc_btnBattleship.gridx = 1;
        gbc_btnBattleship.gridy = 1;
        jpButton.add(btnBattleship, gbc_btnBattleship);

        JButton btnCruiser = new JButton("Cruiser");
        btnCruiser.setPreferredSize(new Dimension(120, 25));
        btnCruiser.setMinimumSize(new Dimension(120, 25));
        btnCruiser.setMaximumSize(new Dimension(120, 25));
        btnCruiser.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc_btnCruiser = new GridBagConstraints();
        gbc_btnCruiser.insets = new Insets(10, 0, 10, 5);
        gbc_btnCruiser.gridx = 0;
        gbc_btnCruiser.gridy = 1;
        jpButton.add(btnCruiser, gbc_btnCruiser);

        JButton btnSubmarine  = new JButton("Submarine");
        btnSubmarine.setPreferredSize(new Dimension(120, 25));
        btnSubmarine.setMinimumSize(new Dimension(120, 25));
        btnSubmarine.setMaximumSize(new Dimension(120, 25));
        btnSubmarine.setBackground(Color.cyan);
        GridBagConstraints gbc_btnSubmarine = new GridBagConstraints();
        gbc_btnSubmarine.insets = new Insets(10, 0, 10, 5);
        gbc_btnSubmarine.gridx = 0;
        gbc_btnSubmarine.gridy = 2;
        jpButton.add(btnSubmarine, gbc_btnSubmarine);

        JButton btnRemove  = new JButton("Remove");
        btnRemove.setPreferredSize(new Dimension(120, 25));
        btnRemove.setMinimumSize(new Dimension(120, 25));
        btnRemove.setMaximumSize(new Dimension(120, 25));
        btnRemove.setBackground(Color.WHITE);
        btnRemove.setForeground(Color.BLACK);
        GridBagConstraints gbc_btnRemove = new GridBagConstraints();
        gbc_btnRemove.insets = new Insets(10, 0, 10, 20);
        gbc_btnRemove.gridx = 1;
        gbc_btnRemove.gridy = 2;
        jpButton.add(btnRemove, gbc_btnRemove);

        btnSubmarine.addActionListener(e -> setCurrentShip(shipColor.get("Submarine")));
        btnCarrier.addActionListener(e -> setCurrentShip(shipColor.get("Carrier")));
        btnDestroyer.addActionListener(e -> setCurrentShip(shipColor.get("Destroyer")));
        btnBattleship.addActionListener(e -> setCurrentShip(shipColor.get("Battleship")));
        btnCruiser.addActionListener(e -> setCurrentShip(shipColor.get("Cruiser")));
        btnRemove.addActionListener(e -> setCurrentShip(shipColor.get("Empty")));

//        inputUsername();

        this.setLocation(100, 100);
        this.setTitle("Battleship");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel.setVisible(true);
        this.setVisible(true);
        this.pack();
    }

    // make invalid ship input invalid (and warning)
    private boolean validateBoard() {
    // validate adjacent

    // validate line

    // validate size

        return true;
    }

    // finished ship inputting, and ready to start a game
    public void join() {
        enableInput(false);
    }

    public void enableInput(boolean isEnable) {
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                myCells[i][j].setEnabled(isEnable);
            }
        }
    }
    
    private void AssignShip() {
        shipColor.put("Battleship", Color.GREEN);
        shipColor.put("Submarine", Color.cyan);
        shipColor.put("Cruiser", Color.LIGHT_GRAY);
        shipColor.put("Carrier", Color.BLACK);
        shipColor.put("Destroyer", Color.BLUE);
        shipColor.put("Empty", Color.WHITE);

        shipID.put(4, "Battleship");
        shipID.put(2, "Submarine");
        shipID.put(3, "Cruiser");
        shipID.put(5, "Carrier");
        shipID.put(1, "Destroyer");
        shipID.put(0, "Empty");
    }

    void showMsg(String msg) {
        taConsole.append(msg);
        taConsole.setCaretPosition(taConsole.getDocument().getLength());
    }

    void showMsgln(String msg) {
        taConsole.append(msg + "\n");
        taConsole.setCaretPosition(taConsole.getDocument().getLength());
    }

    public static Color getCurrentShip() {
        return currentShip;
    }

    public void setCurrentShip(Color currentShip) {
        this.currentShip = currentShip;
        System.out.println(getCurrentShip());
    }

    String getMsg() {
        return msg;
    }

    void setMsg(String msg) {
        this.msg = msg;
    }

    private void sendText() {
        if (taChat.getText() != null) {
            setMsg(taChat.getText());
            taChat.setText("");
        }
    }

    GameAction getGameAction() {
        return gameAction;
    }

    void setGameAction(GameAction gameAction) {
        this.gameAction = gameAction;
    }

    String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private void inputUsername() {
        setUsername(JOptionPane.showInputDialog(null, "Please enter the username:", "Bo"));
    }

    void exitWarning() {
        JOptionPane.showMessageDialog(this, "Disconnected. The program will exit.");
    }

    public int[] inputCell() {
        String rawData = JOptionPane.showInputDialog(this, "Please enter the cell number");
        String[] data = rawData.split(" ");
        int[] cells = new int[2];
        try {
            cells[0] = Integer.parseInt(data[0]);
            cells[1] = Integer.parseInt(data[1]);
            if (cells[0] < 0 || cells[0] > 9 || cells[1] < 0 || cells[1] > 9) {
                JOptionPane.showConfirmDialog(this, "Invalid input");
            }
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(this, "Invalid input");
        }

        return cells;
    }

    public static int[][] getMyBoard() {
        return myBoard;
    }

    public static void setMyBoard(int[][] myBoard) {
        TestGUIV2.myBoard = myBoard;
    }

    public static void changeMyBoard(int value, int row, int col) {
        myBoard[row][col] = value;
    }

    private class ShipActionListener implements ActionListener {
        private JButton jb;
        private int col, row;

        public ShipActionListener(JButton jb, int row, int col) {
            this.jb = jb;
            this.col = col;
            this.row = row;
        }

        public void actionPerformed(ActionEvent e) {
            setShip(this.jb, TestGUIV2.getCurrentShip());
        }

        private void setShip(JButton jb, Color color) {
            jb.setBackground(color);
            System.out.println(color.toString());

            if (color.equals(Color.BLUE)) {
                TestGUIV2.changeMyBoard(1, row, col);
            } else if (color.equals(Color.cyan)) {
                TestGUIV2.changeMyBoard(2, row, col);
            } else if (color.equals(Color.LIGHT_GRAY)) {
                TestGUIV2.changeMyBoard(3, row, col);
            } else if (color.equals(Color.GREEN)) {
                TestGUIV2.changeMyBoard(4, row, col);
            } else if (color.equals(Color.BLACK)) {
                TestGUIV2.changeMyBoard(5, row, col);
            } else if (color.equals(Color.WHITE)) {
                TestGUIV2.changeMyBoard(0, row, col);
            }

//            shipColor.put("Battleship", Color.DARK_GRAY);
//            shipColor.put("Submarine", Color.cyan);
//            shipColor.put("Cruiser", Color.LIGHT_GRAY);
//            shipColor.put("Carrier", Color.BLACK);
//            shipColor.put("Destroyer", Color.BLUE);
//            shipColor.put("Empty", Color.WHITE);
        }
    }

    public static void main (String[] args) {
        TestGUIV2 tg2 = new TestGUIV2();
    }
}