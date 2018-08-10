package demo1;

import demo1.message.Message;
import demo1.message.MessageFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TestingClient {

    public static void main(String[] args) throws IOException {

        Socket s;

        s = new Socket("127.0.0.1", 8080);

        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

        Thread receiveMsg = new Thread(() -> {
            try {
                while (!Thread.interrupted() && s.isConnected() && !s.isClosed()) {
                    Message msg = (Message) ois.readObject();

                    System.out.println("msg " + msg.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        receiveMsg.start();

//        writeMessage(oos, MessageFactory.getReadyMessage());

        writeMessage(oos, MessageFactory.getLoginMessage("ccc"));

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

        writeMessage(oos, MessageFactory.getReadyMessage(gt));
    }

    private static void writeMessage(ObjectOutputStream oos, Message msg) throws IOException {
        oos.writeObject(msg);
        oos.flush();
    }
}
