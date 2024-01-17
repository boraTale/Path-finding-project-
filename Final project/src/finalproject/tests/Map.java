package tests;

import java.util.ArrayList;
import java.util.LinkedList;

import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.*;

public class Map {
    private Tile start;
    private Tile end;
    private Tile[][] grid;

    public Tile getStart() {
        return start;
    }

    public Tile getEnd() {
        return end;
    }

    public Tile[][] getGrid() {
        return grid;
    }

    public String getCoordinates(Tile tile) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j].equals(tile)) {
                    return "(" + i + ", " + j + ")";
                }
            }
        }
        return null;
    }

    public ArrayList<Tile> getTiles() {
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        for (Tile[] row : grid) {
            for (Tile tile : row) {
                tiles.add(tile);
            }
        }
        return tiles;
    }

    public int getNumberOfWalkableTiles() {
        int mountains = 0;
        for (Tile[] row : grid) {
            for (Tile tile : row) {
                if (tile instanceof MountainTile) {
                    mountains++;
                }
            }
        }
        return grid.length * grid[0].length - mountains;
    }

    public LinkedList<Tile> getWaypoints(int[][] wps) {
        LinkedList<Tile> waypoints = new LinkedList<Tile>();
        for (int[] coordinates : wps) {
            waypoints.addLast(grid[coordinates[1]][coordinates[0]]);
        }
        return waypoints;
    }

    /**
     * @param map The 2D array of valid characters that make up the map.
     *            <ul>
     *                <li>s = Start, a facility tile</ly>
     *                <li>e = End, a facility tile</ly>
     *                <li>d = Desert</ly>
     *                <li>f = Facility</ly>
     *                <li>p = Plain</ly>
     *                <li>r = Zombie Ruin</ly>
     *                <li>M = Metro</ly>
     *                <li>m/anything else = Mountain</ly>
     *            </ul>
     */
    public Map(char[][] map) {
        int length = map.length;
        int width = map[0].length;
        grid = new Tile[length][width];
        ArrayList<Tile> metroNeighbours = new ArrayList<Tile>();
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < width; y++) {
                Tile tile;
                switch (map[x][y]) {
                    case 's':
                        tile = new FacilityTile();
                        tile.isStart = true;
                        start = tile;
                        break;
                    case 'e':
                        tile = new FacilityTile();
                        tile.isDestination = true;
                        end = tile;
                        break;
                    case 'd':
                        tile = new DesertTile();
                        break;
                    case 'f':
                        tile = new FacilityTile();
                        break;
                    case 'p':
                        tile = new PlainTile();
                        break;
                    case 'r':
                        tile = new ZombieInfectedRuinTile();
                        break;
                    case 'M':
                        tile = new MetroTile();
                        metroNeighbours.add(tile);
                        break;

                    default:
                        tile = new MountainTile();
                }
                grid[x][y] = tile;
                tile.xCoord = x;
                tile.yCoord = y;
            }
        }

        // add neighbors
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < width; y++) {
                Tile tile = grid[x][y];
                if (x - 1 >= 0)
                tile.addNeighbor(grid[x - 1][y]); // left neighbor
                if (y - 1 >= 0) {
                    tile.addNeighbor(grid[x][y - 1]); // top neighbor
                }
                if (tile.type == TileType.Metro) {
                    for (int j = 0; j < metroNeighbours.size(); j++) {
                        if (metroNeighbours.get(j) != tile) {
                            ((MetroTile) tile).fixMetro(metroNeighbours.get(j));
                            if (((MetroTile) tile).metroTimeCost < 100 && ((MetroTile) tile).metroDistanceCost < 100) {
                                tile.addNeighbor(metroNeighbours.get(j));
                            }
                        }
                    }
                }
            }
        }
    }

    public static char[][] map1 = {
            { 's', 'p', 'p' },
            { 'd', 'm', 'd' },
            { 'p', 'p', 'e' },
    };

    public static char[][] map2 = {
            { 's', 'd', 'd', 'p', 'm' },
            { 'd', 'd', 'm', 'p', 'm' },
            { 'd', 'p', 'd', 'd', 'm' },
            { 'p', 'd', 'p', 'd', 'p' },
            { 'd', 'p', 'p', 'p', 'e' },
    };

    public static char[][] map3 = {
            { 's', 'd', 'd', 'M', 'd' },
            { 'd', 'd', 'm', 'p', 'p' },
            { 'p', 'm', 'd', 'd', 'p' },
            { 'm', 'M', 'p', 'd', 'd' },
            { 'p', 'd', 'p', 'p', 'e' },
    };

    public static char[][] map4 = {
            { 'p', 'p', 'r', 'r', 'x', 'x', 'x', 'x', 'x' },
            { 'f', 's', 'r', 'r', 'd', 'd', 'p', 'p', 'p' },
            { 'd', 'p', 'p', 'x', 'x', 'p', 'x', 'd', 'r' },
            { 'd', 'd', 'p', 'p', 'p', 'r', 'r', 'd', 'p' },
            { 'f', 'd', 'p', 'x', 'x', 'r', 'x', 'r', 'e' },
    };
}
