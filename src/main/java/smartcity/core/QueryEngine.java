package smartcity.core;

import smartcity.model.QueryRequest;
import smartcity.model.QueryResult;

/**
 * Public stable Query Engine interface.
 * Dispatches and coordinates execution of algorithmic requests across all modules.
 */
public interface QueryEngine {
    QueryResult execute(QueryRequest request);
}
