import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProceduriForm extends JFrame{
    private JPanel proceduriPanel;
    private JPanel topPanel;
    private JButton logOutButton;
    private JButton homeButton;
    private JTextField numeText;
    private JTextField prenumeText;
    private JPanel datePanel;
    private JList<Medic> listMedic;
    private JPanel listPanel;
    private JLabel oraLabel;
    private JLabel specLabel;
    private JLabel compLabel;
    private JLabel prenumeLabel;
    private JLabel numeLabel;
    private JTextField specText;
    private JTextField compText;
    private JTextField oraText;
    private JButton cautaMediciButton;
    private JButton creeazaProgramareButton;
    private JDateChooser dateChooser = new JDateChooser();
    private Integer id;
    private Integer idspec;

    private Integer idServ;

    private static String denumirePoli;

    public ProceduriForm(Angajat angajat) {
        setTitle("ProceduriForm");
        setContentPane(proceduriPanel);
        setMinimumSize(new Dimension(600, 500));
        setSize(600, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Initialize the JList before setting the cell renderer
        listMedic = new JList<>();

        listMedic.setCellRenderer(new ProceduriForm.EmployeeCellRenderer());
        listMedic.setPreferredSize(new Dimension(200, 450));
        listMedic.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a ListSelectionListener to the JList
        JScrollPane scrollPane = new JScrollPane(listMedic);

        datePanel.add(dateChooser);
        listPanel.add(listMedic);

        dateChooser.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                selectDate();
            }
        });

        cautaMediciButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMedicList(specText.getText(), dateChooser.getDate(), Time.valueOf(oraText.getText()), compText.getText());
            }
        });

        creeazaProgramareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                creeazaProgramare(numeText.getText(), prenumeText.getText(),  dateChooser.getDate(), Time.valueOf(oraText.getText()), listMedic.getSelectedValue().getAngajat_idangajat());
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ReceptionerForm receptionerForm = new ReceptionerForm(angajat);
            }
        });

        setVisible(true);
    }


    public java.util.List<Medic> getMedics(String specialitateParameter, Date dateParameter, Time hourParameter, String competenta) {
        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        java.util.List<Medic> medics = new ArrayList<>();

        // SQL query
        String sqlQuery = "SELECT a.nume, a.prenume, o.denumirePoli, m.angajat_idangajat, m.idspecialitate, se.idserviciu  " +
                "FROM angajat a " +
                "JOIN medic m ON a.idangajat = m.angajat_idangajat " +
                "JOIN specialitate s ON m.idspecialitate = s.idspecialitate " +
                "JOIN competenta c ON s.idspecialitate = c.specialitate_idspecialitate " +
                "LEFT JOIN orar o ON a.idangajat = o.angajat_idangajat AND o.zi = ? " +                     //1 zi
                "LEFT JOIN concediu co ON a.idangajat = co.id_angajat AND ? BETWEEN co.startConcediu AND co.sfarsitConcediu " +   //2 data
                "LEFT JOIN serviciu se ON c.idcompetenta = se.competenta_idcompetenta AND c.specialitate_idspecialitate = se.competenta_specialitate_idspecialitate " +
                "WHERE a.functia = 'medic' " +
                "  AND s.idspecialitate = (SELECT idspecialitate FROM specialitate WHERE denumire = ?) " +  //3 denumire spec
                " AND c.idcompetenta = (SELECT idcompetenta FROM competenta WHERE denumire = ? AND specialitate_idspecialitate = (SELECT idspecialitate FROM specialitate WHERE denumire = ?)) " +  //4 competena 5denumire spec
                "  AND o.idorar IS NOT NULL " +
                "  AND (co.id_concediu IS NULL OR co.id_concediu IS NULL AND ? NOT BETWEEN co.startConcediu AND co.sfarsitConcediu) " +  //6 data
                "  AND EXISTS (" +
                "      SELECT 1" +
                "      FROM orar o_inner" +
                "      WHERE m.angajat_idangajat = o_inner.angajat_idangajat" +
                "        AND o_inner.zi = ?" +    //7 zi
                "         AND EXISTS (" +
                "    SELECT 1" +
                "    FROM orar o_inner" +
                "    WHERE m.angajat_idangajat = o_inner.angajat_idangajat" +
                "      AND o_inner.zi = ?" +    //8 zi
                "      AND (" +
                "        (? >= o_inner.startOrar AND ? < o_inner.sfarsitOrar) " +    //9 ora 10 ora
                "        OR" +
                "        (? + se.durata * 3600 > o_inner.startOrar AND ? + se.durata * 3600 <= o_inner.sfarsitOrar) " +   //11ora  12ora
                "      )" +
                "      )" +
                "  )" +
                "  AND NOT EXISTS (" +
                "      SELECT 1" +
                "      FROM programare p" +
                "      WHERE m.angajat_idangajat = p.medic_angajat_idangajat" +
                "        AND p.data = ?" +    //13 data
                "        AND (" +
                "        (? >= p.inceput AND ? < p.sfarsit) " +    //14
                "        OR" +
                "        (? + se.durata * 3600 > p.inceput AND ? + se.durata * 3600 <= p.sfarsit)" +
                "      )" +
                "  )";


        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            Date selectedDate = dateChooser.getDate();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String dayOfWeekString = getDayOfWeekString(dayOfWeek);


            java.sql.Date sqlDate = selectedDate != null ? new java.sql.Date(selectedDate.getTime()) : null;


            preparedStatement.setString(1, dayOfWeekString);
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.setString(3, specialitateParameter);
            preparedStatement.setString(4, competenta);
            preparedStatement.setString(5, specialitateParameter);
            preparedStatement.setDate(6, sqlDate);
            preparedStatement.setString(7, dayOfWeekString);
            preparedStatement.setString(8, dayOfWeekString);



            java.sql.Timestamp timeTimestamp = new java.sql.Timestamp(hourParameter.getTime());
            preparedStatement.setTimestamp(9, timeTimestamp);
            preparedStatement.setTimestamp(10, timeTimestamp);

            preparedStatement.setTimestamp(11, timeTimestamp);
            preparedStatement.setTimestamp(12, timeTimestamp);
            preparedStatement.setDate(13, sqlDate);
            preparedStatement.setTimestamp(14, timeTimestamp);
            preparedStatement.setTimestamp(15, timeTimestamp);
            preparedStatement.setTimestamp(16, timeTimestamp);
            preparedStatement.setTimestamp(17, timeTimestamp);

            // Execute query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                   id = resultSet.getInt("angajat_idangajat");
                   idspec = resultSet.getInt("idspecialitate");
                    idServ = resultSet.getInt("idserviciu");
                    Medic medic = new Medic();
                    medic.setNume(resultSet.getString("nume"));
                    medic.setPrenume(resultSet.getString("prenume"));
                    denumirePoli = resultSet.getString("denumirePoli");
                    medic.setAngajat_idangajat(id);
                    medic.setIdspecialitate(idspec);
                    System.out.println(medic);
                    medics.add(medic);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medics;
    }


    public DefaultListModel<Medic> getMedicList(String specialitateParameter, Date dateParameter, Time hourParameter, String competenta) {
        DefaultListModel<Medic> medicListModel = new DefaultListModel<>();
        java.util.List<Medic> medics = getMedics(specialitateParameter, dateParameter, hourParameter, competenta);
        for (Medic medic : medics) {
            System.out.println(medic);
            medicListModel.addElement(medic);
        }
        return medicListModel;
    }

    private static class EmployeeCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Medic) {
                value = ((Medic) value).toString() + ", " + denumirePoli;
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    public void selectDate() {
        if (dateChooser.getDate() != null) {
            Date selectedDate = dateChooser.getDate();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(selectedDate);
        }
    }

    private void updateMedicList(String specialitateParameter, Date dateParameter, Time hourParameter,String competenta) {
        DefaultListModel<Medic> employeeListModel = getMedicList(specialitateParameter, dateParameter, hourParameter, competenta);
        listMedic.setModel(employeeListModel);
    }
    private String getDayOfWeekString(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Duminica";
            case Calendar.MONDAY:
                return "Luni";
            case Calendar.TUESDAY:
                return "Marti";
            case Calendar.WEDNESDAY:
                return "Miercuri";
            case Calendar.THURSDAY:
                return "Joi";
            case Calendar.FRIDAY:
                return "Vineri";
            case Calendar.SATURDAY:
                return "Sambata";
            default:
                return "";
        }
    }

    public void creeazaProgramare(String nume, String prenume, Date date, Time ora, Integer medicId) {
        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        Date selectedDate = dateChooser.getDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekString = getDayOfWeekString(dayOfWeek);
        java.sql.Date sqlDate = selectedDate != null ? new java.sql.Date(selectedDate.getTime()) : null;

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            try (CallableStatement callableStatement = con.prepareCall("{call CreateProgramare(?,?,?,?,?,?)}")) {
                callableStatement.setString(1, nume);
                callableStatement.setString(2, prenume);
                callableStatement.setDate(3, sqlDate);
                callableStatement.setTime(4, ora);
                callableStatement.setInt(5, idServ);
                callableStatement.setInt(6, medicId);

                ResultSet rs = callableStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
