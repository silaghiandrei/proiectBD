import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RapoarteMediciForm extends JFrame {
    private JPanel rapoartePanel;
    private JPanel topPanel;
    private JButton inapoiButton;
    private JLabel lunaLabel;
    private JTextField lunaText;
    private JList<Angajat> listMedici;
    private JPanel buttonPanel;
    private JButton policlinicaApacaButton;
    private JButton policlinicaTitanButton;
    private JButton policlinicaBrancusiButton;
    private DefaultListModel<Angajat> employeeListModel;
    public static Integer profitMed;

    public RapoarteMediciForm(Angajat angajat) {

        setTitle("InspectorForm");
        //setLayout(new BorderLayout());
        setContentPane(rapoartePanel);
        setMinimumSize(new Dimension(400, 350));
        setSize(450, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        employeeListModel = new DefaultListModel<>();
        listMedici.setModel(employeeListModel);

        listMedici.setCellRenderer(new RapoarteMediciForm.EmployeeCellRenderer());
        listMedici.setPreferredSize(new Dimension(200, 300));
        listMedici.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        buttonPanel.setPreferredSize(new Dimension(200, 300));

        // JScrollPane scrollPane = new JScrollPane(listMedici);


        //add(scrollPane, BorderLayout.EAST);
        //add(topPanel, BorderLayout.NORTH);

        policlinicaApacaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear existing elements
                //employeeListModel.clear();
                System.out.println("button clicked");
                updateEmployeeList(Integer.parseInt(lunaText.getText()), policlinicaApacaButton.getText());
            }
        });

        policlinicaTitanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear existing elements
                //employeeListModel.clear();
                //listMedici.setModel(getAngajatList(Integer.parseInt(lunaText.getText()), policlinicaTitanButton.getText()));
                updateEmployeeList(Integer.parseInt(lunaText.getText()), policlinicaTitanButton.getText());
            }
        });

        policlinicaBrancusiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear existing elements
                //employeeListModel.clear();
                //listMedici.setModel(getAngajatList(Integer.parseInt(lunaText.getText()), policlinicaBrancusiButton.getText()));
                updateEmployeeList(Integer.parseInt(lunaText.getText()), policlinicaBrancusiButton.getText());
            }
        });

        inapoiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ContabilForm newcontabil = new ContabilForm(angajat);
            }
        });


        setVisible(true);
    }

    public List<Angajat> fetchMediciFromDatabase(Integer month, String policlinica) {
        List<Angajat> medici = new ArrayList<>();

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String query = "SELECT a.idangajat, a.cnp, a.nume, a.prenume, a.adresa, a.email, a.telefon, a.iban, a.dataAngajarii, a.salariu, m.procentaditional " +
                    "FROM angajat a " +
                    "JOIN medic m ON a.idangajat = m.angajat_idangajat " +
                    "LEFT JOIN orar o ON a.idangajat = o.angajat_idangajat " +
                    "LEFT JOIN programare p ON a.idangajat = p.medic_angajat_idangajat AND MONTH(p.data) = ? " +
                    "WHERE a.functia = 'medic' " +
                    "AND (MONTH(p.data) = ? AND (o.denumirePoli = ? OR o.denumirePoli IS NULL)) " +
                    "GROUP BY a.idangajat, a.cnp, a.nume, a.prenume, a.adresa, a.email, a.telefon, a.iban, a.dataAngajarii, a.salariu, m.procentaditional " +
                    "HAVING COUNT(p.idprogramare) > 0";

            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setInt(1, month);
                statement.setInt(2, month);
                statement.setString(3, policlinica);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Angajat medic = new Angajat();
                        medic.setIdangajat(resultSet.getInt("idangajat"));
                        medic.setCnp(resultSet.getString("cnp"));
                        medic.setNume(resultSet.getString("nume"));
                        medic.setPrenume(resultSet.getString("prenume"));
                        medic.setAdresa(resultSet.getString("adresa"));
                        medic.setEmail(resultSet.getString("email"));
                        medic.setTelefon(resultSet.getString("telefon"));
                        medic.setIban(resultSet.getString("iban"));
                        medic.setDataAngajarii(resultSet.getDate("dataAngajarii"));
                        medic.setSalariu(resultSet.getInt("salariu"));
                        medic.setProcentAditional(resultSet.getInt("procentaditional"));

                        int profit = calculateMedicProfit(medic, month, policlinica);
                        profitMed = profit;
                        System.out.println(profit);

                        medici.add(medic);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medici;
    }



    private int calculateMedicProfit(Angajat medic, Integer month, String policlinica) {
        System.out.println("Calculating profit for Medic: " + medic.getIdangajat() + " for month: " + month + " at Policlinica: " + policlinica);

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        String profitQuery = "SELECT SUM(serviciu.pret * medic.procentaditional / 100) AS totalProfit " +
                "FROM programare p " +
                "JOIN serviciu ON p.serviciu_idserviciu = serviciu.idserviciu " +
                "JOIN medic ON p.medic_angajat_idangajat = medic.angajat_idangajat " +
                "WHERE p.medic_angajat_idangajat = ? AND MONTH(p.data) = ? AND EXISTS (" +
                "  SELECT 1 FROM orar o " +
                "  WHERE o.angajat_idangajat = ? AND o.denumirePoli = ?" +
                ")";

        // Print the generated profit query
        System.out.println("Generated Profit Query: " + profitQuery);

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement profitStatement = con.prepareStatement(profitQuery)) {

            profitStatement.setInt(1, medic.getIdangajat());
            profitStatement.setInt(2, month);
            profitStatement.setInt(3, medic.getIdangajat());
            profitStatement.setString(4, policlinica);

            // Print the parameters for debugging
            System.out.println("Profit Query Parameters - Medic ID: " + medic.getIdangajat() + ", Month: " + month + ", Policlinica: " + policlinica);

            try (ResultSet profitResultSet = profitStatement.executeQuery()) {
                if (profitResultSet.next()) {
                    int totalProfit = profitResultSet.getInt("totalProfit");
                    System.out.println("Total Profit: " + totalProfit);
                    return totalProfit;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Log error or handle it as per your application's requirements
        }

        return 0;  // Return 0 if there is no profit or in case of an exception
    }







    private static class EmployeeCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Angajat) {
                value = ((Angajat) value).toString() + " " + profitMed;
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    public DefaultListModel<Angajat> getAngajatList(Integer month, String denumirePoliclinica) {
        DefaultListModel<Angajat> employeeListModel = new DefaultListModel<>();
        List<Angajat> employees = fetchMediciFromDatabase(month, denumirePoliclinica);
        for (Angajat employee : employees) {
            employeeListModel.addElement(employee);
            System.out.println("Adding to list: " + employee); // Debugging
        }
        return employeeListModel;
    }

    private void updateEmployeeList(Integer month, String denPoli) {
        SwingUtilities.invokeLater(() -> {
            employeeListModel.clear();  // Clear existing elements

            // Add new elements
            List<Angajat> employees = fetchMediciFromDatabase(month, denPoli);
            for (Angajat employee : employees) {
                employeeListModel.addElement(employee);
                System.out.println("Adding to list: " + employee); // Debugging
            }
        });
    }


}
