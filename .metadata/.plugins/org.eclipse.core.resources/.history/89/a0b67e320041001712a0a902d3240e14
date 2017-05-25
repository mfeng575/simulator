package com.kuali.simulator.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kuali.simulator.common.Constants.PASSENGER_STATE;
import com.kuali.simulator.model.Elevator;
import com.kuali.simulator.model.Passenger;

public class ElevatorController {
	private int totalFloor = 1;
	private List<Elevator> elevators = new ArrayList<Elevator>();
	private List<Passenger> passengers = new ArrayList<Passenger>();
	
	public ElevatorController(int totalFloor, int totalElevator) throws Exception 
	{
		this.totalFloor = totalFloor;
		
		for(int i = 0; i < totalElevator; i++)
		{
			elevators.add(new Elevator("Elevator " + i, 1, this));
		}		
		
		run();
	}

	private void run() throws Exception
	{
		while(true)
		{
			updatePassengers();
			
			Thread.sleep(1000); //
		}
	}	
	
	private synchronized void updatePassengers() 
	{
		// TODO remove all delivered passengers
		Iterator<Passenger> iter = passengers.iterator();
        while(iter.hasNext()) 
		{
        	Passenger p = iter.next();
            if (p.getState() == PASSENGER_STATE.DELIVERED) 
            {
            	iter.remove();
            }
        }
        
		// TODO request stops for all waiting passengers
		
	}
	
	public void addPassenger(Passenger passenger) throws Exception
	{
		this.passengers.add(passenger);		
	}
	
	public static void main(String[] args)
	{
		try
		{
			// start the test
			ElevatorController controller = new ElevatorController(10, 3);	
			
			Thread.sleep(30000);

			// add passenger
			controller.addPassenger(new Passenger(1, 4));
		}
		catch (Exception e)
		{
			System.out.println("OOPS : " + e.getMessage());
		}
	}
}
