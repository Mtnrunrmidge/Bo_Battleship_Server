public class Game {
    private int gameID;

    public Game(Player player1, Player player2, int gameID){
        setGameID(gameID);
    }

    public int getGameID(){
        return this.gameID;
    }

    public void setGameID(int gameID){
        this.gameID = gameID;
    }

}
