import java.util.*;

public class Fleet {
    public static enum shipsInFleet { BATTLESHIP, CRUISER, CARRIER, DESTROYER, SUBMARINE };
    private final ArrayList<Ship> ships = new ArrayList<Ship>(5);
    private int shipsRemaining;

    public Fleet(){
        setShipRemaining(ships.size());

    }

    //get methods
    public int getShipsRemaining(){

    }

    public int getShipHitCount(Ship ship){

    }

    public ArrayList<Ship> getFleet(){
        return this.ships;
    }

    //set methods
    public void setShipRemaining(int shipsRemainingInFleet){
        this.shipsRemaining = shipsRemainingInFleet;
    }

    public void setShipHitCount(){
    }

    //other methods
    public void shipHit(Ship ship){

    }

    /**
     * Remove ship from fleet when its been sunk
     * @param sunkenShip
     */
    public void shipSunk(Ship sunkenShip){
        removeShipFromFleetByShipID(sunkenShip.shipID);
    }

    public void removeShipFromFleetByShipID(int shipID){

        int index = shipID - 1;
        this.ships.remove(index);
    }

    public void createNewFleet(){
        for(int shipID = 1; shipID <= shipIDs.size(); shipID++){
            this.ships.add(new Ship.getShipByID(shipID));
        }

    }

}
