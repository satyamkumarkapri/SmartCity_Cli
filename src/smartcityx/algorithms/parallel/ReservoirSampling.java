package smartcityx.algorithms.parallel;

import smartcityx.model.SensorReading;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReservoirSampling {
    public static List<SensorReading> sample(List<SensorReading> stream, int k) {
        List<SensorReading> reservoir = new ArrayList<>(k);
        Random rand = new Random();

        for (int i = 0; i < stream.size(); i++) {
            if (i < k) {
                reservoir.add(stream.get(i));
            } else {
                int j = rand.nextInt(i + 1);
                if (j < k) {
                    reservoir.set(j, stream.get(i));
                }
            }
        }
        return reservoir;
    }
}
