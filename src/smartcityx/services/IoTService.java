package smartcityx.services;

import smartcityx.algorithms.parallel.*;
import smartcityx.model.SensorReading;
import smartcityx.repository.SensorRepository;
import smartcityx.util.ComplexityUtil;
import java.util.ArrayList;
import java.util.List;

public class IoTService {
    private SensorRepository repo;

    public IoTService(SensorRepository repo) {
        this.repo = repo;
    }

    public void analyzeIoTData() {
        List<SensorReading> readings = repo.getAllReadings();
        if (readings.isEmpty()) {
            System.out.println("No IoT readings available.");
            return;
        }

        System.out.println("\nTaking Reservoir Sample of size 10 from " + readings.size() + " readings...");
        ComplexityUtil.display("Reservoir Sampling", "O(N)", "O(N)", "O(N)", "O(K)");
        long start = System.nanoTime();
        List<SensorReading> sample = ReservoirSampling.sample(readings, 10);
        long end = System.nanoTime();
        
        System.out.println("Reservoir Sampling completed in " + (end - start) + " ns");
        for (SensorReading sr : sample) {
            System.out.println(sr);
        }

        List<Double> values = new ArrayList<>();
        for (SensorReading sr : readings) values.add(sr.getValue());

        System.out.println();
        ComplexityUtil.display("Parallel Reduce (Sum/Min/Max)", "O(log N) span", "O(log N) span", "O(log N) span", "O(log N)");
        start = System.nanoTime();
        double sum = ParallelReduce.sum(values);
        double min = ParallelReduce.min(values);
        double max = ParallelReduce.max(values);
        end = System.nanoTime();

        System.out.println("Parallel Reduce Analytics completed in " + (end - start) + " ns");
        System.out.println("Total Readings : " + readings.size());
        System.out.printf("Sum            : %.2f\n", sum);
        System.out.printf("Minimum        : %.2f\n", min);
        System.out.printf("Maximum        : %.2f\n", max);
        System.out.printf("Average        : %.2f\n\n", (sum / readings.size()));
        
        double[] valsArray = new double[readings.size()];
        for(int i=0; i<readings.size(); i++) valsArray[i] = readings.get(i).getValue();
        
        ComplexityUtil.display("Blelloch Scan", "O(log N) span", "O(log N) span", "O(log N) span", "O(N)");
        start = System.nanoTime();
        double[] prefixSums = BlellochScan.prefixSum(valsArray);
        end = System.nanoTime();
        System.out.println("Prefix sum computation (Blelloch Scan) done in " + (end - start) + " ns");
    }

    public void checkPrimeSensorId(long id) {
        System.out.println("Checking primality of sensor ID " + id + " using Miller-Rabin...\n");
        ComplexityUtil.display("Miller-Rabin Primality Test", "O(K * log^3 N)", "O(K * log^3 N)", "O(K * log^3 N)", "O(1)");
        
        long start = System.nanoTime();
        boolean isPrime = MillerRabin.isPrime(id, 5);
        long end = System.nanoTime();
        
        System.out.println("Result: " + (isPrime ? "Probably Prime" : "Composite"));
        System.out.println("Time: " + (end - start) + " ns");
    }
}
