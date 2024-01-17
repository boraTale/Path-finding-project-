package finalproject;

import finalproject.system.Tile;
import finalproject.system.TileType;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Test;

import finalproject.tiles.*;

public class MiniTester {
    public static class TestTile extends Tile {
        // For testing
    }
    
    @Test
    public void tile1() { // 1 point
        Tile t;
        
        t = new PlainTile();
        assertTrue(t.distanceCost == 3 && t.timeCost == 1 && t.damageCost == 0, "Wrong costs for PlainTile.");

        t = new DesertTile();
        assertTrue(t.distanceCost == 2 && t.timeCost == 6 && t.damageCost == 3, "Wrong costs for DesertTile.");
        
        t = new MountainTile();
        assertTrue(t.distanceCost == 100 && t.timeCost == 100 && t.damageCost == 100, "Wrong costs for MountainTile.");
    }
    
    @Test
    public void tile2() { // 1 point
        Tile t;
        
        t = new FacilityTile();
        assertTrue(t.distanceCost == 1 && t.timeCost == 2 && t.damageCost == 0, "Wrong costs for FacilityTile.");
        
        t = new MetroTile();
        assertTrue(t.distanceCost == 1 && t.timeCost == 1 && t.damageCost == 2, "Wrong costs for MetroTile.");
        
        t = new ZombieInfectedRuinTile();
        assertTrue(t.distanceCost == 1 && t.timeCost == 3 && t.damageCost == 5, "Wrong costs for ZombieInfectedRuinTile.");
    }

    private static String expectedVsActualTiles(Tile[] tiles, ArrayList<Tile> arr) {
        String output = new String();
        output += "Expected: ";
        for (int i = 0; i < tiles.length; ++i) {
            output += tiles[i].nodeID + " ";
        }
        output += " Yours: ";
        for (int i = 0; i < arr.size(); ++i) {
            output += arr.get(i).nodeID + " ";
        }
        return output;
    }
    
    // 0 -- 1 -- 2 -- 3 -- 4
    @Test
    public void bFS1() { // 1 point
        Tile[] tiles = new Tile[5];
        for (int i = 0; i < tiles.length; ++i) {
            tiles[i] = new TestTile();
            tiles[i].nodeID = i;
        }
        for (int i = 0; i < tiles.length - 1; ++i)
            tiles[i].addNeighbor(tiles[i + 1]);
        ArrayList<Tile> arr = GraphTraversal.BFS(tiles[0]);
        
        String output = expectedVsActualTiles(tiles, arr);
        
        assertEquals(tiles.length, arr.size(), output);
        for (int i = 0; i < tiles.length; ++i) {
            assertEquals(tiles[i], arr.get(i), output);
        }
    }
    
    // 0 -- 1 -- 2 -- 3 -- 4
    @Test
    public void dFS1() { // 1 point
        Tile[] tiles = new Tile[5];
        for (int i = 0; i < tiles.length; ++i) {
            tiles[i] = new TestTile();
            tiles[i].nodeID = i;
        }
        for (int i = 0; i < tiles.length - 1; ++i)
            tiles[i].addNeighbor(tiles[i + 1]);
        ArrayList<Tile> arr = GraphTraversal.DFS(tiles[0]);
        
        String output = expectedVsActualTiles(tiles, arr);
        
        assertEquals(tiles.length, arr.size(), output);
        for (int i = 0; i < tiles.length; ++i) {
            assertEquals(tiles[i], arr.get(i), output);
        }
    }
    
  
    /*
     *       4
     *       |
     *  3 -- 0 -- 1
     *       |
     *       2
     */
    @Test
    public void bFS2() { // 1 point
        Tile[] tiles = new Tile[5];
        for (int i = 0; i < tiles.length; ++i) {
            tiles[i] = new TestTile();
            tiles[i].nodeID = i;
        }
        for (int i = 1; i < tiles.length; ++i)
            tiles[0].addNeighbor(tiles[i]);
        ArrayList<Tile> arr = GraphTraversal.BFS(tiles[0]);
        
        String output = expectedVsActualTiles(tiles, arr);
        
        assertEquals(tiles.length, arr.size(), output);
        assertEquals(tiles[0], arr.get(0), output);
    }
    
