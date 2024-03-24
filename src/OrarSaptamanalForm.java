import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;


public class OrarSaptamanalForm extends JFrame{
    private JPanel topPanel;
    private JButton homeButton;
    private JButton vineriButton;
    private JList<Angajat> listaAngajati;
    private JButton joiButton;
    private JButton miercuriButton;
    private JButton martiButton;
    private JButton luniButton;
    private JPanel orarSapt;
    private JPanel daysPanel;

    public static Time startOr;
    public static Time sfarsitOr;

    public static String numePoli;

    public OrarSaptamanalForm(Angajat angajat){
        setTitle("OrarSaptForm");
        setLayout(new BorderLayout());
        //setContentPane(orarSapt);
        setMinimumSize(new Dimension(500, 350));
        setSize(500, 350);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        homeButton.setFocusPainted(false);
        homeButton.setBorderPainted(false);

        listaAngajati = new JList<>(getAngajatList(null));
        listaAngajati.setCellRenderer(new OrarSaptamanalForm.EmployeeCellRenderer());
        listaAngajati.setPreferredSize(new Dimension(200, 450));
        listaAngajati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a ListSelectionListener to the JList
        JScrollPane scrollPane = new JScrollPane(listaAngajati);

        // Switch positions of detailsPanel and listaAngajati in the BorderLayout
        add(daysPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if(angajat.getFunctia().equals("inspector resurse umane")) {
                    InspectorForm2 inspectorForm = new InspectorForm2(angajat);
                }
                if(angajat.getFunctia().equals("expert financiar contabil")) {
                    ContabilForm contabilForm = new ContabilForm(angajat);
                }
            }
        });



        luniButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployeeList(luniButton.getText());
            }
        });

        martiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployeeList(martiButton.getText());
            }
        });

        miercuriButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployeeList(miercuriButton.getText());
            }
        });

        joiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployeeList(joiButton.getText());
            }
        });

        vineriButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployeeList(vineriButton.getText());
            }
        });

    }

    public java.util.List<Angajat> fetchEmployeesFromDatabase(String day) {
        java.util.List<Angajat> employees = new ArrayList<>();

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {

            String query = "SELECT a.idangajat, a.cnp, a.nume, a.prenume, a.adresa, a.email, a.telefon, a.iban, a.dataAngajarii, a.functia, a.salariu, a.nrOre, o.startOrar, o.sfarsitOrar, o.denumirePoli " +
                    "FROM angajat a " +
                    "JOIN orar o ON a.idangajat = o.angajat_idangajat " +
                    "WHERE o.zi = ?";

            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setString(1, day);

                try (ResultSet resultSet = statement.executeQuery()) {
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
                        startOr = resultSet.getTime("startOrar");
                        sfarsitOr = resultSet.getTime("sfarsitOrar");
                        numePoli = resultSet.getString("denumirePoli");

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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }


    public DefaultListModel<Angajat> getAngajatList(String day) {
        DefaultListModel<Angajat> employeeListModel = new DefaultListModel<>();
        java.util.List<Angajat> employees = fetchEmployeesFromDatabase(day);
        for (Angajat employee : employees) {
            employeeListModel.addElement(employee);
        }
        return employeeListModel;
    }

    private static class EmployeeCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Angajat) {
                value = ((Angajat) value).toString() +  " " + startOr + " " + sfarsitOr + " " + numePoli;
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    private void updateEmployeeList(String day) {
        DefaultListModel<Angajat> employeeListModel = getAngajatList(day);
        listaAngajati.setModel(employeeListModel);
    }

}
