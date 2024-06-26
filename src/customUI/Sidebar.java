package customUI;

import model.User;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.PermissionChecker;

public class Sidebar extends JPanel {
    private List<SidebarButton> buttons;
    private User loggedInEmployee;

    public Sidebar(User loggedInEmployee) {
        this.loggedInEmployee = loggedInEmployee;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(255, 255, 255, 0));
        setOpaque(false);
        setBounds(75, 105, 321, 680);  // Set the bounds as specified
        buttons = new ArrayList<>();
        initializeButtons();
        layoutButtons();
        setButtonVisibility();
    }

    private void initializeButtons() {
        addSidebarButton("Dashboard", "/img/001-dashboard.png", e -> {
            GUIDashboard window = new GUIDashboard(loggedInEmployee);
            closeAndShowFrame(window.dashboardScreen);
        });

        addSidebarButton("Time In/Out", "/img/008-clock.png", e -> {
            GUITimeInOut timeInOut = new GUITimeInOut(loggedInEmployee);
            closeAndShowFrame(timeInOut.getFrame());
            timeInOut.openWindow();
        });

        addSidebarButton("Payslip", "/img/042-document.png", e -> {
            GUIPayslip payslip = new GUIPayslip(loggedInEmployee);
            closeAndShowFrame(payslip.getFrame());
        });

        addSidebarButton("Leave Request", "/img/022-leave.png", e -> {
            try {
                GUILeaveRequest window = new GUILeaveRequest(loggedInEmployee);
                closeAndShowFrame(window.leaverequestScreen);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        addSidebarButton("Overtime Request", "/img/014-overtime-1.png", e -> {
            GUIOvertimeRequest frame = new GUIOvertimeRequest(loggedInEmployee);
            closeAndShowFrame(frame);
        });

        addSidebarButton("Overtime Management", "/img/014-overtime-1.png", e -> {
            GUI_HROvertimemanagement frame = new GUI_HROvertimemanagement(loggedInEmployee);
            closeAndShowFrame(frame);
        });

        addSidebarButton("Employee Management", "/img/016-attendance.png", e -> {
            try {
                GUI_HREmployeeManagement employeeManagement = new GUI_HREmployeeManagement(loggedInEmployee);
                closeAndShowFrame(employeeManagement);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        addSidebarButton("Attendance Management", "/img/009-calendar.png", e -> {
            GUI_HRAttendanceManagement window = new GUI_HRAttendanceManagement(loggedInEmployee);
            closeAndShowFrame(window.hrattendancemngmnt);
        });

        addSidebarButton("Leave Management", "/img/023-leave-1.png", e -> {
            GUI_HRLeaveManagement window = new GUI_HRLeaveManagement(loggedInEmployee);
            closeAndShowFrame(window.hrleavemngmnt);
        });

        addSidebarButton("Payroll", "/img/026-calculator.png", e -> {
            GUI_PayrollSalaryCalculation window = new GUI_PayrollSalaryCalculation(loggedInEmployee);
            closeAndShowFrame(window.payrollsalarycalc);
        });

        addSidebarButton("Monthly Summary Reports", "/img/027-report.png", e -> {
            GUI_PayrollMonthlySummary window = new GUI_PayrollMonthlySummary(loggedInEmployee);
            closeAndShowFrame(window.payrollmontlysummary);
        });

        addSidebarButton("Permissions Management", "/img/030-secure-shield.png", e -> {
            GUI_ITPermissions window = new GUI_ITPermissions(loggedInEmployee);
            closeAndShowFrame(window.permissionsFrame);
        });

        addSidebarButton("Credentials Management", "/img/032-permission.png", e -> {
            GUI_ITCredentialsManagement window = new GUI_ITCredentialsManagement(loggedInEmployee);
            closeAndShowFrame(window.usermngmntFrame);
        });

        addSidebarButton("Authentication Logs", "/img/033-permission-1.png", e -> {
            GUI_ITLogs window = new GUI_ITLogs(loggedInEmployee);
            closeAndShowFrame(window.authenticationlogs);
        });
    }

    private void addSidebarButton(String text, String iconPath, ActionListener actionListener) {
        SidebarButton button = new SidebarButton(text, iconPath, actionListener);
        buttons.add(button);
    }

    private void layoutButtons() {
        for (SidebarButton button : buttons) {
            button.setAlignmentX(Component.LEFT_ALIGNMENT); // Align buttons to the left
            add(button);
            add(Box.createRigidArea(new Dimension(0, 8))); // Add vertical spacing between buttons
        }
    }

    private void closeCurrentFrame() {
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    private void setButtonVisibility() {
        PermissionChecker permissionChecker = new PermissionChecker(loggedInEmployee);
        List<String> visibleButtons = permissionChecker.getVisibleButtons();
        for (SidebarButton button : buttons) {
            button.setVisible(visibleButtons.contains(button.getText()));
        }
        revalidate();
        repaint();
    }

    private void closeAndShowFrame(JFrame frame) {
        closeCurrentFrame();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
