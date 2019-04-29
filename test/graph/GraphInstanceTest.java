package graph;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

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
    public abstract Graph<String> emptyInstance();
    
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
        assertFalse("expected add success", graph.add("b"));
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
        assertTrue("expected empty graph", graph.vertices().contains("a"));
    }
    
    @Test
    public void removeMultiple() {
        Graph<String> graph = emptyInstance();
        assertTrue("expected add success", graph.add("a"));
        assertTrue("expected add success", graph.add("b"));
        assertTrue("expected add success", graph.add("c"));
        assertFalse("expected remove success", graph.remove("a"));
        assertFalse("expected remove success", graph.remove("b"));
        assertTrue("expected empty graph", graph.vertices().contains("c"));
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
        assertTrue("expected a is source for b", graph.sources("b").get("a") == 1.3);
        assertTrue("expected b is target for a", graph.targets("a").get("b") == 1.3);
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
        assertTrue("expected a is source for b", graph.sources("a").get("a") == 20);
        assertTrue("expected b is target for a", graph.targets("a").get("a") == 20);
    }
    
    @Test
    public void sourceAbsent() {
        Graph<String> graph = emptyInstance();
        graph.set("a", "b", 0);
        assertTrue("expected empty vertices", graph.sources("z").isEmpty());
    }
    
    @Test
    public void sourceMultiplePresent() {
        Graph<String> graph = emptyInstance();
        graph.set("b", "a", 0);
        graph.set("c", "a", 1);

        assertTrue("expected empty vertices", graph.sources("a").get("b") == 0);
        assertTrue("expected empty vertices", graph.sources("a").get("c") == 1);
        assertTrue("expected empty vertices", graph.sources("b").isEmpty());
        assertTrue("expected empty vertices", graph.sources("c").isEmpty());

    }
    
    @Test
    public void targetMultiplePresent() {
        Graph<String> graph = emptyInstance();
        graph.set("a", "b", 0);
        graph.set("a", "c", 1);

        assertTrue("expected empty vertices", graph.targets("a").get("b") == 0);
        assertTrue("expected empty vertices", graph.targets("a").get("c") == 1);
        assertTrue("expected empty vertices", graph.targets("b").get("c") == 1);
        assertTrue("expected empty vertices", graph.targets("b").get("c") == 1);
        
    }
}
