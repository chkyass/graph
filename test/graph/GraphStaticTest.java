package graph;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    
    // Testing strategy
    // empty:   String
    //          Integer
    //          Object

    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.<String>empty().vertices());
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.<Integer>empty().vertices());
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.<Object>empty().vertices());
    }
        
}
