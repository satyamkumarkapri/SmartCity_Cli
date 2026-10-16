package smartcity.services;

import smartcity.algorithms.complexity.KnapsackFPTAS;
import smartcity.algorithms.dynamicprogramming.MatrixChainMultiplication;
import smartcity.algorithms.dynamicprogramming.SetCoverBitmask;
import smartcity.algorithms.dynamicprogramming.SumOverSubsetsDP;
import smartcity.algorithms.graph.BipartiteMatching;
import smartcity.algorithms.graph.Dinic;
import smartcity.model.EnergyResource;
import smartcity.model.ResourceDemand;
import smartcity.model.WaterResource;
import smartcity.repository.ResourceRepository;
import smartcity.structures.CustomFlowEdge;
import smartcity.structures.CustomFlowNetwork;
import smartcity.structures.DynamicArray;
import smartcity.util.ConsoleUtil;

/**
 * Service orchestrating Water & Energy Utilities and Municipal Resource Optimization
 * using M3 (Bitmask DP, Matrix-Chain) and M4 (Network Flow).
 */
public class ResourceManagementService {
    private final ResourceRepository resourceRepo;

    public static class AllocationResult {
        public final int matchedCount;
        public final DynamicArray<BipartiteMatching.MatchingPair> pairs;

        public AllocationResult(int matchedCount, DynamicArray<BipartiteMatching.MatchingPair> pairs) {
            this.matchedCount = matchedCount;
            this.pairs = pairs != null ? pairs : new DynamicArray<>();
        }
    }

    public static class KnapsackResult {
        public final double totalWeight;
        public final double totalBenefit;

        public KnapsackResult(double totalWeight, double totalBenefit) {
            this.totalWeight = totalWeight;
            this.totalBenefit = totalBenefit;
        }
    }

    public ResourceManagementService(ResourceRepository resourceRepo) {
        this.resourceRepo = resourceRepo;
    }

    public void listResources() {
        ConsoleUtil.printSection("Water Treatment Plants (" + resourceRepo.getWaterCount() + " Total)");
        int wShow = Math.min(8, resourceRepo.getWaterCount());
        for (int i = 0; i < wShow; i++) {
            WaterResource w = resourceRepo.getWaterResources().get(i);
            System.out.println("  " + w.toString());
        }

        ConsoleUtil.printSection("Energy Grid Substations (" + resourceRepo.getEnergyCount() + " Total)");
        int eShow = Math.min(8, resourceRepo.getEnergyCount());
        for (int i = 0; i < eShow; i++) {
            EnergyResource e = resourceRepo.getEnergyResources().get(i);
            System.out.println("  " + e.toString());
        }

        ConsoleUtil.printSection("Municipal Zone Utility Demands (" + resourceRepo.getDemandCount() + " Total)");
        int dShow = Math.min(8, resourceRepo.getDemandCount());
        for (int i = 0; i < dShow; i++) {
            ResourceDemand d = resourceRepo.getDemands().get(i);
            System.out.println("  " + d.toString());
        }
    }

    /**
     * Models water distribution flow network from reservoirs to consumption districts.
     */
    public void computeWaterDistributionFlow() {
        ConsoleUtil.printSection("Water Pipeline Distribution Maximum Flow (Dinic)");
        int wCount = resourceRepo.getWaterCount();
        if (wCount == 0) {
            ConsoleUtil.printWarning("No water facilities loaded.");
            return;
        }

        // Model network: Source (0) -> Treatment Plants (1..wCount) -> Municipal Distribution Zones -> Sink
        int n = wCount + 6;
        int source = 0;
        int sink = n - 1;
        CustomFlowNetwork net = new CustomFlowNetwork(n);

        // Connect source to each treatment plant with plant's daily capacity
        for (int i = 0; i < wCount; i++) {
            WaterResource w = resourceRepo.getWaterResources().get(i);
            net.addEdge(new CustomFlowEdge(source, i + 1, w.getCapacityMld()));
            // Connect treatment plants to 4 regional distribution hubs
            int hub = wCount + 1 + (i % 4);
            net.addEdge(new CustomFlowEdge(i + 1, hub, w.getCapacityMld() * 0.75));
        }

        // Connect hubs to sink
        for (int h = 1; h <= 4; h++) {
            net.addEdge(new CustomFlowEdge(wCount + h, sink, 600));
        }

        long start = System.nanoTime();
        Dinic dinic = new Dinic(net, source, sink);
        int maxFlowMld = (int) dinic.value();
        long elapsed = System.nanoTime() - start;

        System.out.printf("  Total Delivered Water Volume : %s%,d Megaliters / day (MLD)%s%n",
                ConsoleUtil.BRIGHT_GREEN, maxFlowMld, ConsoleUtil.RESET);
        System.out.printf("  Computation Time (Dinic)     : %,d ns (%.4f ms)%n",
                elapsed, elapsed / 1_000_000.0);
        ConsoleUtil.printSuccess("Municipal water distribution flow successfully balanced.");
    }

