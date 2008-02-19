package ru.amse.nikitin.graph;

import java.util.Collection;

/**
 * @author Pavel A. Nikitin
 * vertex interface
 *
 */
public interface IVertex<T extends Object> {
	/** dijkstra's predecessor */
	IVertex<T> getPredecessor();
	/** weight getter */
	int getWeight();
	/** color getter */
	int getColor();
	/** painting property */
	boolean isNotPainted();
	/** data getter */
	T getData();
	/** data getter */
	void setData(T newData);
	/** neghbour adder */
	void addNeighbour(IVertex<T> newNeighbour);
	/** all neighbours getter */
	Collection<IVertex<T>> getNeighbours();
}
