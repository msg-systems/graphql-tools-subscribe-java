package com.graphqlio.gts.actuator;

public interface GtsCounterNotification {
	
	/// value byNumber   +/- 
	public void onModifiedCounter(GtsCounterNames counterName, long byNumber);

}
