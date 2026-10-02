package smartcityx.algorithms.flow;

public class FlowEdge {
    private final int from;
    private final int to;
    private final double capacity;
    private double flow;

    public FlowEdge(int from, int to, double capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.flow = 0.0;
    }

    public int from() { return from; }
    public int to() { return to; }
    public double capacity() { return capacity; }
    public double flow() { return flow; }
    public double residualCapacityTo(int vertex) {
        if (vertex == from) return flow;
        else if (vertex == to) return capacity - flow;
        else throw new IllegalArgumentException();
    }

    public void addResidualFlowTo(int vertex, double delta) {
        if (vertex == from) flow -= delta;
        else if (vertex == to) flow += delta;
        else throw new IllegalArgumentException();
    }
    
    public int other(int vertex) {
        if (vertex == from) return to;
        else if (vertex == to) return from;
        else throw new IllegalArgumentException();
    }
}
