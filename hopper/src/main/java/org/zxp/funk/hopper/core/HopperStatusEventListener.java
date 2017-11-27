package org.zxp.funk.hopper.core;

import java.util.EventListener;

public interface HopperStatusEventListener extends EventListener {
    public void statusChanged(HopperStatusEventObject obj);
}