package graph;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * Tests for instance methods of Graph.
 * 
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
        
    // add: present
    //      absent
    //      multiple
    
    // remove:  present
    //          absent
    //          multiple
    //          loop
    
    // vertices:    empty
    //              singleton
    //              multiple
        
    // set:     yes,    yes,    0 
    //          no,     no,     double
    //          yes,    no,     double
    //          no,     yes,    -1
    //          source = destination
    //          already present
    
    // sources: not present
    //          1 present
    //          multiple present
    //          loop
    //          verify directed
    
    // destination: not present
    //              1 present
    //              multiple present
    //              loop
    //              verify directed
        
    
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract <T> Graph<T> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    

    @Test
    public void testInitialVerticesEmpty() {
        Graph<String> empty = emptyInstance();
        
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), empty.vertices());
    }

    @Test
    public void testAddAbsent() {
        Graph<String> graph = emptyInstance();
        assertTrue("expected add success", graph.add("a"));
        assertTrue("expected not empty graph", graph.vertices().contains("a"));      
    }
    
    @Test
    public void testAddPresent() {
        Graph<String> graph = emptyInstance();
        assertTrue("expected add success", graph.add("a"));
        assertFalse("expected add fail", graph.add("a"));
        assertTrue("expected not empty graph", graph.vertices().contains("a"));      
    }
    
    @Test
    public void testAddMultiples() {
        Graph<String> graph = emptyInstance();
        assertTrue("expected add success", graph.add("a"));
        assertTrue("expected add success", graph.add("b"));
        assertTrue("expected not empty graph", graph.vertices().containsAll(Arrays.asList("a","b")));      
    }
    
    @Test
    public void removePresent() {
        Graph<String> graph = emptyInstance();
        assertTrue("expected add success", graph.add("a"));
        assertTrue("expected remove success", graph.remove("a"));
        assertEquals("expected empty graph", Collections.emptySet(), graph.vertices());
    }
    
    @Test
    public void removeAbsent() {
        Graph<String> graph = emptyInstance();
        assertTrue("expected add success", graph.add("a"));
        assertFalse("expected remove success", graph.remove("b"));
        assertTrue("expected contains a", graph.vertices().contains("a"));
    }
    
    @Test
    public void removeMultiple() {
        Graph<String> graph = emptyInstance();
        assertTrue("expected add success", graph.add("a"));
        assertTrue("expected add success", graph.add("b"));
        assertTrue("expected add success", graph.add("c"));
        assertTrue("expected remove success", graph.remove("a"));
        assertTrue("expected remove success", graph.remove("b"));
        assertTrue("expected contains c", graph.vertices().contains("c"));
    }
    
    @Test
    public void setExisting() {
        Graph<String> graph = emptyInstance();
        graph.add("a");
        graph.add("b");
        graph.set("a", "b", 1);
        assertTrue("expected contain vertices", graph.vertices().containsAll(Arrays.asList("a", "b")));
        assertTrue("expected a is source for b", graph.sources("b").get("a") == 1);
        assertTrue("expected b is target for a", graph.targets("a").get("b") == 1);
    }
    
    @Test
    public void setNotExisting() {
        Graph<String> graph = emptyInstance();
        graph.add("z");
        graph.set("a", "b", 1.3);
        assertTrue("expected contain vertices", graph.vertices().containsAll(Arrays.asList("a", "b", "z")));
        assertTrue("expected a is source for b", graph.sources("b").get("a") == 1.3);
        assertTrue("expected b is target for a", graph.targets("a").get("b") == 1.3);
    }
    
    @Test
    public void setFirstExisting() {
        Graph<String> graph = emptyInstance();
        graph.add("a");
        graph.set("a", "b", -1.3);
        assertTrue("expected contain vertices", graph.vertices().containsAll(Arrays.asList("a", "b")));
        assertTrue("expected a is source for b", graph.sources("b").get("a") == -1.3);
        assertTrue("expected b is target for a", graph.targets("a").get("b") == -1.3);
    }
    
    @Test
    public void setSecondExisting() {
        Graph<String> graph = emptyInstance();
        graph.add("b");
        graph.set("a", "b", -70.78680);
        assertTrue("expected contain vertices", graph.vertices().containsAll(Arrays.asList("a", "b")));
        assertTrue("expected a is source for b", graph.sources("b").get("a") == -70.78680);
        assertTrue("expected b is target for a", graph.targets("a").get("b") == -70.78680);
    }
    
    @Test
    public void setLoop() {
        Graph<String> graph = emptyInstance();
        graph.add("b");
        graph.set("a", "a", 0);
        assertTrue("expected contain vertices", graph.vertices().containsAll(Arrays.asList("a", "b")));
        assertTrue("expected a is source for b", graph.sources("a").get("a") == 0);
        assertTrue("expected b is target for a", graph.targets("a").get("a") == 0);
    }
    
    @Test
    public void setChange() {
        Graph<String> graph = emptyInstance();
        graph.set("a", "b", 0);
        graph.set("a", "b", 20);
        assertTrue("expected contain vertices", graph.vertices().containsAll(Arrays.asList("a", "b")));
        assertTrue("expected a is source for b", graph.sources("b").get("a") == 20);
        assertTrue("expected b is target for a", graph.targets("a").get("b") == 20);
    }
    
    @Test
    public void sourceMultiplePresent() {
        Graph<String> graph = emptyInstance();
        graph.set("c", "a", 1);
        graph.set("b", "a", 0);

        assertTrue("expected b source of a", graph.sources("a").get("b") == 0);
        assertTrue("expected a source of a", graph.sources("a").get("c") == 1);
    }
    
    @Test
    public void targetMultiplePresent() {
        Graph<String> graph = emptyInstance();
        graph.set("a", "b", 0);
        graph.set("a", "c", 1);

        assertTrue("expected b target of a", graph.targets("a").get("b") == 0);
        assertTrue("expected c target of a", graph.targets("a").get("c") == 1);
        assertTrue("expected b source of a", graph.sources("b").get("a") == 0);
        assertTrue("expected a source of c", graph.sources("c").get("a") == 1);
        
    }
     @Test
     public void removeLoop() {
         Graph<Integer> graph = emptyInstance();
         graph.set(2, 2, 3);
         graph.set(1, 1, 5);
         graph.set(3, 9, 0);
         assertTrue("expect removed", graph.remove(2));
         assertTrue("expect removed", graph.vertices().containsAll(Arrays.asList(1, 3, 9)));
         assertTrue("expect removed", graph.vertices().size() == 3);
     }

    private StringBuilder lambda_result;

    @Test
    public void dfs() {
         AdjListGraph<Character> graph = new AdjListGraph<>();
         graph.set('a', 'b', 3);
         graph.set('b', 'c', 5);
         graph.set('a', 'd', 0);
         graph.set('d', 'e', 4);
         lambda_result = new StringBuilder();
         graph.dfs('a', n -> lambda_result.append(n));
         assertTrue(lambda_result.toString().equals("abcde")
                 || lambda_result.toString().equals("adebc"));
    }

    @Test
    public void bfs() {
        AdjListGraph<Character> graph = new AdjListGraph<>();
        graph.set('a', 'b', 3);
        graph.set('b', 'c', 5);
        graph.set('a', 'd', 0);
        graph.set('d', 'e', 4);
        lambda_result = new StringBuilder();
        graph.bfs('a', n -> lambda_result.append(n));
        assertTrue(lambda_result.toString().equals("abdce"));
    }

    @Test
    public void minimumSpanningTree() {
        AdjListGraph<Integer> graph = new AdjListGraph<>();
        graph.set(1, 2, 1);
        graph.set(2, 3, 2);
        graph.set(1, 3, 3);
        graph.set(4, 3, 5);
        graph.set(1, 4, 4);
        assertEquals(graph.minimumSpanningTree(e -> e), 7);
    }

    @Test
    public void dijkstra(){
        AdjListGraph<Character> graph = new AdjListGraph<>();
        graph.set('s', 'a', 10);
        graph.set('s', 'c', 5);
        graph.set('c', 'a', 3);
        graph.set('a', 'c', 2);
        graph.set('a', 'b', 1);
        graph.set('c', 'b', 9);
        graph.set('c', 'd', 2);
        graph.set('b', 'd', 4);
        graph.set('d', 'b', 6);

        Map<Character, Double> res = graph.dijkstra('s');
        System.out.println(res);
        assertTrue(res.get('s') == 0.0);
        assertTrue(res.get('a') == 8.0);
        assertTrue(res.get('c') == 5.0);
        assertTrue(res.get('b') == 9.0);
        assertTrue(res.get('d') == 7.0);


    }
    
}
