package smartcity.model;

import smartcity.structures.DynamicArray;
import smartcity.structures.CustomFlowEdge;
import smartcity.util.ConsoleUtil;

/**
 * Result container for Network Flow, Minimum Cut, and Bipartite Matching algorithms.
 */
public class FlowResult extends QueryResult {
    private final double maxFlow;
    private final double minCost;
    private final int source;
    private final int sink;
    private final int augmentationsCount;
    private final DynamicArray<CustomFlowEdge> minCutEdges;
    private final double minCutCapacity;
    private final boolean flowCutDualityVerified;

    public FlowResult(boolean success, String algorithmName, AlgorithmMetrics metrics,
                      String explanation, double maxFlow, double minCost, int source, int sink,
                      int augmentationsCount, DynamicArray<CustomFlowEdge> minCutEdges,
                      double minCutCapacity, boolean flowCutDualityVerified) {
        super(success, algorithmName, metrics, explanation);
        this.maxFlow = maxFlow;
        this.minCost = minCost;
        this.source = source;
        this.sink = sink;
        this.augmentationsCount = augmentationsCount;
        this.minCutEdges = minCutEdges != null ? minCutEdges : new DynamicArray<>();
        this.minCutCapacity = minCutCapacity;
        this.flowCutDualityVerified = flowCutDualityVerified;
    }

    public double getMaxFlow() { return maxFlow; }
    public double getMinCost() { return minCost; }
    public DynamicArray<CustomFlowEdge> getMinCutEdges() { return minCutEdges; }
    public double getMinCutCapacity() { return minCutCapacity; }
    public boolean isFlowCutDualityVerified() { return flowCutDualityVerified; }

    @Override
    public String formatOutput() {
        StringBuilder sb = new StringBuilder(formatCommonHeader("NETWORK FLOW & RESIDUAL ANALYSIS"));
        sb.append(ConsoleUtil.BRIGHT_BLUE + ConsoleUtil.BOLD + "───◆ Flow & Cut Metrics ◆──────────────────────────────────────────────────────" + ConsoleUtil.RESET + "\n");
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : Node %d -> Node %d\n", "Source / Sink", source, sink));
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : " + ConsoleUtil.BRIGHT_GREEN + "%.2f units\n" + ConsoleUtil.RESET, "Maximum Flow", maxFlow));
        if (minCost > 0) {
            sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %.2f\n", "Total Flow Cost", minCost));
        }
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %,d augmentations\n", "Augmentations", augmentationsCount));
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %.2f capacity units\n", "Min-Cut Capacity", minCutCapacity));
        String dualityLabel = flowCutDualityVerified ? (ConsoleUtil.BRIGHT_GREEN + "VERIFIED (Max-Flow == Min-Cut)" + ConsoleUtil.RESET)
                : (ConsoleUtil.RED + "DISCREPANCY DETECTED" + ConsoleUtil.RESET);
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %s\n", "Duality Theorem", dualityLabel));

        if (!minCutEdges.isEmpty()) {
            sb.append("\n" + ConsoleUtil.BRIGHT_BLUE + ConsoleUtil.BOLD + "───◆ Bottleneck Min-Cut Edges (" + minCutEdges.size() + " cuts) ◆───────────────────────────────────────" + ConsoleUtil.RESET + "\n");
            for (CustomFlowEdge e : minCutEdges) {
                sb.append(String.format("   " + ConsoleUtil.CYAN + "Node %-3d" + ConsoleUtil.RESET + " ──(" + ConsoleUtil.YELLOW + "cap: %.1f" + ConsoleUtil.RESET + ")──> " + ConsoleUtil.CYAN + "Node %-3d" + ConsoleUtil.RESET + "\n",
                        e.from(), e.capacity(), e.to()));
            }
        }
        return sb.toString();
    }
}
