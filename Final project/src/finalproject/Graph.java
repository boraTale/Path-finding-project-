package finalproject;

import java.util.ArrayList;
import java.util.HashSet;

import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.*;

public class Graph {
    private ArrayList<Tile> vertices;
    private ArrayList<Edge> allEdges;
	public Graph(ArrayList<Tile> vertices) {
        this.vertices = vertices;
        this.allEdges = new ArrayList<>();
	}

    public void addEdge(Tile origin, Tile destination, double weight){
        Edge newEdge = new Edge(origin, destination, weight);
        allEdges.add(newEdge);
    }
    

	public ArrayList<Edge> getAllEdges() {

        return allEdges;
	}
  

	public ArrayList<Tile> getNeighbors(Tile t) {
        ArrayList<Tile> neighbors = new ArrayList<>();
        for (Edge edge : allEdges) {
            if (edge.getStart().equals(t)) {
                neighbors.add(edge.getEnd());
            }
        }
        return neighbors;
    }
	

	public double computePathCost(ArrayList<Tile> path) {
        double totalCost = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            Tile start = path.get(i);
            Tile end = path.get(i + 1);
            for (Edge edge : allEdges) {
                if (edge.getStart().equals(start) && edge.getEnd().equals(end)) {
                    totalCost += edge.getWeight();
                    break;
                }
            }
        }
        return totalCost;
	}

    public static class Edge{
    	Tile origin;
    	Tile destination;
    	double weight;

        public Edge(Tile s, Tile d, double cost){
            this.origin = s;
            this.destination = d;
            this.weight = cost;
        }

        public Tile getStart(){

            return origin;
        }

        public Tile getEnd() {
            return destination;
        }
        public double getWeight() {
            return weight;
        }
        public void setWeight(double newWeight) {
            this.weight = newWeight;
        }
        
    }
    public ArrayList<Tile> getAllVertices() {
        return new ArrayList<>(vertices);
    }

    public double getEdgeWeight(Tile origin, Tile destination) {
        for (Edge edge : allEdges) {
            if (edge.getStart().equals(origin) && edge.getEnd().equals(destination)) {
                return edge.getWeight();
            }
        }
        return Double.MAX_VALUE; // Assuming no edge means unreachable
    }
    public void updateEdgeWeight(Tile origin, Tile destination, double newWeight) {
        for (Edge edge : allEdges) {
            if (edge.getStart().equals(origin) && edge.getEnd().equals(destination)) {
                edge.setWeight(newWeight);
                break;
            }
        }
    }
    
}