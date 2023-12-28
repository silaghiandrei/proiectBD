import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Autentificare extends JDialog{
    private JTextField usernameText;
    private JLabel autentificare;
    private JLabel username;
    private JPasswordField passwordField;
    private JLabel password;
    private JButton confirmareButton;
    private JButton iesireButton;
    private JPanel autentificarePanou;

    public Utilizator user;

    public Angajat angajat;

    public Autentificare(JFrame parent){
        super(parent);
        setTitle("Autentificare");
        setContentPane(autentificarePanou);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        confirmareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameText.getText();
                String password = String.valueOf(passwordField.getPassword());

                user = getAuthenticatedUser(username, password);

                if(user != null){
                    angajat = getAngajatInfo(user);
                    if(angajat != null && "medic".equalsIgnoreCase(angajat.functia)){
                        openMedicForm();
                        dispose();
                    }
                    if(angajat != null && "inspector resurse umane".equalsIgnoreCase(angajat.functia)){
                        openInspectorForm();
                        dispose();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(Autentificare.this,
                            "Nume de utilizator sau parola incorecte", "Reincercati", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        iesireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    public Utilizator getAuthenticatedUser(String username, String password){
        Utilizator user = null;

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try{
            Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM utilizator WHERE nume_utilizator=? AND parola=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                user = new Utilizator();
                user.numeUtilizator = rs.getString("nume_utilizator");
                user.parola = rs.getString("parola");
                user.angajat_idangajat = rs.getInt("angajat_idangajat");
            }

            stmt.close();
            con.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        return user;
    }

    public Angajat getAngajatInfo(Utilizator user){
        Angajat angajat = null;

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM angajat WHERE idangajat = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                preparedStatement.setInt(1, user.angajat_idangajat);

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        angajat = new Angajat();
                        angajat.idangajat = rs.getInt("idangajat");
                        angajat.cnp = rs.getString("cnp");
                        angajat.nume = rs.getString("nume");
                        angajat.prenume = rs.getString("prenume");
                        angajat.adresa = rs.getString("adresa");
                        angajat.email = rs.getString("email");
                        angajat.telefon = rs.getString("telefon");
                        angajat.iban = rs.getString("iban");
                        angajat.dataAngajarii = rs.getDate("dataAngajarii");
                        angajat.functia = rs.getString("functia");
                        angajat.salariu = rs.getInt("salariu");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return angajat;
    }

    public void openMedicForm(){
        SwingUtilities.invokeLater(()->{
            MedicForm medicForm = new MedicForm();
            medicForm.setVisible(true);
        });
    }

    public void openInspectorForm(){
        SwingUtilities.invokeLater(()->{
            InspectorForm inspectorForm = new InspectorForm();
            inspectorForm.setVisible(true);
        });
    }
    public static void main(String[] args)
    {
        Autentificare autentificare = new Autentificare(null);
        Utilizator user = autentificare.user;

        if(user != null){
            System.out.println("Autentificare reusita");
        }
        else{
            System.out.println("Autentificare esuata");
        }
    }
}
