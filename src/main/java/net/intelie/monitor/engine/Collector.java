package net.intelie.monitor.engine;

import net.intelie.monitor.events.CompositeEvent;
import net.intelie.monitor.events.ServerUnavailable;
import net.intelie.monitor.events.UnhandledEvent;
import net.intelie.monitor.listeners.Listener;
import org.apache.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class Collector {

    private static Logger logger = Logger.getLogger(Collector.class);

    private EngineChecker checker;
    private Listener listener;
    private QueueCollection collection;
    private Timer timer;

    private static final Integer INTERVAL_IN_SECS = 20;


    public Collector(EngineChecker checker, Listener listener, QueueCollection collection) {
        this.checker = checker;
        this.listener = listener;
        this.collection = collection;

        timer = new Timer();
    }

    public void start() {
        timer.schedule(new MonitorTask(), 0, INTERVAL_IN_SECS * 1000);
    }

    public void checkAll() {
        logger.debug("Retrieving information");
        try {
            try {
                checker.connect();
                collection.checkAll(checker);
                logger.debug("Everything is ok.");
            } catch (CompositeEvent e) {
                listener.notify(e);
            } catch (ServerUnavailable e) {
                listener.notify(e);
            } catch (Exception e) {
                listener.notify(new UnhandledEvent(e));
                logger.warn("Error retrieving information: " + e.getMessage());
                logger.debug(e);
            } finally {
                checker.disconnect();
            }
        } catch (Exception e) {
            logger.warn("Error notifying: " + e.getMessage());
            logger.debug(e.getStackTrace());
        }
    }

    class MonitorTask extends TimerTask {
        public void run() {
            checkAll();
        }


    }

}

