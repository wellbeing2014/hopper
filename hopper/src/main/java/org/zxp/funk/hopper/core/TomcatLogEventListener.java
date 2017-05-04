package org.zxp.funk.hopper.core;

import java.util.EventListener;

public interface TomcatLogEventListener extends EventListener {
 
    public void logEvent(TomcatLogEventObject obj);
}