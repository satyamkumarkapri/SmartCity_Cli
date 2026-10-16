package smartcity.structures;

/**
 * Custom Flow Edge representing a directed edge in a flow network
 * with capacity, flow, and optional unit cost (for min-cost max-flow).
 */
public class CustomFlowEdge {
    private final int from;
    private final int to;
    private final double capacity;
    private double flow;
    private final double cost; // Cost per unit of flow

    public CustomFlowEdge(int from, int to, double capacity, double cost) {
        if (from < 0 || to < 0) throw new IllegalArgumentException("Vertex index cannot be negative");
        if (capacity < 0) throw new IllegalArgumentException("Edge capacity must be non-negative");
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.flow = 0.0;
        this.cost = cost;
    }

    public CustomFlowEdge(int from, int to, double capacity) {
        this(from, to, capacity, 0.0);
    }

    public int from() { return from; }
    public int to() { return to; }
    public double capacity() { return capacity; }
    public double flow() { return flow; }
    public double cost() { return cost; }

    public int other(int vertex) {
        if (vertex == from) return to;
        else if (vertex == to) return from;
        else throw new IllegalArgumentException("Illegal endpoint: " + vertex);
    }

    public double residualCapacityTo(int vertex) {
        if (vertex == from) return flow; // Backward edge
        else if (vertex == to) return capacity - flow; // Forward edge
        else throw new IllegalArgumentException("Illegal endpoint: " + vertex);
    }

    public void addResidualFlowTo(int vertex, double delta) {
        if (vertex == from) flow -= delta;
        else if (vertex == to) flow += delta;
        else throw new IllegalArgumentException("Illegal endpoint: " + vertex);
    }

    public double costTo(int vertex) {
        if (vertex == to) return cost;
        else if (vertex == from) return -cost;
        else throw new IllegalArgumentException("Illegal endpoint: " + vertex);
    }

    @Override
    public String toString() {
        return String.format("%d -> %d  %.1f/%.1f (cost: %.1f)", from, to, flow, capacity, cost);
    }
}
