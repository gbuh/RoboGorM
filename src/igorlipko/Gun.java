package igorlipko;

import robocode.util.Utils;

import java.awt.geom.Point2D;

/**
 * RoboGorM - Gun class
 * RoboGorM fire gun
 * 
 * @version 1.0 12 Apr 2018
 * @author  Igor Lipko
 */
public class Gun {
    private RoboGorM bot;
    protected Point2D.Double nextDestination;
    protected Point2D.Double lastPosition;
    protected Point2D.Double myPosition;
    protected double myEnergy;

    public Gun(RoboGorM bot) {
        this.bot = bot;
        nextDestination = new Point2D.Double(bot.getX(), bot.getY());
        lastPosition = nextDestination;
        myPosition = lastPosition;
        myEnergy = bot.getEnergy();
    }

    // I targeting head on enemy and fire gun
    public void doGunFire(Enemy target) {
        double distanceToTarget = myPosition.distance(target.position);
        
        if (bot.getGunTurnRemaining() == 0 && myEnergy > 1) {
            bot.setFire(
                    Math.min(Math.min(myEnergy / 6d, 1300d / distanceToTarget),
                            target.energy / 3d));
        }

        bot.setTurnGunRightRadians(Utils.normalRelativeAngle(
                Calculate.calcAngle(target.position, myPosition)
                        - bot.getGunHeadingRadians()));
    }
}
