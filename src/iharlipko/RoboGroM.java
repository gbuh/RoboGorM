package iharlipko;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * RoboGroM - a Melee and 1v1 bot.
 * 
 * @version 1.2
 * 
 * Date: Sep 12, 2018
 * 
 * @author Ihar_Lipko
 */
public class RoboGroM extends AdvancedRobot {
    private Bearing bear;
    private Gun gun;
    private Evaluate eval;

    public void run() {
        try {
            initComponents();
            initColors();

            setAdjustGunForRobotTurn(true);
            setAdjustRadarForGunTurn(true);
            setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
            // Executable methods
            do {
                bear.myPosition = new Point2D.Double(getX(), getY());
                gun.myEnergy = getEnergy();
                // Waiting until will be scanned all other robots
                // (time around 7 to 9 ticks).
                if (eval.target.live && getTime() > 9) {
                    bear.doMovement(eval.target);
                    gun.doGunFire(eval.target);
                }
                execute();
                // Check for new targets.
                // Only necessary for Narrow Lock because sometimes our radar is already
                // pointed at the enemy and our onScannedRobot code doesn't end up telling
                // it to turn, so the system doesn't automatically call scan() for us
                scan();
            } while (true);
        } catch (RuntimeException re) {
            System.out.println(re);
        }
    }

    // Robot elements initialization process
    private void initComponents() {
        if (bear == null) {
            bear = new Bearing(this);
        }
        if (gun == null) {
            gun = new Gun(this);
        }
        if (eval == null) {
            eval = new Evaluate(this);
        }
    }

    // Robot colors settings
    private void initColors() {
        setColors(Color.green, Color.green, Color.black);
    }

    // When an opponent was scanned evaluating begin
    public void onScannedRobot(ScannedRobotEvent event) {
        try {
            eval.onScannedRobot(event);
        } catch (RuntimeException re) {
            System.out.println(re);
        }
    }

    // When some robot has died, note the information about it
    public void onRobotDeath(RobotDeathEvent event) {
        try {
            eval.onRobotDeath(event);
        } catch (RuntimeException re) {
            System.out.println(re);
        }
    }
}
