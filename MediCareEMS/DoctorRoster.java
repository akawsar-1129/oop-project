import java.util.ArrayList;
import java.util.List;

public class DoctorRoster {

    public static List<Doctor> createDefaultDoctors() {
        List<Doctor> doctors = new ArrayList<>();

        // id, name, specialty
        doctors.add(new Doctor("D01", "Dr. Placeholder 1", "Cardiology"));
        doctors.add(new Doctor("D02", "Dr. Placeholder 2", "Neurology"));
        doctors.add(new Doctor("D03", "Dr. Placeholder 3", "Orthopedics"));
        doctors.add(new Doctor("D04", "Dr. Placeholder 4", "Pediatrics"));
        doctors.add(new Doctor("D05", "Dr. Placeholder 5", "General Surgery"));
        doctors.add(new Doctor("D06", "Dr. Placeholder 6", "Emergency Medicine"));
        doctors.add(new Doctor("D07", "Dr. Placeholder 7", "Radiology"));
        doctors.add(new Doctor("D08", "Dr. Placeholder 8", "Anesthesiology"));
        doctors.add(new Doctor("D09", "Dr. Placeholder 9", "Internal Medicine"));
        doctors.add(new Doctor("D10", "Dr. Placeholder 10", "Dermatology"));
        doctors.add(new Doctor("D11", "Dr. Placeholder 11", "ENT"));
        doctors.add(new Doctor("D12", "Dr. Placeholder 12", "Psychiatry"));
        doctors.add(new Doctor("D13", "Dr. Placeholder 13", "Oncology"));
        doctors.add(new Doctor("D14", "Dr. Placeholder 14", "Gastroenterology"));
        doctors.add(new Doctor("D15", "Dr. Placeholder 15", "Pulmonology"));
        doctors.add(new Doctor("D16", "Dr. Placeholder 16", "Nephrology"));
        doctors.add(new Doctor("D17", "Dr. Placeholder 17", "Urology"));
        doctors.add(new Doctor("D18", "Dr. Placeholder 18", "Ophthalmology"));
        doctors.add(new Doctor("D19", "Dr. Placeholder 19", "Gynecology"));
        doctors.add(new Doctor("D20", "Dr. Placeholder 20", "Endocrinology"));

        return doctors;
    }
}
