package graph;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

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

    private class Edge {
        L src;
        L dst;
        Double value;

        public Edge(L src, L dst, Double value) {
            this.src = src;
            this.dst = dst;
            this.value = value;
        }

    }

    private class Pair {
        L vertex;
        Double cost;

        public Pair(L vertex, Double cost) {
            this.vertex = vertex;
            this.cost = cost;
        }
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

    @Override
    public int minimumSpanningTree(Function<L, Integer> vertex_index) {
        //Get list of edges
        List<Edge> edges = new ArrayList<>();
        for(L src : graph.keySet()) {
            for(L dst : graph.get(src).keySet()) {
                edges.add(new Edge(src, dst, graph.get(src).get(dst)));
            }
        }

        //Sort edges
        Collections.sort(edges, Comparator.comparingDouble(e -> e.value));
        UnionFind unionFind = new UnionFind(edges.size());

        int nb_edge = 0;
        int cost = 0;

        for(Edge edge : edges) {
            if(nb_edge >= graph.keySet().size()-1) {
                break;
            }

            Integer src = vertex_index.apply(edge.src);
            Integer dst = vertex_index.apply(edge.dst);

            //If the two vertex belongs to to the same set they are already connected
            //so adding this edge will create a cycle because spanning tree is an undirected tree
            if(!unionFind.find(src, dst)) {
                nb_edge++;
                cost += edge.value;
                unionFind.union(src, dst);
            }
        }

        return cost;
    }

    private void relaxVertex(L src, L neigh, Map<L, Double> shortestsPaths, Queue<Pair> next) {
        Double previous_cost = shortestsPaths.get(neigh);
        Double new_cost = shortestsPaths.get(src) + graph.get(src).get(neigh);
        if(previous_cost == null || previous_cost > new_cost) {
            shortestsPaths.put(neigh, new_cost);
            next.add(new Pair(neigh, new_cost));
        }
    }

    @Override
    public Map<L, Double> dijkstra(L src) {
        if(!graph.containsKey(src))
            return new HashMap<>();

        Map<L, Double> shortestsPaths = new HashMap<>();
        Queue<Pair> next = new PriorityQueue<>(Comparator.comparingDouble(e -> e.cost));
        Set<L> found = new HashSet<>();
        shortestsPaths.put(src, 0.0);
        next.add(new Pair(src, 0.0));

        while(found.size() < graph.keySet().size()){
            Pair vertex_pair = next.poll();
            if(vertex_pair == null)
                return shortestsPaths;
            L vertex = vertex_pair.vertex;
            if(found.contains(vertex))
                continue;

            found.add(vertex);

            for(L neigh : graph.get(vertex).keySet()) {
                if(found.contains(neigh))
                    continue;
                relaxVertex(vertex, neigh, shortestsPaths, next);
            }
        }

        return shortestsPaths;
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
