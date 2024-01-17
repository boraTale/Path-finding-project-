package tests;

import static org.junit.jupiter.api.Assertions.*;
import static tests.PathTools.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.Test;

import finalproject.*;
import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.*;

public class Level7Tester {
    private final double METRO_COMMUTE_FACTOR = 0.2;

    @Test
    public void fixMetroCorrectCosts() {
        MetroTile tile1 = new MetroTile();
        MetroTile tile2 = new MetroTile();
        tile1.xCoord = 10;
        tile1.yCoord = 6;
        tile2.xCoord = 3;
        tile2.yCoord = 7;
        tile1.fixMetro(tile2);
        tile2.fixMetro(tile1);
        double timeCost = 8 * METRO_COMMUTE_FACTOR;
        double distanceCost = 8 / METRO_COMMUTE_FACTOR;
        assertEquals(timeCost, tile1.metroTimeCost);
        assertEquals(distanceCost, tile1.metroDistanceCost);
        assertEquals(timeCost, tile2.metroTimeCost);
        assertEquals(distanceCost, tile2.metroDistanceCost);
    }

    @Test
    public void metrosConnectInShortestPathMap3() {
        testMetrosConnect(Map.map3, PathfinderType.SHORTEST);
    }

    @Test
    public void metrosConnectInFastestPathMap3() {
        testMetrosConnect(Map.map3, PathfinderType.FASTEST);
    }

    // Not sure if this is necessary
    @Test
    public void metrosConnectInSafestShortestPathMap3() {
        testMetrosConnect(Map.map3, PathfinderType.SAFEST_SHORTEST, 27);
    }

    private void testMetrosConnect(char[][] charMap, PathfinderType pathfinderType, int health) {
        Map map = new Map(charMap);
        PathFindingService pathfinder = makePathfinder(map.getStart(), pathfinderType, health);
        try {
            Field graphField = PathFindingService.class.getDeclaredField("g");
            graphField.setAccessible(true);
            int connections = 0;
            for (Graph.Edge edge : ((Graph) graphField.get(pathfinder)).getAllEdges()) {
                if (edge.getStart().type == TileType.Metro && edge.getEnd().type == TileType.Metro) {
                    connections++;
                }
            }
            assertEquals(2, connections, "There should be an edge connecting each metro station in both directions");
        } catch (Exception e) {
            fail(e);
        }
    }
    
    private void testMetrosConnect(char[][] charMap, PathfinderType pathfinderType) {
        if (pathfinderType != PathfinderType.SAFEST_SHORTEST) {
            testMetrosConnect(charMap, pathfinderType, 0);
        } else {
            fail("Safest shortest pathfinder passed into wrong method");
        }
    }

    @Test
    public void pdfExample() {
        Map map = new Map(Map.map3);
        PathFindingService pathfinder = new FastestPath(map.getStart());
        int[][] expected = new int[][] {
            {0, 0},
            {1, 0},
            {2, 0},
            {3, 0},
            {1, 3},
            {2, 3},
            {2, 4},
            {3, 4},
            {4, 4}
        };
        ArrayList<Tile> actual = pathfinder.findPath(map.getStart());
        matchPath(map, expected, actual, PathfinderType.FASTEST);
    }
}
