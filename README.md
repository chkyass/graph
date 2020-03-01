# graph

## Prerequisites

### graphviz-java
```
<dependency>
    <groupId>guru.nidi</groupId>
    <artifactId>graphviz-java</artifactId>
    <version>0.8.3</version>
</dependency>
```
## Logging
```
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
</dependency>
```

### Implementation
 - Graph represented using adjacency lists. Adjacency lists are represented using HashMap.
 - The graph supports DFS and BFS to apply a lambda function on vertices.
 - Dijkstra algorithm can be applied to the graph and uses PriorityQueue to decrease the time complexity.
 - Implementation of Kruskal's algorithm to find the minimum spanning tree of the graph. It uses the UnionFind structure to detect cycles in Logarithmic time.

### Display
A representation of the graph can be written in a PNG file. This is implemented using the Graphviz library.

