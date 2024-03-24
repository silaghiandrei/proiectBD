import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdaugaAsistent extends JFrame{
    private JPanel addAsistent;
    private JTextField gradText;
    private JTextField tipText;
    private JLabel gradLabel;
    private JLabel tipLabel;
    private JButton adaugaAsistentButton;

    public Integer id;

    public AdaugaAsistent(){
        setTitle("AdaugaAsistentForm");
        setContentPane(addAsistent);
        setMinimumSize(new Dimension(300, 260));
        setSize(300, 260);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        adaugaAsistentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Asistent asistent;
                try {
                    asistent = getAsistentWithMaxId();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                adaugareAsistent(gradText.getText(),tipText.getText(),asistent.getAngajat_idangajat());
                dispose();
            }
        });
        setVisible(true);


    }

    public void adaugareAsistent(String grad, String tip, Integer angajat_idangajat) {

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            // Search for a medic based on the given ID
            Asistent existingAsistent = getAsistentWithMaxId();
            id = existingAsistent.getAngajat_idangajat();

            if (existingAsistent != null) {
                // If a medic with the given ID exists, call the AdaugaMedicDacaEsteMedic procedure
                try (CallableStatement callableStatement = con.prepareCall("{call AdaugaAsistentMedicalDacaEsteAsistentMedical(?,?,?)}")) {
                    callableStatement.setString(1, String.valueOf(angajat_idangajat));
                    callableStatement.setString(2, grad);
                    callableStatement.setString(3, tip);

                    ResultSet rs = callableStatement.executeQuery();
                    // Handle the result if needed
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Asistent with ID " + angajat_idangajat + " not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Helper method to get a Medic by ID
    private Asistent getAsistentWithMaxId() throws SQLException {
        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {

            String query = "SELECT * FROM `asistent medical` WHERE angajat_idangajat = (SELECT MAX(angajat_idangajat) FROM `asistent medical`)";

            try (PreparedStatement statement = con.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Create and return an Asistent object based on the result
                    Asistent asistent = new Asistent();
                    asistent.setAngajat_idangajat(resultSet.getInt("angajat_idangajat"));
                    asistent.setGrad(resultSet.getString("grad"));
                    asistent.setTip(resultSet.getString("tip")); // Assuming "tip" is the correct column name

                    // Set other fields as needed
                    return asistent;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null; // Return null if no Asistent is found
    }

}
