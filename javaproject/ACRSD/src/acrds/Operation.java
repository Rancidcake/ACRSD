package acrds;

import java.sql.Date;

public class Operation {
    private int operationId;
    private Date startDate;
    private Date endDate;
    private String outcome;
    private String contactDetails;
    private int emergencyId;

    public Operation() {}

    public Operation(int operationId, Date startDate, Date endDate, String outcome, String contactDetails, int emergencyId) {
        this.operationId = operationId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.outcome = outcome;
        this.contactDetails = contactDetails;
        this.emergencyId = emergencyId;
    }

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public int getEmergencyId() {
        return emergencyId;
    }

    public void setEmergencyId(int emergencyId) {
        this.emergencyId = emergencyId;
    }

    @Override
    public String toString() {
        return "Operation #" + operationId + " - " + outcome;
    }
}