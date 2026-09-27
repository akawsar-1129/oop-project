import java.util.ArrayList;
import java.util.List;

public class AmbulanceRoster {

    public static final int FLEET_SIZE = 9;

    public static List<Ambulance> createDefaultAmbulances() {
        List<Ambulance> ambulances = new ArrayList<>();
        for (int i = 1; i <= FLEET_SIZE; i++) {
            String id;
            if (i < 10) {
                id = "A0" + i;
            } else {
                id = "A" + i;
            }
            ambulances.add(new Ambulance(id, "Standard"));
        }
        return ambulances;
    }
}
