package acrds;

public class Inventory {
    private int inventoryId;
    private String itemName;
    private String itemType;
    private int quantityAvailable;
    private int reorderLevel;
    private int orderId;
    private int forecastId;

    public Inventory() {}

    public Inventory(int inventoryId, String itemName, String itemType, int quantityAvailable, int reorderLevel, int orderId, int forecastId) {
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.quantityAvailable = quantityAvailable;
        this.reorderLevel = reorderLevel;
        this.orderId = orderId;
        this.forecastId = forecastId;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getForecastId() {
        return forecastId;
    }

    public void setForecastId(int forecastId) {
        this.forecastId = forecastId;
    }

    @Override
    public String toString() {
        return itemName + " (" + itemType + ") - Qty: " + quantityAvailable + " | Reorder @ " + reorderLevel;
    }
}
