import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OrarLunar extends JFrame {
    private JPanel orarLuna;
    private JPanel topPanel;
    private JButton inapoiButton;
    private JPanel datePickerPanel;
    private JLabel selectDataLabel;
    private JLabel orarZiLabel;
    private JDateChooser dateChooser = new JDateChooser();

    public OrarLunar(Angajat angajat) {
        setContentPane(orarLuna);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300, 250);

        orarZiLabel.setText(null);

        datePickerPanel.add(dateChooser);

        // Add ActionListener to respond to date selection
        dateChooser.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                selectDate(angajat);
            }
        });

        inapoiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ContabilForm contabilForm = new ContabilForm(angajat);
            }
        });

        setVisible(true);
    }

    public void selectDate(Angajat angajat) {
        if (dateChooser.getDate() != null) {
            Date selectedDate = dateChooser.getDate();

            // Format the date to display it in the label
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(selectedDate);

            // Get the day of the week
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String dayOfWeekString = getDayOfWeekString(dayOfWeek);

            getSchedule(dayOfWeekString, angajat);
        }
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

    private void getSchedule(String selectedDay, Angajat angajat) {
        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String query = "SELECT idorar, startOrar, sfarsitOrar, denumirePoli " +
                    "FROM orar " +
                    "WHERE angajat_idangajat = ? AND zi = ?";

            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setInt(1, angajat.getIdangajat());
                statement.setString(2, selectedDay);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        //int idOrar = resultSet.getInt("idorar");
                        String startOrar = resultSet.getString("startOrar");
                        String sfarsitOrar = resultSet.getString("sfarsitOrar");
                        String denumirePoli = resultSet.getString("denumirePoli");

                        // Do something with the retrieved data (e.g., update labels or fields)
                        // ...

                        orarZiLabel.setText(startOrar + " - " + sfarsitOrar + " " + denumirePoli);
                        System.out.println(startOrar + " - " + sfarsitOrar + " " + denumirePoli);
                    } else {
                        // Handle the case when no data is found for the selected day
                        // ...
                        orarZiLabel.setText("Zi libera");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
