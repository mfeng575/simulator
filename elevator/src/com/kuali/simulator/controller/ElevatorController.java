package com.kuali.simulator.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kuali.simulator.common.Constants.DIRECTION;
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
			processPassengers();
			
			Thread.sleep(1000); //
		}
	}	
	
	private synchronized void processPassengers() throws Exception
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
            if(p.getState() == PASSENGER_STATE.WAIT)
            {
            	Elevator elevator = requestElevator(p);
            	elevator.addPassenger(p.getInitialFloor(), p.getTargetFloor(), p);
            }
        }
 		
	}
	
	private Elevator requestElevator(Passenger passenger) throws Exception
	{
		Double ranking = null;
		Elevator chosenOne = null;
		
		for(Elevator elevator : elevators)
		{
			// find out ranking of elevators
			Double newRanking = elevator.getRequestCandidateFactor(passenger.getInitialFloor(), 
											   					   passenger.getTargetFloor() > passenger.getInitialFloor()? DIRECTION.UP : DIRECTION.DOWN);
			if(ranking == null)
			{
				ranking = newRanking;
				chosenOne = elevator;
			}
			if(ranking != null && newRanking < ranking)
			{
				ranking = newRanking;
				chosenOne = elevator;
			}		
		}
		
		return chosenOne;
	}
	
	public synchronized void updatePassengers(List<Passenger> passengers, PASSENGER_STATE origState, PASSENGER_STATE newState) 
	{
		for(Passenger passenger : passengers)
		{
			if(passenger.getState() == origState) passenger.setState(newState);
		}
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
