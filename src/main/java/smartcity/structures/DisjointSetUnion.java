package smartcity.structures;

/**
 * Disjoint-Set Union (DSU / Union-Find) with path compression and union-by-rank.
 * Almost linear amortized time O(alpha(N)) per operation.
 */
public class DisjointSetUnion {
    private final int[] parent;
    private final int[] rank;
    private int numSets;

    public DisjointSetUnion(int n) {
        if (n <= 0) throw new IllegalArgumentException("Set size must be positive");
        this.parent = new int[n];
        this.rank = new int[n];
        this.numSets = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int i) {
        if (parent[i] != i) {
            parent[i] = find(parent[i]); // Path compression
        }
        return parent[i];
    }

    public boolean union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);
        if (rootI == rootJ) return false;

        // Union by rank
        if (rank[rootI] < rank[rootJ]) {
            parent[rootI] = rootJ;
        } else if (rank[rootI] > rank[rootJ]) {
            parent[rootJ] = rootI;
        } else {
            parent[rootJ] = rootI;
            rank[rootI]++;
        }
        numSets--;
        return true;
    }

    public boolean connected(int i, int j) {
        return find(i) == find(j);
    }

    public int getNumSets() {
        return numSets;
    }
}
