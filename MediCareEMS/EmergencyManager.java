import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmergencyManager {

    private List<EmergencyService> services;
    private Map<String, EmergencyService> serviceMap;
    private List<Ambulance> ambulances;
    private List<Doctor> doctors;
    private Hospital hospital;

    public EmergencyManager() {
        services = new ArrayList<>();
        serviceMap = new HashMap<>();
        ambulances = AmbulanceRoster.createDefaultAmbulances(); // fixed fleet of 9
        doctors = DoctorRoster.createDefaultDoctors(); // fixed roster of 20
        hospital = new Hospital();
    }

    // Registration

    public boolean isIdActive(String id) {
        return serviceMap.containsKey(id);
    }

    public void registerService(EmergencyService service) throws CapacityExceededException {
        if (serviceMap.containsKey(service.getId())) {
            throw new IllegalArgumentException(
                    "Service ID \"" + service.getId() + "\" is already registered.");
        }
        hospital.admitEmergency(); // if emergency beds are >= 90% full
        services.add(service);
        serviceMap.put(service.getId(), service);
    }

    public EmergencyService searchService(String id) throws ServiceNotFoundException {
        EmergencyService service = serviceMap.get(id);
        if (service == null) {
            throw new ServiceNotFoundException("No service found with ID: " + id);
        }
        return service;
    }

    public List<EmergencyService> getAllServices() {
        return services;
    }

    // Dispatch (auto-assigns the next free ambulance)

    public void dispatchAmbulance(String serviceId) throws ServiceNotFoundException, CapacityExceededException {
        EmergencyService service = searchService(serviceId);
        if (!(service instanceof AmbulanceService)) {
            throw new IllegalArgumentException("Service " + serviceId + " is not an ambulance-type service.");
        }
        if (!"Pending".equals(service.getStatus())) {
            throw new IllegalStateException(
                    "Service " + serviceId + " is not pending (current status: " + service.getStatus() + ").");
        }
        Ambulance chosen = null;
        for (Ambulance a : ambulances) {
            if (a.isAvailable()) {
                chosen = a;
                break;
            }
        }
        if (chosen == null) {
            throw new CapacityExceededException(
                    "All " + AmbulanceRoster.FLEET_SIZE + " ambulances are currently in use.");
        }
        chosen.setAvailable(false);
        ((AmbulanceService) service).dispatch(chosen.getAmbulanceId());
    }

    /** Un-dispatch */
    public void unassignAmbulance(String serviceId) throws ServiceNotFoundException {
        EmergencyService service = searchService(serviceId);
        if (!(service instanceof AmbulanceService)) {
            throw new IllegalArgumentException("Service " + serviceId + " has no ambulance assignment.");
        }
        if (!"Dispatched".equals(service.getStatus())) {
            throw new IllegalStateException("Service " + serviceId + " is not currently dispatched.");
        }
        freeAmbulanceIfHeld(service);
        ((AmbulanceService) service).clearAssignment();
    }

    // Completion

    // Complete a service -> frees bed and ambulance
    public void completeService(String id) throws ServiceNotFoundException {
        EmergencyService service = searchService(id);
        service.setStatus("Completed");
        freeBedIfHeld(service);
        freeAmbulanceIfHeld(service);
        services.remove(service);
        serviceMap.remove(id);
    }

    // Cancel a service - frees its bed
    public void cancelPendingService(String id) throws ServiceNotFoundException {
        EmergencyService service = searchService(id);
        if (!"Pending".equals(service.getStatus())) {
            throw new IllegalStateException(
                    "Only pending services can be cancelled. Service " + id + " is " + service.getStatus() + ".");
        }
        freeBedIfHeld(service);
        services.remove(service);
        serviceMap.remove(id);
    }

    private void freeBedIfHeld(EmergencyService service) {
        if (service.isBedOccupied()) {
            hospital.dischargeEmergency();
            service.setBedOccupied(false);
        }
    }

    private void freeAmbulanceIfHeld(EmergencyService service) {
        if (!(service instanceof AmbulanceService)) {
            return;
        }
        String assignedId = ((AmbulanceService) service).getAssignedAmbulanceId();
        if (assignedId == null) {
            return;
        }
        for (Ambulance a : ambulances) {
            if (a.getAmbulanceId().equals(assignedId)) {
                a.setAvailable(true);
                break;
            }
        }
    }

    // Doctor appointments
    public void appointDoctor(String doctorId, String patientName)
            throws ServiceNotFoundException, CapacityExceededException {
        Doctor doctor = findDoctor(doctorId);
        if (doctor.isNearCapacity()) {
            throw new CapacityExceededException(
                    doctor.getName() + "'s clinic is at 90% capacity ("
                            + doctor.getPatientCount() + "/" + doctor.getCapacity() + ").");
        }
        doctor.addPatient(patientName);
    }

    // Cancel a previously booked appointment
    public void cancelAppointment(String doctorId, String patientName) throws ServiceNotFoundException {
        Doctor doctor = findDoctor(doctorId);
        if (!doctor.removePatient(patientName)) {
            throw new ServiceNotFoundException(
                    "Patient \"" + patientName + "\" was not found in " + doctor.getName() + "'s clinic.");
        }
    }

    private Doctor findDoctor(String doctorId) throws ServiceNotFoundException {
        for (Doctor d : doctors) {
            if (d.getDoctorId().equalsIgnoreCase(doctorId)) {
                return d;
            }
        }
        throw new ServiceNotFoundException("No doctor found with ID: " + doctorId);
    }

    public List<Doctor> getAvailableDoctors() {
        List<Doctor> available = new ArrayList<>();
        for (Doctor d : doctors) {
            if (d.isAvailable()) {
                available.add(d);
            }
        }
        return available;
    }

    public List<Ambulance> getAmbulances() {
        return ambulances;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public Hospital getHospital() {
        return hospital;
    }
}
