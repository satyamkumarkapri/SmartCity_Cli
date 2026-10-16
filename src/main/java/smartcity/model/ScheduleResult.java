package smartcity.model;

import smartcity.structures.DynamicArray;
import smartcity.util.ConsoleUtil;

/**
 * Result container for Critical Path Method (CPM) project scheduling.
 */
public class ScheduleResult extends QueryResult {
    public static class ScheduledTask {
        public final String name;
        public final int duration;
        public final int earlyStart;
        public final int earlyFinish;
        public final int lateStart;
        public final int lateFinish;
        public final int slack;
        public final boolean isCritical;

        public ScheduledTask(String name, int duration, int es, int ef, int ls, int lf, int slack, boolean critical) {
            this.name = name;
            this.duration = duration;
            this.earlyStart = es;
            this.earlyFinish = ef;
            this.lateStart = ls;
            this.lateFinish = lf;
            this.slack = slack;
            this.isCritical = critical;
        }
    }

    private final int makespan;
    private final DynamicArray<String> criticalPath;
    private final DynamicArray<ScheduledTask> tasks;

    public ScheduleResult(boolean success, String algorithmName, AlgorithmMetrics metrics,
                          String explanation, int makespan, DynamicArray<String> criticalPath,
                          DynamicArray<ScheduledTask> tasks) {
        super(success, algorithmName, metrics, explanation);
        this.makespan = makespan;
        this.criticalPath = criticalPath != null ? criticalPath : new DynamicArray<>();
        this.tasks = tasks != null ? tasks : new DynamicArray<>();
    }

    public int getMakespan() { return makespan; }
    public DynamicArray<String> getCriticalPath() { return criticalPath; }
    public DynamicArray<ScheduledTask> getTasks() { return tasks; }

    @Override
    public String formatOutput() {
        StringBuilder sb = new StringBuilder(formatCommonHeader("PROJECT SCHEDULING (CRITICAL PATH METHOD)"));
        sb.append(String.format("║  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : " + ConsoleUtil.BRIGHT_GREEN + "%d time units%s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET,
                "Project Makespan", makespan,
                " ".repeat(Math.max(0, 42 - String.format("%d time units", makespan).length()))));
        sb.append(String.format("║  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : " + ConsoleUtil.BRIGHT_YELLOW + "%-54s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET,
                "Critical Path", criticalPath.toString()));
        sb.append(ConsoleUtil.CYAN).append("╚══════════════════════════════════════════════════════════════════════════════╝\n").append(ConsoleUtil.RESET);

        sb.append("\n" + ConsoleUtil.BRIGHT_BLUE + ConsoleUtil.BOLD + "───◆ Project Task Schedule & Slack Analysis ◆──────────────────────────────────" + ConsoleUtil.RESET + "\n");
        sb.append(String.format(" " + ConsoleUtil.BOLD_BLACK + "%-14s │ %-4s │ %-4s │ %-4s │ %-4s │ %-4s │ %-5s │ %s" + ConsoleUtil.RESET + "\n",
                "Task", "Dur", "ES", "EF", "LS", "LF", "Slack", "Critical?"));
        sb.append("───────────────┼──────┼──────┼──────┼──────┼──────┼───────┼────────────────\n");
        for (ScheduledTask t : tasks) {
            String critStr = t.isCritical ? (ConsoleUtil.BRIGHT_GREEN + ConsoleUtil.BOLD + "★ YES (CRITICAL)" + ConsoleUtil.RESET)
                    : "  No";
            sb.append(String.format(" " + ConsoleUtil.CYAN + "%-14s" + ConsoleUtil.RESET + " │ %-4d │ %-4d │ %-4d │ %-4d │ %-4d │ %-5d │ %s\n",
                    t.name, t.duration, t.earlyStart, t.earlyFinish, t.lateStart, t.lateFinish, t.slack, critStr));
        }
        sb.append("───────────────┴──────┴──────┴──────┴──────┴──────┴───────┴────────────────\n");
        return sb.toString();
    }
}
