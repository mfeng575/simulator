# simulator
Elevator simulator

So here is the basic idea - 

There are controller, elevator, passenger, where controller is the cheese which tie everything together.

Both controller and elevator are continously running engines
 - controller accept passengers and request elevator based off their start floor and intended direction
 - elevator make stops base-off the start & stop floors of requesting passengers
 
The algorithm in requesting the elevator is based off these factors of the elevator
 - current state
 - moving direction 
 - distace to the requesting floor (if moving away it would count distance to snakeback) 
 
 I did not test the program in runtime (even though it compiles).
