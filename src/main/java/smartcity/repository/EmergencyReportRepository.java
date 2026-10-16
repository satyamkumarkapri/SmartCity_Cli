package smartcity.repository;

import smartcity.model.EmergencyReport;

import smartcity.structures.DynamicArray;

/**
 * In-memory repository for emergency dispatch reports.
 */
public class EmergencyReportRepository {
    private final DynamicArray<EmergencyReport> emergencies = new DynamicArray<>();

    public void add(EmergencyReport em) {
        if (em != null) {
            emergencies.add(em);
        }
    }

    public void clear() {
        emergencies.clear();
    }

    public int size() {
        return emergencies.size();
    }

    public EmergencyReport get(int index) {
        if (index >= 0 && index < emergencies.size()) {
            return emergencies.get(index);
        }
        return null;
    }

    public EmergencyReport findById(int id) {
        for (int i = 0; i < emergencies.size(); i++) {
            if (emergencies.get(i).getId() == id) {
                return emergencies.get(i);
            }
        }
        return null;
    }

    public DynamicArray<EmergencyReport> getAll() {
        return emergencies;
    }

    public DynamicArray<EmergencyReport> getActiveEmergencies() {
        DynamicArray<EmergencyReport> active = new DynamicArray<>();
        for (int i = 0; i < emergencies.size(); i++) {
            if (emergencies.get(i).isActive()) {
                active.add(emergencies.get(i));
            }
        }
        return active;
    }
}
