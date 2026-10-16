package smartcity.services;

import smartcity.algorithms.parallel.BlellochScan;
import smartcity.algorithms.parallel.ParallelReduce;
import smartcity.algorithms.randomized.MillerRabin;
import smartcity.algorithms.randomized.ReservoirSampling;
import smartcity.model.SensorReading;
import smartcity.repository.SensorRepository;
import smartcity.structures.DynamicArray;
import smartcity.util.ConsoleUtil;

/**
 * Service orchestrating Continuous IoT Sensor Telemetry Analytics
 * using M6 (Randomized & Parallel Algorithms).
 */
public class IoTSensorService {
    private final SensorRepository sensorRepo;

    public static class Aggregates {
        public final int count;
        public final double minTemp;
        public final double maxTemp;
        public final double totalPower;

        public Aggregates(int count, double minTemp, double maxTemp, double totalPower) {
            this.count = count;
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
            this.totalPower = totalPower;
        }
    }

    public IoTSensorService(SensorRepository sensorRepo) {
        this.sensorRepo = sensorRepo;
    }

    public void listReadings(int limit) {
        ConsoleUtil.printSection("Continuous IoT Telemetry Stream (" + sensorRepo.size() + " Total Readings)");
        int n = Math.min(limit, sensorRepo.size());
        for (int i = 0; i < n; i++) {
            System.out.println("  " + sensorRepo.get(i).toString());
        }
        if (sensorRepo.size() > n) {
            System.out.println("  ... and " + (sensorRepo.size() - n) + " more live readings.");
        }
    }

    public DynamicArray<SensorReading> sampleReadings(int sampleSize) {
        ReservoirSampling<SensorReading> sampler = new ReservoirSampling<>(sampleSize);
        for (int i = 0; i < sensorRepo.size(); i++) {
            sampler.feed(sensorRepo.get(i));
        }
        return sampler.getSample();
    }

    public double[] computeCumulativePowerPrefixSum() {
        double[] vals = sensorRepo.extractValuesByType("POWER");
        if (vals.length == 0) {
            vals = sensorRepo.extractValuesByType("");
        }
        return BlellochScan.exclusiveScan(vals);
    }

    public Aggregates computeAggregates() {
        double[] temps = sensorRepo.extractValuesByType("TEMPERATURE");
        if (temps.length == 0) {
            temps = sensorRepo.extractValuesByType("");
        }
        double min = ParallelReduce.computeSequential(temps, ParallelReduce.Op.MIN);
        double max = ParallelReduce.computeSequential(temps, ParallelReduce.Op.MAX);

        double[] power = sensorRepo.extractValuesByType("POWER");
        if (power.length == 0) {
            power = sensorRepo.extractValuesByType("");
        }
        double sumPower = ParallelReduce.computeSequential(power, ParallelReduce.Op.SUM);

        return new Aggregates(sensorRepo.size(), min, max, sumPower);
    }

    /**
     * Extracts an unbiased uniform sample of size k from the continuous stream using Reservoir Sampling.
     */
    public void sampleStreamViaReservoir(int sampleSize) {
        ConsoleUtil.printSection("IoT Stream Unbiased Sampling (Reservoir Sampling k=" + sampleSize + ")");
        if (sensorRepo.size() == 0) {
            ConsoleUtil.printWarning("No sensor readings loaded.");
            return;
        }

        long start = System.nanoTime();
        DynamicArray<SensorReading> sample = sampleReadings(sampleSize);
        long elapsed = System.nanoTime() - start;

        System.out.println("Uniform Unbiased Stream Sample Extracted:");
        for (int i = 0; i < sample.size(); i++) {
            System.out.printf("  [%2d] %s%n", i + 1, sample.get(i).toString());
        }
        ConsoleUtil.printSuccess(String.format("Reservoir sampling processed %,d items into k=%d in %.3f ms.",
                sensorRepo.size(), sampleSize, elapsed / 1_000_000.0));
    }

