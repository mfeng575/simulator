package com.kuali.simulator.controller;

import java.util.ArrayList;
import java.util.List;

import com.kuali.simulator.model.Elevator;

public class ElevatorController {
	private int totalFloor = 1;
	private List<Elevator> elevators = new ArrayList<Elevator>();
	
	public ElevatorController(int totalFloor, int totalElevator) 
	{
		this.totalFloor = totalFloor;
		
		for(int i = 0; i < totalElevator; i++)
		{
			elevators.add(new Elevator("Elevator " + i, 1));
		}		
	}

	public static void main(String[] args)
	{
		try
		{
			// start the test
			ElevatorController controller = new ElevatorController(10, 3);			

		}
		catch (Exception e)
		{
			System.out.println("OOPS : " + e.getMessage());
		}
	}
}
