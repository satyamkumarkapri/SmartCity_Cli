package smartcityx.repository;

import smartcityx.model.SensorReading;
import java.util.ArrayList;
import java.util.List;

public class SensorRepository {
    private List<SensorReading> readings = new ArrayList<>();

    public void addReading(SensorReading reading) {
        readings.add(reading);
    }
    
    public void addAll(List<SensorReading> batch) {
        readings.addAll(batch);
    }

    public List<SensorReading> getAllReadings() {
        return new ArrayList<>(readings);
    }
}
