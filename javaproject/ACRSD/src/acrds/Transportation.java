package acrds;

public class Transportation {
    private int transportationId;       // Renamed from modeId to transportationId
    private String transportType;       // Remains the same
    private int capacity;               // Remains the same
    private int shipmentId;             // Remains the same

    public Transportation() {}

    public Transportation(int transportationId, String transportType, int capacity, int shipmentId) {
        this.transportationId = transportationId;  // Renamed from modeId to transportationId
        this.transportType = transportType;        // Remains the same
        this.capacity = capacity;                  // Remains the same
        this.shipmentId = shipmentId;              // Remains the same
    }

    public int getTransportationId() {        // Renamed from getModeId to getTransportationId
        return transportationId;
    }

    public void setTransportationId(int transportationId) {  // Renamed from setModeId to setTransportationId
        this.transportationId = transportationId;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    @Override
    public String toString() {
        return "Transportation #" + transportationId + " - " + transportType; // Updated to reflect transportationId
    }
}
