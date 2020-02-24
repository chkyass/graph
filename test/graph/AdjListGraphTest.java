package graph;

import org.junit.Test;

public class HashMapGraphTest extends GraphInstanceTest {
    
    public <T> Graph<T> emptyInstance() {
        return new AdjListGraph<>();
    }
    
    @Test
    public void printNodeAlone() {
        AdjListGraph<Integer> graph = new AdjListGraph<>();
        graph.add(10);
        graph.writeToPng("output/alone");
    }
    
    @Test
    public void printLoop() {
        AdjListGraph<String> graph = new AdjListGraph<>();
        graph.set("a", "a", 1000);
        graph.writeToPng("output/printsloop");
    }
    
    @Test
    public void printBidirectional() {
        AdjListGraph<String> graph = new AdjListGraph<>();
        graph.set("a", "b", 2);
        graph.set("b", "a", 6);
        graph.writeToPng("output/biderctional");

    }
    
    @Test
    public void printMix() {
        AdjListGraph<String> graph = new AdjListGraph<>();
        graph.set("a", "a", 1000);
        graph.set("a", "b", 2);
        graph.set("b", "a", 6);
        graph.set("a", "c", 2);
        graph.set("b", "d", 2);
        graph.set("h", "laios", 9);
        graph.set("d", "laios", 10);
        graph.add("alone");
        graph.writeToPng("output/mix");
    }
}
