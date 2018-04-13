package igorlipko;

import robocode.*;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * RoboGorM - a Melee and 1v1 bot

 * @version 1.0 12 Apr 2018
 * @author  Igor Lipko
 */
public class RoboGorM extends AdvancedRobot {
    private static Bearing bear;
    private static Gun gun;
    private static Evaluate eval;

    public void run() {
        try {
            initComponents();
            initColors();

            setAdjustGunForRobotTurn(true);
            setAdjustRadarForGunTurn(true);
            setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
            // Add my execute methods
            do {
                bear.myPosition = new Point2D.Double(getX(), getY());
                gun.myEnergy = getEnergy();
                // I wait until will be scanned all other robots
                // (time around 7 to 9 ticks).
                if (eval.target.live && getTime() > 9) {
                    bear.doMovement(eval.target);
                    gun.doGunFire(eval.target);
                }
                execute();
            } while (true);
        } catch (RuntimeException re) {
            System.out.println(re);
        }
    }

    // Initialization process for my data structure
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

    // Fancy colors for my robot
    private void initColors() {
        setColors(Color.green, Color.green, Color.black);
    }

    // When I scan an opponent do evaluating
    public void onScannedRobot(ScannedRobotEvent e) {
        try {
            eval.onScannedRobot(e);
        } catch (RuntimeException re) {
            System.out.println(re);
        }
    }

    // When some robot has died, I note the information about it
    public void onRobotDeath(RobotDeathEvent e) {
        try {
            eval.onRobotDeath(e);
        } catch (RuntimeException re) {
            System.out.println(re);
        }
    }
}
