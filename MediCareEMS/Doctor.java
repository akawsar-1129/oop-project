
import java.util.ArrayList;
import java.util.List;

public class Doctor {

    private static final int CLINIC_CAPACITY = 50;
    private static final double WARNING_THRESHOLD = 0.9; // 90% full

    private String doctorId;
    private String name;
    private String specialty;
    private List<String> patients;

    public Doctor(String doctorId, String name, String specialty) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialty = specialty;
        this.patients = new ArrayList<>();
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public int getPatientCount() {
        return patients.size();
    }

    public int getCapacity() {
        return CLINIC_CAPACITY;
    }

    public boolean isAvailable() {
        return patients.size() < CLINIC_CAPACITY;
    }

    public boolean isNearCapacity() {
        return patients.size() >= CLINIC_CAPACITY * WARNING_THRESHOLD;
    }

    public void addPatient(String patientName) {
        patients.add(patientName);
    }

    public boolean removePatient(String patientName) {
        return patients.remove(patientName);
    }

    public String toString() {
        return name + " (" + specialty + ") - " + patients.size() + "/" + CLINIC_CAPACITY
                + " patients - " + (isAvailable() ? "Available" : "Full");
    }
}
