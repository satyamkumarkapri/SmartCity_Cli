package smartcity.model;

/**
 * Enumeration of supported query types in SmartCityX.
 */
public enum QueryType {
    EXACT_SEARCH("Exact Pattern Search"),
    FUZZY_SEARCH("Fuzzy String Matching"),
    DOCUMENT_SIMILARITY("Document Similarity"),
    SUFFIX_ANALYSIS("Suffix Structures and LCP"),
    CITATION_FLOW("Citation and Network Flow"),
    PROJECT_SCHEDULING("Project Scheduling (CPM)"),
    NP_APPROXIMATION("NP-Completeness and Approximation"),
    RANDOMIZED_ALGORITHM("Randomized Algorithms"),
    PARALLEL_ALGORITHM("Parallel Algorithms"),
    ALGORITHM_COMPARISON("Algorithm Comparison Engine");

    private final String displayName;

    QueryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
