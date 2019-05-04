package graph;

import static org.junit.Assert.*;
import org.junit.Test;

public class HashMapGraphTest extends GraphInstanceTest {
    
    public <T> Graph<T> emptyInstance() {
        return new HashtableGraph<>();
    }
    
    
    @Test
    public void printNodeAlone() {
        HashtableGraph<Integer> graph = new HashtableGraph<>();
        graph.add(10);
        graph.writeToPng("test/graph/prints/alone");
    }
    
    @Test
    public void printLoop() {
        HashtableGraph<String> graph = new HashtableGraph<>();
        graph.set("a", "a", 1000);
        graph.writeToPng("test/graph/printsloop");
    }
    
    @Test
    public void printBidirectional() {
        HashtableGraph<String> graph = new HashtableGraph<>();
        graph.set("a", "b", 2);
        graph.set("b", "a", 6);
        graph.writeToPng("test/graph/prints/biderctional");

    }
    
    @Test
    public void printMix() {
        HashtableGraph<String> graph = new HashtableGraph<>();
        graph.set("a", "a", 1000);
        graph.set("a", "b", 2);
        graph.set("b", "a", 6);
        graph.set("a", "c", 2);
        graph.set("b", "d", 2);
        graph.set("h", "laios", 9);
        graph.set("d", "laios", 10);
        graph.add("alone");
        graph.writeToPng("test/graph/prints/mix");
    }
}
