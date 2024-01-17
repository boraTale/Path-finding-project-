package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import finalproject.FastestPath;
import finalproject.PathFindingService;
import finalproject.SafestShortestPath;
import finalproject.ShortestPath;
import finalproject.system.Tile;

public class PathTools {
    public static PathFindingService makePathfinder(Tile start, PathfinderType pathfinderType, int health) {
        switch (pathfinderType) {
            case SHORTEST:
                return new ShortestPath(start);
            case FASTEST:
                return new FastestPath(start);
            case SAFEST_SHORTEST:
                return new SafestShortestPath(start, health);
            default:
                return new ShortestPath(start);
        }
    }

    public static PathFindingService makePathfinder(Tile start, PathfinderType pathfinderType) {
        if (pathfinderType != PathfinderType.SAFEST_SHORTEST) {
            return makePathfinder(start, pathfinderType, 0);
        } else {
            fail("Safest shortest pathfinder passed into wrong method");
            return null;
        }
    }
    
    public enum PathfinderType {
        SHORTEST,
        FASTEST,
        SAFEST_SHORTEST
    }

    public static void matchPath(Map map, int[][] expectedPath, ArrayList<Tile> actualPath, PathfinderType pathfinderType) {
        Tile[][] grid = map.getGrid();
        double expectedWeight = 0;
        double actualWeight = 0;
        assertEquals(grid[expectedPath[0][1]][expectedPath[0][0]], actualPath.get(0), "Start tile should be the same");
        boolean pathEquals = true;
        for (int i = 1; i < expectedPath.length; i++) {
            Tile expected = grid[expectedPath[i][1]][expectedPath[i][0]];
            Tile actual = actualPath.get(i);
            switch (pathfinderType) {
                case SHORTEST:
                    expectedWeight += expected.distanceCost;
                    actualWeight += actual.distanceCost;
                    break;
                case FASTEST:
                    expectedWeight += expected.timeCost;
                    actualWeight += actual.timeCost;
                    break;
                case SAFEST_SHORTEST:
                    expectedWeight += expected.damageCost;
                    actualWeight += actual.damageCost;
                    break;
                default:
                    break;
            }
            pathEquals &= expected.equals(actual);
        }
        if (!pathEquals) {
            assertEquals(expectedWeight, actualWeight, "Neither the exact path nor the weight of path matched");
        }
    }
}
