
public abstract class EmergencyService {

    private String id;
    private String patientName;
    private String location;
    private String status;
    private boolean bedOccupied;

    public EmergencyService(String id, String patientName) {
        this(id, patientName, "Unknown");
    }

    public EmergencyService(String id, String patientName, String location) {
        this.id = id;
        this.patientName = patientName;
        this.location = location;
        this.status = "Pending";
        this.bedOccupied = true;
    }

    public abstract String describe();

    public String getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isBedOccupied() {
        return bedOccupied;
    }

    public void setBedOccupied(boolean bedOccupied) {
        this.bedOccupied = bedOccupied;
    }
}
