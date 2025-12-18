package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderLogger {
    private static final SimpleDateFormat timeFormat =
            new SimpleDateFormat("HH:mm:ss");

    public static synchronized void log(String message) {
        String timestamp = timeFormat.format(new Date());
        System.out.println("[" + timestamp + "] " + message);
    }

    public static synchronized void logWithThread(String message) {
        String timestamp = timeFormat.format(new Date());
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + timestamp + "][" + threadName + "] " + message);
    }

    public static synchronized void error(String message, Exception e) {
        String timestamp = timeFormat.format(new Date());
        System.err.println("[" + timestamp + "] ОШИБКА: " + message);
        if (e != null) {
            e.printStackTrace();
        }
    }

    public static void logSystemStart() {
        log("========================================");
        log("СИСТЕМА РЕСТОРАНА ЗАПУЩЕНА");
        log("========================================");
    }

    public static void logSystemEnd(int totalOrders) {
        log("========================================");
        log("СИСТЕМА РЕСТОРАНА ОСТАНОВЛЕНА");
        log("Всего обработано заказов: " + totalOrders);
        log("========================================");
    }
}
