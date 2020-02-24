package graph;

public class UnionFind {
    private int sets [];
    //Size of sets
    private int sizes [];

    public UnionFind(int size) {
        sets = new int[size];
        sizes = new int [size];
        for (int i = 0; i < size; i++) {
            sets[i] = i;
            sizes[i] = 1;
        }
    }

    private int root(int e) {
        while(sets[e] != e) {
            // make node to point to his grand parent to flatten the tree
            sets[e] = sets[sets[e]];
            e = sets[e];
        }

        return e;
    }

    // Log(N)
    public boolean find(int a, int b) {
        return root(a) == root(b);
    }

    // Log(N)
    public void union(int a, int b) {
        int i = root(a);
        int j = root(b);

        if(sizes[i] <= sizes[j]) {
            sets[i] = j;
            sizes[j] += sizes[i];
        } else {
            sets[j] = i;
            sizes[i] += sizes[j];
        }
    }
}
