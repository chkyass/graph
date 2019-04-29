package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HashtableGraph implements Graph<String> {
    private HashMap<String, Set<Edge>> graph;    
    private HashMap<String, Set<Edge>> sources;    

    
    public HashtableGraph() {
        graph = new HashMap<>();
    }
    
  
    @Override
    public boolean add(String vertex) {
        graph.putIfAbsent(vertex, new HashSet<>());
        return false;
    }

    @Override
    public int set(String source, String target, double weight) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean remove(String vertex) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Set<String> vertices() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Double> sources(String target) {
        Set<Edge> neighbours = this.sources.get(target);
        Map<String, Double> sources = new HashMap<>();
        
        for(Edge e : neighbours) {
            sources.put(e.next, e.weight);
        }
        return sources;
    }

    @Override
    public Map<String, Double> targets(String source) {
        Set<Edge> neighbours = this.graph.get(source);
        Map<String, Double> targets = new HashMap<>();
        
        for(Edge e : neighbours) {
            targets.put(e.next, e.weight);
        }
        return targets;
    }
    
    
    /**
     * 
     * Immutable Edge data type representing an edge.
     * Contain the weight of the edge and a source or destination vertex 
     * 
     * @param <V>
     */
    private class Edge {
       public double weight;
       public String next;
       
       public Edge(String vertex, double weight) {
           this.weight = weight;
           this.next = vertex;
       }
       
    }
    
}
