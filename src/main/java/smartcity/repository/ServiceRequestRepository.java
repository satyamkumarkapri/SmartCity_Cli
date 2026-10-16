package smartcity.repository;

import smartcity.model.ServiceRequest;
import smartcity.structures.DynamicArray;

/**
 * In-memory repository for municipal service requests.
 */
public class ServiceRequestRepository {
    private final DynamicArray<ServiceRequest> requests = new DynamicArray<>();

    public void add(ServiceRequest req) {
        if (req != null) {
            requests.add(req);
        }
    }

    public void clear() {
        requests.clear();
    }

    public int size() {
        return requests.size();
    }

    public ServiceRequest get(int index) {
        if (index >= 0 && index < requests.size()) {
            return requests.get(index);
        }
        return null;
    }

    public ServiceRequest findById(int id) {
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).getId() == id) {
                return requests.get(i);
            }
        }
        return null;
    }

    public DynamicArray<ServiceRequest> getAll() {
        return requests;
    }
}
