package smartcityx.algorithms.parallel;

public class BrentsTheorem {
    public static double estimateParallelBound(double totalWork, double span, int processors) {
        return (totalWork - span) / processors + span;
    }
}
