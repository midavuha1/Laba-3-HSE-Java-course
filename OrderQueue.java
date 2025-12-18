package util;

import model.Order;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderQueue {
    private final BlockingQueue<Order> kitchenQueue = new LinkedBlockingQueue<>();
    private volatile int totalOrdersProcessed = 0;

    public void putOrderToKitchen(Order order) throws InterruptedException {
        order.setStatus(Order.OrderStatus.IN_QUEUE);
        kitchenQueue.put(order);

        OrderLogger.log(order.getAssignedWaiter() + " поместил " + order +
                " в очередь кухни. В очереди: " + kitchenQueue.size());
    }

    public Order takeOrderFromKitchen() throws InterruptedException {
        Order order = kitchenQueue.take();
        order.setStatus(Order.OrderStatus.COOKING);
        return order;
    }

    public int getQueueSize() {
        return kitchenQueue.size();
    }


    public void incrementProcessedOrders() {
        totalOrdersProcessed++;
    }

    public int getTotalOrdersProcessed() {
        return totalOrdersProcessed;
    }
}
