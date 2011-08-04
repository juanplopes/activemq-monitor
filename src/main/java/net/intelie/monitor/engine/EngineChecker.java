package net.intelie.monitor.engine;

import net.intelie.monitor.events.QueueNotFound;
import net.intelie.monitor.events.ServerUnavailable;

public interface EngineChecker {
    void connect() throws ServerUnavailable;
    void disconnect();
    long getDequeueCount(String queueName) throws QueueNotFound;
}
