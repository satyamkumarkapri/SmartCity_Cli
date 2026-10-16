package smartcity.model;

/**
 * Encapsulates an execution request sent to the public QueryEngine API.
 */
public class QueryRequest {
    private final QueryType queryType;
    private final String algorithmVariant;
    private final String primaryInput;
    private final String secondaryInput;
    private final double numericParam1;
    private final double numericParam2;
    private final Object customData;

    private QueryRequest(Builder builder) {
        this.queryType = builder.queryType;
        this.algorithmVariant = builder.algorithmVariant;
        this.primaryInput = builder.primaryInput;
        this.secondaryInput = builder.secondaryInput;
        this.numericParam1 = builder.numericParam1;
        this.numericParam2 = builder.numericParam2;
        this.customData = builder.customData;
    }

    public QueryType getQueryType() { return queryType; }
    public String getAlgorithmVariant() { return algorithmVariant; }
    public String getPrimaryInput() { return primaryInput; }
    public String getSecondaryInput() { return secondaryInput; }
    public double getNumericParam1() { return numericParam1; }
    public double getNumericParam2() { return numericParam2; }
    public Object getCustomData() { return customData; }

    public static class Builder {
        private final QueryType queryType;
        private String algorithmVariant = "DEFAULT";
        private String primaryInput = "";
        private String secondaryInput = "";
        private double numericParam1 = 0;
        private double numericParam2 = 0;
        private Object customData = null;

        public Builder(QueryType queryType) {
            this.queryType = queryType;
        }

        public Builder algorithmVariant(String variant) {
            this.algorithmVariant = variant;
            return this;
        }

        public Builder primaryInput(String input) {
            this.primaryInput = input;
            return this;
        }

        public Builder secondaryInput(String input) {
            this.secondaryInput = input;
            return this;
        }

        public Builder numericParam1(double val) {
            this.numericParam1 = val;
            return this;
        }

        public Builder numericParam2(double val) {
            this.numericParam2 = val;
            return this;
        }

        public Builder customData(Object data) {
            this.customData = data;
            return this;
        }

        public QueryRequest build() {
            return new QueryRequest(this);
        }
    }
}
