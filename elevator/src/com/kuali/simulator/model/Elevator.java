package com.kuali.simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.kuali.simulator.common.Constants;
import com.kuali.simulator.common.Constants.DIRECTION;
import com.kuali.simulator.common.Constants.ELEVATOR_STATE;
import com.kuali.simulator.common.Constants.PASSENGER_STATE;
import com.kuali.simulator.controller.ElevatorController;

public class Elevator {

	private String name;
	private int currentFloor = 1;
	private Integer nextStop = null;
	private int totalFloorsTravelled = 0;
	private Integer ultimateHigh, ultimateLow = null;
	private HashMap<Integer, List<Passenger>> stops = new HashMap<Integer, List<Passenger>>();
	private DIRECTION movingDirection;
	private ELEVATOR_STATE state;
	private ElevatorController controller;
	
	public Elevator (String name, int initialFloor, ElevatorController controller) throws Exception
	{
		this.name = name;		
		this.currentFloor = initialFloor;
		this.controller = controller;
		
		run();
	}
	
	private void run() throws Exception
	{
		// simulate the elevator running
		while(true)
		{
			// if not found any floor to stop, then stop
			if(stops.isEmpty())
			{
				movingDirection = DIRECTION.NONE;
				state = ELEVATOR_STATE.IDLE;
				// do nothing and skip all logic
				continue;
			}
			
			if(stops.containsKey(currentFloor))
			{
				// if this is the floor, then stop and open the door
				state = ELEVATOR_STATE.LOADING;
				
				// unload passengers
				controller.updatePassengers(stops.get(currentFloor), PASSENGER_STATE.TRANSIT, PASSENGER_STATE.DELIVERED);
				
				// load passengers
				controller.updatePassengers(stops.get(currentFloor), PASSENGER_STATE.ASSIGNED, PASSENGER_STATE.TRANSIT);
				
				// remove the stop
				stops.remove(currentFloor);
				
				Thread.sleep(Constants.timeOpenCloseDoor);
				
				// elevator should stop now
				if(stops.isEmpty())
				{
					continue;
				}
			}
			
			//  found the nearest stop and moving to that direction
			nextStop = findNextStop();
			
			movingDirection = nextStop > currentFloor ? DIRECTION.UP : DIRECTION.DOWN;
			
			state = ELEVATOR_STATE.TRANSIT;
			Thread.sleep(Constants.timeMovingBetweenFloor);
			
			// update current floor
			currentFloor += movingDirection == DIRECTION.UP ? 1 : -1; 	
			totalFloorsTravelled++;
		}
	}
	
	private Integer findNextStop() throws Exception
	{
		// find the next stop
		Integer nextStop = null;
		
		Integer nextStopDown = null;
		Integer nextStopUp = null;
		
		// if originally moving down and there's no stop below, then see if there's any stop requested above, if so change direction to up
		if(movingDirection == DIRECTION.DOWN)
		{
			for(Integer stop : stops.keySet())
			{
				if(stop < currentFloor)
				{
					if(nextStopDown == null) nextStopDown = stop;
					else if(stop > nextStopDown) nextStopDown = stop;
				}
				if(stop < currentFloor)
				{
					if(nextStopUp == null) nextStopUp = stop;
					else if(stop > nextStopUp) nextStopUp = stop;
				}
			}
			
			if(nextStopDown != null) nextStop = nextStopDown;
			else if(nextStopUp != null) nextStop = nextStopUp;
		}

		// if originally moving up and there's no stop above, then see if there's any stop requested below, if so change direction to down
		if(movingDirection == DIRECTION.UP)
		{
			for(Integer stop : stops.keySet())
			{
				if(stop < currentFloor)
				{
					if(nextStopDown == null) nextStopDown = stop;
					else if(stop > nextStopDown) nextStopDown = stop;
				}
				if(stop < currentFloor)
				{
					if(nextStopUp == null) nextStopUp = stop;
					else if(stop > nextStopUp) nextStopUp = stop;
				}
			}
			
			if(nextStopUp != null) nextStop = nextStopUp;
			else if(nextStopDown != null) nextStop = nextStopDown;
		}
		
		if(nextStop != null) return nextStop;
		
		// this should not happen
		throw new Exception ("Error in finding next stop");
	}
	
	public void addPassenger(int startFloor, int targetFloor, Passenger passenger) throws Exception
	{
		if(stops.containsKey(startFloor))
		{
			List<Passenger> passengers = stops.get(startFloor);
			if(passenger != null) passengers.add(passenger);
		}
		else
		{
			List<Passenger> passengers = new ArrayList<Passenger>();
			if(passenger != null) passengers.add(passenger);
			stops.put(startFloor, passengers);
		}

		if(stops.containsKey(targetFloor))
		{
			List<Passenger> passengers = stops.get(targetFloor);
			if(passenger != null) passengers.add(passenger);
		}
		else
		{
			List<Passenger> passengers = new ArrayList<Passenger>();
			if(passenger != null) passengers.add(passenger);
			stops.put(targetFloor, passengers);
		}
		
		passenger.setState(PASSENGER_STATE.ASSIGNED);
		
		if(ultimateHigh == null) ultimateHigh = startFloor;
		else if(ultimateHigh < startFloor) ultimateHigh = startFloor;
		
		if(ultimateLow == null) ultimateLow = startFloor;
		else if(ultimateLow > startFloor) ultimateLow = startFloor;

		if(ultimateHigh == null) ultimateHigh = targetFloor;
		else if(ultimateHigh < targetFloor) ultimateHigh = targetFloor;
		
		if(ultimateLow == null) ultimateLow = targetFloor;
		else if(ultimateLow > targetFloor) ultimateLow = targetFloor;
	}
	
