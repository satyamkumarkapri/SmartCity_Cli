package smartcity.repository;

import smartcity.model.InfrastructureVertex;
import smartcity.model.InfrastructureEdge;
import smartcity.structures.DynamicArray;

/**
 * Repository for city civil infrastructure network graph.
 */
public class InfrastructureRepository {
    private final DynamicArray<InfrastructureVertex> vertices = new DynamicArray<>();
    private final DynamicArray<InfrastructureEdge> edges = new DynamicArray<>();

    public void addVertex(InfrastructureVertex v) { if (v != null) vertices.add(v); }
    public void addEdge(InfrastructureEdge e) { if (e != null) edges.add(e); }

    public void clear() {
        vertices.clear();
        edges.clear();
    }

    public int getVertexCount() { return vertices.size(); }
    public int getEdgeCount() { return edges.size(); }

    public DynamicArray<InfrastructureVertex> getVertices() { return vertices; }
    public DynamicArray<InfrastructureEdge> getEdges() { return edges; }

    /**
     * Builds standard 0-indexed edge array int[M][2] for Vertex Cover algorithms.
     */
    public int[][] buildEdgePairs() {
        int m = edges.size();
        int[][] pairs = new int[m][2];
        for (int i = 0; i < m; i++) {
            InfrastructureEdge e = edges.get(i);
            // 1-based vertex ID to 0-based
            pairs[i][0] = Math.max(0, e.getU() - 1);
            pairs[i][1] = Math.max(0, e.getV() - 1);
        }
        return pairs;
    }

    /**
     * Builds standard adjacency matrix boolean[N][N] for Vertex Cover algorithms.
     */
    public boolean[][] buildAdjacencyMatrix() {
        int n = Math.max(1, vertices.size());
        boolean[][] adj = new boolean[n][n];
        for (int i = 0; i < edges.size(); i++) {
            InfrastructureEdge e = edges.get(i);
            int u = Math.max(0, e.getU() - 1);
            int v = Math.max(0, e.getV() - 1);
            if (u < n && v < n) {
                adj[u][v] = true;
                adj[v][u] = true;
            }
        }
        return adj;
    }
}