    /*
     *       4
     *       |
     *  3 -- 0 -- 1
     *       |
     *       2
     */
    @Test
    public void dFS2() { // 1 point
        Tile[] tiles = new Tile[5];
        for (int i = 0; i < tiles.length; ++i) {
            tiles[i] = new TestTile();
            tiles[i].nodeID = i;
        }
        for (int i = 1; i < tiles.length; ++i)
            tiles[0].addNeighbor(tiles[i]);
        ArrayList<Tile> arr = GraphTraversal.DFS(tiles[0]);
        
        String output = expectedVsActualTiles(tiles, arr);
        
        assertEquals(tiles.length, arr.size(), output);
        assertEquals(tiles[0], arr.get(0), output);
    }
    
    public static Graph graph = null;
    public static HashMap<Tile, Integer> tile2id = null;
    public static ArrayList<Tile> tiles = null;
    /*
     *     0  1  2  3  4
     *   +---------------
     *  0|    3     2
     *  1|       1  5
     *  2|          4  5
     *  3|             2
     *  4|
     */
    public static void buildGraph() {
        if (graph != null)
            return;
        tiles = new ArrayList<Tile>();
        tile2id = new HashMap<Tile, Integer>();
        for (int i = 0; i < 5; ++i) {
            TestTile t  = new TestTile();
            t.nodeID = i;
            tile2id.put(t, i);
            tiles.add(t);
        }
        
        graph = new Graph(tiles);
        graph.addEdge(tiles.get(0), tiles.get(1), 3);
        graph.addEdge(tiles.get(0), tiles.get(3), 2);
        graph.addEdge(tiles.get(1), tiles.get(2), 1);
        graph.addEdge(tiles.get(1), tiles.get(3), 5);
        graph.addEdge(tiles.get(2), tiles.get(3), 4);
        graph.addEdge(tiles.get(2), tiles.get(4), 5);
        graph.addEdge(tiles.get(3), tiles.get(4), 2);
    }
    
    @Test
    public void edges() { // 2 points
        int[][] edges = new int[5][5];
        for (int i = 0; i < 5; ++i)
            for(int j = 0; j < 5; ++j)
                edges[i][j] = 0;
        edges[0][1] = 3;
        edges[0][3] = 2;
        edges[1][2] = 1;
        edges[1][3] = 5;
        edges[2][3] = 4;
        edges[2][4] = 5;
        edges[3][4] = 2;
        
        buildGraph();
        ArrayList<Graph.Edge> allEdges = graph.getAllEdges();
        assertEquals(7, allEdges.size(), "There should be 7 edges.");
        for (Graph.Edge e: allEdges) {
            Tile s = e.getStart();
            Tile t = e.getEnd();
            int i, j;
            assertTrue(tile2id.containsKey(s));
            i = tile2id.get(s);
            assertTrue(tile2id.containsKey(t));
            j = tile2id.get(t);
            assertEquals(edges[i][j], e.weight, "Wrong edge or duplicate edge.");
            edges[i][j] = 0;
        }
    }

    @Test
    public void neighbors1() { // 1 points
        buildGraph();
        ArrayList<Tile> neighbors = graph.getNeighbors(tiles.get(0));
        assertEquals(2, neighbors.size(), "Tile 0 should have 2 neighbors");
        assertTrue((neighbors.get(0) == tiles.get(1) && neighbors.get(1) == tiles.get(3)) || (neighbors.get(0) == tiles.get(3) && neighbors.get(1) == tiles.get(1)), "Tile 0 should have tiles 1 and 3 as neighbors.");
    }
    
    @Test
    public void neighbors2() { // 1 points
        buildGraph();
        ArrayList<Tile> neighbors = graph.getNeighbors(tiles.get(3));
        assertEquals(1, neighbors.size(), "Tile 3 should have 1 neighbor");
        assertEquals(tiles.get(4), neighbors.get(0), "Tile 3 should have tile 4 as neighbor.");
    }
    
