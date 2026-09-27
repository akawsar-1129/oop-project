
public class AmbulanceService extends EmergencyService implements EmergencyRespondable {

    private String assignedAmbulanceId;
    private boolean available;

    public AmbulanceService(String id, String patientName) {
        super(id, patientName);
        this.available = true;
    }

    public AmbulanceService(String id, String patientName, String location) {
        super(id, patientName, location);
        this.available = true;
    }

    public String describe() {
        return "[Ambulance Service] ID: " + getId() + ", Patient: " + getPatientName()
                + ", Location: " + getLocation() + ", Status: " + getStatus()
                + (assignedAmbulanceId != null ? ", Ambulance: " + assignedAmbulanceId : "");
    }

    // Overloaded dispatch methods

    public void dispatch() {
        dispatch("Unassigned");
    }

    public void dispatch(String ambulanceId) {
        this.assignedAmbulanceId = ambulanceId;
        this.available = false;
        setStatus("Dispatched");
    }

    /** Un-dispatch */
    public void clearAssignment() {
        this.assignedAmbulanceId = null;
        this.available = true;
        setStatus("Pending");
    }

    public void complete() {
        this.assignedAmbulanceId = null;
        this.available = true;
        setStatus("Completed");
    }

    public boolean isAvailable() {
        return available;
    }

    public String getAssignedAmbulanceId() {
        return assignedAmbulanceId;
    }
}
