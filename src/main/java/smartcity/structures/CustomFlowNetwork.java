package smartcity.structures;

/**
 * Custom Flow Network representation with residual edges.
 * Backed by custom DynamicArray (no java.util collections).
 */
public class CustomFlowNetwork {
    private final int V;
    private int E;
    private final DynamicArray<DynamicArray<CustomFlowEdge>> adj;

    public CustomFlowNetwork(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a FlowNetwork must be non-negative");
        this.V = V;
        this.E = 0;
        this.adj = new DynamicArray<>(V);
        for (int v = 0; v < V; v++) {
            this.adj.add(new DynamicArray<>());
        }
    }

    public void addEdge(CustomFlowEdge e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj.get(v).add(e);
        adj.get(w).add(e);
        E++;
    }

    public DynamicArray<CustomFlowEdge> adj(int v) {
        validateVertex(v);
        return adj.get(v);
    }

    public DynamicArray<CustomFlowEdge> edges() {
        DynamicArray<CustomFlowEdge> list = new DynamicArray<>();
        for (int v = 0; v < V; v++) {
            for (CustomFlowEdge e : adj(v)) {
                if (e.to() != v) {
                    list.add(e);
                }
            }
        }
        return list;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void validateVertex(int v) {
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("Vertex " + v + " is not between 0 and " + (V - 1));
        }
    }
}
