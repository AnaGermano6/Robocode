package man;

import java.awt.Window;

import robocode.*;


public class DodgeBot extends AdvancedRobot {

	private final double BASE_ENERGY = 100;
	
  double previousEnergy = 100;
  int movementDirection = 1;
  int gunDirection = 1;
  public void run() {
    setTurnGunRight(Integer.MAX_VALUE);
  }
  public void onScannedRobot(
    ScannedRobotEvent e) {
      // Stay at right angles to the opponent	
      setTurnRight(e.getBearing()+90-
         30*movementDirection);
         
     // If the bot has small energy drop,
    // assume it fired
    double changeInEnergy =
      previousEnergy-e.getEnergy();
    if (changeInEnergy>0 &&
        changeInEnergy<=3) {
         // Dodge!
         movementDirection =
          -movementDirection;
         setAhead((e.getDistance()/4+25)*movementDirection);
     }
    // When a bot is spotted,
    // sweep the gun and radar
   gunDirection = -gunDirection;
   setTurnGunRight(Integer.MAX_VALUE*gunDirection);
    
    // Fire directly at target
    if(e.getDistance() > getBattleFieldWidth()*0.8)
    	fire (0.1 ) ;
    else if(e.getDistance() > getBattleFieldWidth()*0.6)
    	fire (1 ) ;
    else if(e.getDistance() > getBattleFieldWidth()*0.4)
    	fire (2 ) ;
    else if(e.getDistance() > getBattleFieldWidth()*0.1)
    	fire (3 ) ;
    
    // Track the energy level
    previousEnergy = e.getEnergy();
    
    
  }
}
