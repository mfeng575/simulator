package com.kuali.simulator.model;

import com.kuali.simulator.common.Constants.PASSENGER_STATE;

public class Passenger {
	private int initialFloor;
	private int targetFloor;
	private PASSENGER_STATE state;
	
	public Passenger(int initialFloor, int targetFloor)
	{
		this.initialFloor = initialFloor;
		this.targetFloor = targetFloor;
		state = PASSENGER_STATE.WAIT;
	}
	
	public int getInitialFloor() {
		return initialFloor;
	}
	public void setInitialFloor(int initialFloor) {
		this.initialFloor = initialFloor;
	}
	public int getTargetFloor() {
		return targetFloor;
	}
	public void setTargetFloor(int targetFloor) {
		this.targetFloor = targetFloor;
	}
	public PASSENGER_STATE getState() {
		return state;
	}
	public void setState(PASSENGER_STATE state) {
		this.state = state;
	}
}
