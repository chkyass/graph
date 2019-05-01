package graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HashtableGraph implements Graph<String> {
    private HashMap<String, HashMap<String, Double>> graph;    
    private HashMap<String, HashMap<String, Double>> sources;
    
    public HashtableGraph() {
        this.graph = new HashMap<>();
        this.sources = new HashMap<>();
    }
    
  
    @Override
    public boolean add(String vertex) {
        return graph.putIfAbsent(vertex, new HashMap<>()) == null;
       
    }

    @Override
    public double set(String source, String target, double weight) {
        this.add(target);
        
        // Update the sources
        if(sources.containsKey(target))
            sources.get(target).put(source, weight);
        else {
            HashMap<String, Double> src = new HashMap<>();
            src.put(source, weight);
            sources.put(target, src);
        }
        
        //update the graph
        if(graph.containsKey(source)) {
            Double ret = graph.get(source).put(target, weight);
            return ret == null ? 0 : ret ;
        }
        
        HashMap<String, Double> targets = new HashMap<>();
        targets.put(target, weight);
        graph.put(source, targets);     
        return 0;
    }

    @Override
    public boolean remove(String vertex) {
        HashMap<String, Double> s = sources.get(vertex);
        if (s != null) {
            for (String source : s.keySet()) {
                HashMap<String, Double> targets = graph.get(source);
                if(targets != null)
                    targets.keySet().removeIf(e -> e.equals(vertex));
            }
        }
            
        return graph.remove(vertex) != null;
    }

    @Override
    public Set<String> vertices() {
        return graph.keySet();
    }

    @Override
    public Map<String, Double> sources(String target) {
        return this.sources.get(target);
    }

    @Override
    public Map<String, Double> targets(String source) {
        return this.graph.get(source);
    }
    
    
}
