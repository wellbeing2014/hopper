package org.zxp.funk.hopper.core;

import java.util.EventObject;

public class HopperStatusEventObject extends EventObject {

	
	private static final long serialVersionUID = 6263405970037813714L;
	public HopperStatus status;
	public HopperStatusEventObject(Object source,HopperStatus status) {
		super(source);
		this.status =status;
	}

}
