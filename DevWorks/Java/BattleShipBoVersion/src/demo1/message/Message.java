package demo1.message;

import java.io.Serializable;

public abstract class Message implements Serializable {

    private String username;
    public enum MessageType {LOGIN, SYSTEM, CHAT, GAME_ACTION, RESULT}
    public abstract MessageType getMessageType();
    public String module = "battleship1";

    public void setUsername(String username) {
//        if (username == null) {
//            throw new IllegalArgumentException("Username cannot be null");
//        }
//        if (this.username != null) {
//            throw new IllegalStateException("Username cannot be changed.");
//        }

        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    protected Message(String username) {
        setUsername(username);
    }

//    protected Message() {
//        setUsername(null);
//    }

    public String getModule() {
        return module;
    }

    @Override
    public String toString() {
        return "Message{" +
                "username='" + username + '\'' +
                ", module='" + module + '\'' +
                '}';
    }
}
