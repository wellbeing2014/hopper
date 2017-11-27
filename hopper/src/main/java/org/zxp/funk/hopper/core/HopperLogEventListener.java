package org.zxp.funk.hopper.core;

import java.util.EventListener;

public interface HopperLogEventListener extends EventListener {
 
    public void logEvent(HopperLogEventObject obj);
}