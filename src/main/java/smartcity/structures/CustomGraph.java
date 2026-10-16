package smartcity.structures;

/**
 * Custom Directed/Undirected Graph representation with adjacency lists.
 * Implemented using custom DynamicArray and IntArrayList (no java.util collections).
 */
public class CustomGraph {
    private final int V;
    private int E;
    private final DynamicArray<IntArrayList> adj;
    private final boolean directed;

    public CustomGraph(int vertices, boolean directed) {
        if (vertices < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.V = vertices;
        this.E = 0;
        this.directed = directed;
        this.adj = new DynamicArray<>(vertices);
        for (int i = 0; i < vertices; i++) {
            this.adj.add(new IntArrayList());
        }
    }

    public CustomGraph(int vertices) {
        this(vertices, false);
    }

    public void addEdge(int u, int v) {
        validateVertex(u);
        validateVertex(v);
        adj.get(u).add(v);
        if (!directed && u != v) {
            adj.get(v).add(u);
        }
        E++;
    }

    public IntArrayList adj(int v) {
        validateVertex(v);
        return adj.get(v);
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public boolean isDirected() {
        return directed;
    }

    public void validateVertex(int v) {
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("Vertex " + v + " is not between 0 and " + (V - 1));
        }
    }
}
