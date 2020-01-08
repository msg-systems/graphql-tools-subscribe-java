package com.thinkenterprise.graphqlio.server.gts.actuator;

public interface GtsCounterNotification {
	
	/// value byNumber   +/- 
	public void onModifiedCounter(GtsCounterNames counterName, long byNumber);

}
