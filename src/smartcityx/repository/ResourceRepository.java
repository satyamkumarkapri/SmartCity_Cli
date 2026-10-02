package smartcityx.repository;

import smartcityx.model.Resource;
import smartcityx.model.Infrastructure;
import java.util.ArrayList;
import java.util.List;

public class ResourceRepository {
    private List<Resource> resources = new ArrayList<>();
    private List<Infrastructure> infrastructures = new ArrayList<>();

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public List<Resource> getAllResources() {
        return new ArrayList<>(resources);
    }

    public void addInfrastructure(Infrastructure infra) {
        infrastructures.add(infra);
    }

    public List<Infrastructure> getAllInfrastructures() {
        return new ArrayList<>(infrastructures);
    }
}
