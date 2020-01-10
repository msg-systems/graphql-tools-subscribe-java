package com.thinkenterprise.gts.actuator;

public interface GtsCounterNotification {
	
	/// value byNumber   +/- 
	public void onModifiedCounter(GtsCounterNames counterName, long byNumber);

}
