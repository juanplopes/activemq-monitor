package net.intelie.monitor.engine;

import net.intelie.monitor.listeners.QueueMonitorListener;
import net.intelie.monitor.notifiers.EmailNotifier;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MonitorConfiguration {
    private static Logger logger = Logger.getLogger(MonitorConfiguration.class);
    private String[] recipients, monitoredQueues;
    private String server, port, path;
    private String domain, brokerName, company;
    private long notificationInterval;

    public MonitorConfiguration(String resource) {
        this(MonitorConfiguration.class.getClassLoader().getResourceAsStream(resource));
    }

    public MonitorConfiguration(InputStream stream) {
        try {
            Properties properties = new Properties();
            properties.load(stream);
            server = properties.getProperty("server");
            port = properties.getProperty("port");
            path = properties.getProperty("connectorPath");

            domain = properties.getProperty("domain");
            brokerName = properties.getProperty("brokerName");

            notificationInterval = Long.parseLong(properties.getProperty("minInterval")) * 60 * 1000;

            company = properties.getProperty("company");

            recipients = properties.getProperty("recipients").split(",");
            monitoredQueues = properties.getProperty("monitor").split(",");

            trimAll(recipients);
            trimAll(monitoredQueues);
        } catch (IOException e) {
            throw new RuntimeException("Could not load properties. Is file activemq-monitor.properties in classpath?", e);
        }
    }

    private void trimAll(String[] values) {
        for (int i =0; i<values.length; i++)
            if (values[i] != null)
                values[i] = values[i].trim();
    }

    public QueueMonitorListener createListener() {
        return new QueueMonitorListener(company, new EmailNotifier(recipients), notificationInterval);
    }

    public QueueCollection createQueueMonitors() {
        return new QueueCollection(monitoredQueues);
    }

    public ActiveMQChecker createChecker() {
        return new ActiveMQChecker(server, port, path, domain, brokerName);
    }
}
