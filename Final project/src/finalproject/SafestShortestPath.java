package finalproject;


import java.util.ArrayList;
import java.util.LinkedList;

import finalproject.system.Tile;

public class SafestShortestPath extends ShortestPath {
	public int health;
	public Graph costGraph;
	public Graph damageGraph;
	public Graph aggregatedGraph;


	public SafestShortestPath(Tile start, int health) {
		super(start);
		this.health = health;
		generateGraph();
	}

	@Override
	public void generateGraph() {
		ArrayList<Tile> reachableTiles = GraphTraversal.BFS(source);


		costGraph = new Graph(reachableTiles);
		damageGraph = new Graph(reachableTiles);
		aggregatedGraph = new Graph(reachableTiles);

		for (Tile tile : reachableTiles) {
			for (Tile neighbor : tile.neighbors) {
				if (neighbor.isWalkable()) {
					double distanceCost = neighbor.distanceCost;
					double damageCost = neighbor.damageCost;

					costGraph.addEdge(tile, neighbor, distanceCost);
					damageGraph.addEdge(tile, neighbor, damageCost);
					aggregatedGraph.addEdge(tile, neighbor, damageCost);
				}
			}
		}
	}
	@Override
	public ArrayList<Tile> findPath(Tile startNode, LinkedList<Tile> waypoints) {
		this.g = costGraph;
		ArrayList<Tile> pathPc = super.findPath(startNode, waypoints);
		if (calculateTotalDamage(pathPc) <= health) {
			return pathPc;
		}


		this.g = damageGraph;
		ArrayList<Tile> pathPd = super.findPath(startNode, waypoints);
		if (calculateTotalDamage(pathPd) > health) {
			return null;
		}

		while (true) {

			double lambda = calculateLambda(pathPc, pathPd);
			updateAggregatedGraphWeights(lambda);

			this.g = aggregatedGraph;
			ArrayList<Tile> pathPr = super.findPath(startNode, waypoints);

			double totalAggCostPc = calculateTotalAggregatedCost(pathPc, lambda);
			double totalAggCostPr = calculateTotalAggregatedCost(pathPr, lambda);

			if (totalAggCostPr == totalAggCostPc) {
				return pathPd;
			} else if (calculateTotalDamage(pathPr) <= health) {
				pathPd = pathPr;
			} else {
				pathPc = pathPr;
			}
		}
	}
	private double calculateTotalDamage(ArrayList<Tile> path) {
		double totalDamage = 0.0;
		if (path != null) {
			for (Tile tile : path) {
				totalDamage += tile.damageCost;
			}
		}
		return totalDamage;
	}
	private double calculateLambda(ArrayList<Tile> pc, ArrayList<Tile> pd) {
		double cPc = calculateTotalCost(pc);
		double cPd = calculateTotalCost(pd);
		double dPc = calculateTotalDamage(pc);
		double dPd = calculateTotalDamage(pd);

		return (cPc - cPd) / (dPd - dPc);
	}
	private void updateAggregatedGraphWeights(double lambda) {
		for (Tile tile : aggregatedGraph.getAllVertices()) {
			for (Tile neighbor : tile.neighbors) {
				if (neighbor.isWalkable()) {
					double distanceCost = costGraph.getEdgeWeight(tile, neighbor);
					double damageCost = damageGraph.getEdgeWeight(tile, neighbor);
					double aggregatedCost = distanceCost + lambda * damageCost;
					aggregatedGraph.updateEdgeWeight(tile, neighbor, aggregatedCost);
				}
			}
		}
	}
	private double calculateTotalAggregatedCost(ArrayList<Tile> path, double lambda) {
		double totalCost = 0.0;
		if (path != null) {
			for (int i = 0; i < path.size() - 1; i++) {
				Tile u = path.get(i);
				Tile v = path.get(i + 1);
				double distanceCost = costGraph.getEdgeWeight(u, v);
				double damageCost = damageGraph.getEdgeWeight(u, v);
				totalCost += distanceCost + lambda * damageCost;
			}
		}
		return totalCost;
	}

	private double calculateTotalCost(ArrayList<Tile> path) {
		double totalCost = 0.0;
		if (path != null) {
			for (Tile tile : path) {
				totalCost += tile.distanceCost;
			}
		}
		return totalCost;
	}


}
