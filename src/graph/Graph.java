package graph;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A mutable weighted directed graph with labeled vertices.
 * Vertices have distinct labels of type {@code L} when compared
 * using the {@link Object#equals(Object) equals} method.
 * Edges are directed and have a weight of type {@code double}.
 * 
 * @param <L> type of vertex labels in this graph
 */
public interface Graph<L> {
    
    /**
     * Add a vertex to this graph.
     * 
     * @param vertex label for the new vertex
     * @return true if this graph did not already include a vertex with the
     *         given label; otherwise false (and this graph is not modified)
     */
    boolean add(L vertex);
    
    /**
     * Add or change a weighted directed edge in this graph.
     * vertices with the given labels are added to the graph if they do not
     * already exist.
     * 
     * @param source label of the source vertex
     * @param target label of the target vertex
     * @param weight nonnegative weight of the edge
     * @return the previous weight of the edge, or zero if there was no such
     *         edge
     */
    double set(L source, L target, double weight);
    
    /**
     * Remove a vertex from this graph; any edges to or from the vertex are
     * also removed.
     * 
     * @param vertex label of the vertex to remove
     * @return true if this graph included a vertex with the given label;
     *         otherwise false (and this graph is not modified)
     */
    boolean remove(L vertex);
    
    /**
     * Get all the vertices in this graph.
     * 
     * @return the set of labels of vertices in this graph
     */
    Set<L> vertices();
    
    /**
     * Get the source vertices with directed edges to a target vertex and the
     * weights of those edges.
     * 
     * @param target a label
     * @return a map where the key set is the set of labels of vertices such
     *         that this graph includes an edge from that vertex to target, and
     *         the value for each key is the (nonzero) weight of the edge from
     *         the key to target
     */
    Map<L, Double> sources(L target);
    
    /**
     * Get the target vertices with directed edges from a source vertex and the
     * weights of those edges.
     * 
     * @param source a label
     * @return a map where the key set is the set of labels of vertices such
     *         that this graph includes an edge from source to that vertex, and
     *         the value for each key is the (nonzero) weight of the edge from
     *         source to the key
     */
    Map<L, Double> targets(L source);

    /**
     * Apply the given function to all vertices in depth first order. Use iterative
     * implementation to make the code faster
     *
     * @param function to apply
     */
    void dfs(Consumer<L> function);

    /**
     * Apply the given function to the connected sub graph starting from vertex
     * in depth first order. Use iterative implementation to make the code faster
     *
     * @param vertex entry point for dfs
     * @param function to apply on vertices
     */
    void dfs(L vertex, Consumer<L> function);

    /**
     * Apply the given function to all vertices in breath first order
     *
     * @param function
     */
    void bfs(Consumer<L> function);

    /**
     * Apply the given function to the connected sub graph starting from vertex
     * in breath first order
     *
     * @param vertex entry point for bfs
     * @param function to apply on vertices
     */
    void bfs(L vertex, Consumer<L> function);


}
