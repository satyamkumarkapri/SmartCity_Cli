package smartcityx.algorithms.parallel;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelReduce {
    
    public static double sum(List<Double> data) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new SumTask(data, 0, data.size()));
    }
    
    public static double min(List<Double> data) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new MinTask(data, 0, data.size()));
    }
    
    public static double max(List<Double> data) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new MaxTask(data, 0, data.size()));
    }

    private static class SumTask extends RecursiveTask<Double> {
        private static final int THRESHOLD = 100;
        private List<Double> data;
        private int start;
        private int end;

        public SumTask(List<Double> data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Double compute() {
            if (end - start <= THRESHOLD) {
                double sum = 0;
                for (int i = start; i < end; i++) sum += data.get(i);
                return sum;
            } else {
                int mid = start + (end - start) / 2;
                SumTask left = new SumTask(data, start, mid);
                SumTask right = new SumTask(data, mid, end);
                left.fork();
                double rightResult = right.compute();
                double leftResult = left.join();
                return leftResult + rightResult;
            }
        }
    }
    
    private static class MinTask extends RecursiveTask<Double> {
        private static final int THRESHOLD = 100;
        private List<Double> data;
        private int start;
        private int end;

        public MinTask(List<Double> data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Double compute() {
            if (end - start <= THRESHOLD) {
                double min = Double.MAX_VALUE;
                for (int i = start; i < end; i++) min = Math.min(min, data.get(i));
                return min;
            } else {
                int mid = start + (end - start) / 2;
                MinTask left = new MinTask(data, start, mid);
                MinTask right = new MinTask(data, mid, end);
                left.fork();
                double rightResult = right.compute();
                double leftResult = left.join();
                return Math.min(leftResult, rightResult);
            }
        }
    }

    private static class MaxTask extends RecursiveTask<Double> {
        private static final int THRESHOLD = 100;
        private List<Double> data;
        private int start;
        private int end;

        public MaxTask(List<Double> data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Double compute() {
            if (end - start <= THRESHOLD) {
                double max = -Double.MAX_VALUE;
                for (int i = start; i < end; i++) max = Math.max(max, data.get(i));
                return max;
            } else {
                int mid = start + (end - start) / 2;
                MaxTask left = new MaxTask(data, start, mid);
                MaxTask right = new MaxTask(data, mid, end);
                left.fork();
                double rightResult = right.compute();
                double leftResult = left.join();
                return Math.max(leftResult, rightResult);
            }
        }
    }
}
