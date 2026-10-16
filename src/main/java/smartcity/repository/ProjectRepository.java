package smartcity.repository;

import smartcity.algorithms.graph.CriticalPathMethod;
import smartcity.model.ProjectTask;
import smartcity.model.ProjectDependency;
import smartcity.structures.DynamicArray;
import smartcity.structures.CustomHashMap;

/**
 * Repository for municipal capital projects, maintenance tasks, and dependency DAGs.
 */
public class ProjectRepository {
    private final DynamicArray<ProjectTask> tasks = new DynamicArray<>();
    private final DynamicArray<ProjectDependency> dependencies = new DynamicArray<>();

    public void addTask(ProjectTask t) { if (t != null) tasks.add(t); }
    public void addDependency(ProjectDependency d) { if (d != null) dependencies.add(d); }

    public void clear() {
        tasks.clear();
        dependencies.clear();
    }

    public int getTaskCount() { return tasks.size(); }
    public int getDependencyCount() { return dependencies.size(); }

    public DynamicArray<ProjectTask> getTasks() { return tasks; }
    public DynamicArray<ProjectDependency> getDependencies() { return dependencies; }

    /**
     * Converts loaded tasks and dependencies into CriticalPathMethod.Task array.
     */
    public CriticalPathMethod.Task[] buildCPMTasks() {
        int n = tasks.size();
        CriticalPathMethod.Task[] cpm = new CriticalPathMethod.Task[n];

        // Map task_id -> 0-based index
        CustomHashMap<Integer, Integer> idToIdx = new CustomHashMap<>();
        for (int i = 0; i < n; i++) {
            idToIdx.put(tasks.get(i).getTaskId(), i);
        }

        // Collect predecessors for each task
        @SuppressWarnings({"unchecked", "rawtypes"})
        DynamicArray<Integer>[] predList = new DynamicArray[n];
        for (int i = 0; i < n; i++) predList[i] = new DynamicArray<>();

        for (int i = 0; i < dependencies.size(); i++) {
            ProjectDependency dep = dependencies.get(i);
            Integer pIdx = idToIdx.get(dep.getPredecessorId());
            Integer sIdx = idToIdx.get(dep.getSuccessorId());
            if (pIdx != null && sIdx != null) {
                predList[sIdx].add(pIdx);
            }
        }

        for (int i = 0; i < n; i++) {
            ProjectTask pt = tasks.get(i);
            int[] preds = new int[predList[i].size()];
            for (int k = 0; k < predList[i].size(); k++) {
                preds[k] = predList[i].get(k);
            }
            cpm[i] = new CriticalPathMethod.Task(i, pt.getTaskName(), pt.getDurationDays(), preds);
        }
        return cpm;
    }
}
