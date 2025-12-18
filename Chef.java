package staff;

import model.Order;
import util.OrderLogger;
import util.OrderQueue;

public class Chef implements Runnable {
    private static int counter = 1;
    private final int id;
    private final OrderQueue orderQueue;
    private volatile boolean working = true;
    private int ordersCooked = 0;

    public Chef(OrderQueue orderQueue) {
        this.id = counter++;
        this.orderQueue = orderQueue;
    }

    @Override
    public void run() {
        OrderLogger.logWithThread("начал работу");

        try {
            while (working && !Thread.currentThread().isInterrupted()) {

                Order order = orderQueue.takeOrderFromKitchen();

                OrderLogger.logWithThread("начал готовить " + order);

                int cookingTime = order.getDishType().getCookingTime();
                Thread.sleep(cookingTime);

                order.setStatus(Order.OrderStatus.READY);
                ordersCooked++;
                orderQueue.incrementProcessedOrders();

                OrderLogger.logWithThread("приготовил " + order.getDishType().getName() +
                        " за " + (cookingTime/1000) + " сек");

                // Уведомляем официанта о готовности
                synchronized (order) {
                    order.notifyAll();
                }
            }
        } catch (InterruptedException e) {
            OrderLogger.logWithThread("был прерван");
            Thread.currentThread().interrupt();
        } finally {
            OrderLogger.logWithThread("завершил работу. Приготовлено блюд: " + ordersCooked);
        }
    }

    public void stopWorking() {
        this.working = false;
    }

    public int getOrdersCooked() {
        return ordersCooked;
    }

    @Override
    public String toString() {
        return "Повар #" + id;
    }
}