package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class GraphTraversal
{
	public static ArrayList<Tile> BFS(Tile s)
	{ArrayList<Tile> visited = new ArrayList<>();
		LinkedList<Tile> queue = new LinkedList<>();

		visited.add(s);
		queue.add(s);

		while (!queue.isEmpty()) {
			Tile current = queue.poll();

			for (Tile neighbor : current.neighbors) {
				if (!visited.contains(neighbor) && neighbor.isWalkable()) {
					visited.add(neighbor);
					queue.add(neighbor);
				}
			}
		}
		return visited;

	}

	public static ArrayList<Tile> DFS(Tile s) {
		ArrayList<Tile> visited = new ArrayList<>();
		DFSUtil(s, visited);
		return visited;
	}

	private static void DFSUtil(Tile current, ArrayList<Tile> visited) {
		visited.add(current);

		for (Tile neighbor : current.neighbors) {
			if (!visited.contains(neighbor) && neighbor.isWalkable()) {
				DFSUtil(neighbor, visited);
			}
		}
	}

}  