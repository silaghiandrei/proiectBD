import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdministratorForm extends JFrame{

    private JPanel mainPanel;
    private JPanel bottomPanel;
    private JPanel topPanel;
    private JButton logOutButton;
    private JButton homeButton;
    private JButton addButton;
    private JButton updateButton;
    private JButton datePersButton;

    public AdministratorForm(Angajat angajat){

        setTitle("AdministratorForm");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(450, 429));
        setSize(450, 470);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //homeButton.setFocusPainted(false);
        logOutButton.setFocusPainted(false);
        addButton.setFocusPainted(false);
        updateButton.setFocusPainted(false);
        datePersButton.setFocusPainted(false);
        //homeButton.setBorderPainted(false);
        logOutButton.setBorderPainted(false);
        addButton.setBorderPainted(false);
        updateButton.setBorderPainted(false);
        datePersButton.setBorderPainted(false);

        bottomPanel.setVisible(true);


        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Autentificare newAutentificare = new Autentificare(null);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                openAdaugaAngajatForm(angajat);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                openModificaAngajatForm(angajat);
            }
        });

        datePersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                openDetaliiForm(angajat);
            }
        });

        setVisible(true);
    }

    public void openAdaugaAngajatForm(Angajat angajat){
        SwingUtilities.invokeLater(()->{
            AdaugaAngajatForm adaugaAngajatForm = new AdaugaAngajatForm(angajat);
            adaugaAngajatForm.setVisible(true);
        });
    }

    public void openModificaAngajatForm(Angajat angajat){
        SwingUtilities.invokeLater(()->{
            ModificaAngajatForm modificaAngajatForm = new ModificaAngajatForm(angajat);
            modificaAngajatForm.setVisible(true);
        });
    }

    public void openDetaliiForm(Angajat angajat){
        SwingUtilities.invokeLater(()->{
            DetaliiAngajat detaliiAngajat = new DetaliiAngajat(angajat);
            detaliiAngajat.setVisible(true);
        });
    }
}
