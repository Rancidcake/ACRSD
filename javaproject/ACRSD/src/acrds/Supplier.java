package acrds;

public class Supplier {
    private int supplierId;
    private String supplierName;
    private String location;
    private double reliabilityScore;
    private int contractDuration;
    private String sourceType;
    private int inventoryId;

    public Supplier(int supplierId, String supplierName, String location, double reliabilityScore, int contractDuration, String sourceType, int inventoryId) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.location = location;
        this.reliabilityScore = reliabilityScore;
        this.contractDuration = contractDuration;
        this.sourceType = sourceType;
        this.inventoryId = inventoryId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getReliabilityScore() {
        return reliabilityScore;
    }

    public void setReliabilityScore(double reliabilityScore) {
        this.reliabilityScore = reliabilityScore;
    }

    public int getContractDuration() {
        return contractDuration;
    }

    public void setContractDuration(int contractDuration) {
        this.contractDuration = contractDuration;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }
}
