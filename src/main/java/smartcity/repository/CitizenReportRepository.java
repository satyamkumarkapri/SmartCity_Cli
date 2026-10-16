package smartcity.repository;

import smartcity.model.CitizenReport;
import smartcity.model.enums.ReportCategory;
import smartcity.model.enums.ReportStatus;
import smartcity.structures.DynamicArray;

/**
 * In-memory repository for citizen reports with indexing and query capabilities.
 */
public class CitizenReportRepository {
    private final DynamicArray<CitizenReport> reports = new DynamicArray<>();

    public void add(CitizenReport report) {
        if (report != null) {
            reports.add(report);
        }
    }

    public void clear() {
        reports.clear();
    }

    public int size() {
        return reports.size();
    }

    public CitizenReport get(int index) {
        if (index >= 0 && index < reports.size()) {
            return reports.get(index);
        }
        return null;
    }

    public CitizenReport findById(int id) {
        for (int i = 0; i < reports.size(); i++) {
            if (reports.get(i).getId() == id) {
                return reports.get(i);
            }
        }
        return null;
    }

    public DynamicArray<CitizenReport> getAll() {
        return reports;
    }

    public DynamicArray<CitizenReport> findByCategory(ReportCategory category) {
        DynamicArray<CitizenReport> result = new DynamicArray<>();
        for (int i = 0; i < reports.size(); i++) {
            if (reports.get(i).getCategory() == category) {
                result.add(reports.get(i));
            }
        }
        return result;
    }

    public DynamicArray<CitizenReport> findByStatus(ReportStatus status) {
        DynamicArray<CitizenReport> result = new DynamicArray<>();
        for (int i = 0; i < reports.size(); i++) {
            if (reports.get(i).getStatus() == status) {
                result.add(reports.get(i));
            }
        }
        return result;
    }
}
