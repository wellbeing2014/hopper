package org.zxp.funk.hopper.core;

import java.util.EventObject;

public class TomcatLogEventObject extends EventObject {

	
	private static final long serialVersionUID = 6263405970037813714L;
	public String log;
	public TomcatLogEventObject(Object source,String log) {
		super(source);
		this.log =log;
	}

}
