package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class PathFindingService {
	Tile source;
	Graph g;

	public PathFindingService(Tile start) {

        this.source = start;
    }

	public abstract void generateGraph();

    public ArrayList<Tile> findPath(Tile startNode) {
        initSingleSource(g, startNode);

        TilePriorityQ queue = new TilePriorityQ(new ArrayList<>(g.getAllVertices()));

        Tile destination = null;
        while (!queue.isEmpty()) {
            Tile u = queue.removeMin();
            if (u.isDestination) {
                destination = u;
                break;
            }
            for (Tile v : g.getNeighbors(u)) {
                relax(u, v, queue);
            }
        }

        if (destination != null) {
            return buildPath(destination);
        } else {
            return new ArrayList<>();
        }
    }

    private void initSingleSource(Graph g, Tile start) {
        for (Tile v : g.getAllVertices()) {
            v.costEstimate = Double.MAX_VALUE;
            v.predecessor = null;
        }
        start.costEstimate = 0;
    }

    private void relax(Tile u, Tile v, TilePriorityQ queue) {
        double weight = g.getEdgeWeight(u, v);
        if (v.costEstimate > u.costEstimate + weight) {
            v.costEstimate = u.costEstimate + weight;
            v.predecessor = u;
            queue.updateKeys(v, u, v.costEstimate);
        }
    }

    private ArrayList<Tile> buildPath(Tile destination) {
        ArrayList<Tile> path = new ArrayList<>();
        for (Tile at = destination; at != null; at = at.predecessor) {
            path.add(0, at);
        }
        return path;
    }

    public ArrayList<Tile> findPath(Tile start, Tile end) {
        initSingleSource(g, start);
        TilePriorityQ queue = new TilePriorityQ(new ArrayList<>(g.getAllVertices()));

        while (!queue.isEmpty()) {
            Tile u = queue.removeMin();
            if (u.equals(end)) {
                break;
            }
            for (Tile v : g.getNeighbors(u)) {
                relax(u, v, queue);
            }
        }

        return buildPath(end);
    }


    public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints){
        ArrayList<Tile> fullPath = new ArrayList<>();
        Tile currentStart = start;

        for (Tile waypoint : waypoints) {
            ArrayList<Tile> pathSegment = findPath(currentStart, waypoint);
            if (!pathSegment.isEmpty()) {
                pathSegment.remove(pathSegment.size() - 1);
            }
            fullPath.addAll(pathSegment);
            currentStart = waypoint;
        }


        Tile destination = findDestinationTile();
        fullPath.addAll(findPath(currentStart, destination));

        return fullPath;
    }
    private Tile findDestinationTile() {
        for (Tile tile : g.getAllVertices()) {
            if (tile.isDestination) {
                return tile;
            }
        }
        return null;
    }
        
}

