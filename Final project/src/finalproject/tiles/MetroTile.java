package finalproject.tiles;

import finalproject.system.Tile;
import finalproject.system.TileType;

public class MetroTile extends Tile {
	public double metroTimeCost = 100;
	public double metroDistanceCost = 100;
	public double metroCommuteFactor = 0.2;


    public MetroTile() {
        super(1, 1, 2);
        this.type = TileType.Metro;
        fixMetro(this);
    }

    public void fixMetro(Tile node) {
        if (node instanceof MetroTile) {
            MetroTile metroNode = (MetroTile) node;
            int metroDistance = Math.abs(this.xCoord - metroNode.xCoord) + Math.abs(this.yCoord - metroNode.yCoord);

            this.metroTimeCost = metroDistance * metroCommuteFactor;
            this.metroDistanceCost = metroDistance / metroCommuteFactor;
        }
    }
}
