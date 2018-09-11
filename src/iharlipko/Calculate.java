package iharlipko;

import java.awt.geom.Point2D;

/**
 * RoboGroM - Calculate class. Calculate points and angles for RoboGorM current or last position,
 * destination points, etc
 * 
 * @version 1.1
 * 
 * Date: Sep 10, 2018
 * 
 * @author Ihar_Lipko
 */
public class Calculate {
    public static Point2D.Double calcPoint(Point2D.Double position, double distance,
            double angle) {
        return new Point2D.Double(position.x + distance * Math.sin(angle),
                position.y + distance * Math.cos(angle));
    }

    public static double calcAngle(Point2D.Double position2, Point2D.Double position1) {
        return Math.atan2(position2.x - position1.x, position2.y - position1.y);
    }
}
