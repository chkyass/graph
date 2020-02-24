package graph;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
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
public class AdjListGraph<L> implements Graph<L> {
    private HashMap<L, Map<L, Double>> graph;
    private HashMap<L, Map<L, Double>> sources;
    private enum Mode {
        dfs, bfs
    }

    public AdjListGraph() {
        this.graph = new HashMap<>();
        this.sources = new HashMap<>();
    }
    
  
    @Override
    public boolean add(L vertex) {
        return graph.putIfAbsent(vertex, new HashMap<>()) == null;
       
    }

    @Override
    public double set(L source, L target, double weight) {
        // create target if it does not exist
        this.add(target);
        
        // Update the list of parents of the target
        if(sources.containsKey(target))
            sources.get(target).put(source, weight);
        else {
            Map<L, Double> src = new HashMap<>();
            src.put(source, weight);
            sources.put(target, src);
        }

        // The two vertex exists. Create or update the edge
        if(graph.containsKey(source)) {
            Double ret = graph.get(source).put(target, weight);
            return ret == null ? 0 : ret ;
        }

        // Source does not exist, create it and link it to target
        Map<L, Double> targets = new HashMap<>();
        targets.put(target, weight);
        graph.put(source, targets);     
        return 0;
    }

    @Override
    public boolean remove(L vertex) {
        Map<L, Double> s = sources.get(vertex);
        if (s != null) {
            // remove edges pointing to vertex
            for (L source : s.keySet()) {
                if(graph.containsKey(source))
                    graph.get(source).keySet().remove(vertex);
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
     * Helper for DFS and BFS. Is used to walk through connected nodes of the graph from a given source
     *
     * @param seen store all already seen nodes for cycle detection
     * @param next store next unseen nodes to visit. Element ordering depend on the mode of visit
     *             DFS or BFS
     * @param func to apply to all nodes
     * @param mode DFS or BFS
     */
    private void graphWalk(Set<L> seen, Deque<L> next, Consumer<L> func, Mode mode) {
        while (!next.isEmpty()) {
            L vertex = next.pop();
            func.accept(vertex);

            seen.add(vertex);
            for(L neigh : graph.get(vertex).keySet()){
                if(!seen.contains(neigh)) {
                    if(mode == Mode.dfs)
                        next.addFirst(neigh);
                    else if(mode == Mode.bfs)
                        next.addLast(neigh);
                }
            }
        }
    }

    @Override
    public void dfs(Consumer<L> function) {
        Deque<L> next = new ArrayDeque<>();
        Set<L> seen = new HashSet<>();

        for(L vertex : graph.keySet()){
            if(!seen.contains(vertex))
                graphWalk(seen, next, function, Mode.dfs);
        }
    }

    @Override
    public void dfs(L vertex, Consumer<L> function) {
        if(!graph.containsKey(vertex))
            return;

        Deque<L> next = new ArrayDeque<>();
        next.add(vertex);
        Set<L> seen = new HashSet<>();
        graphWalk(seen, next, function, Mode.dfs);
    }


    @Override
    public void bfs(Consumer<L> function) {
        ArrayDeque<L> next = new ArrayDeque<>();
        Set<L> seen = new HashSet<>();

        for(L vertex : graph.keySet()){
            if(!seen.contains(vertex))
                graphWalk(seen, next, function, Mode.bfs);
        }
    }

    @Override
    public void bfs(L vertex, Consumer<L> function) {
        if(!graph.containsKey(vertex))
            return;

        Deque<L> next = new ArrayDeque<>();
        next.add(vertex);
        Set<L> seen = new HashSet<>();
        graphWalk(seen, next, function, Mode.bfs);
    }

    /** 
     * Write the graph into a png. Use the Ggraphviz-java library to do this
     *
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
