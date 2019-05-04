package graph;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.model.MutableGraph;

import static guru.nidi.graphviz.model.Factory.*;


/**
 * Use two hashmaps. The first one store all the children of a given node. 
 * The second one store all the parents of a given node. The labels of a node should
 * be immutable.
 * 
 * @param <L> type of vertex labels in this graph. Must implement toString() and be immutable
 */
public class HashtableGraph<L> implements Graph<L> {
    private HashMap<L, HashMap<L, Double>> graph;    
    private HashMap<L, HashMap<L, Double>> sources;
    
    public HashtableGraph() {
        this.graph = new HashMap<>();
        this.sources = new HashMap<>();
    }
    
  
    @Override
    public boolean add(L vertex) {
        return graph.putIfAbsent(vertex, new HashMap<>()) == null;
       
    }

    @Override
    public double set(L source, L target, double weight) {
        // In case target does not exist
        this.add(target);
        
        // Update the target parents
        if(sources.containsKey(target))
            sources.get(target).put(source, weight);
        else {
            HashMap<L, Double> src = new HashMap<>();
            src.put(source, weight);
            sources.put(target, src);
        }
        
        // Update the source children
        if(graph.containsKey(source)) {
            Double ret = graph.get(source).put(target, weight);
            return ret == null ? 0 : ret ;
        }
        
        HashMap<L, Double> targets = new HashMap<>();
        targets.put(target, weight);
        graph.put(source, targets);     
        return 0;
    }

    @Override
    public boolean remove(L vertex) {
        HashMap<L, Double> s = sources.get(vertex);
        if (s != null) {
            for (L source : s.keySet()) {
                if(graph.containsKey(source))
                    graph.get(source).keySet().removeIf(e -> e.equals(vertex));
            }
        }
            
        return graph.remove(vertex) != null;
    }

    @Override
    public Set<L> vertices() {
        return graph.keySet();
    }

    @Override
    public Map<L, Double> sources(L target) {
        return this.sources.get(target);
    }

    @Override
    public Map<L, Double> targets(L source) {
        return this.graph.get(source);
    }
    
    /** 
     * Write the graph into a png. Use the Ggraphviz-java library to do this
     * Use of mutable graph to avoid useless copies.
     * @param filename of the png in which the graph will be drawn.
     *        Accept full and relative paths.
     */
    public void writeToPng(String filename) {
        MutableGraph g = mutGraph().setDirected(true);
        
        for(L source : this.graph.keySet()) {         
            LinkTarget neighbors [] = this.graph.get(source).entrySet()
                                              .stream()
                                              .map(e -> to(mutNode(e.getKey().toString()))
                                                        .with(Label.of(e.getValue().toString()))
                                                  )
                                              .toArray(LinkTarget[]::new);

            g.add(mutNode(source.toString()).addLink(neighbors));

        }
        
        try {
           Graphviz.fromGraph(g).width(500).render(Format.PNG).toFile(new File(filename));
        } catch (IOException e) {
           System.out.println("Failing to write the output file");
        }
    }
    
}
