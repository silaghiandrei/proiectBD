import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdaugaMedic extends JFrame{
    private JPanel addMedic;
    private JLabel postDidacticLabel;
    private JTextField postDidacticText;
    private JButton adaugaMedicButton;
    private JLabel graLabel;
    private JLabel codParafaLabel;
    private JTextField gradText;
    private JTextField codText;
    private JLabel titluLabel;
    private JTextField titluText;
    private JLabel procentLabel;
    private JTextField procentText;
    private JTextField specialitateText;
    private JLabel specilalitateLabel;

    public Integer id;
    //////////////////////////////////////To do
    //salveaza id-l ultimului angajat introdus
    //cauta in ttabela medic medicul cu acel Id si modifica-l

    public AdaugaMedic(){
        setTitle("AdaugaAngajatForm");
        setContentPane(addMedic);
        setMinimumSize(new Dimension(450, 429));
        setSize(500, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        adaugaMedicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Medic medic;
                try {
                    medic = getMedicWithMaxId();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                adaugareMedic(postDidacticText.getText(),gradText.getText(),codText.getText(),titluText.getText(),Integer.parseInt(procentText.getText()), medic.getAngajat_idangajat(),Integer.parseInt(specialitateText.getText()));
                dispose();
            }
        });
        setVisible(true);
    }

    public void adaugareMedic(String postdidactic, String grad, String codparafa, String titlustintiific, Integer procentaditional, Integer angajat_idangajat, Integer idspecialitate) {

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            // Search for a medic based on the given ID
            Medic existingMedic = getMedicWithMaxId();
            id = existingMedic.getAngajat_idangajat();

            if (existingMedic != null) {
                // If a medic with the given ID exists, call the AdaugaMedicDacaEsteMedic procedure
                try (CallableStatement callableStatement = con.prepareCall("{call AdaugaMedicDacaEsteMedic(?,?,?,?,?,?,?)}")) {
                    callableStatement.setString(1, String.valueOf(angajat_idangajat));
                    callableStatement.setString(2, String.valueOf(idspecialitate));
                    callableStatement.setString(3, postdidactic);
                    callableStatement.setString(4, grad);
                    callableStatement.setString(5, codparafa);
                    callableStatement.setString(6, titlustintiific);
                    callableStatement.setString(7, String.valueOf(procentaditional));

                    ResultSet rs = callableStatement.executeQuery();
                    // Handle the result if needed
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Medic with ID " + angajat_idangajat + " not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Helper method to get a Medic by ID
    private Medic getMedicWithMaxId() throws SQLException {

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {

            String query = "SELECT * FROM medic WHERE angajat_idangajat = (SELECT MAX(angajat_idangajat) FROM medic)";

            try (PreparedStatement statement = con.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Create and return a Medic object based on the result
                    Medic medic = new Medic();
                    medic.setAngajat_idangajat(resultSet.getInt("angajat_idangajat"));
                    medic.setIdspecialitate(resultSet.getInt("idspecialitate"));
                    medic.setGrad(resultSet.getString("grad"));
                    medic.setCodparafa(resultSet.getString("codparafa"));
                    medic.setProcentaditional(resultSet.getInt("procentaditional"));
                    medic.setTitlustintiific(resultSet.getString("titlustintiific"));
                    medic.setPostdidactic(resultSet.getString("postdidactic"));
                    // Set other fields as needed
                    return medic;
                }
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null; // Return null if no Medic is found
    }
}
