import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MedicForm extends JFrame{
    private JPanel medicPanel;
    private JPanel topPanel;
    private JButton logOutButton;
    private JPanel istoricPanel;
    private JLabel raportLabel;
    private JLabel numePacientLabel;
    private JLabel prenumePacientLabel;
    private JTextField numePacText;
    private JTextField prenumePacText;
    private JTextField numeMedText;
    private JTextField prenumeMedText;
    private JTextField dataText;
    private JTextArea istoricText;
    private JLabel istoric;
    private JLabel dataLabel;
    private JLabel simptome;
    private JTextArea simptomeText;
    private JLabel invest;
    private JTextArea investText;
    private JLabel diag;
    private JTextArea diagText;
    private JLabel recom;
    private JTextArea recomText;
    private JTextArea codText;
    private JButton adaugaButton;
    private JButton programariButton;

    public MedicForm(Angajat angajat) {

        setTitle("MedicForm");
        setContentPane(medicPanel);
        setMinimumSize(new Dimension(450, 600));
        setSize(450, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Autentificare autentificare = new Autentificare(null);
            }
        });

        adaugaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adaugaRaport(numePacText.getText(), prenumePacText.getText(), numeMedText.getText(), prenumeMedText.getText(), istoricText.getText(), simptomeText.getText(), investText.getText(), diagText.getText(), recomText.getText(), codText.getText(), Date.valueOf(dataText.getText()));
            }
        });

        programariButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProgramari(angajat);
            }
        });

    }

    public void adaugaRaport(String numePac, String prenumePac, String numeMed, String prenumeMed, String istoric, String simptome, String investigatii, String diagnostic, String recomandari, String parafat, Date data) {
        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            try (CallableStatement callableStatement = con.prepareCall("{call InsertRaportMedical(?,?,?,?,?,?,?,?,?,?,?)}")) {
                callableStatement.setString(1, numePac);
                callableStatement.setString(2, prenumePac);
                callableStatement.setString(3, numeMed);
                callableStatement.setString(4, prenumeMed);
                callableStatement.setString(5, istoric);
                callableStatement.setString(6, simptome);
                callableStatement.setString(7, investigatii);
                callableStatement.setString(8, diagnostic);
                callableStatement.setString(9, recomandari);
                callableStatement.setString(10, parafat);
                callableStatement.setDate(11, data);

                ResultSet rs = callableStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void openProgramari(Angajat angajat){
        SwingUtilities.invokeLater(()->{
            ProgramariForm prog = new ProgramariForm(angajat);
            prog.setVisible(true);
        });
    }
}
