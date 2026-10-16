package smartcity.model;

/**
 * Represents a directed precedence constraint between project tasks.
 */
public class ProjectDependency {
    private int predecessorId;
    private int successorId;
    private int lagDays;

    public ProjectDependency(int predecessorId, int successorId, int lagDays) {
        this.predecessorId = predecessorId;
        this.successorId = successorId;
        this.lagDays = lagDays;
    }

    public int getPredecessorId() { return predecessorId; }
    public int getSuccessorId() { return successorId; }
    public int getLagDays() { return lagDays; }

    @Override
    public String toString() {
        return String.format("Task [%2d] -> Task [%2d] (Lag: %d days)", predecessorId, successorId, lagDays);
    }
}