    @Test
    public void pathCost() { // 2 points
        buildGraph();
        ArrayList<Tile> path = new ArrayList<Tile>();
        for (int i = 0; i < 5; ++i)
            path.add(tiles.get(i));
        double cost = graph.computePathCost(path);
        assertEquals(10, cost, "Cost of path 0-1-2-3-4 should be 10.");
    }
    
    @Test
    public void removeMin() { // 3 points
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        int a[] = { 3, 1, 8, 4, 9, 5, 2, 6, 10, 7 };
        for (int i = 0; i < a.length; ++i) {
            Tile t = new TestTile();
            t.costEstimate = a[i];
            tiles.add(t);
        }
        Arrays.sort(a);
        TilePriorityQ q = new TilePriorityQ(tiles);
        for (int i = 0; i < a.length; ++i) {
            Tile t = q.removeMin();
            assertEquals(a[i], t.costEstimate);
        }
    }
    
    @Test
    public void updateKeys() { // 3 points
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        int a[] = { 13, 7, 10, 9, 11, 5, 14, 12, 2, 15, 4, 6, 16, 3, 8 };
        int b[] = { 7, 14, 13, 2, 8, 6, 5, 4, 3, 11, 0, 10, 1, 12, 9 };
        for (int i = 0; i < a.length; ++i) {
            Tile t = new TestTile();
            t.costEstimate = a[i];
            tiles.add(t);
        }
        TilePriorityQ q = new TilePriorityQ(tiles);
        Tile t0 = new TestTile();
        t0.costEstimate = 0.5;
        for (int i = 0; i < a.length; ++i) {
            Tile t1 = tiles.get(b[i]);
            q.updateKeys(t1, t0, 1);
            Tile t2 = q.removeMin();
            assertEquals(t2, t1);
            assertEquals(1, t2.costEstimate);
        }
    }
    
    public static char[][] smMap = {
            { 's', 'd' },
            { 'z', 'e' },
    };
    public static char[][] lgMap = {
            { 's', 'M', 'p', 'z' },
            { 'f', 'm', 'p', 'd' },
            { 'f', 'f', 'd', 'p' },
            { 'd', 'f', 'M', 'e' },
    };
    public static char[][] dgMap = {
            { 's', 'z', 'z', 'p' },
            { 'p', 'm', 'z', 'p' },
            { 'p', 'p', 'z', 'z' },
            { 'z', 'p', 'p', 'e' },
    };
    public static ArrayList<Tile> world = null;
    public static Tile[][] tileArray = null;
    
    public static void buildWorld(char[][] map, boolean useMetro) {
        world = new ArrayList<Tile>();
        tileArray = new Tile[map.length][map[0].length];
        ArrayList<MetroTile> metros = new ArrayList<MetroTile>();
        
        int id = 0;
        for (int i = 0; i < map.length; ++i)
            for (int j = 0; j < map[0].length; ++j) {
                Tile t = null;
                switch (map[i][j]) {
                case 's':
                    t = new FacilityTile();
                    t.isStart = true;
                    break;
                    
                case 'e':
                    t = new FacilityTile();
                    t.isDestination = true;
                    break;
                    
                case 'p':
                    t = new PlainTile();
                    break;
                    
                case 'd':
                    t = new DesertTile();
                    break;
                    
                case 'm':
                    t = new MountainTile();
                    break;
                    
                case 'f':
                    t = new FacilityTile();
                    break;
                    
                case 'M':
                    t = new MetroTile();
                    metros.add((MetroTile)t);
                    break;
                    
                case 'z':
                    t = new ZombieInfectedRuinTile();
                    break;
                    
                default:
                    t = new MountainTile();
                }
                
                t.nodeID = id++;
                tileArray[i][j] = t;
                world.add(t);
            }
        
        for (int i = 0; i < map.length; ++i)
            for (int j = 0; j < map[0].length - 1; ++j)
                tileArray[i][j].addNeighbor(tileArray[i + 0][j + 1]);
        
        for (int i = 0; i < map.length - 1; ++i)
            for (int j = 0; j < map[0].length; ++j)
                tileArray[i][j].addNeighbor(tileArray[i + 1][j + 0]);
        
        if (useMetro) {
            for (int i = 0; i < metros.size() - 1; ++i)
                for (int j = i + 1; j < metros.size(); ++j) {
                    MetroTile a = metros.get(i);
                    MetroTile b = metros.get(j);
                    a.fixMetro(b);
                    if (a.metroTimeCost < 100 && a.metroDistanceCost < 100)
                        a.addNeighbor(b);
                }
        }
    }
    
