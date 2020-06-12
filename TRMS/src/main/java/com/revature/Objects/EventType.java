package com.revature.Objects;

import java.io.Serializable;

public class EventType implements Serializable {

	private int eventTypeId;
	private String eventType;
	private double coverage;
	
	public EventType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(int eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public double getCoverage() {
		return coverage;
	}

	public void setCoverage(double coverage) {
		this.coverage = coverage;
	}
	
	
	
}
