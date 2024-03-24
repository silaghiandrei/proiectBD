import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class AsistentForm extends JFrame{
    private JPanel panel1;
    private JPanel asistentForm;
    private JPanel topPanel;
    private JButton logOutButton;
    private JButton homeButton;
    private JList<Analize> analizaList;
    private JPanel datePanel;
    private JLabel numeLabel;
    private JLabel prenumeLabel;
    private JTextField numeText;
    private JTextField prenumeText;
    private JTextField rezultatText;
    private JButton validareButton;
    private JLabel rezultatLabel;
    private JButton cautareButton;


    public AsistentForm(Angajat angajat) {

        setTitle("AsistentForm");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(450, 429));
        setSize(600, 470);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        analizaList = new JList<>();

        analizaList.setCellRenderer(new AsistentForm.AnalizeCellRenderer());
        analizaList.setPreferredSize(new Dimension(200, 450));
        analizaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(analizaList);

        add(datePanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Autentificare newAutentificare = new Autentificare(null);
            }
        });
        cautareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAnalizeList(numeText.getText(), prenumeText.getText());
            }
        });

        validareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validareRaport(numeText.getText(), prenumeText.getText(), analizaList.getSelectedValue().getTip(), rezultatText.getText());
            }
        });

        setVisible(true);
    }

    public java.util.List<Analize> getAnalize(String nume, String prenume) {
        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        java.util.List<Analize> analize = new ArrayList<>();

        // SQL query
        String sqlQuery = "SELECT " +
                "    p.idpacient, " +
                "    p.nume, " +
                "    p.prenume, " +
                "    a.tip_analize, " +
                "    a.pret_analize " +
                "FROM " +
                "    pacient p " +
                "JOIN " +
                "    (" +
                "        SELECT " +
                "            id_pacient, " +
                "            MAX(data) AS latest_data " +
                "        FROM " +
                "            programare_analize " +
                "        GROUP BY " +
                "            id_pacient " +
                "    ) latest_pa ON p.idpacient = latest_pa.id_pacient " +
                "JOIN " +
                "    programare_analize pa ON pa.id_pacient = latest_pa.id_pacient AND pa.data = latest_pa.latest_data " +
                "JOIN " +
                "    analize a ON pa.id_analize = a.id_analize " +
                "WHERE " +
                "    p.nume = ? AND p.prenume = ?;";


        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, nume);
            preparedStatement.setString(2, prenume);

            // Execute query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String tip = resultSet.getString("tip_analize");
                    Integer pret = resultSet.getInt("pret_analize");

                    Analize analiza = new Analize();

                    analiza.setTip(tip);
                    analiza.setPrice(pret);
                    analize.add(analiza);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return analize;
    }


    private DefaultListModel<Analize> getAnalizeList(String nume, String prenume) {
        DefaultListModel<Analize> analizeListModel = new DefaultListModel<>();
        java.util.List<Analize> analize = getAnalize(nume, prenume);
        for (Analize analiza : analize) {
            analizeListModel.addElement(analiza);
        }
        return analizeListModel;
    }


    private static class AnalizeCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Analize) {
                value = ((Analize) value).toString();

            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    private void updateAnalizeList(String nume, String prenume) {
        DefaultListModel<Analize> analizeListModel = getAnalizeList(nume, prenume);
        analizaList.setModel(analizeListModel);
    }

    private void validareRaport(String nume, String prenume, String tip, String rezultat){

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            try (CallableStatement callableStatement = con.prepareCall("{call AddRaportAnalize(?,?,?,?)}")) {
                callableStatement.setString(1, nume);
                callableStatement.setString(2, prenume);
                callableStatement.setString(3, tip);
                callableStatement.setString(4, rezultat);

                ResultSet rs = callableStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
