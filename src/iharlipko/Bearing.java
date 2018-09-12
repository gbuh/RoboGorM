package iharlipko;

import robocode.util.Utils;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * RoboGroM - Bearing class (RoboGroM movement).
 * 
 * @version 1.1
 * 
 * Date: Sep 10, 2018
 * 
 * @author Ihar_Lipko
 */
public class Bearing {
    private RoboGroM bot;
    protected Point2D.Double nextDestination;
    protected Point2D.Double lastPosition;
    protected Point2D.Double myPosition;
//    protected double addLast;

    public Bearing(RoboGroM bot) {
        this.bot = bot;
        nextDestination = new Point2D.Double(bot.getX(), bot.getY());
        lastPosition = nextDestination;
        myPosition = lastPosition;
    }

    public void doMovement(Enemy target) {
        double distanceToTarget = myPosition.distance(target.position);
        double distanceToNextDestination = myPosition.distance(nextDestination);

        // Search a new destination point
        if (distanceToNextDestination < 15) {

            // Formula to increase 1-v-1 performance.
            // With more robots addLast will mostly be 1
//            addLast = 1 - Math.rint(Math.pow(Math.random(), bot.getOthers()));

            Rectangle2D.Double battleField = new Rectangle2D.Double(30, 30,
                    bot.getBattleFieldWidth() - 60,
                    bot.getBattleFieldHeight() - 60);
            Point2D.Double testPoint;
            int i = 0;

            do {
                // calculate the testPoint somewhere around the current position.
                testPoint = Calculate.calcPoint(myPosition,
                        Math.min(distanceToTarget * 0.8,
                                100 + 200 * Math.random()),
                        2 * Math.PI * Math.random());
                if (battleField.contains(testPoint)) {
                    nextDestination = testPoint;
                }
            } while (i++ < 200);

            lastPosition = myPosition;

        } else {

            double angle = Calculate.calcAngle(nextDestination, myPosition)
                    - bot.getHeadingRadians();
            double direction = 1;

            if (Math.cos(angle) < 0) {
                angle += Math.PI;
                direction = -1;
            }

            bot.setAhead(distanceToNextDestination * direction);
            bot.setTurnRightRadians(angle = Utils.normalRelativeAngle(angle));
            bot.setMaxVelocity(Math.abs(angle) > 1 ? 0 : 8d);
        }
    }
}
