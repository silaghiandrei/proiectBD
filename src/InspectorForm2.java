import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.sql.*;

public class InspectorForm2 extends JFrame {

    private JPanel inspectorPanel;
    private JLabel orarLabel;
    private JLabel luniOrar;
    private JLabel martiOrar;
    private JLabel miercuriOrar;
    private JLabel joiOrar;
    private JLabel vineriOrar;
    private JTextField numeText;
    private JTextField prenumeText;
    private JTextField functieText;
    private JButton cautareButton;
    private JPanel topPanel;
    private JButton logOutButton;
    private JLabel luni;
    private JPanel infoPanel;
    private JButton datePersButton;
    private JLabel numePrenume;
    private JButton orarSaptamanalButton;

    public InspectorForm2(Angajat angajat){
        setTitle("InspectorForm");
        setContentPane(inspectorPanel);
        setMinimumSize(new Dimension(500, 350));
        setSize(500, 350);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        datePersButton.setFocusPainted(false);
        datePersButton.setBorderPainted(false);
        logOutButton.setFocusPainted(false);
        logOutButton.setBorderPainted(false);
        orarSaptamanalButton.setFocusPainted(false);
        orarSaptamanalButton.setBorderPainted(false);

        luniOrar.setText(null);
        martiOrar.setText(null);
        miercuriOrar.setText(null);
        joiOrar.setText(null);
        vineriOrar.setText(null);
        numePrenume.setText(null);
        cautareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getOrarAngajat(numeText.getText(), prenumeText.getText(), functieText.getText());
                numePrenume.setText(numeText.getText() + " " + prenumeText.getText());
                numeText.setText(null);
                prenumeText.setText(null);
                functieText.setText(null);

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

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Autentificare newAutentificare = new Autentificare(null);
            }
        });

        setVisible(true);
    }

    public void getOrarAngajat(String numeCautare, String prenumeCautare, String functiaCautare){

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try(Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD)){
            String sql = "select orar.zi, orar.startOrar, orar.sfarsitOrar, orar.denumirePoli from orar " +
                    " join angajat on angajat.idangajat = orar.angajat_idangajat " +
                    " where angajat.nume = ? and angajat.prenume = ? and angajat.functia = ?";
            try(PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                preparedStatement.setString(1, numeCautare);
                preparedStatement.setString(2, prenumeCautare);
                preparedStatement.setString(3, functiaCautare);

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        if(rs.getString("zi").equals("Luni")){
                            luniOrar.setText(String.valueOf(rs.getTime("startOrar")) + " - " + String.valueOf(rs.getTime("sfarsitOrar")) + " " + rs.getString("denumirePoli"));
                            luniOrar.setVisible(true);
                        }
                        if(rs.getString("zi").equals("Marti")){
                            martiOrar.setText(String.valueOf(rs.getTime("startOrar")) + " - " + String.valueOf(rs.getTime("sfarsitOrar")) + " " + rs.getString("denumirePoli"));
                            martiOrar.setVisible(true);
                        }
                        if(rs.getString("zi").equals("Miercuri")){
                            miercuriOrar.setText(String.valueOf(rs.getTime("startOrar")) + " - " + String.valueOf(rs.getTime("sfarsitOrar")) + " " + rs.getString("denumirePoli"));
                            miercuriOrar.setVisible(true);
                        }
                        if(rs.getString("zi").equals("Joi")){
                            joiOrar.setText(String.valueOf(rs.getTime("startOrar")) + " - " + String.valueOf(rs.getTime("sfarsitOrar")) + " " + rs.getString("denumirePoli"));
                            joiOrar.setVisible(true);
                        }
                        if(rs.getString("zi").equals("Vineri")){
                            vineriOrar.setText(String.valueOf(rs.getTime("startOrar")) + " - " + String.valueOf(rs.getTime("sfarsitOrar")) + " " + rs.getString("denumirePoli"));
                            vineriOrar.setVisible(true);
                        }
                    }
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
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

}
