import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;

public class AnalizeForm extends JFrame{
    private JPanel analizePanel;
    private JPanel topPanel;
    private JButton logOutButton;
    private JButton inapoiButton;
    private JList<Analize> analizeList;
    private JTextField numeField;
    private JTextField prenumeField;
    private JButton creeazaProgramareButton;
    private JPanel createPanel;
    private JPanel dataPanel;
    private JDateChooser dateChooser = new JDateChooser();


    public AnalizeForm(Angajat angajat) {
        setTitle("AnalizeForm");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(500, 429));
        setSize(500, 470);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        analizeList = new JList<>(getAnalizeList());
        analizeList.setCellRenderer(new AnalizeForm.AnalizeCellRenderer());
        analizeList.setPreferredSize(new Dimension(200, 450));
        analizeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Add a ListSelectionListener to the JList
        JScrollPane scrollPane = new JScrollPane(analizeList);

        // Switch positions of detailsPanel and listaAngajati in the BorderLayout
        add(createPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        dataPanel.add(dateChooser);

        // Add ActionListener to respond to date selection
        dateChooser.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
               // selectDate(angajat);
            }
        });

        //java.util.List<Analize> newList = new ArrayList<>();
        ListSelectionModel selectionModel = analizeList.getSelectionModel();
        analizeList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Handle selection change if needed
                //newList = ;
            }
        });

        creeazaProgramareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nume = numeField.getText();
                String prenume = prenumeField.getText();
               //Date date = (Date) dateChooser.getDate();
                for(Analize analize : analizeList.getSelectedValuesList()){
                    creeazaProgramare(analize, nume, prenume);
                }

            }
        });

        inapoiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ReceptionerForm receptionerForm = new ReceptionerForm(angajat);
            }
        });

        setVisible(true);
    }

    public java.util.List<Analize> fetchAnalizeFromDatabase() {
        java.util.List<Analize> analize = new ArrayList<>();

        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {

            String query = "SELECT a.id_analize, a.tip_analize, a.pret_analize FROM analize a";

                try (PreparedStatement statement = con.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        Integer idanalize = resultSet.getInt(1);
                        String tip = resultSet.getString(2);
                        Integer pret = resultSet.getInt(3);

                        Analize analiza = new Analize();

                       analiza.setId(idanalize);
                       analiza.setTip(tip);
                       analiza.setPrice(pret);

                        analize.add(analiza);
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return analize;
    }


    public DefaultListModel<Analize> getAnalizeList() {
        DefaultListModel<Analize> analizeListModel = new DefaultListModel<>();
        java.util.List<Analize> analize = fetchAnalizeFromDatabase();
        for (Analize analiza : analize) {
            analizeListModel.addElement(analiza);
        }
        return analizeListModel;
    }

    private static class AnalizeCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Analize) {
                value = ((Analize) value).toString();
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    public void creeazaProgramare(Analize analiza, String nume, String prenume) {
        final String DB_URL = "jdbc:mysql://localhost/mydb";
        final String USERNAME = "root";
        final String PASSWORD = "mysqlpass";

        try (Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            try (CallableStatement callableStatement = con.prepareCall("{call AdaugaProgramareAnalize(?,?,?,?)}")) {
                callableStatement.setString(1, nume);
                callableStatement.setString(2, prenume);
                callableStatement.setInt(3, analiza.getId());

                // Convert java.util.Date to java.sql.Date
                java.util.Date utilDate = dateChooser.getDate();
                java.sql.Date sqlDate = utilDate != null ? new java.sql.Date(utilDate.getTime()) : null;
                callableStatement.setDate(4, sqlDate);

                ResultSet rs = callableStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


}
