package smartcity.algorithms.parallel;

import smartcity.model.ParallelResult;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Parallel Reduction Primitive using ForkJoinPool divide-and-conquer.
 * Supports Sum, Min, Max reductions over primitive double arrays of arbitrary length.
 * Handles non-power-of-two lengths, small inputs, and compares with sequential execution.
 */
public class ParallelReduce {
    public static final int THRESHOLD = 1000;
    private static final ForkJoinPool POOL = new ForkJoinPool();

    public enum Op { SUM, MIN, MAX }

    public static ParallelResult runReduction(double[] data, Op op) {
        if (data == null || data.length == 0) {
            return new ParallelResult(true, "Parallel Reduction", null,
                    "Empty input array.", op.name(), 0, 0.0, 0, 0, 1.0, POOL.getParallelism(), "O(N)", "O(log N)", true);
        }

        int n = data.length;

        // Sequential reference
        long tSeq0 = System.nanoTime();
        double seqResult = computeSequential(data, op);
        long tSeq = System.nanoTime() - tSeq0;

        // Parallel reduction
        long tPar0 = System.nanoTime();
        double parResult = POOL.invoke(new ReduceTask(data, 0, n, op));
        long tPar = System.nanoTime() - tPar0;

        double speedup = (tPar > 0) ? (double) tSeq / tPar : 1.0;
        boolean matches = Math.abs(seqResult - parResult) < 1e-6;

        return new ParallelResult(true, "Parallel Reduction (" + op.name() + ")", null,
                "Binary tree reduction DAG with work T1 = O(N) and span Tinf = O(log N).",
                op.name(), n, parResult, tSeq, tPar, speedup, POOL.getParallelism(), "O(N)", "O(log N)", matches);
    }

    public static double computeSequential(double[] data, Op op) {
        if (data.length == 0) return 0.0;
        double acc = data[0];
        for (int i = 1; i < data.length; i++) {
            if (op == Op.SUM) acc += data[i];
            else if (op == Op.MIN) acc = Math.min(acc, data[i]);
            else if (op == Op.MAX) acc = Math.max(acc, data[i]);
        }
        return acc;
    }

    private static class ReduceTask extends RecursiveTask<Double> {
        private static final long serialVersionUID = 1L;
        private final double[] data;
        private final int start;
        private final int end;
        private final Op op;

        public ReduceTask(double[] data, int start, int end, Op op) {
            this.data = data;
            this.start = start;
            this.end = end;
            this.op = op;
        }

        @Override
        protected Double compute() {
            if (end - start <= THRESHOLD) {
                double acc = data[start];
                for (int i = start + 1; i < end; i++) {
                    if (op == Op.SUM) acc += data[i];
                    else if (op == Op.MIN) acc = Math.min(acc, data[i]);
                    else if (op == Op.MAX) acc = Math.max(acc, data[i]);
                }
                return acc;
            } else {
                int mid = start + (end - start) / 2;
                ReduceTask left = new ReduceTask(data, start, mid, op);
                ReduceTask right = new ReduceTask(data, mid, end, op);
                left.fork();
                double rightAns = right.compute();
                double leftAns = left.join();

                if (op == Op.SUM) return leftAns + rightAns;
                else if (op == Op.MIN) return Math.min(leftAns, rightAns);
                else return Math.max(leftAns, rightAns);
            }
        }
    }
}
