package smartcity.model;

/**
 * Represents a municipal engineering or maintenance project task for CPM scheduling.
 */
public class ProjectTask {
    private int taskId;
    private String taskName;
    private int durationDays;
    private String department;
    private int estimatedBudget;

    public ProjectTask(int taskId, String taskName, int durationDays, String department, int estimatedBudget) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.durationDays = durationDays;
        this.department = department;
        this.estimatedBudget = estimatedBudget;
    }

    public int getTaskId() { return taskId; }
    public String getTaskName() { return taskName; }
    public int getDurationDays() { return durationDays; }
    public String getDepartment() { return department; }
    public int getEstimatedBudget() { return estimatedBudget; }

    @Override
    public String toString() {
        return String.format("[Task %02d] %-32s | Dur: %2d days | Dept: %-18s | Budget: $%,7d",
                taskId, taskName, durationDays, department, estimatedBudget);
    }
}
