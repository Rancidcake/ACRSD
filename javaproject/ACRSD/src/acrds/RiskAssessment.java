package acrds;

public class RiskAssessment {
    private int assessmentId;
    private String riskLevel;
    private int operationId;

    // Constructor
    public RiskAssessment(int assessmentId, String riskLevel, int operationId) {
        this.assessmentId = assessmentId;
        this.riskLevel = riskLevel;
        this.operationId = operationId;
    }

    // Getters and Setters
    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }
}
