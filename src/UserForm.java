import javax.swing.*;
import java.awt.*;

public class UserForm extends JFrame{
    private JPanel userPanou;
    private JLabel datePersonaleLabel;
    private JLabel numeLabel;
    private JLabel prenumeLabel;
    private JLabel adresaLabel;

    public UserForm(Angajat angajat){
        setTitle("UserForm");
        setContentPane(userPanou);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        numeLabel.setText("Nume:" + angajat.getNume());
        prenumeLabel.setText("Prenume:" + angajat.getPrenume());
        adresaLabel.setText("Adresa:" + angajat.getAdresa());
    }

}
