import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModificaAngajatForm extends JFrame {
    private JPanel updatePanel;
    private JPanel topPanel;
    private JButton homeButton;
    private JList<Angajat> listaAngajati;
    private JPanel detailsPanel;
    private JTextField numeText;
    private JTextField ibanText;
    private JButton updateButton;
    private JLabel numeLabel;
    private JLabel prenumeLabel;
    private JLabel cnpLabel;
    private JLabel adresLabel;
    private JLabel emailLabel;
    private JLabel telefonLabel;
    private JTextField prenumeText;
    private JTextField cnpText;
    private JTextField adresaText;
    private JTextField emailText;
    private JTextField telefonText;
    private JLabel ibanLabel;
    private JLabel dataAngajarii;
    private JTextField dataAngText;
    private JLabel functieLabel;
    private JTextField functiaText;
    private JLabel salariuLabel;
    private JLabel nrOreLabel;
    private JTextField nrOreText;
    private JTextField salariuText;
    private JButton deleteButton;

    public Integer idA;

    public String functia;

    public ModificaAngajatForm(Angajat angajat) {
        setTitle("AdministratorForm");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(450, 429));
        setSize(600, 470);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        listaAngajati = new JList<>(getAngajatList());
        listaAngajati.setCellRenderer(new EmployeeCellRenderer());

        listaAngajati.setPreferredSize(new Dimension(200, 450));

        listaAngajati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a ListSelectionListener to the JList
        listaAngajati.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Get the selected item
                    Angajat selectedEmployee = listaAngajati.getSelectedValue();

                    // Display details in the details label
                    if (selectedEmployee != null) {
                        numeText.setText(selectedEmployee.getNume());
                        prenumeText.setText(selectedEmployee.getPrenume());
                        cnpText.setText(selectedEmployee.getCnp());
                        adresaText.setText(selectedEmployee.getAdresa());
                        emailText.setText(selectedEmployee.getEmail());
                        telefonText.setText(selectedEmployee.getTelefon());
                        ibanText.setText(selectedEmployee.getIban());
                        dataAngText.setText(String.valueOf(selectedEmployee.getDataAngajarii()));
                        functiaText.setText(selectedEmployee.getFunctia());
                        salariuText.setText(String.valueOf(selectedEmployee.getSalariu()));
                        nrOreText.setText(String.valueOf(selectedEmployee.getNrOre()));
                        idA = selectedEmployee.getIdangajat();
                        functia = selectedEmployee.getFunctia();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaAngajati);

        // Switch positions of detailsPanel and listaAngajati in the BorderLayout
        add(detailsPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        homeButton.setFocusPainted(false);
        homeButton.setBorderPainted(false);

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AdministratorForm administratorForm = new AdministratorForm(angajat);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificareAngajat(numeText.getText(),
                        prenumeText.getText(),
                        cnpText.getText(),
                        adresaText.getText(),
                        emailText.getText(),
                        telefonText.getText(),
                        ibanText.getText(),
                        java.sql.Date.valueOf(dataAngText.getText()),
                        functiaText.getText(),
                        Integer.parseInt(salariuText.getText()),
                        Integer.parseInt(salariuText.getText()),
                        idA
                );

                setVisible(true);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stergereAngajat(idA);
                listaAngajati.clearSelection();

            }
        });
    }

            public List<Angajat> fetchEmployeesFromDatabase() {
                List<Angajat> employees = new ArrayList<>();

                final String DB_URL = "jdbc:mysql://localhost/mydb";
                final String USERNAME = "root";
                final String PASSWORD = "mysqlpass";

                try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {

                    String query = "SELECT idangajat, cnp, nume, prenume, adresa, email, telefon, iban, dataAngajarii, functia, salariu, nrOre FROM angajat";
                    try (PreparedStatement statement = con.prepareStatement(query);
                         ResultSet resultSet = statement.executeQuery()) {

                        while (resultSet.next()) {
                            Integer idangajat = resultSet.getInt(1);
                            String cnp = resultSet.getString(2);
                            String nume = resultSet.getString(3);
                            String prenume = resultSet.getString(4);
                            String adresa = resultSet.getString(5);
                            String email = resultSet.getString(6);
                            String telefon = resultSet.getString(7);
                            String iban = resultSet.getString(8);
                            Date dataAngajarii = resultSet.getDate(9);
                            String functia = resultSet.getString(10);
                            Integer salariu = resultSet.getInt(11);
                            Integer nrOre = resultSet.getInt(12);

                            Angajat employee = new Angajat();

                            employee.setIdangajat(idangajat);
                            employee.setNume(nume);
                            employee.setPrenume(prenume);
                            employee.setCnp(cnp);
                            employee.setAdresa(adresa);
                            employee.setEmail(email);
                            employee.setTelefon(telefon);
                            employee.setIban(iban);
                            employee.setDataAngajarii(dataAngajarii);
                            employee.setFunctia(functia);
                            employee.setSalariu(salariu);
                            employee.setNrOre(nrOre);

                            employees.add(employee);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return employees;
            }

            public DefaultListModel<Angajat> getAngajatList() {
                DefaultListModel<Angajat> employeeListModel = new DefaultListModel<>();
                List<Angajat> employees = fetchEmployeesFromDatabase();
                for (Angajat employee : employees) {
                    employeeListModel.addElement(employee);
                }
                return employeeListModel;
            }

            private static class EmployeeCellRenderer extends DefaultListCellRenderer {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    if (value instanceof Angajat) {
                        value = ((Angajat) value).toString();
                    }
                    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                }
            }

    public void modificareAngajat(String nume, String prenume, String cnp, String adresa, String email, String telefon, String iban, Date dataAngajarii, String functia, Integer salariu, Integer nrOre, Integer id) {
        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD))  {
            String storedProcedureCall = "{call ModificaAngajat(?,?,?,?,?,?,?,?,?,?,?,?)}";
            try (PreparedStatement preparedStatement = con.prepareStatement(storedProcedureCall)) {
                preparedStatement.setString(1, cnp);
                preparedStatement.setString(2, nume);
                preparedStatement.setString(3, prenume);
                preparedStatement.setString(4, adresa);
                preparedStatement.setString(5, email);
                preparedStatement.setString(6, telefon);
                preparedStatement.setString(7, iban);
                preparedStatement.setDate(8, dataAngajarii);
                preparedStatement.setString(9, functia);
                preparedStatement.setInt(10, salariu);
                preparedStatement.setInt(11, nrOre);
                preparedStatement.setInt(12, id);

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void stergereAngajat(Integer id) {
        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            try (CallableStatement callableStatement = con.prepareCall("{call StergeAngajat(?)}")) {
                callableStatement.setInt(1, id);

                ResultSet rs = callableStatement.executeQuery();
                // If the record was deleted successfully, remove the item from the JList
                if (rs.next() && "Record deleted successfully".equals(rs.getString("Result"))) {
                    DefaultListModel<Angajat> listModel = (DefaultListModel<Angajat>) listaAngajati.getModel();

                    // Iterate through the list model to find the item with the matching ID
                    for (int i = 0; i < listModel.size(); i++) {
                        Angajat employee = listModel.getElementAt(i);
                        if (employee.getIdangajat().equals(id)) {
                            listModel.removeElementAt(i);
                            break;  // Break the loop once the item is found and removed
                        }
                    }

                    // Clear the details panel after deletion
                    clearDetailsPanel();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Helper method to clear details panel
    private void clearDetailsPanel() {
        numeText.setText("");
        prenumeText.setText("");
        cnpText.setText("");
        adresaText.setText("");
        emailText.setText("");
        telefonText.setText("");
        ibanText.setText("");
        dataAngText.setText("");
        functiaText.setText("");
        salariuText.setText("");
        nrOreText.setText("");
        idA = null;  // Reset the selected employee id
        listaAngajati.clearSelection();  // Clear selection in the JList
    }


    public Integer getIdA() {
        return idA;
    }
}

