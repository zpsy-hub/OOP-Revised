package view;

import java.awt.EventQueue;

import javax.swing.JFrame;

import model.Employee;
import model.Permission;
import model.User;
import DAO.EmployeeDAO;
import DAO.PermissionDAO;
import customUI.ImagePanel;
import customUI.Sidebar;
import customUI.SidebarButton;
import service.SQL_client;
import service.PermissionService;
import service.PermissionWithStatus;
import util.SessionManager;
import util.SignOutButton;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;

public class GUI_ITPermissions {
    public JFrame permissionsFrame;
    private static User loggedInEmployee;
    private final EmployeeDAO employeeDAO;
    private JTable permissionsTable;
    private JTable userstable;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    User loggedInEmployee = SessionManager.getLoggedInUser();
                    GUI_ITPermissions window = new GUI_ITPermissions(loggedInEmployee);
                    window.permissionsFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public GUI_ITPermissions(User loggedInEmployee) {
        GUI_ITPermissions.loggedInEmployee = loggedInEmployee;
        employeeDAO = EmployeeDAO.getInstance(); // Instantiate EmployeeDAO
        initialize();
        populateUserTable();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        permissionsFrame = new JFrame();
        permissionsFrame.setBounds(100, 100, 1280, 800);
        permissionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        permissionsFrame.getContentPane().setLayout(null);

        // Main panel with background image
        ImagePanel mainPanel = new ImagePanel("/img/credentials  mngmnt.png");
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setBounds(0, 0, 1280, 800);
        permissionsFrame.getContentPane().add(mainPanel);
        mainPanel.setLayout(null);
		
        // Use the Sidebar class
        Sidebar sidebar = new Sidebar(loggedInEmployee);
        sidebar.setBounds(0, 92, 321, 680);
        mainPanel.add(sidebar);

        // Sign Out button initialization
        SignOutButton signOutButton = new SignOutButton(SignOutButton.getSignOutActionListener(permissionsFrame));
        signOutButton.setBounds(1125, 24, 111, 40);
        mainPanel.add(signOutButton);
        
        JLabel lblPermissions = new JLabel("Users");
        lblPermissions.setFont(new Font("Poppins", Font.PLAIN, 22));
        lblPermissions.setBounds(374, 146, 379, 33);
        mainPanel.add(lblPermissions);     
        
        JLabel lblPermissions_1 = new JLabel("Permissions");
        lblPermissions_1.setFont(new Font("Poppins", Font.PLAIN, 20));
        lblPermissions_1.setBounds(800, 146, 379, 33);
        mainPanel.add(lblPermissions_1);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(374, 189, 420, 370);
        mainPanel.add(scrollPane_1);

        userstable = new JTable();
        scrollPane_1.setViewportView(userstable);

        userstable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userstable.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Emp ID", "Last Name", "First Name", "Position" }));
        userstable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = userstable.getSelectedRow();
                    if (selectedRow != -1) {
                        int selectedEmpId = (int) userstable.getValueAt(selectedRow, 0);
                        Employee selectedEmployee = employeeDAO.getEmployeeById(selectedEmpId);
                        updatePermissionsTable(selectedEmployee);
                    }
                }
            }
        });

        scrollPane_1.setViewportView(userstable);
        
        JButton btnNewButton = new JButton("Grant Access");
        btnNewButton.setFont(new Font("Poppins Medium", Font.PLAIN, 16));
        btnNewButton.setBounds(826, 595, 179, 40);
        mainPanel.add(btnNewButton);
        
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userstable.getSelectedRow();
                if (selectedRow != -1) {
                    int selectedEmpId = (int) userstable.getValueAt(selectedRow, 0);
                    Employee selectedEmployee = employeeDAO.getEmployeeById(selectedEmpId);
                    int selectedPermissionRow = permissionsTable.getSelectedRow();
                    if (selectedPermissionRow != -1) {
                        DefaultTableModel permissionsTableModel = (DefaultTableModel) permissionsTable.getModel();
                        String permissionName = (String) permissionsTableModel.getValueAt(selectedPermissionRow, 0);
                        PermissionDAO permissionDAO = PermissionDAO.getInstance();
                        model.Permission selectedPermission = permissionDAO.getAllPermissions().stream()
                                .filter(permission -> permission.getPermissionName().equals(permissionName))
                                .findFirst().orElse(null);
                        if (selectedPermission != null) {
                            permissionDAO.grantAccess(selectedEmployee.getEmpId(), selectedPermission.getPermissionId());
                            updatePermissionsTable(selectedEmployee);
                        } else {
                            JOptionPane.showMessageDialog(null, "Permission not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a permission to grant access.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an employee.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton btnRevokeAccess = new JButton("Revoke Access");
        btnRevokeAccess.setFont(new Font("Poppins Medium", Font.PLAIN, 16));
        btnRevokeAccess.setBounds(1025, 595, 179, 40);
        mainPanel.add(btnRevokeAccess);
        
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setBounds(800, 189, 420, 370);
                mainPanel.add(scrollPane);
                
                        permissionsTable = new JTable();
                        scrollPane.setViewportView(permissionsTable);
                        
                                JPanel panel = new JPanel();
                                scrollPane.setRowHeaderView(panel);
                                panel.setLayout(null);
        
        btnRevokeAccess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userstable.getSelectedRow();
                if (selectedRow != -1) {
                    int selectedEmpId = (int) userstable.getValueAt(selectedRow, 0);
                    Employee selectedEmployee = employeeDAO.getEmployeeById(selectedEmpId);
                    int selectedPermissionRow = permissionsTable.getSelectedRow();
                    if (selectedPermissionRow != -1) {
                        DefaultTableModel permissionsTableModel = (DefaultTableModel) permissionsTable.getModel();
                        String permissionName = (String) permissionsTableModel.getValueAt(selectedPermissionRow, 0);
                        PermissionDAO permissionDAO = PermissionDAO.getInstance();
                        model.Permission selectedPermission = permissionDAO.getAllPermissions().stream()
                                .filter(permission -> permission.getPermissionName().equals(permissionName))
                                .findFirst().orElse(null);
                        if (selectedPermission != null) {
                            permissionDAO.revokeAccess(selectedEmployee.getEmpId(), selectedPermission.getPermissionId());
                            updatePermissionsTable(selectedEmployee);
                        } else {
                            JOptionPane.showMessageDialog(null, "Permission not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a permission to revoke access.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an employee.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
    }

    // Update the permissions table
    private void updatePermissionsTable(Employee selectedEmployee) {
        DefaultTableModel permissionsTableModel = new DefaultTableModel(new Object[][] {}, new String[] { "Permission Name", "Access" });
        if (selectedEmployee != null) {
            try (Connection connection = SQL_client.getInstance().getConnection()) {
                PermissionDAO permissionDAO = PermissionDAO.getInstance();
                List<model.Permission> allPermissions = permissionDAO.getAllPermissions(); // Get all permissions
                List<model.Permission> userPermissions = permissionDAO.getPermissionsByEmployeeId(selectedEmployee.getEmpId(), connection); // Get permissions for the selected user
                for (model.Permission permission : allPermissions) {
                    boolean hasPermission = userPermissions.stream()
                            .anyMatch(userPermission -> userPermission.getPermissionId() == permission.getPermissionId());
                    permissionsTableModel.addRow(new Object[] { permission.getPermissionName(), hasPermission ? "Granted" : "Not Granted" });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        permissionsTable.setModel(permissionsTableModel);
    }




    // Method to populate the user table
    private void populateUserTable() {
        DefaultTableModel userTableModel = (DefaultTableModel) userstable.getModel();
        List<Employee> employees = employeeDAO.getAllEmployees();
        for (Employee employee : employees) {
            userTableModel.addRow(new Object[] { employee.getEmpId(), employee.getLastName(), employee.getFirstName(), employee.getPosition() });
        }
    }
}
