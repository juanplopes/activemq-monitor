package net.intelie.monitor;

import net.intelie.monitor.engine.ActiveMQChecker;
import net.intelie.monitor.engine.QueueCollection;
import net.intelie.monitor.engine.Collector;
import net.intelie.monitor.engine.MonitorConfiguration;
import net.intelie.monitor.listeners.QueueMonitorListener;
import org.apache.log4j.Logger;


public class Main {

    private static Logger logger = Logger.getLogger(Main.class);

    private static QueueCollection monitorCollection;

    public static void main(String[] args) {
        try {
            MonitorConfiguration configuration = new MonitorConfiguration("activemq-monitor.properties");

            ActiveMQChecker checker = configuration.createChecker();
            QueueMonitorListener listener = configuration.createListener();
            QueueCollection monitors = configuration.createQueueMonitors();
            Collector collector = new Collector(checker, listener, monitors);
            collector.start();;
        } catch(Throwable e) {

        }
    }
    
}