    public AllocationResult allocateResourcesBipartite() {
        int eCount = Math.min(15, resourceRepo.getEnergyCount());
        int dCount = Math.min(25, resourceRepo.getDemandCount());
        if (eCount == 0 || dCount == 0) {
            return new AllocationResult(0, new DynamicArray<>());
        }

        boolean[][] bp = new boolean[eCount][dCount];
        for (int i = 0; i < eCount; i++) {
            EnergyResource e = resourceRepo.getEnergyResources().get(i);
            for (int j = 0; j < dCount; j++) {
                ResourceDemand d = resourceRepo.getDemands().get(j);
                if (e.getRatedMva() >= d.getRequiredQuantity()) {
                    bp[i][j] = true;
                }
            }
        }

        BipartiteMatching.MatchingResult res = BipartiteMatching.findMatching(bp);
        return new AllocationResult(res.matchCount, res.pairs);
    }

    public KnapsackResult optimizeEnergyDispatch(double maxCapacity, double epsilon) {
        int count = Math.min(20, resourceRepo.getEnergyCount());
        int[] weights = new int[count];
        int[] values = new int[count];
        for (int i = 0; i < count; i++) {
            EnergyResource e = resourceRepo.getEnergyResources().get(i);
            weights[i] = Math.max(1, e.getRatedMva());
            values[i] = Math.max(1, (int) (e.getRatedMva() * (100.0 - e.getCurrentLoadPct())));
        }

        KnapsackFPTAS.FPTASResult res = KnapsackFPTAS.solve(weights, values, (int) maxCapacity, epsilon);
        return new KnapsackResult(res.totalWeight, res.totalValue);
    }

    /**
     * Optimizes multi-zone municipal resource allocation using Bitmask DP.
     */
    public void optimizeResourceAllocationBitmask() {
        ConsoleUtil.printSection("Resource Allocation Optimization (Bitmask DP)");
        int dCount = Math.min(10, resourceRepo.getDemandCount());
        if (dCount == 0) {
            ConsoleUtil.printWarning("No demands loaded.");
            return;
        }

        System.out.printf("Evaluating optimal utility bundle for %d distinct municipal zones...%n", dCount);

        // Model 6 municipal supplier packages
        int[] subsets = new int[]{
                0b0000000111, // Supplier 1 covers zones 0, 1, 2
                0b0000011100, // Supplier 2 covers zones 2, 3, 4
                0b0001110000, // Supplier 3 covers zones 4, 5, 6
                0b0111000000, // Supplier 4 covers zones 6, 7, 8
                0b1100000001, // Supplier 5 covers zones 0, 8, 9
                0b1111111111  // Supplier 6 covers all zones
        };
        int[] costs = new int[]{45000, 52000, 48000, 61000, 39000, 195000};

        long start = System.nanoTime();
        SetCoverBitmask.SetCoverResult res = SetCoverBitmask.solveSetCover(dCount, subsets, costs);
        long elapsed = System.nanoTime() - start;

        System.out.printf("  Minimum Total Procurement Cost : %s$%,d%s%n",
                ConsoleUtil.BRIGHT_GREEN, res.minCost, ConsoleUtil.RESET);
        System.out.printf("  Selected Supplier Packages     : %s%n", res.selectedSubsets.toString());
        System.out.printf("  Bitmask DP Runtime             : %,d ns (%.4f ms)%n", elapsed, elapsed / 1_000_000.0);
        ConsoleUtil.printSuccess("Bitmask DP municipal resource coverage optimized.");
    }

    /**
     * Matrix Chain Multiplication optimizer for multi-stage sensor correlation covariance matrices.
     */
    public void optimizeAnalyticsMatrixChain() {
        ConsoleUtil.printSection("Analytics Operations Optimization (Matrix Chain Multiplication)");
        // Sensor feature matrices: e.g. 5 analytical steps with dimensions:
        // Traffic (100x30), Air Quality (30x50), Weather (50x20), Energy (20x40), Hospital (40x10)
        int[] dims = new int[]{100, 30, 50, 20, 40, 10};

        long start = System.nanoTime();
        MatrixChainMultiplication.Result res = MatrixChainMultiplication.multiplyOrder(dims);
        long elapsed = System.nanoTime() - start;

        System.out.println("  Analytical Matrix Dimensions : [100x30] x [30x50] x [50x20] x [20x40] x [40x10]");
        System.out.printf("  Minimum Scalar Multiplications: %s%,d operations%s%n",
                ConsoleUtil.BRIGHT_GREEN, res.minCost, ConsoleUtil.RESET);
        System.out.println("  Optimal Parenthesization     : " + ConsoleUtil.BRIGHT_CYAN + res.parenthesization + ConsoleUtil.RESET);
        System.out.printf("  Optimization Time            : %,d ns (%.4f ms)%n", elapsed, elapsed / 1_000_000.0);
        ConsoleUtil.printSuccess("Optimal matrix chain multiplication order determined.");
    }