	public Double getRequestCandidateFactor(int requestFloor, Constants.DIRECTION reqeuestDirection) throws Exception
	{
		// to simply the algorithm, return the factor base-off the distance from the next hit of the target floor
		
		// if disabled then return null
		if(underMaintenance()) return null;
		
		// if idle, then calculate the distance as is
		if(getState() == Constants.ELEVATOR_STATE.IDLE)
		{
			return getStaticDistance(requestFloor);
		}
	
		// TODO if moving towards target floor
		if(   movingDirection == Constants.DIRECTION.DOWN 
		   && reqeuestDirection == Constants.DIRECTION.DOWN
		   && requestFloor <= currentFloor )				   
		{
			return getState() == Constants.ELEVATOR_STATE.LOADING ? getStaticDistance(requestFloor) : getMovingDistance(requestFloor);
		}
		if(   movingDirection == Constants.DIRECTION.UP 
		   && reqeuestDirection == Constants.DIRECTION.UP
		   && requestFloor >= currentFloor )			   
		{
			return getState() == Constants.ELEVATOR_STATE.LOADING ? getStaticDistance(requestFloor) : getMovingDistance(requestFloor);
		}		
		if(	  movingDirection == Constants.DIRECTION.DOWN 
		   && reqeuestDirection == Constants.DIRECTION.UP
		   && requestFloor <= ultimateLow )
					   
		{
			return getState() == Constants.ELEVATOR_STATE.LOADING ? getStaticDistance(requestFloor) : getMovingDistance(requestFloor);
		}		
		if(	  movingDirection == Constants.DIRECTION.UP 
		   && reqeuestDirection == Constants.DIRECTION.DOWN
		   && requestFloor >= ultimateHigh )						   
		{
			return getState() == Constants.ELEVATOR_STATE.LOADING ? getStaticDistance(requestFloor) : getMovingDistance(requestFloor);
		}		
		
		// TODO if moving away from target floor
		if(   movingDirection == Constants.DIRECTION.DOWN 
			   && reqeuestDirection == Constants.DIRECTION.DOWN
			   && requestFloor > currentFloor )
				   
		{
			return getSnakeBackDistance(ultimateLow) + (getState() == Constants.ELEVATOR_STATE.LOADING ? getStaticDistance(requestFloor) : getMovingDistance(requestFloor));
		}
		if(   movingDirection == Constants.DIRECTION.UP 
			   && reqeuestDirection == Constants.DIRECTION.UP
			   && requestFloor < currentFloor )
				   
		{
			return getSnakeBackDistance(ultimateHigh) + (getState() == Constants.ELEVATOR_STATE.LOADING ? getStaticDistance(requestFloor) : getMovingDistance(requestFloor));
		}		
		if(	  movingDirection == Constants.DIRECTION.DOWN 
		   && reqeuestDirection == Constants.DIRECTION.UP
		   && requestFloor > ultimateLow )
					   
		{
			return getSnakeBackDistance(ultimateLow) + (getState() == Constants.ELEVATOR_STATE.LOADING ? getStaticDistance(requestFloor) : getMovingDistance(requestFloor));
		}		
		if(	  movingDirection == Constants.DIRECTION.UP 
		   && reqeuestDirection == Constants.DIRECTION.DOWN
		   && requestFloor < ultimateHigh )
							   
		{
			return getSnakeBackDistance(ultimateHigh) + (getState() == Constants.ELEVATOR_STATE.LOADING ? getStaticDistance(requestFloor) : getMovingDistance(requestFloor));
		}
		
		// running out of time to figure out what else is possible, so throw an exception so we can tell in unit testing
		throw new Exception("unsupported use case");
	}

	private Double getStaticDistance(int requestFloor)
	{
		return Double.valueOf(Math.abs(currentFloor - requestFloor));
	}

	private Double getMovingDistance(int requestFloor)
	{
		return Double.valueOf(Math.abs(currentFloor - requestFloor)) - 0.5;
	}

	private Double getSnakeBackDistance(int ultimateStop)
	{
		return 2 * Double.valueOf(Math.abs(currentFloor - ultimateStop));
	}

	public ELEVATOR_STATE getState() {
		return state;
	}
	
	public boolean underMaintenance()
	{
		return totalFloorsTravelled > 100; // too small? how do you define a trip?
	} 
	
}
