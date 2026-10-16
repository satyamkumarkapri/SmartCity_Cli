package smartcity.repository;

import smartcity.structures.CustomFlowEdge;
import smartcity.structures.CustomFlowNetwork;
import smartcity.structures.CustomGraph;

/**
 * Provides explicit citation and scientific flow networks between computer science papers
 * and Wikipedia documents, maintaining separation from raw document text.
 */
public class CitationNetworkProvider {

    /**
     * Builds an explicit citation flow network between foundational computer science articles.
     * Documents:
     * 0: Turing (1936) [Source / Foundational]
     * 1: Lovelace (1842)
     * 2: Algorithm General Theory
     * 3: Knuth-Morris-Pratt (1977)
     * 4: Information Retrieval / Search
     * 5: Wikipedia / Knowledge Graph [Sink]
     */
    public static CustomFlowNetwork buildCitationFlowNetwork() {
        CustomFlowNetwork network = new CustomFlowNetwork(6);
        // From Turing
        network.addEdge(new CustomFlowEdge(0, 1, 10.0, 2.0));
        network.addEdge(new CustomFlowEdge(0, 2, 25.0, 1.0));
        // From Lovelace
        network.addEdge(new CustomFlowEdge(1, 2, 15.0, 1.5));
        network.addEdge(new CustomFlowEdge(1, 4, 8.0, 3.0));
        // From Algorithm Theory
        network.addEdge(new CustomFlowEdge(2, 3, 20.0, 1.2));
        network.addEdge(new CustomFlowEdge(2, 4, 18.0, 1.8));
        // From KMP
        network.addEdge(new CustomFlowEdge(3, 4, 12.0, 1.0));
        network.addEdge(new CustomFlowEdge(3, 5, 14.0, 2.5));
        // From Information Retrieval to Knowledge Graph Sink
        network.addEdge(new CustomFlowEdge(4, 5, 30.0, 1.0));
        return network;
    }

    /**
     * Builds standard citation dependency graph.
     */
    public static CustomGraph buildCitationDependencyGraph() {
        CustomGraph graph = new CustomGraph(6, true);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);
        graph.addEdge(3, 5);
        graph.addEdge(4, 5);
        return graph;
    }

    public static String[] getCitationNodeLabels() {
        return new String[]{
                "Doc 0: Turing (Foundations)",
                "Doc 1: Lovelace (Early Computation)",
                "Doc 2: Algorithm Theory",
                "Doc 3: Knuth-Morris-Pratt",
                "Doc 4: Information Retrieval",
                "Doc 5: Wikipedia Knowledge Graph"
        };
    }
}