    // test ShortestPath
    @Test
    public void sPathGenerateGraph1() { // 2 points
        buildWorld(smMap, false);
        ShortestPath shortest = new ShortestPath(world.get(0));
        ArrayList<Graph.Edge> edges = shortest.g.getAllEdges();
        assertEquals(8, edges.size(), "There should be 8 edges.");
        for (Graph.Edge e: edges) {
            assertEquals(e.destination.distanceCost, e.weight, "Weight should be the distance cost.");
        }
    }
    
    // test ShortestPath
    @Test
    public void sPathGenerateGraph2() { // 3 points
        buildWorld(lgMap, false);
        ShortestPath shortest = new ShortestPath(world.get(0));
        ArrayList<Graph.Edge> edges = shortest.g.getAllEdges();
        assertEquals(40, edges.size(), "There should be 40 edges. Note that MountainTile is not reachable.");
        for (Graph.Edge e: edges) {
            assertEquals(e.destination.distanceCost, e.weight, "Weight should be the distance cost.");
        }
    }
    
    // test findPath(Tile startNode)
    @Test
    public void findPath1Arg1() { // 2 points
        buildWorld(smMap, false);
        ShortestPath shortest = new ShortestPath(world.get(0));
        ArrayList<Tile> path = shortest.findPath(world.get(0));
        assertEquals(3, path.size(), "Path length (number of vertices, including start and end) should be 3.");
        assertEquals(2, shortest.g.computePathCost(path), "Path cost should be 2.");
    }
    
    // test findPath(Tile startNode)
    @Test
    public void findPath1Arg2() { // 3 points
        buildWorld(lgMap, false);
        ShortestPath shortest = new ShortestPath(world.get(0));
        ArrayList<Tile> path = shortest.findPath(world.get(0));
        assertTrue(path.size() == 6 || path.size() == 7, "Wrong path length");
        if (path.size() == 6) { // to furthest (weighted) vertex
            /*
             * s
             * f
             * f f d p
             */
            Tile[] a = { tileArray[0][0], tileArray[1][0], tileArray[2][0],
                    tileArray[2][1], tileArray[2][2], tileArray[2][3]
            };
            for (int i = 0; i < a.length; ++i) {
                assertEquals(a[i], path.get(i), "Wrong path.");
            }
            
            assertEquals(8, shortest.g.computePathCost(path), "Path cost should be 8.");
        } else if (path.size() == 7) { // to destination
            /* s
             * f
             * f f
             *   f M e
            */
            Tile[] a = { tileArray[0][0], tileArray[1][0], tileArray[2][0], tileArray[2][1],
                    tileArray[3][1], tileArray[3][2], tileArray[3][3]
            };
            for (int i = 0; i < a.length; ++i) {
                assertEquals(a[i], path.get(i), "Wrong path.");
            }
            
            assertEquals(6, shortest.g.computePathCost(path), "Path cost should be 6.");
        }
    }
    
    // test findPath(Tile start, Tile end)
    @Test
    public void findPath2Args() { // 1 point
        buildWorld(lgMap, false);
        ShortestPath shortest = new ShortestPath(world.get(0));
        ArrayList<Tile> path = shortest.findPath(world.get(0), world.get(world.size() - 1));
        /*
         * s
         * f
         * f f
         *   f M e
         */
        assertEquals(7, path.size(), "Path length (number of vertices, including start and end) should be 7.");
        assertEquals(6, shortest.g.computePathCost(path), "Path cost should be 6.");
    }
    
