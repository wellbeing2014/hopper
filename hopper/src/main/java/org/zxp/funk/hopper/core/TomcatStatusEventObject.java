package org.zxp.funk.hopper.core;

import java.util.EventObject;

public class TomcatStatusEventObject extends EventObject {

	
	private static final long serialVersionUID = 6263405970037813714L;
	public TomcatStatus status;
	public TomcatStatusEventObject(Object source,TomcatStatus status) {
		super(source);
		this.status =status;
	}

}