    /**
     * Solves small municipal resource subset evaluation and selection using Sum-Over-Subsets (SOS DP).
     * Implements Image 2 requirement: "DP on subsets for small city-resource selection problems."
     * Transforms an O(3^N) submask enumeration into an optimal O(N * 2^N) bit-dimensional DP.
     */
    public void optimizeSubsetResourceSelection() {
        ConsoleUtil.printSection("Municipal Resource Selection (DP on Subsets / SOS DP)");
        // Model 4 strategic city zones: 0: Hospital District, 1: SCADA Water Grid, 2: Metro Transit, 3: Emergency Core
        String[] zones = {"Hospital District", "SCADA Water Grid", "Metro Transit Hub", "Emergency Core"};
        int n = zones.length;
        int numCombinations = 1 << n; // 16 combinations

        // Base marginal utility contribution when ONLY that specific exact subset of equipment is online
        long[] baseUtility = new long[numCombinations];
        baseUtility[0b0000] = 0;   // No resources
        baseUtility[0b0001] = 120; // Only Zone 0
        baseUtility[0b0010] = 150; // Only Zone 1
        baseUtility[0b0100] = 90;  // Only Zone 2
        baseUtility[0b1000] = 200; // Only Zone 3
        baseUtility[0b0011] = 80;  // Synergy bonus for Zone 0 + 1
        baseUtility[0b0101] = 50;  // Synergy bonus for Zone 0 + 2
        baseUtility[0b1001] = 110; // Synergy bonus for Zone 0 + 3
        baseUtility[0b0110] = 70;  // Synergy bonus for Zone 1 + 2
        baseUtility[0b1010] = 95;  // Synergy bonus for Zone 1 + 3
        baseUtility[0b1100] = 60;  // Synergy bonus for Zone 2 + 3
        baseUtility[0b0111] = 130; // Synergy 0+1+2
        baseUtility[0b1011] = 140; // Synergy 0+1+3
        baseUtility[0b1101] = 115; // Synergy 0+2+3
        baseUtility[0b1110] = 125; // Synergy 1+2+3
        baseUtility[0b1111] = 250; // Global city-wide full deployment synergy

        long start = System.nanoTime();
        long[] cumulativeUtility = SumOverSubsetsDP.computeSOS(baseUtility, n);
        long elapsed = System.nanoTime() - start;

        System.out.printf("Evaluating %d Municipal Zones (2^%d = %d Subset Configurations):%n", n, n, numCombinations);
        for (int i = 0; i < n; i++) {
            System.out.printf("  Zone [%d] (Bit 2^%d = %2d): %s%n", i, i, 1 << i, zones[i]);
        }
        System.out.println();

        System.out.println(ConsoleUtil.BOLD_BLACK + " Mask │ Binary │ Included City Zones                  │ Cumulative Utility (SOS DP)" + ConsoleUtil.RESET);
        System.out.println("──────┼────────┼──────────────────────────────────────┼────────────────────────────");
        for (int mask = 0; mask < numCombinations; mask++) {
            StringBuilder desc = new StringBuilder();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    if (desc.length() > 0) desc.append(" + ");
                    desc.append(zones[i].split(" ")[0]);
                }
            }
            if (desc.length() == 0) desc.append("(None - Inactive)");

            String binStr = String.format("%4s", Integer.toBinaryString(mask)).replace(' ', '0');
            System.out.printf("  %2d  │  %s  │ %-36s │ %s%,8d points%s%n",
                    mask, binStr, desc.toString(), ConsoleUtil.BRIGHT_GREEN, cumulativeUtility[mask], ConsoleUtil.RESET);
        }
        System.out.println("──────┴────────┴──────────────────────────────────────┴────────────────────────────");

        int bruteForceOps = (int) Math.pow(3, n); // 3^4 = 81
        int sosOps = n * (1 << n);               // 4 * 16 = 64
        System.out.printf(" Algorithmic Efficiency : SOS DP O(N * 2^N) = %d ops vs Brute-Force O(3^N) = %d ops%n",
                sosOps, bruteForceOps);
        System.out.printf(" Execution Time         : %,d ns (%.4f ms)%n", elapsed, elapsed / 1_000_000.0);
        ConsoleUtil.printSuccess("DP on Subsets (Sum-Over-Subsets) evaluation completed successfully.");
    }
}
