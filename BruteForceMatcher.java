import java.util.List;

public class BruteForceMatcher {

    public static Driver findNearestDriver(List<Driver> drivers, double userX, double userY) {
        Driver nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Driver d : drivers) {
            if (d.isAvailable()) {
                double dist = d.distanceTo(userX, userY);

                if (dist < minDistance) {
                    minDistance = dist;
                    nearest = d;
                }
            }
        }

        if (nearest != null) {
            nearest.setAvailable(false);
        }

        return nearest;
    }
}