    // test findPath(Tile start, LinkedList<Tile> waypoints)
    @Test
    public void findPath3Args() { // 1 point
        buildWorld(lgMap, false);
        ShortestPath shortest = new ShortestPath(world.get(0));
        LinkedList<Tile> waypoints = new LinkedList<Tile>();
        waypoints.add(tileArray[2][2]);
        ArrayList<Tile> path = shortest.findPath(world.get(0), waypoints);
        /*
         * s
         * f
         * f f d
         *     M e
         */
        assertEquals(7, path.size(), "Path length (number of vertices, including start and end) should be 7.");
        assertEquals(7, shortest.g.computePathCost(path), "Path cost should be 7.");
    }
    
    // test FastestPath
    @Test
    public void fPathGenerateGraph1() { // 1 point
        buildWorld(smMap, false);
        FastestPath shortest = new FastestPath(world.get(0));
        ArrayList<Graph.Edge> edges = shortest.g.getAllEdges();
        assertEquals(8, edges.size(), "There should be 8 edges.");
        for (Graph.Edge e: edges) {
            assertEquals(e.destination.timeCost, e.weight, "Weight should be the time cost.");
        }
    }
    
    // test FastestPath
    @Test
    public void fPathGenerateGraph2() { // 1 point
        buildWorld(lgMap, false);
        FastestPath shortest = new FastestPath(world.get(0));
        ArrayList<Graph.Edge> edges = shortest.g.getAllEdges();
        assertEquals(40, edges.size(), "There should be 40 edges. Note that MountainTile is not reachable.");
        for (Graph.Edge e: edges) {
            assertEquals(e.destination.timeCost, e.weight, "Weight should be the time cost.");
        }
    }
    
    // test ShortestPath (with metro)
    @Test
    public void sPathGenerateGraphWithMetro() { // 1 point
        buildWorld(lgMap, true);
        ShortestPath shortest = new ShortestPath(world.get(0));
        ArrayList<Graph.Edge> edges = shortest.g.getAllEdges();
        assertEquals(42, edges.size(), "There should be 42 edges. Note that MountainTile is not reachable.");
        for (Graph.Edge e: edges) {
            if (e.origin.getTileType() == TileType.Metro && e.destination.getTileType() == TileType.Metro) {
                assertEquals(((MetroTile)e.destination).metroDistanceCost, e.weight, "Weight should be the metro distance cost.");
            } else {
                assertEquals(e.destination.distanceCost, e.weight, "Weight should be the distance cost.");
            }
        }
    }
    
    // test FastestPath (with metro)
    @Test
    public void fPathGenerateGraphWithMetro() { // 1 point
        buildWorld(lgMap, true);
        FastestPath shortest = new FastestPath(world.get(0));
        ArrayList<Graph.Edge> edges = shortest.g.getAllEdges();
        assertEquals(42, edges.size(), "There should be 42 edges. Note that MountainTile is not reachable.");
        for (Graph.Edge e: edges) {
            if (e.origin.getTileType() == TileType.Metro && e.destination.getTileType() == TileType.Metro) {
                assertEquals(((MetroTile)e.destination).metroTimeCost, e.weight, "Weight should be the metro time cost.");
            } else {
                assertEquals(e.destination.timeCost, e.weight, "Weight should be the time cost.");
            }
        }
    }
    
    // test SafestShortestPath
    @Test
    public void sSPathGenerateGraph() { // 2 points
        buildWorld(lgMap, false);
        SafestShortestPath sspath = new SafestShortestPath(world.get(0), 100);
        ArrayList<Graph.Edge> edges;
        edges = sspath.costGraph.getAllEdges();
        assertEquals(40, edges.size(), "There should be 40 edges. Note that MountainTile is not reachable.");
        for (Graph.Edge e: edges) {
            assertEquals(e.destination.distanceCost, e.weight, "Weight should be the distance cost.");
        }
        edges = sspath.damageGraph.getAllEdges();
        assertEquals(40, edges.size(), "There should be 40 edges. Note that MountainTile is not reachable.");
        for (Graph.Edge e: edges) {
            assertEquals(e.destination.damageCost, e.weight, "Weight should be the damage cost.");
        }
        edges = sspath.aggregatedGraph.getAllEdges();
        assertEquals(40, edges.size(), "There should be 40 edges. Note that MountainTile is not reachable.");
        for (Graph.Edge e: edges) {
            assertEquals(e.destination.damageCost, e.weight, "Weight should be the damage cost.");
        }
    }
    
