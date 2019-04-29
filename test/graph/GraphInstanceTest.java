package graph;

import static org.junit.Assert.*;

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
    //          no,     no,     Max
    //          yes,    no,     Min
    //          no,     yes,    -1
    //          source = destination
    //          already present
    
    // sources: not present
    //          1 present
    //          multiple present
    //          loop
    
    // destination: not present
    //              1 present
    //              multiple present
    //              loop
        
    
    
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
    public void testAddVertice() {
        Graph<String> graph = emptyInstance();
        assertTrue("expected add success", graph.add("a"));
        assertTrue("expected not empty graph", graph.vertices().contains("a"));      
    }
    
    // TODO other tests for instance methods of Graph
    
}
