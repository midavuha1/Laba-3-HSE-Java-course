package staff;

import model.Client;
import model.Order;
import util.OrderLogger;
import util.OrderQueue;

public class Waiter extends Thread {
    private static int counter = 1;
    private final int id;
    private final OrderQueue orderQueue;
    private volatile boolean working = true;
    private int ordersServed = 0;

    public Waiter(OrderQueue orderQueue) {
        this.id = counter++;
        this.orderQueue = orderQueue;
        this.setName("Официант-" + id);
    }

    @Override
    public void run() {
        OrderLogger.log(this + " начал работу");

        try {
            while (working && !Thread.currentThread().isInterrupted()) {
                Thread.sleep((long)(Math.random() * 3000) + 1000);

                if (working) {
                    serveClient();
                }
            }
        } catch (InterruptedException e) {
            OrderLogger.log(this + " был прерван");
            Thread.currentThread().interrupt();
        } finally {
            OrderLogger.log(this + " завершил работу. Обслужено заказов: " + ordersServed);
        }
    }

    private void serveClient() {
        Client client = new Client();

        Order order = client.makeOrder(this);
        OrderLogger.logWithThread("Принял " + order);

        try {
            orderQueue.putOrderToKitchen(order);

            synchronized (order) {
                while (order.getStatus() != Order.OrderStatus.READY) {
                    if (orderQueue.getQueueSize() > 3) {
                        OrderLogger.logWithThread("Ожидает свободного повара... В очереди: " +
                                orderQueue.getQueueSize() + " заказов");
                    }
                    order.wait(1000); // Проверяем каждую секунду
                }
            }

            deliverOrder(order, client);
            ordersServed++;

        } catch (InterruptedException e) {
            OrderLogger.error(this + " был прерван при обслуживании", e);
            Thread.currentThread().interrupt();
        }
    }

    private void deliverOrder(Order order, Client client) {
        order.setStatus(Order.OrderStatus.DELIVERED);
        client.receiveOrder(order);
        OrderLogger.logWithThread("Доставил " + order.getDishType().getName() +
                " к столу " + client);
    }

    public void stopWorking() {
        this.working = false;
    }

    public int getOrdersServed() {
        return ordersServed;
    }

    @Override
    public String toString() {
        return "Официант #" + id;
    }
}
