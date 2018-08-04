public class Player {

    private Gameboard gameboard;
    private GuessesByPlayer guessesByPlayer;
    private Fleet fleet;
    private String username;
    private String ipAddress;
    private bool isTurn;

    /**
     * Constructor for player object
     */
    public Player(String username, Gameboard gameboard, GuessesByPlayer guessesBoard, Fleet fleet, bool isTurn){
        setUsername(username);
        setGameboard(gameboard);
        setGuessesByPlayerBoard(guessesBoard);
        setFleet(fleet);
        setIsTurn(isTurn);
    }

    //getters

    /**
     * Gets the username for the player
     * @return
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * gets the gameboard for the player
     * @return
     */
    public Gameboard getGameboard(){
        return this.gameboard;
    }

    /**
     * gets the Guesses by player board
     * @return
     */
    public GuessesByPlayer getGuessesByPlayerBoard(){
        return this.guessesByPlayer;
    }

    /**
     * Gets the fleet for the player
     * @return
     */
    public Fleet getFleet(){
        return this.fleet;
    }

    /**
     * Gets the true/false if its the player's turn.
     * @return
     */
    public bool getIsTurn(){
        return this.isTurn;
    }

    //setters

    /**
     * Sets the username for the player
     * @param username
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Gameboard has the placement of ships by the player
     * @param gameboard
     */
    public void setGameboard(Gameboard gameboard){
        this.gameboard = gameboard;
    }

    /**
     * Set guesses By Player to keep track of the guesses the player has made throughout the game
     * @param guessesByPlayerBoard
     */
    public void setGuessesByPlayerBoard(GuessesByPlayer guessesByPlayerBoard){
        this.guessesByPlayer = guessesByPlayerBoard;
    }

    /**
     * Sets the fleet for the player
     * @param fleet
     */
    public void setFleet(Fleet fleet){
        this.fleet = fleet;
    }

    /**
     * Sets whether or not it is the player's turn
     * @param isTurn
     */
    public void setIsTurn(bool isTurn){
        this.isTurn = isTurn;
    }

}
