import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MedSmartGUI extends JFrame {

    private final EmergencyManager manager;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public MedSmartGUI() {
        manager = new EmergencyManager();

        setTitle("MedSmart: Emergency Management System");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columns = { "ID", "Type", "Patient", "Location", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton(buttonPanel, "1. Register Emergency", e -> registerService());
        addButton(buttonPanel, "2. View Ambulances", e -> viewAmbulances());
        addButton(buttonPanel, "3. Search Service by ID", e -> searchService());
        addButton(buttonPanel, "4. View All Services", e -> refreshTable());
        addButton(buttonPanel, "5. Dispatch Ambulance", e -> dispatchAmbulance());
        addButton(buttonPanel, "6. Complete Service", e -> completeService());
        addButton(buttonPanel, "7. Cancel Pending Service", e -> cancelPendingService());
        addButton(buttonPanel, "8. Un-dispatch Service", e -> unassignAmbulance());
        addButton(buttonPanel, "9. Book Doctor Appointment", e -> bookAppointment());
        addButton(buttonPanel, "10. Cancel Doctor Appointment", e -> cancelAppointment());
        addButton(buttonPanel, "11. View Doctors & Availability", e -> viewDoctors());
        addButton(buttonPanel, "12. General Admission", e -> generalAdmission());
        addButton(buttonPanel, "13. Discharge (General Bed)", e -> generalDischarge());
        addButton(buttonPanel, "14. Hospital Bed Status", e -> viewHospitalStatus());

        JScrollPane buttonScroll = new JScrollPane(buttonPanel);
        buttonScroll.setPreferredSize(new Dimension(240, 0));
        buttonScroll.setBorder(BorderFactory.createEmptyBorder());
        add(buttonScroll, BorderLayout.WEST);

        refreshTable();
    }

    /**
     * Central place that catches every *unchecked* exception thrown by an action.
     */
    private void addButton(JPanel panel, String label, java.awt.event.ActionListener action) {
        JButton btn = new JButton(label);
        btn.addActionListener(e -> {
            try {
                action.actionPerformed(e);
            } catch (NumberFormatException ex) {
                showError("Please enter a valid whole number.");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                showError(ex.getMessage());
            }
        });
        panel.add(btn);
    }

    // ---- Action 1: Register Emergency (reserves an emergency bed) ----
    private void registerService() {
        String id = JOptionPane.showInputDialog(this, "Service ID:");
        if (id == null || id.isBlank())
            return;
        String patient = JOptionPane.showInputDialog(this, "Patient name:");
        String location = JOptionPane.showInputDialog(this, "Location:");

        String[] types = { "Ambulance Service", "Advanced Ambulance" };
        String type = (String) JOptionPane.showInputDialog(this, "Service type:", "Choose type",
                JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
        if (type == null)
            return;

        EmergencyService service;
        if ("Advanced Ambulance".equals(type)) {
            String paramedic = JOptionPane.showInputDialog(this, "Paramedic name:");
            service = new AdvancedAmbulance(id, patient, location, true, paramedic);
        } else {
            service = new AmbulanceService(id, patient, location);
        }

        try {
            manager.registerService(service);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Service registered (emergency bed reserved):\n"
                    + service.describe());
        } catch (CapacityExceededException ex) {
            showError(ex.getMessage());
        }
    }

    // ---- Action 2: View Ambulances (fixed fleet of 9) ----
    private void viewAmbulances() {
        StringBuilder sb = new StringBuilder();
        for (Ambulance a : manager.getAmbulances()) {
            sb.append(a).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Ambulance Fleet", JOptionPane.INFORMATION_MESSAGE);
    }

    // ---- Action 3 ----
    private void searchService() {
        String id = JOptionPane.showInputDialog(this, "Enter Service ID to search:");
        if (id == null)
            return;
        try {
            EmergencyService service = manager.searchService(id);
            JOptionPane.showMessageDialog(this, service.describe());
        } catch (ServiceNotFoundException ex) {
            showError(ex.getMessage());
        }
    }

    // ---- Action 5: Dispatch (auto-assigns the next free ambulance) ----
    private void dispatchAmbulance() {
        String id = JOptionPane.showInputDialog(this, "Service ID to dispatch:");
        if (id == null)
            return;
        try {
            manager.dispatchAmbulance(id);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Ambulance auto-assigned and dispatched for service " + id);
        } catch (ServiceNotFoundException | CapacityExceededException ex) {
            showError(ex.getMessage());
        }
    }

    // ---- Action 6: Complete Service (frees bed + ambulance, removes row) ----
    private void completeService() {
        String id = JOptionPane.showInputDialog(this, "Service ID to complete:");
        if (id == null)
            return;
        try {
            manager.completeService(id);
            refreshTable();
            JOptionPane.showMessageDialog(this,
                    "Service " + id + " completed. Bed and ambulance freed for the next patient.");
        } catch (ServiceNotFoundException ex) {
            showError(ex.getMessage());
        }
    }

    // ---- Action 7: Cancel a service that hasn't been dispatched yet ----
    private void cancelPendingService() {
        String id = JOptionPane.showInputDialog(this, "Service ID to cancel:");
        if (id == null)
            return;
        try {
            manager.cancelPendingService(id);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Service " + id + " cancelled. Emergency bed freed.");
        } catch (ServiceNotFoundException ex) {
            showError(ex.getMessage());
        }
    }

    // ---- Action 8: Un-dispatch (free the ambulance, keep the service open) ----
    private void unassignAmbulance() {
        String id = JOptionPane.showInputDialog(this, "Service ID to un-dispatch:");
        if (id == null)
            return;
        try {
            manager.unassignAmbulance(id);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Ambulance freed. Service " + id + " is Pending again.");
        } catch (ServiceNotFoundException ex) {
            showError(ex.getMessage());
        }
    }

    // ---- Action 9: Book Doctor Appointment ----
    private void bookAppointment() {
        String doctorId = JOptionPane.showInputDialog(this, doctorListText() + "\nEnter Doctor ID:");
        if (doctorId == null || doctorId.isBlank())
            return;
        String patientName = JOptionPane.showInputDialog(this, "Patient name:");
        if (patientName == null || patientName.isBlank())
            return;

        try {
            manager.appointDoctor(doctorId, patientName);
            JOptionPane.showMessageDialog(this, "Appointment booked with " + doctorId + " for " + patientName);
        } catch (ServiceNotFoundException | CapacityExceededException ex) {
            showError(ex.getMessage());
        }
    }

    // ---- Action 10: Cancel Doctor Appointment ----
    private void cancelAppointment() {
        String doctorId = JOptionPane.showInputDialog(this, doctorListText() + "\nEnter Doctor ID:");
        if (doctorId == null || doctorId.isBlank())
            return;
        String patientName = JOptionPane.showInputDialog(this, "Patient name to cancel:");
        if (patientName == null || patientName.isBlank())
            return;

        try {
            manager.cancelAppointment(doctorId, patientName);
            JOptionPane.showMessageDialog(this, "Appointment for " + patientName + " with " + doctorId + " cancelled.");
        } catch (ServiceNotFoundException ex) {
            showError(ex.getMessage());
        }
    }

    private String doctorListText() {
        StringBuilder list = new StringBuilder("Doctor IDs:\n");
        for (Doctor d : manager.getDoctors()) {
            list.append(d.getDoctorId()).append(" - ").append(d.getName())
                    .append(" (").append(d.getSpecialty()).append(")\n");
        }
        return list.toString();
    }

    // ---- Action 11 ----
    private void viewDoctors() {
        StringBuilder sb = new StringBuilder();
        for (Doctor d : manager.getDoctors()) {
            sb.append(d.getDoctorId()).append(": ").append(d).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "All Doctors", JOptionPane.INFORMATION_MESSAGE);
    }

    // ---- Action 12: General Admission (simple counter, non-emergency beds) ----
    private void generalAdmission() {
        boolean admitted = manager.getHospital().admitGeneral();
        if (admitted) {
            JOptionPane.showMessageDialog(this, "Patient admitted to a general bed.\n"
                    + manager.getHospital().getStatusSummary());
        } else {
            showError("No general beds available (200/200 occupied).");
        }
    }

    // ---- Action 13: Discharge from a general bed ----
    private void generalDischarge() {
        boolean discharged = manager.getHospital().dischargeGeneral();
        if (discharged) {
            JOptionPane.showMessageDialog(this, "Patient discharged from a general bed.\n"
                    + manager.getHospital().getStatusSummary());
        } else {
            showError("No general beds are currently occupied.");
        }
    }

    // ---- Action 14 ----
    private void viewHospitalStatus() {
        JOptionPane.showMessageDialog(this, manager.getHospital().getStatusSummary(),
                "Hospital Bed Status", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (EmergencyService s : manager.getAllServices()) {
            String type = s.getClass().getSimpleName();
            tableModel.addRow(new Object[] { s.getId(), type, s.getPatientName(), s.getLocation(), s.getStatus() });
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MedSmartGUI().setVisible(true));
    }
}
