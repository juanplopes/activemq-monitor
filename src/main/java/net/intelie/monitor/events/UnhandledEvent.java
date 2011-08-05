package net.intelie.monitor.events;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UnhandledEvent extends BaseEvent {
    public UnhandledEvent(Throwable e) {
        super("Unhandled exception: " + e.getClass().getName(),
                "Unhandled exception: " + e.getClass().getName() + " (" + e.getMessage() + ")\n\n" + getStackTrace(e),
                e);
    }

    static String getStackTrace(Throwable e) {
        StringWriter w = new StringWriter();
        PrintWriter p = new PrintWriter(w);
        e.printStackTrace(p);
        return w.getBuffer().toString();
    }
}
