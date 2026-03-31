import java.util.*;

public class ExperimentRunner {

    public static void main(String[] args) {
        int[] sizes = {50000, 10000, 1000, 5000};
        int requests = 500; // simulate real system load

        Random rand = new Random();

        for (int size : sizes) {

            List<Driver> drivers1 = new ArrayList<>();
            List<Driver> drivers2 = new ArrayList<>();

            // Generate same drivers
            for (int i = 0; i < size; i++) {
                double x = rand.nextDouble() * 100;
                double y = rand.nextDouble() * 100;

                drivers1.add(new Driver(i, x, y));
                drivers2.add(new Driver(i, x, y));
            }

            double userX = 50;
            double userY = 50;

            // ---------------- HEAP APPROACH ----------------
            RideMatchingSystem system = new RideMatchingSystem(userX, userY);

            for (Driver d : drivers1) {
                system.addDriver(d);
            }

            long heapStart = System.nanoTime();

            for (int i = 0; i < requests; i++) {
                system.findNearestDriver();
            }

            long heapEnd = System.nanoTime();

            // ---------------- BRUTE FORCE ----------------
            long bruteStart = System.nanoTime();

            for (int i = 0; i < requests; i++) {
                BruteForceMatcher.findNearestDriver(drivers2, userX, userY);
            }

            long bruteEnd = System.nanoTime();

            // ---------------- RESULTS ----------------
            System.out.println("Drivers: " + size + ", Requests: " + requests);
            System.out.println("Heap Time (ms): " + (heapEnd - heapStart) / 1_000_000.0);
            System.out.println("Brute Force Time (ms): " + (bruteEnd - bruteStart) / 1_000_000.0);
            System.out.println("---------------------------------------");
        }
    }
}