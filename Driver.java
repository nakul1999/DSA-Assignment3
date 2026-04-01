/*
the Driver Class,

this class contains the information about the driver :- 
DriverID,
X and Y coordinate 
Is the driver is available at the moment.

*/

public class Driver {
    private int driverId;
    private double x;
    private double y;
    private boolean available;

    public Driver(int driverId, double x, double y) {
        this.driverId = driverId;
        this.x = x;
        this.y = y;
        this.available = true;
    }

    public int getDriverId() {
        return driverId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }


/*
    we will use this method to calculate the distance from the driver to the user.
    we make the Driver X and Y coordinates and compare them to the user X and Y coordinates
    and calculate the euclidian distance.

*/
    public double distanceTo(double userX, double userY) {
        return Math.sqrt(Math.pow(this.x - userX, 2) + Math.pow(this.y - userY, 2));
    }

    @Override
    public String toString() {
        return "Driver {driverId =" + driverId + ", location=(" + x + "," + y + ")}";
    }
}