package model;

import staff.Waiter;
import util.OrderLogger;

public class Client {
    private static int counter = 1;
    private final int id;
    private final String name;
    private boolean hasOrder = false;

    public Client() {
        this.id = counter++;
        this.name = "Клиент #" + id;
    }

    public String getName() { return name; }
    public boolean hasOrder() { return hasOrder; }
    public void setHasOrder(boolean hasOrder) { this.hasOrder = hasOrder; }

    public Order makeOrder(Waiter waiter) {
        DishType dishType = DishType.getRandomDish();
        Order order = new Order(dishType, waiter, this);
        hasOrder = true;

        OrderLogger.log(this + " делает заказ: " + dishType.getName() +
                " (приготовление: " + dishType.getCookingTime()/1000 + " сек)");
        return order;
    }

    public void receiveOrder(Order order) {
        OrderLogger.log(this + " получил " + order.getDishType().getName() +
                " от " + order.getAssignedWaiter());
        hasOrder = false;
    }

    @Override
    public String toString() {
        return name;
    }
}
