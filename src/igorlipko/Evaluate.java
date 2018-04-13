package igorlipko;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

/**
 * RoboGorM - Evaluate class
 * Evaluate RoboGorM game state
 * 
 * @version 1.0 12 Apr 2018
 * @author  Igor Lipko
 */
public class Evaluate {
    private RoboGorM bot;
    protected Point2D.Double lastPosition;
    protected Point2D.Double myPosition;
    protected Enemy target;
    protected HashMap<String, Enemy> enemies;
    double addLast;

    public Evaluate(RoboGorM bot) {
        this.bot = bot;
        myPosition = new Point2D.Double(bot.getX(), bot.getY());
        lastPosition = myPosition;
        target = new Enemy();
        enemies = new HashMap<>();
        addLast = 1 - Math.rint(Math.pow(Math.random(), bot.getOthers()));
    }

    public double evaluate(Point2D.Double p, double addLast) {
        // This is basically here that the robot uses more space on the
        // battlefield. In melee it is dangerous to stay somewhere too long.
        double eval = addLast * 0.08 / p.distanceSq(lastPosition);

        for (Map.Entry<String, Enemy> enemy : enemies.entrySet()) {
            Enemy en = (Enemy) enemy.getValue();

            if (en.live) {
                eval += Math.min(en.energy / bot.getEnergy(), 2)
                        * (1 + Math.abs(Math.cos(Calculate.calcAngle(myPosition, p)
                                - Calculate.calcAngle(en.position, p))))
                        / p.distanceSq(en.position);
            }
        }
        return eval;
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        Enemy en = (Enemy) enemies.get(e.getName());

        if (en == null) {
            en = new Enemy();
            enemies.put(e.getName(), en);
        }

        en.energy = e.getEnergy();
        en.live = true;
        en.position = Calculate.calcPoint(myPosition, e.getDistance(),
                bot.getHeadingRadians() + e.getBearingRadians());

        // Target selection: the one closer to you is the most dangerous
        // so attack him
        if (!target.live || e.getDistance() < myPosition.distance(target.position)) {
            target = en;
        }

        // I locks the radar if there is only one opponent left
        if (bot.getOthers() == 1)
            bot.setTurnRadarLeftRadians(bot.getRadarTurnRemainingRadians());
    }

    // Change flag for robot death
    public void onRobotDeath(RobotDeathEvent e) {
            ((Enemy)enemies.get(e.getName())).live = false;
        }
}
