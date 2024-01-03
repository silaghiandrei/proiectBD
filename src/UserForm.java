import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserForm extends JFrame{
    private JPanel userPanou;
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

    public UserForm(Angajat angajat){
        setTitle("UserForm");
        setContentPane(userPanou);
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
        setVisible(true);
    }

}
