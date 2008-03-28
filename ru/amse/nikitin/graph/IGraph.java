package ru.amse.nikitin.simulator.util.graph;

import java.util.Collection;

/**
 * @author Pavel A. Nikitin
 * graph interface
 *
 */
public interface IGraph<T extends Object> {
	/** new vertex */
	IVertex<T> newVertex(T data);
	/** vertex getter */
	IVertex<T> getVertex(int index);
	/** all vertices getter */
	Collection<IVertex<T>> getVertices();
	/** neighbour adder */
	void addNeighbour(IVertex<T> a, IVertex<T> b);
	/** neighbour by id */
	void addNeighbour(int a, int b);
	/** dijkstra's shortest paths solving */
	void solvePaths(int root);
	/** graph painting */
	void solveColors();
	/** graph's colors count */
	int getColorsCount();
}
