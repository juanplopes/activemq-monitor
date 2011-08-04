package net.intelie.monitor.notifiers;

/**
 * Created by IntelliJ IDEA.
 * User: juanplopes
 * Date: 8/5/11
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Notifier {
    void send(String subject, String body);
}
