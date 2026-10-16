package smartcity.repository;

import smartcity.model.SensorReading;
import smartcity.structures.DynamicArray;

/**
 * Repository for municipal IoT telemetry readings.
 */
public class SensorRepository {
    private final DynamicArray<SensorReading> readings = new DynamicArray<>();

    public void add(SensorReading r) { if (r != null) readings.add(r); }

    public void clear() {
        readings.clear();
    }

    public int size() {
        return readings.size();
    }

    public SensorReading get(int index) {
        return (index >= 0 && index < readings.size()) ? readings.get(index) : null;
    }

    public DynamicArray<SensorReading> getAll() {
        return readings;
    }

    /**
     * Extracts values of a specific sensor type as a double array for parallel algorithms.
     */
    public double[] extractValuesByType(String sensorType) {
        DynamicArray<Double> vals = new DynamicArray<>();
        for (int i = 0; i < readings.size(); i++) {
            SensorReading r = readings.get(i);
            if (sensorType == null || sensorType.isEmpty() || r.getSensorType().equalsIgnoreCase(sensorType)) {
                vals.add(r.getValue());
            }
        }
        double[] arr = new double[vals.size()];
        for (int i = 0; i < vals.size(); i++) {
            arr[i] = vals.get(i);
        }
        return arr;
    }
}
