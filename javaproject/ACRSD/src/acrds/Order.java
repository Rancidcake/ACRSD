package acrds;


import java.sql.Date;

public class Order {
    private int orderId;
    private int quantityOrdered;
    private Date expectedDeliveryDate;
    private String orderStatus;
    private Date orderDate;

    public Order() {}

    public Order(int orderId, int quantityOrdered, Date expectedDeliveryDate, String orderStatus, Date orderDate) {
        this.orderId = orderId;
        this.quantityOrdered = quantityOrdered;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }

    // Getters and setters omitted for brevity

    @Override
    public String toString() {
        return "Order #" + orderId + " - Qty:" + quantityOrdered + " Status:" + orderStatus;
    }
}