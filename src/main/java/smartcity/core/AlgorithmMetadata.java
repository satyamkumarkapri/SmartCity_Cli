package smartcity.core;

/**
 * Metadata record for every algorithm catalogued in SmartCityX.
 */
public class AlgorithmMetadata {
    private final String id;
    private final String name;
    private final String module;
    private final String problemDefinition;
    private final String intuition;
    private final String inputContract;
    private final String outputContract;
    private final String keyInvariants;
    private final String correctnessReasoning;
    private final String timeBest;
    private final String timeAvg;
    private final String timeWorst;
    private final String auxiliarySpace;
    private final String limitations;
    private final boolean implemented;

    public AlgorithmMetadata(String id, String name, String module, String problemDefinition,
                             String intuition, String inputContract, String outputContract,
                             String keyInvariants, String correctnessReasoning, String timeBest,
                             String timeAvg, String timeWorst, String auxiliarySpace,
                             String limitations, boolean implemented) {
        this.id = id;
        this.name = name;
        this.module = module;
        this.problemDefinition = problemDefinition;
        this.intuition = intuition;
        this.inputContract = inputContract;
        this.outputContract = outputContract;
        this.keyInvariants = keyInvariants;
        this.correctnessReasoning = correctnessReasoning;
        this.timeBest = timeBest;
        this.timeAvg = timeAvg;
        this.timeWorst = timeWorst;
        this.auxiliarySpace = auxiliarySpace;
        this.limitations = limitations;
        this.implemented = implemented;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getModule() { return module; }
    public String getProblemDefinition() { return problemDefinition; }
    public String getIntuition() { return intuition; }
    public String getInputContract() { return inputContract; }
    public String getOutputContract() { return outputContract; }
    public String getKeyInvariants() { return keyInvariants; }
    public String getCorrectnessReasoning() { return correctnessReasoning; }
    public String getTimeBest() { return timeBest; }
    public String getTimeAvg() { return timeAvg; }
    public String getTimeWorst() { return timeWorst; }
    public String getAuxiliarySpace() { return auxiliarySpace; }
    public String getLimitations() { return limitations; }
    public boolean isImplemented() { return implemented; }

    public String formatDetailedExplorer() {
        StringBuilder sb = new StringBuilder();
        sb.append("================================================================================\n");
        sb.append(String.format(" COMPLEXITY EXPLORER: %s (%s)\n", name.toUpperCase(), module));
        sb.append("================================================================================\n");
        sb.append(String.format("%-22s: %s\n", "Status", implemented ? "FULLY IMPLEMENTED & TESTED" : "CONCEPTUAL PREVIEW"));
        sb.append(String.format("%-22s: %s\n", "Time Complexity (Best)", timeBest));
        sb.append(String.format("%-22s: %s\n", "Time Complexity (Avg)", timeAvg));
        sb.append(String.format("%-22s: %s\n", "Time Complexity (Worst)", timeWorst));
        sb.append(String.format("%-22s: %s\n", "Auxiliary Space", auxiliarySpace));
        sb.append("--------------------------------------------------------------------------------\n");
        sb.append("PROBLEM DEFINITION:\n  ").append(problemDefinition).append("\n\n");
        sb.append("ALGORITHM INTUITION:\n  ").append(intuition).append("\n\n");
        sb.append("INPUT CONTRACT:\n  ").append(inputContract).append("\n\n");
        sb.append("OUTPUT CONTRACT:\n  ").append(outputContract).append("\n\n");
        sb.append("KEY INVARIANTS:\n  ").append(keyInvariants).append("\n\n");
        sb.append("CORRECTNESS REASONING:\n  ").append(correctnessReasoning).append("\n\n");
        sb.append("PRACTICAL LIMITATIONS:\n  ").append(limitations).append("\n");
        sb.append("================================================================================\n");
        return sb.toString();
    }
}
