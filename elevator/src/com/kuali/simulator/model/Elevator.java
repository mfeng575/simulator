package com.kuali.simulator.model;

public class Elevator {

	private String name;
	private int currentFloor = 1;
	private int totalFloorsTravelled = 0;
	
	public Elevator (String name, int initialFloor)
	{
		this.name = name;		
		this.currentFloor = initialFloor;
	}
	
	
}
