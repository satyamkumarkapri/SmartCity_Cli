package smartcity.repository;

import smartcity.model.TrafficNode;
import smartcity.model.TrafficEdge;
import smartcity.structures.DynamicArray;

/**
 * Repository for traffic network vertices (intersections) and edges (arterials).
 */
public class TrafficRepository {
    private final DynamicArray<TrafficNode> nodes = new DynamicArray<>();
    private final DynamicArray<TrafficEdge> edges = new DynamicArray<>();

    public void addNode(TrafficNode node) {
        if (node != null) nodes.add(node);
    }

    public void addEdge(TrafficEdge edge) {
        if (edge != null) edges.add(edge);
    }

    public void clear() {
        nodes.clear();
        edges.clear();
    }

    public int getNodeCount() { return nodes.size(); }
    public int getEdgeCount() { return edges.size(); }

    public TrafficNode getNode(int index) {
        return (index >= 0 && index < nodes.size()) ? nodes.get(index) : null;
    }

    public TrafficEdge getEdge(int index) {
        return (index >= 0 && index < edges.size()) ? edges.get(index) : null;
    }

    public DynamicArray<TrafficNode> getNodes() { return nodes; }
    public DynamicArray<TrafficEdge> getEdges() { return edges; }

    public int findNodeIndexById(String id) {
        if (id == null) return -1;
        for (int i = 0; i < nodes.size(); i++) {
            TrafficNode node = nodes.get(i);
            if (id.equalsIgnoreCase(String.valueOf(node.getNodeId())) ||
                id.equalsIgnoreCase(String.format("TNODE-%03d", node.getNodeId())) ||
                id.equalsIgnoreCase(node.getName())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Builds a CustomFlowNetwork suitable for Dinic, EdmondsKarp, FordFulkerson.
     */
    public smartcity.structures.CustomFlowNetwork buildFlowNetwork() {
        int n = Math.max(1, nodes.size());
        smartcity.structures.CustomFlowNetwork net = new smartcity.structures.CustomFlowNetwork(n);
        for (int i = 0; i < edges.size(); i++) {
            TrafficEdge e = edges.get(i);
            int u = e.getSource();
            int v = e.getTarget();
            if (u >= 0 && u < n && v >= 0 && v < n) {
                net.addEdge(new smartcity.structures.CustomFlowEdge(u, v, e.getCapacity()));
            }
        }
        return net;
    }

    /**
     * Builds a capacity matrix suitable for maximum flow algorithms.
     */
    public int[][] buildCapacityMatrix() {
        int n = Math.max(1, nodes.size());
        int[][] cap = new int[n][n];
        for (int i = 0; i < edges.size(); i++) {
            TrafficEdge e = edges.get(i);
            int u = e.getSource();
            int v = e.getTarget();
            if (u >= 0 && u < n && v >= 0 && v < n) {
                cap[u][v] += e.getCapacity();
            }
        }
        return cap;
    }
}

