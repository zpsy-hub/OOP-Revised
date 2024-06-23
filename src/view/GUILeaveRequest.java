package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import model.Leave;
import model.LeaveType;
import model.User;
import DAO.LeaveDAO;
import customUI.ImagePanel;
import customUI.Sidebar;
import customUI.SidebarButton;
import service.LeaveRequestService;
import util.LeaveRequestComboPopulator;
import util.SessionManager;
import util.SignOutButton;

public class GUILeaveRequest {

    public JFrame leaverequestScreen;
    private JTable leavehistoryTable_1;
    private User loggedInEmployee;
    private JLabel leaveTotal;
    private JLabel vacationTotal;
    private JLabel sickTotal;
    private JLabel emergencyTotal;
    private JTextField textField_ComputedDays;
    private JComboBox<String> startmonthComboBox;
    private JComboBox<String> startdayComboBox;
    private JComboBox<String> startyearComboBox;
    private JComboBox<String> endmonthComboBox;
    private JComboBox<String> enddayComboBox;
    private JComboBox<String> endyearComboBox;
    private JComboBox<String> leaveTypeComboBox;
    private JLabel textField;
    private LeaveDAO leaveDAO;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    User loggedInEmployee = SessionManager.getLoggedInUser();
                    GUILeaveRequest window = new GUILeaveRequest(loggedInEmployee);
                    window.leaverequestScreen.setVisible(true);
                    window.leaverequestScreen.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     * @throws IOException
     */
    public GUILeaveRequest(User loggedInEmployee) throws IOException {
        this.loggedInEmployee = loggedInEmployee;
        initialize();
        populateComboBoxes();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        
        // Initialize DAO instance
        leaveDAO = LeaveDAO.getInstance();

        leaverequestScreen = new JFrame();
        leaverequestScreen.setTitle("MotorPH Payroll System");
        leaverequestScreen.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\shane\\GitHub\\IT110-OOP-MotorPH-Payroll\\Icons\\MotorPH Icon.png"));
        leaverequestScreen.setBackground(new Color(255, 255, 255));
        leaverequestScreen.setBounds(100, 100, 1280, 800);
        leaverequestScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        leaverequestScreen.getContentPane().setLayout(null);

        // Main panel with background image
        ImagePanel mainPanel = new ImagePanel("/img/leave request.png");
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setBounds(0, 0, 1280, 800);
        leaverequestScreen.getContentPane().add(mainPanel);
        mainPanel.setLayout(null);

        // Use the Sidebar class
        Sidebar sidebar = new Sidebar(loggedInEmployee);
        sidebar.setBounds(0, 92, 321, 680);
        mainPanel.add(sidebar);

        // Sign Out button initialization
        SignOutButton signOutButton = new SignOutButton(SignOutButton.getSignOutActionListener(leaverequestScreen));
        signOutButton.setBounds(1125, 24, 111, 40);
        mainPanel.add(signOutButton);

        JPanel leavesPanel = new JPanel();
        leavesPanel.setBounds(367, 116, 243, 135);
        leavesPanel.setOpaque(false);
        mainPanel.add(leavesPanel);
        leavesPanel.setLayout(null);

        JLabel totalleavesLabel = new JLabel("Total Available Leaves:");
        totalleavesLabel.setForeground(new Color(255, 255, 255));
        totalleavesLabel.setFont(new Font("Poppins Medium", Font.PLAIN, 14));
        totalleavesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalleavesLabel.setBounds(0, 21, 243, 13);
        leavesPanel.add(totalleavesLabel);

        leaveTotal = new JLabel();
        leaveTotal.setForeground(new Color(255, 255, 255));
        leaveTotal.setHorizontalAlignment(SwingConstants.CENTER);
        leaveTotal.setFont(new Font("Poppins SemiBold", Font.PLAIN, 36));
        leaveTotal.setBounds(0, 56, 243, 43);
        leavesPanel.add(leaveTotal);

        JPanel vacationPanel = new JPanel();
        vacationPanel.setBounds(639, 116, 175, 135);
        vacationPanel.setBorder(null);
        vacationPanel.setOpaque(false);
        mainPanel.add(vacationPanel);
        vacationPanel.setLayout(null);

        JLabel vacationLabel = new JLabel("Vacation Leaves Left:");
        vacationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        vacationLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        vacationLabel.setBounds(0, 21, 172, 13);
        vacationPanel.add(vacationLabel);

        vacationTotal = new JLabel();
        vacationTotal.setHorizontalAlignment(SwingConstants.CENTER);
        vacationTotal.setForeground(new Color(0, 0, 0));
        vacationTotal.setFont(new Font("Poppins SemiBold", Font.PLAIN, 36));
        vacationTotal.setBounds(0, 56, 172, 43);
        vacationPanel.add(vacationTotal);

        JPanel sickPanel = new JPanel();
        sickPanel.setBounds(846, 116, 175, 135);
        sickPanel.setBorder(null);
        sickPanel.setOpaque(false);
        mainPanel.add(sickPanel);
        sickPanel.setLayout(null);

        JLabel sickleavesLabel = new JLabel("Sick Leaves Left:");
        sickleavesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sickleavesLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        sickleavesLabel.setBounds(0, 21, 175, 13);
        sickPanel.add(sickleavesLabel);

        sickTotal = new JLabel();
        sickTotal.setHorizontalAlignment(SwingConstants.CENTER);
        sickTotal.setForeground(Color.BLACK);
        sickTotal.setFont(new Font("Poppins SemiBold", Font.BOLD, 36));
        sickTotal.setBounds(0, 56, 175, 43);
        sickPanel.add(sickTotal);

        JPanel emergencyPanel = new JPanel();
        emergencyPanel.setBounds(1054, 116, 175, 135);
        emergencyPanel.setBorder(null);
        emergencyPanel.setOpaque(false);
        mainPanel.add(emergencyPanel);
        emergencyPanel.setLayout(null);

        JLabel emergencyleavesLabel = new JLabel("Emergency Leaves Left:");
        emergencyleavesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emergencyleavesLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        emergencyleavesLabel.setBounds(0, 21, 175, 13);
        emergencyPanel.add(emergencyleavesLabel);

        emergencyTotal = new JLabel();
        emergencyTotal.setHorizontalAlignment(SwingConstants.CENTER);
        emergencyTotal.setForeground(Color.BLACK);
        emergencyTotal.setFont(new Font("Poppins SemiBold", Font.BOLD, 36));
        emergencyTotal.setBounds(0, 56, 175, 43);
        emergencyPanel.add(emergencyTotal);

        JLabel leaveapplicationLabel = new JLabel("Leave Application");
        leaveapplicationLabel.setBounds(380, 290, 280, 33);
        leaveapplicationLabel.setFont(new Font("Poppins", Font.PLAIN, 20));
        mainPanel.add(leaveapplicationLabel);

        JLabel lblSelectLeaveType = new JLabel("Select Leave Type:");
        lblSelectLeaveType.setBounds(380, 343, 160, 21);
        lblSelectLeaveType.setFont(new Font("Poppins", Font.PLAIN, 16));
        mainPanel.add(lblSelectLeaveType);

        JLabel lblStartDate = new JLabel("Start Date:");
        lblStartDate.setBounds(380, 404, 100, 21);
        lblStartDate.setFont(new Font("Poppins", Font.PLAIN, 16));
        mainPanel.add(lblStartDate);

        JLabel lblMonth = new JLabel("Month:");
        lblMonth.setBounds(478, 405, 53, 21);
        lblMonth.setFont(new Font("Poppins", Font.PLAIN, 14));
        mainPanel.add(lblMonth);

        JLabel lblDay = new JLabel("Day:");
        lblDay.setBounds(478, 447, 53, 21);
        lblDay.setFont(new Font("Poppins", Font.PLAIN, 14));
        mainPanel.add(lblDay);        

        JLabel lblYear = new JLabel("Year:");
        lblYear.setBounds(617, 447, 44, 21);
        lblYear.setFont(new Font("Poppins", Font.PLAIN, 14));
        mainPanel.add(lblYear);

        JLabel lblEndDate = new JLabel("End Date:");
        lblEndDate.setBounds(380, 511, 100, 21);
        lblEndDate.setFont(new Font("Poppins", Font.PLAIN, 16));
        mainPanel.add(lblEndDate);

        JLabel lblMonth_1 = new JLabel("Month:");
        lblMonth_1.setBounds(478, 512, 53, 21);
        lblMonth_1.setFont(new Font("Poppins", Font.PLAIN, 14));
        mainPanel.add(lblMonth_1);        

        JLabel lblYear_1 = new JLabel("Year:");
        lblYear_1.setBounds(617, 553, 44, 21);
        lblYear_1.setFont(new Font("Poppins", Font.PLAIN, 14));
        mainPanel.add(lblYear_1);    

        JLabel lblDay_1 = new JLabel("Day:");
        lblDay_1.setHorizontalAlignment(SwingConstants.LEFT);
        lblDay_1.setBounds(478, 553, 53, 21);
        lblDay_1.setFont(new Font("Poppins", Font.PLAIN, 14));
        mainPanel.add(lblDay_1);

        // Combo boxes
        leaveTypeComboBox = new JComboBox<>();
        leaveTypeComboBox.setBounds(541, 337, 200, 32);
        leaveTypeComboBox.setBackground(new Color(255, 255, 255));
        leaveTypeComboBox.setFont(new Font("Poppins", Font.PLAIN, 16));
        mainPanel.add(leaveTypeComboBox);

        startmonthComboBox = new JComboBox<String>();
        startmonthComboBox.setBounds(541, 399, 200, 32);
        startmonthComboBox.setBackground(new Color(255, 255, 255));
        startmonthComboBox.setMaximumRowCount(13);
        startmonthComboBox.setFont(new Font("Poppins", Font.PLAIN, 16));
        mainPanel.add(startmonthComboBox);

        startdayComboBox = new JComboBox<String>();
        startdayComboBox.setBounds(541, 441, 69, 32);
        startdayComboBox.setBackground(new Color(255, 255, 255));
        startdayComboBox.setMaximumRowCount(32);
        startdayComboBox.setFont(new Font("Poppins", Font.PLAIN, 16));
        mainPanel.add(startdayComboBox);

        startyearComboBox = new JComboBox<String>();
        startyearComboBox.setBounds(659, 441, 82, 32);
        startyearComboBox.setFont(new Font("Poppins", Font.PLAIN, 16));
        startyearComboBox.setBackground(Color.WHITE);
        mainPanel.add(startyearComboBox);        

        endmonthComboBox = new JComboBox<String>();
        endmonthComboBox.setBounds(541, 505, 200, 32);
        endmonthComboBox.setMaximumRowCount(13);
        endmonthComboBox.setFont(new Font("Poppins", Font.PLAIN, 16));
        endmonthComboBox.setBackground(Color.WHITE);
        mainPanel.add(endmonthComboBox);

        enddayComboBox = new JComboBox<String>();
        enddayComboBox.setBounds(541, 547, 69, 32);
        enddayComboBox.setMaximumRowCount(32);
        enddayComboBox.setFont(new Font("Poppins", Font.PLAIN, 16));
        enddayComboBox.setBackground(Color.WHITE);
        mainPanel.add(enddayComboBox);

        endyearComboBox = new JComboBox<String>();
        endyearComboBox.setBounds(659, 547, 82, 32);
        endyearComboBox.setFont(new Font("Poppins", Font.PLAIN, 16));
        endyearComboBox.setBackground(Color.WHITE);
        mainPanel.add(endyearComboBox);

        JPanel leavehistoryPanel = new JPanel();
        leavehistoryPanel.setBounds(785, 337, 430, 400);
        leavehistoryPanel.setBackground(new Color(255, 255, 255));
        mainPanel.add(leavehistoryPanel);
        leavehistoryPanel.setLayout(new GridLayout(1, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        leavehistoryPanel.add(scrollPane);

        leavehistoryTable_1 = new JTable();
        scrollPane.setViewportView(leavehistoryTable_1);
        leavehistoryTable_1.setRowHeight(28);
        leavehistoryTable_1.setBounds(736, 331, 523, 381);
        leavehistoryTable_1.setRowSelectionAllowed(false);
        leavehistoryTable_1.setEnabled(false);
        leavehistoryTable_1.setFont(new Font("Poppins", Font.PLAIN, 12));

        // Set the table model using populateLeaveHistoryTable method
        populateLeaveHistoryTable(leavehistoryTable_1);

        JButton sendleaveButton = new JButton("Send Leave Request");
        sendleaveButton.setBounds(541, 677, 200, 60);
        sendleaveButton.setFont(new Font("Poppins Medium", Font.PLAIN, 16));
        sendleaveButton.setBackground(Color.WHITE);
        mainPanel.add(sendleaveButton);

        sendleaveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Parse dates from the combo boxes
                    String start = startyearComboBox.getSelectedItem().toString() + "-" +
                                   String.format("%02d", startmonthComboBox.getSelectedIndex() + 1) + "-" +
                                   String.format("%02d", Integer.parseInt((String) startdayComboBox.getSelectedItem()));
                    String end = endyearComboBox.getSelectedItem().toString() + "-" +
                                 String.format("%02d", endmonthComboBox.getSelectedIndex() + 1) + "-" +
                                 String.format("%02d", Integer.parseInt((String) enddayComboBox.getSelectedItem()));

                    Date startDate = Date.valueOf(start);
                    Date endDate = Date.valueOf(end);

                    // Ensure the start date is before the end date
                    if (!startDate.before(endDate)) {
                        JOptionPane.showMessageDialog(leaverequestScreen, "Start date must be before end date.", "Date Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Calculate total days
                    long daysBetween = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()) + 1;
                    int totalDays = (int) daysBetween;

                    int leaveTypeId = getLeaveTypeIdFromName((String) leaveTypeComboBox.getSelectedItem());
                    int remainingDays = calculateRemainingDays(loggedInEmployee.getId(), leaveTypeId, totalDays);

                    if (remainingDays < 0) {
                        JOptionPane.showMessageDialog(leaverequestScreen, "Insufficient leave balance.", "Leave Balance Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Create and add the leave record
                    Leave leave = new Leave(
                        loggedInEmployee.getId(),
                        leaveTypeId,
                        LocalDate.now().getYear(),
                        new Date(System.currentTimeMillis()),
                        startDate,
                        endDate,
                        totalDays,
                        "Pending",
                        null, // Date approved is null initially
                        remainingDays
                    );

                    leaveDAO.addLeave(leave);

                    // Refresh GUI components
                    populateLeaveHistoryTable(leavehistoryTable_1);
                    updateLeaveData();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(leaverequestScreen, "Please ensure all date fields are selected correctly.", "Date Parsing Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(leaverequestScreen, "Error processing the leave request: " + ex.getMessage(), "Application Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        JLabel leavehistoryLabel = new JLabel("Leave Request History");
        leavehistoryLabel.setBounds(785, 290, 335, 33);
        leavehistoryLabel.setFont(new Font("Poppins", Font.PLAIN, 20));
        mainPanel.add(leavehistoryLabel);

        JLabel employeeNameLabel = new JLabel("");
        employeeNameLabel.setBounds(746, 36, 400, 33);
        employeeNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        employeeNameLabel.setFont(new Font("Tw Cen MT", Font.PLAIN, 32));
        mainPanel.add(employeeNameLabel);

        JLabel lblTotalDays = new JLabel("Total Days: ");
        lblTotalDays.setBounds(380, 626, 120, 21);
        lblTotalDays.setHorizontalAlignment(SwingConstants.LEFT);
        lblTotalDays.setFont(new Font("Poppins", Font.PLAIN, 16));
        mainPanel.add(lblTotalDays);

        textField_ComputedDays = new JTextField();
        textField_ComputedDays.setBounds(541, 621, 200, 32);
        textField_ComputedDays.setEditable(false);
        textField_ComputedDays.setHorizontalAlignment(SwingConstants.CENTER);
        textField_ComputedDays.setFont(new Font("Poppins", Font.BOLD, 16));
        mainPanel.add(textField_ComputedDays);
        textField_ComputedDays.setColumns(10);

        textField = new JLabel();
        textField.setBounds(300, 594, 200, 32);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("Tw Cen MT", Font.BOLD, 20));
        mainPanel.add(textField);

        JButton ClearFormButton = new JButton("Clear Form");
        ClearFormButton.setBounds(375, 677, 156, 60);
        ClearFormButton.setFont(new Font("Poppins Medium", Font.PLAIN, 16));
        ClearFormButton.setBackground(Color.WHITE);
        mainPanel.add(ClearFormButton);
        updateLeaveData();    
        // Add ActionListener to ClearFormButton
        ClearFormButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Reset combo boxes to their default values
                leaveTypeComboBox.setSelectedIndex(0);
                startyearComboBox.setSelectedIndex(0);
                startmonthComboBox.setSelectedIndex(0);
                startdayComboBox.setSelectedIndex(0);
                endyearComboBox.setSelectedIndex(0);
                endmonthComboBox.setSelectedIndex(0);
                enddayComboBox.setSelectedIndex(0);
                // Clear computed days text field
                textField_ComputedDays.setText("");
            }
        });

        try {
            populateComboBoxes(); 
            calculateTotalDays();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add action listeners to combo boxes to trigger calculation of total days upon user input
        startmonthComboBox.addActionListener(e -> {
            if (!isAnyComboBoxAtDefaultSelection()) {
                calculateTotalDays();
            }
        });

        startdayComboBox.addActionListener(e -> {
            if (!isAnyComboBoxAtDefaultSelection()) {
                calculateTotalDays();
            }
        });

        startyearComboBox.addActionListener(e -> {
            if (!isAnyComboBoxAtDefaultSelection()) {
                calculateTotalDays();
            }
        });

        endmonthComboBox.addActionListener(e -> {
            if (!isAnyComboBoxAtDefaultSelection()) {
                calculateTotalDays();
            }
        });

        enddayComboBox.addActionListener(e -> {
            if (!isAnyComboBoxAtDefaultSelection()) {
                calculateTotalDays();
            }
        });

        endyearComboBox.addActionListener(e -> {
            if (!isAnyComboBoxAtDefaultSelection()) {
                calculateTotalDays();
            }
        });

        leaveTypeComboBox.addActionListener(e -> {
            if (!isAnyComboBoxAtDefaultSelection()) {
                calculateTotalDays();
            }
        });

        // Set employee name dynamically
        if (loggedInEmployee != null) {
            employeeNameLabel.setText(loggedInEmployee.getFirstName() + " " + loggedInEmployee.getLastName());
        }
    }

    // Method to fetch and update leave data for the logged-in employee
    private void updateLeaveData() {
        try {
            int vacationLeaves = leaveDAO.getLeaveBalance(loggedInEmployee.getId(), 1);
            int sickLeaves = leaveDAO.getLeaveBalance(loggedInEmployee.getId(), 3);
            int emergencyLeaves = leaveDAO.getLeaveBalance(loggedInEmployee.getId(), 2);

            int totalLeaves = vacationLeaves + sickLeaves + emergencyLeaves;
            leaveTotal.setText(String.valueOf(totalLeaves));
            vacationTotal.setText(String.valueOf(vacationLeaves));
            sickTotal.setText(String.valueOf(sickLeaves));
            emergencyTotal.setText(String.valueOf(emergencyLeaves));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void openWindow() {
        leaverequestScreen.setVisible(true);
    }

    // Method to populate the combo boxes
    private void populateComboBoxes() throws IOException {
        LeaveRequestComboPopulator.populateMonths(startmonthComboBox);
        LeaveRequestComboPopulator.populateDays(startdayComboBox, 1, 12);
        LeaveRequestComboPopulator.populateCurrentYear(startyearComboBox);
        LeaveRequestComboPopulator.populateMonths(endmonthComboBox);
        LeaveRequestComboPopulator.populateDays(enddayComboBox, 1, 12);
        LeaveRequestComboPopulator.populateCurrentYear(endyearComboBox);
        LeaveRequestComboPopulator.populateLeaveTypes(leaveTypeComboBox);
    }

    private boolean isAnyComboBoxAtDefaultSelection() {
        return startyearComboBox.getSelectedIndex() == 0 ||
               startmonthComboBox.getSelectedIndex() == 0 ||
               startdayComboBox.getSelectedIndex() == 0 ||
               endyearComboBox.getSelectedIndex() == 0 ||
               endmonthComboBox.getSelectedIndex() == 0 ||
               enddayComboBox.getSelectedIndex() == 0 ||
               leaveTypeComboBox.getSelectedIndex() == 0;
    }

    private void calculateTotalDays() {
        if (isAnyComboBoxAtDefaultSelection()) {
            textField_ComputedDays.setText("Please select all date fields.");
            return;
        }

        LocalDate startDate = LocalDate.of(Integer.parseInt(startyearComboBox.getSelectedItem().toString()),
                                           startmonthComboBox.getSelectedIndex() + 1,
                                           Integer.parseInt(startdayComboBox.getSelectedItem().toString()));
        LocalDate endDate = LocalDate.of(Integer.parseInt(endyearComboBox.getSelectedItem().toString()),
                                         endmonthComboBox.getSelectedIndex() + 1,
                                         Integer.parseInt(enddayComboBox.getSelectedItem().toString()));

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1; // Including start day
        if (daysBetween <= 0) {
            textField_ComputedDays.setText("End date must be after start date.");
            return;
        }

        textField_ComputedDays.setText(String.valueOf(daysBetween));
    }


    private int getLeaveTypeIdFromName(String leaveTypeName) {
        switch (leaveTypeName) {
            case "Sick Leave":
                return 3;
            case "Vacation Leave":
                return 1;
            case "Emergency Leave":
                return 2;
            default:
                return 0;
        }
    }

    private void populateLeaveHistoryTable(JTable table) {
        try {
            // Retrieve leave request logs for the logged-in employee
            List<Leave> leaveLogs = leaveDAO.getLeaveLogsByEmployeeId(loggedInEmployee.getId());

            // Create a DefaultTableModel
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Timestamp");
            model.addColumn("ID");
            model.addColumn("Leave Type ID");
            model.addColumn("Year");
            model.addColumn("Date Submitted");
            model.addColumn("Start Date");
            model.addColumn("End Date");
            model.addColumn("Days Taken");
            model.addColumn("Date Approved");
            model.addColumn("Leave Days Remaining");

            // Populate the model with leave request logs
            for (Leave leaveLog : leaveLogs) {
                model.addRow(new Object[]{
                    leaveLog.getDateSubmitted(),
                    leaveLog.getEmpId(),
                    leaveLog.getLeaveTypeId(),
                    leaveLog.getYear(),
                    leaveLog.getDateSubmitted(),
                    leaveLog.getStartDate(),
                    leaveLog.getEndDate(),
                    leaveLog.getDaysTaken(),
                    leaveLog.getDateApproved(),
                    leaveLog.getLeaveDaysRemaining()
                });
            }

            // Set the model for the table
            leavehistoryTable_1.setModel(model);

            // Enable sorting
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sorter);
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int calculateRemainingDays(int empId, int leaveTypeId, int daysTaken) {
        // Fetch the current balance from the database
        int currentBalance = leaveDAO.getLeaveBalance(empId, leaveTypeId);

        // Subtract the days taken from the current balance
        return currentBalance - daysTaken;
    }

}
