package com.kuali.simulator.model;

import java.util.ArrayList;
import java.util.List;

import com.kuali.simulator.common.Constants.DIRECTION;
import com.kuali.simulator.controller.ElevatorController;

public class Elevator {

	private String name;
	private int currentFloor = 1;
	private int totalFloorsTravelled = 0;
	private List<Integer> stops = new ArrayList<Integer>();
	private DIRECTION movingDirection;
	
	public Elevator (String name, int initialFloor, ElevatorController controller)
	{
		this.name = name;		
		this.currentFloor = initialFloor;
		
		run();
	}
	
	private void run()
	{
		// simulate the elevator running
		while(true)
		{
			// TODO find out it should stop

			// TODO find out what's the direction to move
			
			// update current floor
			currentFloor += movingDirection == DIRECTION.UP ? 1 : -1; 	
			totalFloorsTravelled++;
		}
	}
	
	public boolean underMaintenance()
	{
		return totalFloorsTravelled > 100; // too small? how do you define a trip?
	} 
	
}
