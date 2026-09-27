
public class Hospital {

    private static final int TOTAL_GENERAL_BEDS = 200;
    private static final int TOTAL_EMERGENCY_BEDS = 50;
    private static final double WARNING_THRESHOLD = 0.9; // 90% full

    private int occupiedGeneralBeds;
    private int occupiedEmergencyBeds;

    public Hospital() {
        occupiedGeneralBeds = 0;
        occupiedEmergencyBeds = 0;
    }

    public void admitEmergency() throws CapacityExceededException {
        if (occupiedEmergencyBeds >= TOTAL_EMERGENCY_BEDS * WARNING_THRESHOLD) {
            throw new CapacityExceededException(
                    "Emergency bed capacity exceeded: " + occupiedEmergencyBeds + "/"
                            + TOTAL_EMERGENCY_BEDS + " beds occupied (90% threshold reached).");
        }
        occupiedEmergencyBeds++;
    }

    public void dischargeEmergency() {
        if (occupiedEmergencyBeds > 0) {
            occupiedEmergencyBeds--;
        }
    }

    public boolean admitGeneral() {
        if (occupiedGeneralBeds < TOTAL_GENERAL_BEDS) {
            occupiedGeneralBeds++;
            return true;
        }
        return false;
    }

    public boolean dischargeGeneral() {
        if (occupiedGeneralBeds > 0) {
            occupiedGeneralBeds--;
            return true;
        }
        return false;
    }

    public int getOccupiedEmergencyBeds() {
        return occupiedEmergencyBeds;
    }

    public int getTotalEmergencyBeds() {
        return TOTAL_EMERGENCY_BEDS;
    }

    public int getOccupiedGeneralBeds() {
        return occupiedGeneralBeds;
    }

    public int getTotalGeneralBeds() {
        return TOTAL_GENERAL_BEDS;
    }

    public String getStatusSummary() {
        return "General beds: " + occupiedGeneralBeds + "/" + TOTAL_GENERAL_BEDS
                + "\nEmergency beds: " + occupiedEmergencyBeds + "/" + TOTAL_EMERGENCY_BEDS;
    }
}
