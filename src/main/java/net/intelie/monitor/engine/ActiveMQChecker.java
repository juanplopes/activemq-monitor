package net.intelie.monitor.engine;

import net.intelie.monitor.events.QueueNotFound;
import net.intelie.monitor.events.ServerUnavailable;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.log4j.Logger;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Hashtable;


public class ActiveMQChecker implements EngineChecker {
    private static Logger logger = Logger.getLogger(ActiveMQChecker.class);
    private JMXConnector connector;
    private MBeanServerConnection connection;
    private String server, port, path, domain, brokerName;

    public ActiveMQChecker(String server, String port, String path, String domain, String brokerName) {
        this.server = server;
        this.port = port;
        this.path = path;
        this.domain = domain;
        this.brokerName = brokerName;
    }

    @Override
    public void connect() throws ServerUnavailable {
        String fullUrl = "service:jmx:rmi:///jndi/rmi://" + server + ":" + port + path;
        try {
            logger.debug("Connecting to " + server + " on port " + port);

            JMXServiceURL url = new JMXServiceURL(fullUrl);
            logger.debug("To url " + url.toString());
            this.connector = JMXConnectorFactory.newJMXConnector(url, new HashMap());
            this.connector.connect();
            this.connection = connector.getMBeanServerConnection();

            logger.debug("Connected " + connector.getConnectionId());
        } catch (MalformedURLException e) {
            throw new ServerUnavailable(fullUrl, e);
        } catch (IOException e) {
            throw new ServerUnavailable(fullUrl, e);
        }
    }

    @Override
    public void disconnect() {
        try {
            connector.close();
        } catch (IOException e) {
            logger.error("Error closing connection");
        }
    }

    @Override
    public long getDequeueCount(String queueName) throws QueueNotFound {
        Hashtable<String, String> table = getParamTable(queueName);

        try {
            QueueViewMBean queueView = JMX.newMBeanProxy(connection, new ObjectName(this.domain, table), QueueViewMBean.class);
            return queueView.getDequeueCount();
        } catch (UndeclaredThrowableException e) {
            throw new QueueNotFound(queueName, e);
        } catch (MalformedObjectNameException e) {
            throw new QueueNotFound(queueName, e);
        }
    }

    private Hashtable<String, String> getParamTable(String queueName) {
        Hashtable<String, String> table = new Hashtable<String, String>();
        table.put("BrokerName", this.brokerName);
        table.put("Type", "Queue");
        table.put("Destination", queueName);
        return table;
    }
}
