import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class ReceptionerForm extends JFrame{
    private JPanel receptionerPanel;
    private JPanel topPanel;
    private JButton logOutButton;
    private JButton analizeButton;
    private JButton proceduriButton;
    private JTextField numeText;
    private JButton inregistreazaPacientButton;
    private JTextField prenumeText;
    private JTextField cnpText;
    private JTextField varstaText;
    private JTextField sexText;
    private JTextField phoneText;
    private JList<Programare> progList;
    private JButton bonButton;
    private JButton progButton;
    private JPanel panelList;
    private static String numeMed;
    private static String prenumeMed;
    private static Date dataPro;
    private static Integer id_medic;
    private static Integer id_pacient;
    private  static Integer id_programare;
    private static   Integer pretProg;

    public ReceptionerForm(Angajat angajat) {

        setTitle("ReceptionerForm");
        setContentPane(receptionerPanel);
        setMinimumSize(new Dimension(600, 330));
        setSize(700, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        progList = new JList<>();
        progList.setCellRenderer(new ReceptionerForm.ProgramariCellRenderer());
        progList.setPreferredSize(new Dimension(200, 450));
        progList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panelList.add(progList);

        analizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAnalizeForm(angajat);
            }
        });

        proceduriButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProceduriForm(angajat);
            }
        });

        inregistreazaPacientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerPacient(numeText.getText(), prenumeText.getText(), cnpText.getText(), Integer.parseInt(varstaText.getText()), sexText.getText(), phoneText.getText());
            }
        });

       progButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               updateProgramareList(numeText.getText(), prenumeText.getText());
           }
       });

       bonButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               Programare programare = progList.getSelectedValue();
               elibereazaBon((Date) programare.getData(), programare.getPret(), programare.getId_programare(), programare.getId_medic(), programare.getId_pacient());
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

    public void openAnalizeForm(Angajat angajat){
        SwingUtilities.invokeLater(()->{
            AnalizeForm analizeForm = new AnalizeForm(angajat);
            analizeForm.setVisible(true);
        });
    }

    public void openProceduriForm(Angajat angajat){
        SwingUtilities.invokeLater(()->{
            ProceduriForm proceduriForm = new ProceduriForm(angajat);
            proceduriForm.setVisible(true);
        });
    }

    public void registerPacient(String nume, String prenume, String cnp, Integer varsta, String sex, String telefon){

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            try (CallableStatement callableStatement = con.prepareCall("{call UpdatePacient(?,?,?,?,?,?)}")) {
                callableStatement.setString(1, nume);
                callableStatement.setString(2, prenume);
                callableStatement.setString(3, cnp);
                callableStatement.setInt(4, varsta);
                callableStatement.setString(5, sex);
                callableStatement.setString(6, telefon);

                // Convert java.util.Date to java.sql.Date

                ResultSet rs = callableStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public java.util.List<Programare> getProgramare(String nume, String prenume) {

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        java.util.List<Programare> programare = new ArrayList<>();

        String sqlQuery = "SELECT p.nume AS pacient_nume, p.prenume AS pacient_prenume, a_medic.nume AS medic_nume, a_medic.prenume AS medic_prenume, pr.data, s.pret, pr.medic_angajat_idangajat, pr.pacient_idpacient, pr.idprogramare  " +
                "FROM programare pr " +
                "JOIN pacient p ON pr.pacient_idpacient = p.idpacient " +
                "JOIN medic m ON pr.medic_angajat_idangajat = m.angajat_idangajat " +
                "JOIN angajat a_medic ON m.angajat_idangajat = a_medic.idangajat " +
                "LEFT JOIN serviciu s ON pr.serviciu_idserviciu = s.idserviciu " +
                "WHERE" +
                "    pr.data = CURDATE()" +
                "    AND p.nume = ? " +
                "    AND p.prenume = ? ";



        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, nume);
            preparedStatement.setString(2, prenume);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String pacNume = resultSet.getString("pacient_nume");
                    String pacPrenume = resultSet.getString("pacient_prenume");
                    numeMed = resultSet.getString("medic_nume");
                    prenumeMed = resultSet.getString("medic_prenume");
                    dataPro = resultSet.getDate("data");
                    pretProg = resultSet.getInt("pret");
                    id_medic = resultSet.getInt("medic_angajat_idangajat");
                    id_pacient = resultSet.getInt("pacient_idpacient");
                    id_programare = resultSet.getInt("idprogramare");

                    Programare programare1 = new Programare();

                    programare1.setNumePac(pacNume);
                    programare1.setPrenumePac(pacPrenume);
                    programare1.setNumeMed(numeMed);
                    programare1.setPrenumeMed(prenumeMed);
                    programare1.setData(dataPro);
                    programare1.setPret(pretProg);
                    programare1.setId_programare(id_programare);
                    programare1.setId_medic(id_medic);
                    programare1.setId_pacient(id_pacient);

                    System.out.println(programare1 + " " + id_programare +" " + id_medic + " " + id_pacient);
                    programare.add(programare1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return programare;
    }
    private DefaultListModel<Programare> getProgramareList(String nume, String prenume) {
        DefaultListModel<Programare> programareListModel = new DefaultListModel<>();
        java.util.List<Programare> programares = getProgramare(nume, prenume);
        for (Programare programare : programares) {
            System.out.println(programare);
            programareListModel.addElement(programare);
        }
        return programareListModel;
    }

    private static class ProgramariCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Programare) {
                value = ((Programare) value).toString();
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    private void updateProgramareList(String nume, String prenume) {
        DefaultListModel<Programare> programareListModel = getProgramareList(nume, prenume);
        System.out.println("Size of programareListModel: " + programareListModel.size()); // Check size
        System.out.println("Content of programareListModel: " + programareListModel); // Check content
        progList.setModel(programareListModel);
        progList.revalidate();
        progList.repaint();
    }

    public void elibereazaBon(Date data, Integer pret, Integer idProg, Integer idMedic, Integer idPacient) {

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD))  {
            String storedProcedureCall = "{call InsertBonFiscal(?,?,?,?,?)}";
            try (PreparedStatement preparedStatement = con.prepareStatement(storedProcedureCall)) {

                preparedStatement.setDate(1, data);
                preparedStatement.setInt(2, pret);
                preparedStatement.setInt(3, idProg);
                preparedStatement.setInt(4, idMedic);
                preparedStatement.setInt(5, idPacient);

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