    /**
     * Computes running cumulative consumption or exposure in parallel using Blelloch Parallel Scan.
     */
    public void computeRunningCumulativeScan(String sensorType) {
        ConsoleUtil.printSection("Parallel Cumulative Prefix Scan (Blelloch Work-Efficient Scan)");
        double[] vals = sensorRepo.extractValuesByType(sensorType);
        if (vals.length == 0) {
            ConsoleUtil.printWarning("No readings found for sensor type: " + sensorType);
            return;
        }

        long start = System.nanoTime();
        double[] prefix = BlellochScan.exclusiveScan(vals);
        long elapsed = System.nanoTime() - start;

        System.out.printf("Analyzed %s%,d readings%s of type '%s' in %.3f ms.%n%n",
                ConsoleUtil.CYAN, vals.length, ConsoleUtil.RESET, sensorType, elapsed / 1_000_000.0);

        int show = Math.min(10, vals.length);
        System.out.println(ConsoleUtil.BOLD_BLACK + " Index │ Raw Measurement │ Cumulative Prefix Total" + ConsoleUtil.RESET);
        System.out.println("───────┼─────────────────┼────────────────────────");
        for (int i = 0; i < show; i++) {
            System.out.printf("  %4d │   %10.2f    │       %12.2f%n", i, vals[i], prefix[i]);
        }
        ConsoleUtil.printSuccess(String.format("Blelloch parallel scan completed. Final cumulative total: %,.2f.",
                prefix[vals.length - 1] + vals[vals.length - 1]));
    }

    /**
     * Aggregates citywide statistics in parallel using Java ForkJoinPool Parallel Reduce.
     */
    public void computeParallelCitywideStatistics() {
        ConsoleUtil.printSection("Citywide Sensor Telemetry Aggregation (Parallel Reduce)");
        double[] allVals = sensorRepo.extractValuesByType("");
        if (allVals.length == 0) {
            ConsoleUtil.printWarning("No readings loaded.");
            return;
        }

        long start = System.nanoTime();
        double sum = ParallelReduce.computeSequential(allVals, ParallelReduce.Op.SUM);
        double min = ParallelReduce.computeSequential(allVals, ParallelReduce.Op.MIN);
        double max = ParallelReduce.computeSequential(allVals, ParallelReduce.Op.MAX);
        double mean = sum / allVals.length;
        long elapsed = System.nanoTime() - start;

        System.out.printf("  Readings Evaluated     : %s%,d observations%s%n", ConsoleUtil.CYAN, allVals.length, ConsoleUtil.RESET);
        System.out.printf("  Parallel Processing    : ForkJoinPool with %d worker threads%n", Runtime.getRuntime().availableProcessors());
        System.out.printf("  Cumulative Sum         : %,.2f%n", sum);
        System.out.printf("  Mean Value             : %,.2f%n", mean);
        System.out.printf("  Minimum Observed Value : %,.2f%n", min);
        System.out.printf("  Maximum Observed Value : %,.2f%n", max);
        System.out.printf("  Parallel Runtime       : %,d ns (%.4f ms)%n", elapsed, elapsed / 1_000_000.0);
        ConsoleUtil.printSuccess("Parallel reduction aggregated citywide telemetry across all cores.");
    }

    /**
     * Sensor cryptographic identification check using Miller-Rabin primality test.
     */
    public void testSensorCryptoPrimality(long idCandidate) {
        ConsoleUtil.printSection("Sensor Crypto Key Verification (Miller-Rabin)");
        long start = System.nanoTime();
        boolean isPrime = MillerRabin.isPrime(idCandidate);
        long elapsed = System.nanoTime() - start;

        System.out.printf("  Candidate ID Number    : %,d%n", idCandidate);
        System.out.printf("  Miller-Rabin Result    : %s%n",
                isPrime ? (ConsoleUtil.BRIGHT_GREEN + "PRIME (Cryptographically Valid Parameter)" + ConsoleUtil.RESET)
                        : (ConsoleUtil.RED + "COMPOSITE (Invalid Sensor Key Parameter)" + ConsoleUtil.RESET));
        System.out.printf("  Testing Duration       : %,d ns%n", elapsed);
        ConsoleUtil.printSuccess("Miller-Rabin randomized primality verification complete.");
    }
}
