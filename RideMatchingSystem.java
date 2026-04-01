import java.util.ArrayList;
import java.util.List;


/*

This is our Ride Matching System,

here we are taking user coordinates and creating a heap based on that.
we will add the drivers, and sort it based on how close they are to the driver

*/

public class RideMatchingSystem {

    private List<Driver> drivers;
    private MinHeap heap;

    public RideMatchingSystem(double userX, double userY) {
        drivers = new ArrayList<>();
        heap = new MinHeap(userX, userY);
    }

    public void addDriver(Driver driver) {
        drivers.add(driver);
        heap.insert(driver); // insert once
    }

/*

we will find the driver now, based on the least distance to the User.
once we get the driver we will make them unavailable.

*/
    public Driver findNearestDriver() {
        Driver nearest = heap.extractMin();

        if (nearest != null) {
            nearest.setAvailable(false);
        }

        return nearest;
    }
}