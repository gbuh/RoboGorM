package igorlipko;

import java.awt.geom.Point2D;

/**
 * RoboGorM - Calculate class
 * Calculate points and angles for RoboGorM current or last position,
 * destination points, etc
 * 
 * @version 1.0 12 Apr 2018
 * @author  Igor Lipko
 */
public class Calculate {
    public static Point2D.Double calcPoint(Point2D.Double p, double dist,
            double ang) {
        return new Point2D.Double(p.x + dist * Math.sin(ang),
                p.y + dist * Math.cos(ang));
    }

    public static double calcAngle(Point2D.Double p2, Point2D.Double p1) {
        return Math.atan2(p2.x - p1.x, p2.y - p1.y);
    }
}
