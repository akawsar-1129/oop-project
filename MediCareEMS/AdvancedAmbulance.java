
public class AdvancedAmbulance extends AmbulanceService {

    private boolean hasICUEquipment;
    private String paramedicName;

    public AdvancedAmbulance(String id, String patientName, boolean hasICUEquipment, String paramedicName) {
        super(id, patientName);
        this.hasICUEquipment = hasICUEquipment;
        this.paramedicName = paramedicName;
    }

    public AdvancedAmbulance(String id, String patientName, String location,
            boolean hasICUEquipment, String paramedicName) {
        super(id, patientName, location);
        this.hasICUEquipment = hasICUEquipment;
        this.paramedicName = paramedicName;
    }

    @Override
    public String describe() {
        return "[Advanced Ambulance] ID: " + getId() + ", Patient: " + getPatientName()
                + ", Location: " + getLocation() + ", Status: " + getStatus()
                + ", ICU Equipped: " + hasICUEquipment + ", Paramedic: " + paramedicName
                + (getAssignedAmbulanceId() != null ? ", Ambulance: " + getAssignedAmbulanceId() : "");
    }

    public boolean hasICUEquipment() {
        return hasICUEquipment;
    }

    public String getParamedicName() {
        return paramedicName;
    }
}
