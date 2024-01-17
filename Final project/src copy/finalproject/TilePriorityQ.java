package finalproject;

import java.util.ArrayList;
import java.util.Arrays;


import finalproject.system.Tile;

public class TilePriorityQ {
	private ArrayList<Tile> heap;
	public TilePriorityQ (ArrayList<Tile> vertices) {
		this.heap = new ArrayList<>(vertices);
		for (int i = heap.size() / 2 - 1; i >= 0; i--) {
			heapifyDown(i);
		}
	}
	public Tile removeMin() {
		if (heap.isEmpty()) {
			return null;
		}
		Tile minTile = heap.get(0);
		Tile lastTile = heap.remove(heap.size() - 1);

		if (!heap.isEmpty()) {
			heap.set(0, lastTile);
			heapifyDown(0);
		}
		return minTile;

	}

	public void updateKeys(Tile t, Tile newPred, double newEstimate) {
		int index = heap.indexOf(t);
		if (index != -1) {
			t.predecessor = newPred;
			t.costEstimate = newEstimate;
			heapifyUp(index);
		}
	}
	private void heapifyDown(int i) {
		int left = 2 * i + 1;
		int right = 2 * i + 2;
		int smallest = i;

		if (left < heap.size() && heap.get(left).costEstimate < heap.get(smallest).costEstimate) {
			smallest = left;
		}

		if (right < heap.size() && heap.get(right).costEstimate < heap.get(smallest).costEstimate) {
			smallest = right;
		}

		if (smallest != i) {
			swap(i, smallest);
			heapifyDown(smallest);
		}
	}

	private void heapifyUp(int i) {
		while (i > 0 && heap.get(parent(i)).costEstimate > heap.get(i).costEstimate) {
			swap(i, parent(i));
			i = parent(i);
		}
	}

	private int parent(int i) {
		return (i - 1) / 2;
	}

	private void swap(int i, int j) {
		Tile temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
	}
	public boolean isEmpty() {
		return heap.isEmpty();
	}
	
}
