package demo1;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class TestGUI extends JFrame{

    private JTextArea taChat;
    private JTextArea taConsole;
    private String msg = "";
    private String username = "";
    private GameAction gameAction;
    public enum GameAction {HIT, JOIN, EXIT, MINE, OPPONENT}
    private int[][] myBoard;

    TestGUI() {
        setMinimumSize(new Dimension(700, 700));

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{0, 0};
        gbl_panel.rowHeights = new int[]{0, 0, 0};
        gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE, 1.0};
        panel.setLayout(gbl_panel);

        JScrollPane spConsole = new JScrollPane();
        spConsole.setBorder(new EmptyBorder(10, 20, 5, 20));
        spConsole.setMinimumSize(new Dimension(650, 450));
        spConsole.setPreferredSize(new Dimension(650, 450));
        spConsole.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints gbc_spConsole = new GridBagConstraints();
        gbc_spConsole.insets = new Insets(0, 0, 5, 0);
        gbc_spConsole.fill = GridBagConstraints.BOTH;
        gbc_spConsole.gridx = 0;
        gbc_spConsole.gridy = 0;
        panel.add(spConsole, gbc_spConsole);

        JScrollPane spChat = new JScrollPane();
        spChat.setBorder(new EmptyBorder(5, 20, 10, 20));
        spChat.setMinimumSize(new Dimension(650, 150));
        spChat.setPreferredSize(new Dimension(650, 150));
        spChat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints gbc_spChat = new GridBagConstraints();
        gbc_spChat.insets = new Insets(0, 0, 5, 0);
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

        btnSendMessage.setForeground(Color.WHITE);
//        btnStart.setForeground(Color.WHITE);
//        btnBust.setForeground(Color.WHITE);
        btnMine.setForeground(Color.WHITE);
        btnJoin.setForeground(Color.WHITE);
        btnOpponent.setForeground(Color.WHITE);
        btnHit.setForeground(Color.WHITE);
        btnExit.setForeground(Color.WHITE);

        inputUsername();

        this.setLocation(100, 100);
        this.setTitle("Battleship");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel.setVisible(true);
        this.setVisible(true);
        this.pack();
    }

    void showMsg(String msg) {
        taConsole.append(msg);
        taConsole.setCaretPosition(taConsole.getDocument().getLength());
    }

    void showMsgln(String msg) {
        taConsole.append(msg + "\n");
        taConsole.setCaretPosition(taConsole.getDocument().getLength());
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

    public int[][] getMyBoard() {
        return myBoard;
    }

    public void setMyBoard(int[][] myBoard) {
        this.myBoard = myBoard;
    }

    public void changeMyBoard(int value, int row, int col) {
        this.myBoard[row][col] = value;
    }

    public static void main (String[] args) {
        new TestGUI();
    }
}