    // test SafestShortestPath.findPath(Tile start, LinkedList<Tile> waypoints)
    @Test
    public void safeFindPath1() { // 1 point
        buildWorld(dgMap, false);
        SafestShortestPath shortest = new SafestShortestPath(world.get(0), 100);
        LinkedList<Tile> waypoints = new LinkedList<Tile>();
        ArrayList<Tile> path = shortest.findPath(world.get(0), waypoints);
        /*
         * s z z
         *     z
         *     z z
         *       e
         */
        assertEquals(7, path.size(), "Path length (number of vertices, including start and end) should be 7.");
        
        Tile[] a = { tileArray[0][0], tileArray[0][1], tileArray[0][2], tileArray[1][2],
                tileArray[2][2], tileArray[2][3], tileArray[3][3]
        };
        for (int i = 0; i < a.length; ++i) {
            assertEquals(a[i], path.get(i), "Wrong path.");
        }
        
        assertEquals(6, shortest.costGraph.computePathCost(path), "Path cost should be 6.");
    }
    
    // test SafestShortestPath.findPath(Tile start, LinkedList<Tile> waypoints)
    @Test
    public void safeFindPath2() { // 1 point
        buildWorld(dgMap, false);
        SafestShortestPath shortest = new SafestShortestPath(world.get(0), 1);
        LinkedList<Tile> waypoints = new LinkedList<Tile>();
        ArrayList<Tile> path = shortest.findPath(world.get(0), waypoints);
        /*
         * s
         * p
         * p p
         *   p p e
         */
        assertEquals(7, path.size(), "Path length (number of vertices, including start and end) should be 7.");
        assertEquals(16, shortest.costGraph.computePathCost(path), "Path cost should be 16.");
    }
    
    // test SafestShortestPath.findPath(Tile start, LinkedList<Tile> waypoints)
    @Test
    public void safeFindPath3() { // 1 point
        buildWorld(dgMap, false);
        SafestShortestPath shortest = new SafestShortestPath(world.get(0), 100);
        LinkedList<Tile> waypoints = new LinkedList<Tile>();
        waypoints.add(tileArray[3][2]);
        ArrayList<Tile> path = shortest.findPath(world.get(0), waypoints);
        /*
         * s z z
         *     z
         *     z
         *     p e
         */
        assertEquals(7, path.size(), "Path length (number of vertices, including start and end) should be 7.");
        assertEquals(8, shortest.costGraph.computePathCost(path), "Path cost should be 8.");
    }
    
    // test SafestShortestPath.findPath(Tile start, LinkedList<Tile> waypoints)
    @Test
    public void safeFindPath4() { // 1 point
        buildWorld(dgMap, false);
        SafestShortestPath shortest = new SafestShortestPath(world.get(0), 100);
        LinkedList<Tile> waypoints = new LinkedList<Tile>();
        waypoints.add(tileArray[3][0]);
        waypoints.add(tileArray[3][2]);
        ArrayList<Tile> path = shortest.findPath(world.get(0), waypoints);
        /*
         * s
         * p
         * p
         * z p p e
         */
        assertEquals(7, path.size(), "Path length (number of vertices, including start and end) should be 7.");
        
        Tile[] a = { tileArray[0][0], tileArray[1][0], tileArray[2][0], tileArray[3][0],
                tileArray[3][1], tileArray[3][2], tileArray[3][3]
        };
        for (int i = 0; i < a.length; ++i) {
            assertEquals(a[i], path.get(i), "Wrong path.");
        }
        
        assertEquals(14, shortest.costGraph.computePathCost(path), "Path cost should be 14.");
    }
}

