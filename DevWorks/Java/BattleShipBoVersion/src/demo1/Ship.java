package demo1;

public class Ship {

    public enum ShipType implements GridType {
        Destroyer, Submarine, Cruiser, Battleship, Carrier
    }
    private int ShipId;
    private int lifePoints;
    private int maxLife;
    private boolean isSunk;
    private ShipType shipType;

    public Ship(ShipType st) {

        initializeShip(st);
    }

    private void initializeShip(ShipType st) {
        setShipType(st);
        setSunk(false);

        switch (st) {
            case Destroyer:
                setMaxLife(2);
                setShipId(1);
                break;
            case Submarine:
                setMaxLife(3);
                setShipId(2);
                break;
            case Cruiser:
                setMaxLife(3);
                setShipId(3);
                break;
            case Battleship:
                setMaxLife(4);
                setShipId(4);
                break;
            case Carrier:
                setMaxLife(5);
                setShipId(5);
                break;
        }

        setLifePoints(getMaxLife());
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        if (lifePoints > getMaxLife() || lifePoints < 0) {
            throw new IllegalArgumentException("Invalid life points.");
        }
        this.lifePoints = lifePoints;
    }

    public int getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(int maxLife) {
        if (maxLife > 5 || maxLife < 0) {
            throw new IllegalArgumentException("All ships have a maximum life ranging from 2 to 5.");
        }
        this.maxLife = maxLife;
    }

    public boolean isSunk() {
        return isSunk;
    }

    public void setSunk(boolean sunk) {
        isSunk = sunk;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType st) {
        if (st == null) {
            throw new IllegalArgumentException("Every ship must have a type.");
        }
        shipType = st;
    }

    public int getShipId() {
        return ShipId;
    }

    public void setShipId(int shipId) {
        ShipId = shipId;
    }

    public static int[] getShipAttributes(ShipType st) {
        switch (st) {
            case Destroyer:
                return new int[]{1, 2};

            case Submarine:
                return new int[]{2, 3};

            case Cruiser:
                return new int[]{3, 3};

            case Battleship:
                return new int[]{4, 4};

            case Carrier:
                return new int[]{5, 5};

            default:
                throw new IllegalArgumentException("Invalid ship type.");
        }
    }
}
