package smartcity.algorithms.graph;

import smartcity.model.ScheduleResult;
import smartcity.structures.CircularQueue;
import smartcity.structures.DynamicArray;
import smartcity.structures.IntArrayList;

/**
 * Project Scheduling via the Critical Path Method (CPM).
 * Determines project makespan, earliest/latest start and finish dates,
 * task slack (float), and the critical path.
 * Time Complexity: O(V + E) where V = tasks, E = precedence dependencies.
 */
public class CriticalPathMethod {

    public static class Task {
        public final int id;
        public final String name;
        public final int duration;
        public final IntArrayList dependencies;

        public Task(int id, String name, int duration, int[] deps) {
            this.id = id;
            this.name = name;
            this.duration = duration;
            this.dependencies = new IntArrayList();
            if (deps != null) {
                for (int d : deps) this.dependencies.add(d);
            }
        }
    }

    public static ScheduleResult scheduleProject(Task[] tasks) {
        int n = tasks.length;
        int[] inDegree = new int[n];
        DynamicArray<IntArrayList> succ = new DynamicArray<>(n);
        for (int i = 0; i < n; i++) succ.add(new IntArrayList());

        for (int i = 0; i < n; i++) {
            IntArrayList deps = tasks[i].dependencies;
            for (int d = 0; d < deps.size(); d++) {
                int pred = deps.get(d);
                succ.get(pred).add(i);
                inDegree[i]++;
            }
        }

        // Topological Sort (Kahn's Algorithm)
        CircularQueue<Integer> q = new CircularQueue<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) q.enqueue(i);
        }

        IntArrayList topoOrder = new IntArrayList();
        while (!q.isEmpty()) {
            int u = q.dequeue();
            topoOrder.add(u);
            IntArrayList children = succ.get(u);
            for (int i = 0; i < children.size(); i++) {
                int v = children.get(i);
                if (--inDegree[v] == 0) {
                    q.enqueue(v);
                }
            }
        }

        if (topoOrder.size() < n) {
            throw new IllegalArgumentException("Circular dependency detected in project task graph.");
        }

        // Forward Pass: Earliest Start (ES) and Earliest Finish (EF)
        int[] es = new int[n];
        int[] ef = new int[n];
        int makespan = 0;

        for (int i = 0; i < topoOrder.size(); i++) {
            int u = topoOrder.get(i);
            ef[u] = es[u] + tasks[u].duration;
            if (ef[u] > makespan) makespan = ef[u];

            IntArrayList children = succ.get(u);
            for (int c = 0; c < children.size(); c++) {
                int v = children.get(c);
                if (ef[u] > es[v]) es[v] = ef[u];
            }
        }

        // Backward Pass: Latest Finish (LF) and Latest Start (LS)
        int[] lf = new int[n];
        int[] ls = new int[n];
        for (int i = 0; i < n; i++) lf[i] = makespan;

        for (int i = topoOrder.size() - 1; i >= 0; i--) {
            int u = topoOrder.get(i);
            ls[u] = lf[u] - tasks[u].duration;

            IntArrayList deps = tasks[u].dependencies;
            for (int d = 0; d < deps.size(); d++) {
                int pred = deps.get(d);
                if (ls[u] < lf[pred]) lf[pred] = ls[u];
            }
        }

        DynamicArray<String> criticalTasks = new DynamicArray<>();
        DynamicArray<ScheduleResult.ScheduledTask> scheduledList = new DynamicArray<>();

        for (int i = 0; i < n; i++) {
            int slack = ls[i] - es[i];
            boolean isCritical = (slack == 0);
            if (isCritical) {
                criticalTasks.add(tasks[i].name);
            }
            scheduledList.add(new ScheduleResult.ScheduledTask(
                    tasks[i].name, tasks[i].duration, es[i], ef[i], ls[i], lf[i], slack, isCritical));
        }

        return new ScheduleResult(true, "Critical Path Method (CPM)", null,
                "Identified project bottlenecks; delaying any critical task directly increases the " + makespan + "-unit makespan.",
                makespan, criticalTasks, scheduledList);
    }
}
