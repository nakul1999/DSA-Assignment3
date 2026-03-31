import java.util.ArrayList;
import java.util.List;

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

    public Driver findNearestDriver() {
        Driver nearest = heap.extractMin();

        if (nearest != null) {
            nearest.setAvailable(false);
        }

        return nearest;
    }
}