# MediCare Emergency Management System

Java Swing OOP project. All classes are in the default package, so they
can compile and run straight from one folder.

## Files
- `EmergencyService.java` — abstract base class (**abstraction**, **encapsulation**, overloaded constructors)
- `AmbulanceService.java` — extends EmergencyService, implements EmergencyRespondable (**inheritance**, **interface**, overloaded dispatch methods)
- `AdvancedAmbulance.java` — extends AmbulanceService (3-level **inheritance** chain: EmergencyService → AmbulanceService → AdvancedAmbulance)
- `EmergencyRespondable.java` — **interface**
- `Ambulance.java` — a single physical ambulance resource
- `AmbulanceRoster.java` — builds the **fixed fleet of 9 ambulances** (A01–A09) at startup
- `Doctor.java` — a doctor with a 50-patient clinic capacity, availability check, and appointment cancellation
- `DoctorRoster.java` — builds the fixed list of 20 doctors at startup (**names are placeholders — edit this file**)
- `Hospital.java` — 200 general beds + 50 emergency beds, with the 90%-full check on the emergency pool
- `CapacityExceededException.java`, `ServiceNotFoundException.java` — custom **exceptions**
- `EmergencyManager.java` — business logic, `ArrayList` + `HashMap` (**collections**)
- `MediSmartGUI.java` — Swing **GUI**, entry point (`main` method)

## What changed in this version
- **Duplicate IDs are rejected.** Registering a service with an ID that's already active shows "already registered" instead of silently corrupting the list. Once that service is completed or cancelled, the ID is free to use again.
- **Ambulances are now a fixed fleet of 9** (A01–A09), like the 20 doctors. There's no more "Add Ambulance" button — instead there's "View Ambulances" to see the fleet and who's using what.
- **Dispatch is fully automatic.** Pick a pending service and hit Dispatch — the system finds the next free ambulance from the 9 and assigns it. If all 9 are busy, you get a clear `CapacityExceededException` message. No manual ambulance-ID typing anymore.
- **Ambulances are freed automatically.** Completing a service, or hitting "Un-dispatch," returns that ambulance to the available pool immediately — it isn't lost forever after one use.
- **Completing a service now clears its row entirely** (not just marks it "Completed") — it frees the bed and the ambulance and removes the entry, so the slot is instantly ready for the next patient.
- **Fire Rescue Service has been removed** to keep the codebase simpler and focused on the ambulance chain your requirements specify.
- **Cancel is now three separate, precise actions:**
  - *Cancel Pending Service* — removes a service that hasn't been dispatched yet, frees its bed.
  - *Un-dispatch Service* — frees the assigned ambulance and returns a dispatched service to Pending (the service itself isn't deleted).
  - *Cancel Doctor Appointment* — removes a specific patient from a specific doctor's list.
- **General Admission / Discharge buttons added**, wired to the simple bed counter already in `Hospital` (no per-patient tracking, matching the emergency-side design's level of detail there).

## How capacity works
- **Emergency beds (50 total):** registering a new emergency service reserves one bed. Completing or cancelling a service frees it. Once occupancy is **at or above 90% (45/50)**, the next registration is blocked with `CapacityExceededException`.
- **Ambulance fleet (9 total, fixed IDs):** dispatch auto-assigns the next free one. Completing or un-dispatching a service returns its ambulance to the pool. If all 9 are in use, dispatch throws `CapacityExceededException`.
- **Doctor clinics (50 patients each, 20 doctors):** booking an appointment checks that *specific* doctor's load. Once that doctor is at or above 90% (45/50), the next booking for them is blocked the same way — other doctors are unaffected. Cancelling an appointment removes that patient and frees a slot.
- **General beds (200):** a simple admit/discharge counter in `Hospital`, no per-patient tracking, wired to its own two GUI buttons.

## How to compile and run

### From VS Code
1. Open this folder in VS Code (`File → Open Folder`).
2. Open `MediCareGUI.java`.
3. Click the **Run ▶** button above `public static void main`.

### From the command line
```
cd MediCareEMS
javac *.java
java MediCareGUI
```

## Where each requirement lives
| # | Requirement | Where |
|---|---|---|
| 1 | Register a service (rejects duplicate IDs, reserves an emergency bed) | `EmergencyManager.registerService()` |
| 2 | View the ambulance fleet | `EmergencyManager.getAmbulances()` |
| 3 | Search by ID | `EmergencyManager.searchService()` |
| 4 | View all services | `EmergencyManager.getAllServices()` |
| 5 | Dispatch ambulance (auto-assigns from fixed fleet of 9) | `EmergencyManager.dispatchAmbulance()` |
| 6 | Complete service (frees bed + ambulance, removes row) | `EmergencyManager.completeService()` |
| 7 | Cancel pending service (frees bed) | `EmergencyManager.cancelPendingService()` |
| 8 | Un-dispatch (frees ambulance, keeps service open) | `EmergencyManager.unassignAmbulance()` |
| 9 | Book doctor appointment | `EmergencyManager.appointDoctor()` |
| 10 | Cancel doctor appointment | `EmergencyManager.cancelAppointment()` |
| 11 | Doctor availability | `Doctor.isAvailable()`, `EmergencyManager.getAvailableDoctors()` |
| 12 | General admission | `Hospital.admitGeneral()` |
| 13 | General discharge | `Hospital.dischargeGeneral()` |
| 14 | Hospital bed status | `Hospital.getStatusSummary()` |
| — | Error messages | `MediSmartGUI.showError()` + try/catch around every action |

## OOP parameter checklist
| Concept | Where |
|---|---|
| Abstraction | `EmergencyService` (abstract class) |
| Inheritance | `EmergencyService → AmbulanceService → AdvancedAmbulance` |
| Polymorphism | `describe()` differs per subclass |
| Interface | `EmergencyRespondable` |
| Encapsulation | Private fields + getters/setters throughout |
| Overloading | Multiple constructors (`EmergencyService`, `AmbulanceService`, `AdvancedAmbulance`) and dispatch methods (`AmbulanceService.dispatch()` / `dispatch(String)`) |
| Exception | `CapacityExceededException` (custom, checked) |
| Collections | `ArrayList` + `HashMap` in `EmergencyManager` |
| GUI | Java Swing (`MediSmartGUI`) |
| Exception handling | try/catch around every GUI action; checked exceptions per-action, unchecked exceptions centrally in `addButton` |
