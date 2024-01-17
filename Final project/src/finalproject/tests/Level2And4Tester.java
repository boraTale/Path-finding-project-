package tests;

import static org.junit.jupiter.api.Assertions.*;
import static tests.PathTools.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.Test;

import finalproject.*;
import finalproject.system.Tile;

public class Level2And4Tester {
    @Test
    public void graphConstructorMakesNoEdges() {
        Graph graph = new Graph(new Map(Map.map1).getTiles());
        assertEquals(0, graph.getAllEdges().size(), "The graph constructor should make no edges");
    }

    @Test
    public void addEdgeAddsEdge() {
        char[][] charMap = new char[][] { { 's', 'e' } };
        double weight = 10;
        Map map = new Map(charMap);
        Graph graph = new Graph(map.getTiles());
        graph.addEdge(map.getStart(), map.getEnd(), weight);
        assertEquals(1, graph.getAllEdges().size(), "There should be one edge in the graph");
        Graph.Edge edge = graph.getAllEdges().get(0);
        try {
            Field originField = Graph.Edge.class.getDeclaredField("origin");
            Field destinationField = Graph.Edge.class.getDeclaredField("destination");
            Field weightField = Graph.Edge.class.getDeclaredField("weight");
            originField.setAccessible(true);
            destinationField.setAccessible(true);
            weightField.setAccessible(true);
            assertEquals(map.getStart(), (Tile) originField.get(edge), "Origin of edge should be correct");
            assertEquals(map.getEnd(), (Tile) destinationField.get(edge), "Destination of edge should be correct");
            assertEquals(weight, weightField.getDouble(edge), "Weight of edge should be correct");
        } catch (Exception e) {
            fail(e);
        }
    }

    //#region Pathfinder tests
    //#region Generates correct edges
    @Test
    public void shortestPathGeneratesEdgesMap1() {
        testPathfinderConstructorMakesEdges(Map.map1, 16, PathfinderType.SHORTEST);
    }

    @Test
    public void fastestPathGeneratesEdgesMap1() {
        testPathfinderConstructorMakesEdges(Map.map1, 16, PathfinderType.FASTEST);
    }

    @Test
    public void safestShortestPathGeneratesEdgesMap1() {
        testPathfinderConstructorMakesEdges(Map.map1, 16, PathfinderType.SAFEST_SHORTEST, 30);
    }

    private void testPathfinderConstructorMakesEdges(char[][] charMap, int expected, PathfinderType pathfinderType, int health) {
        Map map = new Map(charMap);
        PathFindingService pathfinder = makePathfinder(map.getStart(), pathfinderType, health);
        try {
            Field graphField = PathFindingService.class.getDeclaredField("g");
            graphField.setAccessible(true);
            Graph graph = (Graph) graphField.get(pathfinder);
            assertEquals(expected, graph.getAllEdges().size(), "Should have the correct number of edges");
        } catch (Exception e) {
            fail(e);
        }
    }

    private void testPathfinderConstructorMakesEdges(char[][] charMap, int expected, PathfinderType pathfinderType) {
        if (pathfinderType != PathfinderType.SAFEST_SHORTEST) {
            testPathfinderConstructorMakesEdges(charMap, expected, pathfinderType, 0);
        } else {
            fail();
        }
    }
    //#endregion

