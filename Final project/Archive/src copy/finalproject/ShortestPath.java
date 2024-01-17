package finalproject;


import finalproject.system.Tile;

public class ShortestPath extends PathFindingService {
    private static final double METRO_COMMUTE_FACTOR = 0.2;

    public ShortestPath(Tile start) {
        super(start);
        generateGraph();
    }

	@Override
	public void generateGraph() {
        g = new Graph(GraphTraversal.BFS(source));

        for (Tile tile : GraphTraversal.BFS(source) ){
            for (Tile neighbor : tile.neighbors) {
                if (neighbor.isWalkable()) {
                    double weight;
                    if (tile.isMetro() && neighbor.isMetro()) {
                        weight = calculateMetroDistanceCost(tile, neighbor);
                    } else {
                        weight = neighbor.distanceCost;
                    }
                    g.addEdge(tile, neighbor, weight);
                }
            }
        }
    }
    private double calculateMetroDistanceCost(Tile t1, Tile t2) {
        int manhattanDistance = Math.abs(t1.xCoord - t2.xCoord) + Math.abs(t1.yCoord - t2.yCoord);
        return manhattanDistance / METRO_COMMUTE_FACTOR;
    }

}