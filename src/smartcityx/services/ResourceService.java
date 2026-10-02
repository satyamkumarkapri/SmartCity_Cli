package smartcityx.services;

import smartcityx.algorithms.dp.BitmaskDP;
import smartcityx.algorithms.dp.SubsetDP;
import smartcityx.model.Resource;
import smartcityx.repository.ResourceRepository;
import smartcityx.util.ComplexityUtil;
import java.util.List;

public class ResourceService {
    private ResourceRepository repo;

    public ResourceService(ResourceRepository repo) {
        this.repo = repo;
    }

    public void viewResources() {
        List<Resource> resources = repo.getAllResources();
        for (Resource r : resources) {
            System.out.println(r);
        }
    }

    public void optimizeResourceSelection(double budget) {
        List<Resource> resources = repo.getAllResources();
        int n = Math.min(15, resources.size());
        
        ComplexityUtil.display("Bitmask DP", "O(2^N * N)", "O(2^N * N)", "O(2^N * N)", "O(1)");
        
        double[] costs = new double[n];
        double[] benefits = new double[n];

        for (int i = 0; i < n; i++) {
            costs[i] = resources.get(i).getCost();
            benefits[i] = resources.get(i).getQuantity();
        }

        long start = System.nanoTime();
        List<Integer> selectedIndices = BitmaskDP.selectOptimalResources(costs, benefits, budget);
        long end = System.nanoTime();

        System.out.println("Optimized Selection completed in " + (end - start) + " ns");
        System.out.println("Selected Resources within budget ₹" + budget + ":");
        double totalCost = 0;
        for (int i : selectedIndices) {
            System.out.println(" - " + resources.get(i).getName() + " (Cost: ₹" + resources.get(i).getCost() + ")");
            totalCost += resources.get(i).getCost();
        }
        System.out.println("Total Cost: ₹" + totalCost);
    }
    
    public void subsetSumOptimization(int exactBudget) {
        List<Resource> resources = repo.getAllResources();
        int[] costs = new int[resources.size()];
        for (int i = 0; i < resources.size(); i++) {
            costs[i] = (int) resources.get(i).getCost();
        }
        
        ComplexityUtil.display("Subset DP", "O(N * W)", "O(N * W)", "O(N * W)", "O(N * W)");

        long start = System.nanoTime();
        List<Integer> selected = SubsetDP.findSubsetSum(costs, exactBudget);
        long end = System.nanoTime();

        System.out.println("Subset DP completed in " + (end - start) + " ns");
        if (selected.isEmpty()) {
            System.out.println("No combination found matching exactly ₹" + exactBudget);
        } else {
            System.out.println("Found combination matching exactly ₹" + exactBudget + ":");
            for (int idx : selected) {
                System.out.println(" - " + resources.get(idx).getName() + " (Cost: ₹" + resources.get(idx).getCost() + ")");
            }
        }
    }
}