    // #region Compute path costs
    @Test
    public void computePathCostDistance() {
        char[][] charMap = { { 's', 'f', 'e' } };
        int[][] path = new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 } };
        testComputePathCost(charMap, path, 2, PathfinderType.SHORTEST);
    }

    @Test
    public void computePathCostTime() {
        char[][] charMap = { { 's', 'f', 'e' } };
        int[][] path = new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 } };
        testComputePathCost(charMap, path, 4, PathfinderType.FASTEST);
    }

    private void testComputePathCost(char[][] charMap, int[][] path, double expected, PathfinderType pathfinderType, int health) {
        Map map = new Map(charMap);
        Tile[][] grid = map.getGrid();
        PathFindingService pathfinder = makePathfinder(map.getStart(), pathfinderType, health);
        ArrayList<Tile> list = new ArrayList<Tile>();
        for (int[] pair : path) {
            list.add(grid[pair[0]][pair[1]]);
        }
        try {
            Field graphField = PathFindingService.class.getDeclaredField("g");
            graphField.setAccessible(true);
            Graph graph = (Graph) graphField.get(pathfinder);
            assertEquals(expected, graph.computePathCost(list), "Should compute distance correctly");
        } catch (Exception e) {
            fail(e);
        }
    }

    private void testComputePathCost(char[][] charMap, int[][] path, double expected, PathfinderType pathfinderType) {
        if (pathfinderType != PathfinderType.SAFEST_SHORTEST) {
            testComputePathCost(charMap, path, expected, pathfinderType, 0);
        } else {
            fail();
        }
    }
    // #endregion
    //#endregion
    
    // #region Get neighbors
    @Test
    public void getWalkableNeighborsMap1() {
        testGetWalkableNeighbors(Map.map1);
    }

    @Test
    public void getWalkableNeighborsMap2() {
        testGetWalkableNeighbors(Map.map2);
    }

    @Test
    public void getWalkableNeighborsMap3() {
        testGetWalkableNeighbors(Map.map3);
    }

    @Test
    public void getWalkableNeighborsMap4() {
        testGetWalkableNeighbors(Map.map4);
    }

    private void testGetWalkableNeighbors(char[][] charMap) {
        Map map = new Map(charMap);
        ArrayList<Tile> tiles = map.getTiles();
        PathFindingService pathfinder = new ShortestPath(map.getStart());
        for (Tile tile : tiles) {
            if (tile.isWalkable()) {
                ArrayList<Tile> walkableNeighbors = new ArrayList<Tile>(tile.neighbors);
                walkableNeighbors.removeIf(t -> !t.isWalkable());
                try {
                    Field graphField = PathFindingService.class.getDeclaredField("g");
                    graphField.setAccessible(true);
                    ArrayList<Tile> graphNeighbors = ((Graph) graphField.get(pathfinder)).getNeighbors(tile);
                    assertEquals(walkableNeighbors.size(), graphNeighbors.size(), "Tiles should have the correct number of walkable neighbors");
                    for (Tile neighbor : walkableNeighbors) {
                        assertTrue(graphNeighbors.contains(neighbor), "Tiles should have the correct list of neighbors");
                    }
                } catch (Exception e) {
                    fail(e);
                }
            }
        }
    }
    // #endregion

    // #region findPath returns a path
    @Test
    public void findPathReturnsPathMap1() {
        testFindPathReturnsPath(Map.map1);
    }

    @Test
    public void findPathReturnsPathMap2() {
        testFindPathReturnsPath(Map.map2);
    }

    @Test
    public void findPathReturnsPathMap3() {
        testFindPathReturnsPath(Map.map3);
    }

    @Test
    public void findPathReturnsPathMap4() {
        testFindPathReturnsPath(Map.map4);
    }

    private void testFindPathReturnsPath(char[][] charMap) {
        Map map = new Map(charMap);
        PathFindingService pathfinder = new ShortestPath(map.getStart());
        ArrayList<Tile> path = pathfinder.findPath(map.getStart());
        assertNotNull(path, "Path should not be null");
        assertNotEquals(path.size(), 0, "Path should contain more than zero tiles");
    }
    // #endregion

    // #region findPath start and end tiles
    @Test
    public void findPathStartAndEndMap1() {
        testFindPathStartAndEnd(Map.map1);
    }

    @Test
    public void findPathStartAndEndMap2() {
        testFindPathStartAndEnd(Map.map2);
    }

    @Test
    public void findPathStartAndEndMap3() {
        testFindPathStartAndEnd(Map.map3);
    }

    @Test
    public void findPathStartAndEndMap4() {
        testFindPathStartAndEnd(Map.map4);
    }

    private void testFindPathStartAndEnd(char[][] charMap) {
        Map map = new Map(charMap);
        PathFindingService pathfinder = new ShortestPath(map.getStart());
        ArrayList<Tile> path = pathfinder.findPath(map.getStart());
        assertTrue(path.get(0).isStart, "Tile at beginning of path should be the start tile");
        assertTrue(path.get(path.size() - 1).isDestination, "Tile at end of path should be the destination tile");
    }
    // #endregion

    //#region PDF examples
    @Test
    public void shortesPathPDFExampleMap1() {
        int[][] path = new int[][] {
            { 0, 0 },
            { 0, 1 },
            { 0, 2 },
            { 1, 2 },
            { 2, 2 }
        };
        testShortestPathMatches(Map.map1, path);
    }

    @Test
    public void shortestPathPDFExampleMap2() {
        int[][] path = new int[][] {
            { 0, 0 },
            { 1, 0 },
            { 1, 1 },
            { 1, 2 },
            { 2, 2 },
            { 3, 2 },
            { 3, 3 },
            { 3, 4 },
            { 4, 4 }
        };
        testShortestPathMatches(Map.map2, path);
    }

    private void testShortestPathMatches(char[][] charMap, int[][] expectedPath) {
        Map map = new Map(charMap);
        PathFindingService pathfinder = new ShortestPath(map.getStart());
        ArrayList<Tile> path = pathfinder.findPath(map.getStart());
        matchPath(map, expectedPath, path, PathfinderType.SHORTEST);
    }
    //#endregion
}
