
public class Ambulance {

    private String ambulanceId;
    private String type; // "Standard" ambulance or "Advanced" ambulance
    private boolean available;

    public Ambulance(String ambulanceId, String type) {
        this.ambulanceId = ambulanceId;
        this.type = type;
        this.available = true;
    }

    public String getAmbulanceId() {
        return ambulanceId;
    }

    public String getType() {
        return type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String toString() {
        return ambulanceId + " (" + type + ") - " + (available ? "Available" : "In Use");
    }
}
