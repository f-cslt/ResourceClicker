package main;

import java.awt.event.ActionEvent;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.Timer;

public abstract class Ticker {
    private static final Timer timer = initTimer();
    private static final ConcurrentHashMap<String, Runnable> listeners = new ConcurrentHashMap<>();

    private static Timer initTimer() {
        Timer t = new Timer(10, Ticker::notifyListeners);
        t.setRepeats(true);

        return t;
    }

    public static void start() {
        timer.start();
    }

    public static void stop() {
        timer.stop();
    }

    public static void addListener(String tag, Runnable listener) {
        listeners.put(tag, listener);
    }

    public static void removeListener(String tag) {
        listeners.remove(tag);
    }

    private static void notifyListeners(ActionEvent evt) {
        for (Runnable e : listeners.values()) {
            e.run();
        }
    }
}
