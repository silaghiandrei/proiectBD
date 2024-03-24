import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContabilForm extends JFrame {
    private JPanel contabilPanel;
    private JPanel topPanel;
    private JButton logOutButton;
    private JButton datePersButton;
    private JButton orarSaptamanalButton;
    private JList<String> listaLuni;
    private JLabel venituriLabel;
    private JLabel cheltLabel;
    private JLabel profitLabel;
    private JPanel profitPanel;
    private JLabel vLabel;
    private JLabel cLabel;
    private JLabel pLabel;
    private JButton rapoarteButton;
    private JButton orarLunarButton;

    private Integer venituri;

    public ContabilForm(Angajat angajat) {
        setTitle("ContabilForm");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(450, 429));
        setSize(600, 470);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        String[] months = {"Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"};

        listaLuni = new JList<>(months);

        listaLuni.setCellRenderer(new CustomListCellRenderer());
        listaLuni.setPreferredSize(new Dimension(200, 450));

        JScrollPane scrollPane = new JScrollPane(listaLuni);

        add(profitPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        vLabel.setText("0 RON");
        cLabel.setText("0 RON");
        pLabel.setText("0 RON");

        datePersButton.setFocusPainted(false);
        datePersButton.setBorderPainted(false);
        logOutButton.setFocusPainted(false);
        logOutButton.setBorderPainted(false);
        orarSaptamanalButton.setFocusPainted(false);
        orarSaptamanalButton.setBorderPainted(false);
        listaLuni.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedMonth = listaLuni.getSelectedValue();

                    int monthIndex = getMonthIndex(selectedMonth);
                    if (monthIndex != -1) {
                        updateVenituriLabel(monthIndex);
                        updateCheltuieliAndProfitLabels(monthIndex);
                    } else {
                        System.out.println("Invalid month selected");
                    }}
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Autentificare newAutentificare = new Autentificare(null);
            }
        });
        datePersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                openDetaliiForm(angajat);
            }
        });

        orarSaptamanalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                openOrarSaptamana(angajat);
            }
        });


        rapoarteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                openRapoarte(angajat);
            }
        });

        orarLunarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openOrarLunar(angajat);
            }
        });

        setVisible(true);
    }

    private void updateVenituriLabel(int selectedMonth) {
        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String query = "SELECT SUM(serviciu.pret) " +
                    "FROM programare " +
                    "JOIN serviciu ON programare.serviciu_idserviciu = serviciu.idserviciu " +
                    "WHERE MONTH(programare.data) = ?";

            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setInt(1, selectedMonth);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int totalVenituri = resultSet.getInt(1);
                        vLabel.setText(totalVenituri + " RON");
                        venituri = totalVenituri;
                    } else {
                        vLabel.setText("Venituri: 0 RON");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateCheltuieliAndProfitLabels(int selectedMonth) {
        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            // Calculate total salaries for the selected month
            String salariesQuery = "SELECT SUM(salariu) FROM angajat";
            try (PreparedStatement salariesStatement = con.prepareStatement(salariesQuery)) {


                try (ResultSet salariesResultSet = salariesStatement.executeQuery()) {
                    int totalSalaries = (salariesResultSet.next()) ? salariesResultSet.getInt(1) : 0;

                    // Calculate total profit for medics in the selected month
                    String profitQuery = "SELECT SUM(serviciu.pret * medic.procentaditional / 100) " +
                            "FROM programare " +
                            "JOIN serviciu ON programare.serviciu_idserviciu = serviciu.idserviciu " +
                            "JOIN medic ON programare.medic_angajat_idangajat = medic.angajat_idangajat " +
                            "WHERE MONTH(programare.data) = ?";
                    try (PreparedStatement profitStatement = con.prepareStatement(profitQuery)) {
                        profitStatement.setInt(1, selectedMonth);

                        try (ResultSet profitResultSet = profitStatement.executeQuery()) {
                            int totalProfit = (profitResultSet.next()) ? profitResultSet.getInt(1) : 0;

                            // Update the labels
                            int totalCheltuieliAndProfit = totalSalaries + totalProfit;
                            cLabel.setText(totalCheltuieliAndProfit + " RON");
                            pLabel.setText(venituri - totalCheltuieliAndProfit + " RON");
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    private static class CustomListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBorder(new EmptyBorder(5, 10, 5, 10));  // Add padding
            label.setFont(label.getFont().deriveFont(16f));  // Change the font size (16f in this case)
            label.setHorizontalAlignment(SwingConstants.LEFT);
            return label;
        }
    }

    private int getMonthIndex(String monthName) {
        String[] months = {"Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"};

        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(monthName)) {
                return i + 1; // Months in SQL are usually represented as 1-indexed
            }
        }

        return -1; // Invalid month name
    }

    public void openDetaliiForm(Angajat angajat){
        SwingUtilities.invokeLater(()->{
            DetaliiAngajat detaliiAngajat = new DetaliiAngajat(angajat);
            detaliiAngajat.setVisible(true);
        });
    }

    public void openOrarSaptamana(Angajat angajat){
        SwingUtilities.invokeLater(()->{
            OrarSaptamanalForm orar = new OrarSaptamanalForm(angajat);
            orar.setVisible(true);
        });
    }

    public void openRapoarte(Angajat angajat){
        SwingUtilities.invokeLater(()->{
            RapoarteMediciForm rapoarte = new RapoarteMediciForm(angajat);
            rapoarte.setVisible(true);
        });
    }

    public void openOrarLunar(Angajat angajat){
        SwingUtilities.invokeLater(()->{
            OrarLunar orar = new OrarLunar(angajat);
            orar.setVisible(true);
        });
    }
}
