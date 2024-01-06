import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class InspectorForm extends JFrame{
    private JButton deautentificareButton;
    private JLabel datePersonaleLabel;
    private JLabel numeLabel;
    private JLabel prenumeLabel;
    private JLabel cnpLabel;
    private JLabel adresaLabel;
    private JLabel emailLabel;
    private JLabel telefonLabel;
    private JLabel ibanLabel;
    private JLabel dataAngajareLabel;
    private JLabel functiaLabel;
    private JLabel salariuLabel;
    private JPanel inspectorPanou;
    private JButton operatiiFinanciarContabileButton;
    private JLabel numeCauatreLabel;
    private JTextField numeCautareText;
    private JTextField prenumeCautaretext;
    private JLabel prenumeCautareabel;
    private JTextField functieCautareText;
    private JLabel functieCautareLabel;
    private JButton cautareAngajatButton;
    private JLabel numePrenumeLabel;
    private JTextArea orarTextArea;

    public InspectorForm(Angajat angajat){
        setTitle("InspectorForm");
        setContentPane(inspectorPanou);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        numeLabel.setText("Nume: " + angajat.getNume());
        prenumeLabel.setText("Prenume: " + angajat.getPrenume());
        cnpLabel.setText("CNP: " +  angajat.getCnp());
        adresaLabel.setText("Adresa: " + angajat.getAdresa());
        emailLabel.setText("Email: " + angajat.getEmail());
        telefonLabel.setText("Telefon: " + angajat.getTelefon());
        ibanLabel.setText("IBAN: " + angajat.getIban());
        dataAngajareLabel.setText("Data angajarii: " + angajat.getDataAngajarii());
        functiaLabel.setText("Functia: " + angajat.getFunctia());
        salariuLabel.setText("Salariu: " + angajat.getSalariu() + " RON");

        numePrenumeLabel.setVisible(false);
        orarTextArea.setLineWrap(true);
        orarTextArea.setWrapStyleWord(true);

        deautentificareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Autentificare newAutentificare = new Autentificare(null);
            }
        });

        cautareAngajatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getOrarAngajat(numeCautareText.getText(), prenumeCautaretext.getText(), functieCautareText.getText());
                numePrenumeLabel.setText(numeCautareText.getText() + " " + prenumeCautaretext.getText());
                numePrenumeLabel.setVisible(true);
                numeCautareText.setText(null);
                prenumeCautaretext.setText(null);
                functieCautareText.setText(null);
            }
        });
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
                        orarTextArea.append(" " + rs.getString("zi") + " " +
                                rs.getTime("startOrar") + " " +
                                rs.getTime("sfarsitOrar") + " " +
                                rs.getString("denumirePoli") + " \n");
                    }
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
