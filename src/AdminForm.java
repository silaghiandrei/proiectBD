
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class AdminForm extends JFrame{
    private JPanel adminPanou;
    private JLabel datePersonaleLabel;
    private JLabel numeLabel;
    private JLabel prenumeLabel;
    private JLabel adresaLabel;
    private JLabel cnpLabel;
    private JLabel emailLabel;
    private JLabel telefonLabel;
    private JLabel ibanLabel;
    private JLabel dataAngajareLabel;
    private JLabel functiaLabel;
    private JLabel salariuLabel;
    private JButton deautentificareButton;
    private JButton adaugaAngajatButton;
    private JButton actualizareDateAngajatButton;
    private JButton stergeAngajatButton;
    private JPanel modulPanel;
    private JTextField numeUtilizatorText;
    private JTextField prenumeUtilizatorText;
    private JLabel prenumeUtilizatorLabel;
    private JLabel numeUtilizatorLabel;
    private JLabel cnpUtilizatorLabel;
    private JTextField cnpUtilizatorText;
    private JLabel adresaUtilizatorLabel;
    private JTextField adresaUtilizatorText;
    private JLabel emailUtilizatorLabel;
    private JLabel telefonUtilizatorLabel;
    private JTextField emailUtilizatorText;
    private JTextField telefonUtilizatorText;
    private JLabel ibanUtilizatorLabel;
    private JTextField ibanUtilizatorText;
    private JLabel dataAngajariiLabel;
    private JTextField dataAngajariiText;
    private JLabel functiaUtilizatorLabel;
    private JTextField functiaUtilizatorText;
    private JLabel salariuUtilizatorLabel;
    private JTextField salariuUtilizatorText;

    public AdminForm(Angajat angajat){
        setTitle("AdminForm");
        setContentPane(adminPanou);
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

        deautentificareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Autentificare newAutentificare = new Autentificare(null);
            }
        });

        adaugaAngajatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.sql.Date sqlDate = null;
                Integer parsedInteger = null;
                try{
                    sqlDate = java.sql.Date.valueOf(dataAngajariiText.getText());
                    parsedInteger = Integer.parseInt(salariuUtilizatorText.getText());
                }
                catch(IllegalArgumentException exc){
                    exc.printStackTrace();
                }
                adaugareAngajat(numeUtilizatorText.getText(), prenumeUtilizatorText.getText(), cnpUtilizatorText.getText(), adresaUtilizatorText.getText(), emailUtilizatorText.getText(),
                        telefonUtilizatorText.getText(), ibanUtilizatorText.getText(), sqlDate, functiaUtilizatorText.getText(), parsedInteger);
                numeUtilizatorText.setText(null);
                prenumeUtilizatorText.setText(null);
                cnpUtilizatorText.setText(null);
                adresaUtilizatorText.setText(null);
                emailUtilizatorText.setText(null);
                telefonUtilizatorText.setText(null);
                ibanUtilizatorText.setText(null);
                dataAngajariiText.setText(null);
                functiaUtilizatorText.setText(null);
                salariuUtilizatorText.setText(null);
            }
        });

        stergeAngajatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stergereAngajat(numeUtilizatorText.getText(), prenumeUtilizatorText.getText());
                numeUtilizatorText.setText(null);
                prenumeUtilizatorText.setText(null);
            }
        });

        actualizareDateAngajatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.sql.Date sqlDate = null;
                Integer parsedInteger = null;
                try{
                    sqlDate = java.sql.Date.valueOf(dataAngajariiText.getText());
                    parsedInteger = Integer.parseInt(salariuUtilizatorText.getText());
                }
                catch(IllegalArgumentException exc){
                    exc.printStackTrace();
                }
                modificareAngajat(numeUtilizatorText.getText(), prenumeUtilizatorText.getText(), cnpUtilizatorText.getText(), adresaUtilizatorText.getText(), emailUtilizatorText.getText(),
                        telefonUtilizatorText.getText(), ibanUtilizatorText.getText(), sqlDate, functiaUtilizatorText.getText(), parsedInteger);
                numeUtilizatorText.setText(null);
                prenumeUtilizatorText.setText(null);
                cnpUtilizatorText.setText(null);
                adresaUtilizatorText.setText(null);
                emailUtilizatorText.setText(null);
                telefonUtilizatorText.setText(null);
                ibanUtilizatorText.setText(null);
                dataAngajariiText.setText(null);
                functiaUtilizatorText.setText(null);
                salariuUtilizatorText.setText(null);
            }
        });

        setVisible(true);
    }

    public void adaugareAngajat(String nume, String prenume, String cnp, String adresa, String email, String telefon, String iban, Date dataAngajarii, String functia, Integer salariu){

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try(Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            try(CallableStatement callableStatement = con.prepareCall("{call AdaugaAngajat(?,?,?,?,?,?,?,?,?,?)}")){
                callableStatement.setString(1,cnp);
                callableStatement.setString(2,nume);
                callableStatement.setString(3,prenume);
                callableStatement.setString(4,adresa);
                callableStatement.setString(5,email);
                callableStatement.setString(6,telefon);
                callableStatement.setString(7,iban);
                callableStatement.setDate(8,dataAngajarii);
                callableStatement.setString(9,functia);
                callableStatement.setInt(10,salariu);

                ResultSet rs = callableStatement.executeQuery();
                con.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void stergereAngajat(String nume, String prenume){

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try(Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)){
            try(CallableStatement callableStatement = con.prepareCall("{call StergeAngajat(?,?)}")){
                callableStatement.setString(1,nume);
                callableStatement.setString(2,prenume);

                ResultSet rs = callableStatement.executeQuery();
                con.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void modificareAngajat(String nume, String prenume, String cnp, String adresa, String email, String telefon, String iban, Date dataAngajarii, String functia, Integer salariu){

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try(Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            try(CallableStatement callableStatement = con.prepareCall("{call ModificaAngajat(?,?,?,?,?,?,?,?,?,?)}")){
                callableStatement.setString(1,cnp);
                callableStatement.setString(2,nume);
                callableStatement.setString(3,prenume);
                callableStatement.setString(4,adresa);
                callableStatement.setString(5,email);
                callableStatement.setString(6,telefon);
                callableStatement.setString(7,iban);
                callableStatement.setDate(8,dataAngajarii);
                callableStatement.setString(9,functia);
                callableStatement.setInt(10,salariu);

                ResultSet rs = callableStatement.executeQuery();
                con.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }
}
