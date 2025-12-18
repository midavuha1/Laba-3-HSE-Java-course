import model.Client;
import staff.Chef;
import staff.Waiter;
import util.OrderLogger;
import util.OrderQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int NUM_WAITERS = 3;
    private static final int NUM_CHEFS = 2;
    private static final int NUM_CLIENTS = 15;
    private static final int SIMULATION_TIME_SECONDS = 60;


    public static void main(String[] args) {
        OrderLogger.logSystemStart();

        OrderQueue orderQueue = new OrderQueue();

        List<Waiter> waiters = new ArrayList<>();
        for (int i = 0; i < NUM_WAITERS; i++) {
            Waiter waiter = new Waiter(orderQueue);
            waiters.add(waiter);
            waiter.start();
        }

        ExecutorService kitchen = Executors.newFixedThreadPool(NUM_CHEFS);
        List<Chef> chefs = new ArrayList<>();

        for (int i = 0; i < NUM_CHEFS; i++) {
            Chef chef = new Chef(orderQueue);
            chefs.add(chef);
            kitchen.execute(chef);
        }

        OrderLogger.log("Ресторан открыт! Официанты: " + NUM_WAITERS +
                ", Повара: " + NUM_CHEFS);
        OrderLogger.log("Симуляция будет работать " + SIMULATION_TIME_SECONDS + " секунд");

        try {
            Thread.sleep(SIMULATION_TIME_SECONDS * 1000L);
        } catch (InterruptedException e) {
            OrderLogger.error("Главный поток был прерван", e);
        }

        OrderLogger.log("Завершение работы ресторана...");

        for (Waiter waiter : waiters) {
            waiter.stopWorking();
            waiter.interrupt();
        }

        for (Chef chef : chefs) {
            chef.stopWorking();
        }

        kitchen.shutdown();
        try {
            if (!kitchen.awaitTermination(5, TimeUnit.SECONDS)) {
                kitchen.shutdownNow();
            }
        } catch (InterruptedException e) {
            kitchen.shutdownNow();
        }

        for (Waiter waiter : waiters) {
            try {
                waiter.join(2000);
            } catch (InterruptedException e) {
                OrderLogger.error("Ошибка при ожидании официанта", e);
            }
        }

        printStatistics(waiters, chefs, orderQueue, kitchen);
        OrderLogger.logSystemEnd(orderQueue.getTotalOrdersProcessed());
    }

    private static void printStatistics(List<Waiter> waiters, List<Chef> chefs, OrderQueue queue, ExecutorService kitchen) {
        OrderLogger.log("\n========== СТАТИСТИКА ==========");
        OrderLogger.log("Всего обработано заказов: " + queue.getTotalOrdersProcessed());
        OrderLogger.log("Заказов в очереди на кухне: " + queue.getQueueSize());

        OrderLogger.log("\n--- Официанты ---");
        for (Waiter waiter : waiters) {
            OrderLogger.log(waiter + ": обслужено " + waiter.getOrdersServed() + " заказов");
        }

        OrderLogger.log("\n--- Повара ---");
        for (Chef chef : chefs) {
            OrderLogger.log(chef + ": приготовлено " + chef.getOrdersCooked() + " блюд");
        }

        boolean allWaitersFinished = waiters.stream().allMatch(w -> !w.isAlive());
        OrderLogger.log("\nВсе официанты завершили работу: " +
                (allWaitersFinished ? "ДА" : "НЕТ"));

        boolean kitchenTerminated = kitchen.isTerminated();
        OrderLogger.log("Кухня завершила работу: " +
                (kitchenTerminated ? "ДА" : "НЕТ"));

        OrderLogger.log("================================\n");
    }
}