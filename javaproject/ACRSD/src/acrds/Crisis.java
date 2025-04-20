package acrds;

public class Crisis {
    private int crisisId;
    private String crisisType;
    private String severityLevel;
    private String status;
    private int responseId;

    // Default constructor
    public Crisis() {}

    // Constructor with parameters
    public Crisis(int crisisId, String crisisType, String severityLevel, String status, int responseId) {
        this.crisisId = crisisId;
        this.crisisType = crisisType;
        this.severityLevel = severityLevel;
        this.status = status;
        this.responseId = responseId;
    }

    // Getters and setters
    public int getCrisisId() {
        return crisisId;
    }

    public void setCrisisId(int crisisId) {
        this.crisisId = crisisId;
    }

    public String getCrisisType() {
        return crisisType;
    }

    public void setCrisisType(String crisisType) {
        this.crisisType = crisisType;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    @Override
    public String toString() {
        return crisisType + " - " + severityLevel + " - " + status + " (Response ID: " + responseId + ")";
    }
}
