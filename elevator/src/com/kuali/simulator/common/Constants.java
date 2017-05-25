package com.kuali.simulator.common;

public interface Constants {
	public int timeMovingBetweenFloor = 5000; // 5 seconds
	public int timeOpenCloseDoor = 20000; // 20 seconds
	
	public enum DIRECTION {UP, DOWN, NONE};
	public enum ELEVATOR_STATE {IDLE, TRANSIT, LOADING};
}
