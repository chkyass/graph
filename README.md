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
The edges of the graph are represented by a hash table with the destination vertex as the key and the weight as the value. The graph is stored in a hashmap with the vertices as  key and edges coming from it as value. The purpose of this representation is to take advantage of the constant removal and access time of hashmap.

A second hashmap associates a vertex with all the edges pointing to it. These edges are also represented as hashmap. This makes it possible to obtain all the edges that target a vertex in constant access time. In addition, the removal of all the dead edges of the graph when a vertex is deleted can be done without to have to run trough all the graph.

### Display
A representation of the graph can be wrote in a PNG file. This, is implemented using graphviz library.

### Strategy
I chose to optimize access to time at the expense of the space used. For this reason, HashtableGraph stores a mapping of each vertex on all edges of which it is the source and destination. The bad side of this is that I am storing the graph twice.
