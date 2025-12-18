package model;

import staff.Waiter;

public class Order {
    private static int counter = 1;
    private final int id;
    private final DishType dishType;
    private OrderStatus status;
    private final Waiter assignedWaiter;
    private final Client client;

    public enum OrderStatus {
        CREATED,       // Заказ создан
        IN_QUEUE,      // В очереди на кухне
        COOKING,       // Готовится
        READY,         // Готов
        DELIVERED      // Доставлен клиенту
    }

    public Order(DishType dishType, Waiter waiter, Client client) {
        this.id = counter++;
        this.dishType = dishType;
        this.status = OrderStatus.CREATED;
        this.assignedWaiter = waiter;
        this.client = client;
    }

    public int getId() { return id; }
    public DishType getDishType() { return dishType; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public Waiter getAssignedWaiter() { return assignedWaiter; }
    public Client getClient() { return client; }

    @Override
    public String toString() {
        return "Заказ #" + id + " [" + dishType + "] для " + client + " (Статус: " + status + ")";
    }
}
