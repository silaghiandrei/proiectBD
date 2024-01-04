import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JTextField textField1;
    private JTextField textField2;
    private JLabel prenumeCautareabel;
    private JTextField textField3;
    private JLabel functieCautareLabel;
    private JButton cautareAngajat;

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

        deautentificareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Autentificare newAutentificare = new Autentificare(null);
            }
        });
    }
}
