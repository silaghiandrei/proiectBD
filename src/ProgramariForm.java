import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class ProgramariForm extends JFrame{
    private JPanel topPanel;
    private JButton logOutButton;
    private JButton homeButton;
    private JList listaPacienti;
    private JPanel progPanel;
    private JPanel listaPanel;

    public ProgramariForm(Angajat angajat) {
        setTitle("ProgramariForm");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(450, 429));
        setSize(600, 470);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        listaPacienti = new JList<>();

        listaPacienti.setCellRenderer(new ProgramariForm.ProgramariCellRenderer());
        listaPacienti.setPreferredSize(new Dimension(200, 450));
        listaPacienti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(listaPacienti);

        add(listaPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        updatePacientiList(angajat.getIdangajat());

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MedicForm medicForm = new MedicForm(angajat);
            }
        });

        setVisible(true);
    }


    public java.util.List<Pacient> getPacienti(Integer medicId) {

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        java.util.List<Pacient> pacienti = new ArrayList<>();

        String sqlQuery = "SELECT p.nume, p.prenume " +
                "FROM programare pr " +
                "JOIN pacient p ON pr.pacient_idpacient = p.idpacient " +
                "WHERE pr.medic_angajat_idangajat = ? " +
                "  AND pr.data = CURDATE();";



        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, medicId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");

                    Pacient pacient = new Pacient();

                    pacient.setNume(nume);
                    pacient.setPrenume(prenume);
                    pacienti.add(pacient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pacienti;
    }
    private DefaultListModel<Pacient> getPacientList(Integer medicId) {
        DefaultListModel<Pacient> pacientListModel = new DefaultListModel<>();
        java.util.List<Pacient> pacienti = getPacienti(medicId);
        for (Pacient pacient : pacienti) {
            pacientListModel.addElement(pacient);
        }
        return pacientListModel;
    }


    private static class ProgramariCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Pacient) {
                value = ((Pacient) value).toString();
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    private void updatePacientiList(Integer medicId) {
        DefaultListModel<Pacient> pacientListModel = getPacientList(medicId);
        listaPacienti.setModel(pacientListModel);
    }
}
