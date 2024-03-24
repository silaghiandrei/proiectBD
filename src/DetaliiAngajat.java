import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DetaliiAngajat extends JFrame {

    private JPanel topPanel;
    private JButton logOutButton;
    private JButton homeButton;
    private JLabel numeLabel;
    private JLabel numeLabel1;
    private JLabel prenumeLabel;
    private JLabel cnpLabel;
    private JLabel prenumeLabel1;
    private JLabel adresaLabel;
    private JLabel cnpLabel1;
    private JLabel adresaLabel1;
    private JLabel emailLabel;
    private JLabel emailLabel1;
    private JLabel telefonLabel1;
    private JLabel telefonLabel;
    private JLabel ibanLable;
    private JLabel ibanLabel1;
    private JLabel dataLabe;
    private JLabel dataLabel1;
    private JLabel functiaLabel;
    private JLabel functiaLabel1;
    private JLabel salariuLabel;
    private JLabel salariuLabel1;
    private JLabel nrOreLabel;
    private JLabel nrOreLabel1;
    private JPanel detailsPanel;

    public DetaliiAngajat(Angajat angajat){
        setTitle("AdaugaAngajatForm");
        setContentPane(detailsPanel);
        setMinimumSize(new Dimension(500, 450));
        setSize(500, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        numeLabel1.setText(angajat.getNume());
        prenumeLabel1.setText(angajat.getPrenume());
        cnpLabel1.setText(angajat.getCnp());
        adresaLabel1.setText(angajat.getAdresa());
        emailLabel1.setText(angajat.getEmail());
        telefonLabel1.setText(angajat.getTelefon());
        ibanLabel1.setText(angajat.getIban());
        dataLabel1.setText(String.valueOf(angajat.getDataAngajarii()));
        functiaLabel1.setText(angajat.getFunctia());
        salariuLabel1.setText(String.valueOf(angajat.getSalariu()));
        nrOreLabel1.setText(String.valueOf(angajat.getNrOre()));

        homeButton.setFocusPainted(false);
        homeButton.setBorderPainted(false);

        logOutButton.setFocusPainted(false);
        logOutButton.setBorderPainted(false);

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if(angajat.getFunctia().equals("administrator")){
                    AdministratorForm newAdmin = new AdministratorForm(angajat);
                }
                if(angajat.getFunctia().equals("inspector resurse umane")){
                    InspectorForm2 newInspector = new InspectorForm2(angajat);
                }
                if(angajat.getFunctia().equals("expert financiar contabil")){
                    ContabilForm newContabil = new ContabilForm(angajat);
                }
                if(angajat.getFunctia().equals("medic")){
                    MedicForm newMedic = new MedicForm(angajat);
                }
                if(angajat.getFunctia().equals("asistent medical")){
                    AsistentForm newAsistent = new AsistentForm(angajat);
                }
                if(angajat.getFunctia().equals("receptioner")){
                    ReceptionerForm newReceptioner = new ReceptionerForm(angajat);
                }
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Autentificare autentificare = new Autentificare(null);
            }
        });

        setVisible(true);
    }
}
