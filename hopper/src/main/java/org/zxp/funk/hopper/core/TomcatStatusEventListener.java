package org.zxp.funk.hopper.core;

import java.util.EventListener;

public interface TomcatStatusEventListener extends EventListener {
    public void statusChanged(TomcatStatusEventObject obj);
}