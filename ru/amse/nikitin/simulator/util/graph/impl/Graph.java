package ru.amse.nikitin.simulator.util.graph.impl;

import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;

import ru.amse.nikitin.simulator.util.graph.IGraph;
import ru.amse.nikitin.simulator.util.graph.IVertex;

/**
 * @author Pavel A. Nikitin
 * graph implementation
 *
 */
public class Graph<T extends Object> implements IGraph<T> {
	protected int colorsCount;
	protected List<IVertex<T>> vertices = new LinkedList<IVertex<T>>();
	protected DijkstraAlg dijkstraAlg = new DijkstraAlg();
	protected ColoringAlg coloringAlg = new ColoringAlg();
	
	/* package-private */ class DijkstraAlg {
		protected void init(List<IVertex<T>> vertices, int root) {
			for (IVertex<T> v: vertices) {
				((Vertex<T>)v).setWeight(-1);
				((Vertex<T>)v).setPredecessor(null);
			}
			((Vertex<T>)vertices.get(root)).setWeight(0);
		}
		protected void relax(Vertex<T> u, Vertex<T> v) {
			int du = u.getWeight();
			int dv = v.getWeight();
			
			if (du != -1) if ((dv == -1) || (dv > du + 1)) {
				v.setWeight(du + 1);
				v.setPredecessor(u);
			}
		}
		/* package-private */ void run(List<IVertex<T>> vertices, int root) {
			PriorityQueue<IVertex<T>> Q = new PriorityQueue<IVertex<T>>();
			init(vertices, root);
			for (IVertex<T> v: vertices) {
				Q.add(v);
			}
			while (!Q.isEmpty()) {
				Vertex<T> u = (Vertex<T>)Q.remove(); // extract min
				for (IVertex<T> v: u.getNeighbours()) {
					boolean exists = Q.remove(v);
					relax(u, (Vertex<T>)v);
					if (exists) Q.add(v);
				}
			}
		}
	}
	
	/* package-private */ class ColoringAlg {
		/* package-private */ int run(List<IVertex<T>> vertices) {
			for (IVertex<T> v: vertices) {
				((Vertex<T>)v).setColor(-1);
			}
			int colorsCount = 0;
			Set<IVertex<T>> undesired = new HashSet<IVertex<T>>();
			for (IVertex<T> v: vertices) {
				if (v.isNotPainted()) {
					undesired.clear();
					for (IVertex<T> u: v.getNeighbours()) {
						undesired.add(u);
					}
					((Vertex<T>)v).setColor(colorsCount);
					for (IVertex<T> u: vertices) {
						if ((u.isNotPainted()) && (!undesired.contains(u))) {
							((Vertex<T>)u).setColor(colorsCount);
							for (IVertex<T> w: u.getNeighbours()) {
								undesired.add(w);
							}
						}
					}
					colorsCount++;
				}
			}
			return colorsCount;
		}
	}

	public Graph() {
	}

	public IVertex<T> newVertex(T data) {
		Vertex<T> v = new Vertex<T>(data, vertices.size());
		vertices.add(v);
		return v;
	}

	public IVertex<T> getVertex(int index) {
		return vertices.get(index);
	}

	public Collection<IVertex<T>> getVertices() {
		return Collections.unmodifiableCollection(vertices);
	}
	
	public void addNeighbour(IVertex<T> a, IVertex<T> b) {
		a.addNeighbour(b);
		// b.addNeighbour(a); // DAMN WRONG!!!
	}
	
	public void addNeighbour(int a, int b) {
		addNeighbour(vertices.get(a), vertices.get(b)); 
	}
	
	public void solvePaths(int root) {
		dijkstraAlg.run(vertices, root);
	}

	public void solveColors() {
		colorsCount = coloringAlg.run(vertices);
	}
	
	public int getColorsCount() {
		return colorsCount;
	}
}
