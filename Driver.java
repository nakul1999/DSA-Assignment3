public class Driver {
    private int id;
    private double x;
    private double y;
    private boolean available;

    public Driver(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.available = true;
    }

    public int getId() {
        return id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double distanceTo(double userX, double userY) {
        return Math.sqrt(Math.pow(this.x - userX, 2) + Math.pow(this.y - userY, 2));
    }

    @Override
    public String toString() {
        return "Driver{id=" + id + ", location=(" + x + "," + y + ")}";
    }
}