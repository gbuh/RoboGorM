package iharlipko;

import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

import java.awt.geom.Point2D;
import java.util.HashMap;

/**
 * RoboGroM - Evaluate class Evaluate RoboGroM game state.
 * 
 * @version 1.2
 * 
 * Date: Sep 12, 2018
 * 
 * @author Ihar_Lipko
 */
public class Evaluate {
    private RoboGroM bot;
    protected Point2D.Double lastPosition;
    protected Point2D.Double myPosition;
    protected Enemy target;
    protected HashMap<String, Enemy> enemies;

    public Evaluate(RoboGroM bot) {
        this.bot = bot;
        myPosition = new Point2D.Double(bot.getX(), bot.getY());
        lastPosition = myPosition;
        target = new Enemy();
        enemies = new HashMap<>();
    }

    public void onScannedRobot(ScannedRobotEvent event) {
        Enemy enemy = (Enemy) enemies.get(event.getName());

        if (enemy == null) {
            enemy = new Enemy();
            enemies.put(event.getName(), enemy);
        }

        enemy.energy = event.getEnergy();
        enemy.live = true;
        enemy.position = Calculate.calcPoint(myPosition, event.getDistance(),
                bot.getHeadingRadians() + event.getBearingRadians());

        // Target selection: the one closer to you is the most dangerous so attack him
        if (!target.live || event.getDistance() < myPosition.distance(target.position)) {
            target = enemy;
        }

        // Locks the radar if there is only one opponent left
        if (bot.getOthers() == 1) {
            double radarTurn =
                    // Absolute bearing to target
                    bot.getHeadingRadians() + event.getBearingRadians()
                    // Subtract current radar heading to get turn required
                            - bot.getRadarHeadingRadians();

            bot.setTurnRadarRightRadians(1.9 * Utils.normalRelativeAngle(radarTurn));
        }
    }

    // Change flag for robot death
    public void onRobotDeath(RobotDeathEvent event) {
        ((Enemy) enemies.get(event.getName())).live = false;
    }
}
