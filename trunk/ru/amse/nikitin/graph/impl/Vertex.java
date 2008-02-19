package ru.amse.nikitin.graph.impl;

import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;

import ru.amse.nikitin.graph.IVertex;

/**
 * @author Pavel A. Nikitin
 * vertex implementation
 *
 */
public class Vertex<T extends Object> implements IVertex<T>, Comparable<IVertex> {
	protected int id;
	protected int weight;
	protected int color;
	protected T data;
	protected List<IVertex<T>> neighbours = new LinkedList<IVertex<T>>();
	protected IVertex<T> predecessor;

	/* package-private */ Vertex(T data, int id) {
		this.data = data;
		this.id = id;
	}
	
	/* package-private */ void setPredecessor(IVertex<T> predecessor) {
		this.predecessor = predecessor;
	}
	
	/* package-private */ void setColor(int color) {
		this.color = color;
	}
	
	/* package-private */ void setWeight(int weight) {
		this.weight = weight;
	}
	
	public void addNeighbour(IVertex<T> newNeighbour) {
		neighbours.add(newNeighbour);
	}
	
	public IVertex<T> getPredecessor() {
		return predecessor;
	}
	
	public int getColor() {
		return color;
	}
	
	public boolean isNotPainted() {
		return (color < 0);
	}
	
	public int getWeight() {
		return weight;
	}

	public T getData() {
		return data;
	}

	public void setData(T newData) {
		data = newData;
	}

	public Collection<IVertex<T>> getNeighbours() {
		return Collections.unmodifiableCollection(neighbours);
	}

	public int compareTo(IVertex v) {
		long vw = v.getWeight();
		if (weight == -1) return (weight == vw ? 0 : -1);
		return (weight > vw ? -1 : (weight == vw ? 0 : 1));
	}
}
