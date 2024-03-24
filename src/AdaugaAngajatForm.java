            import javax.swing.*;
            import java.awt.*;
            import java.awt.event.ActionEvent;
            import java.awt.event.ActionListener;
            import java.sql.*;

            public class AdaugaAngajatForm extends JFrame{
                private JPanel addPanel;
                private JPanel topPanel;
                private JButton homeButton;
                private JLabel numeLabel;
                private JTextField numeText;
                private JLabel prenumeLabel;
                private JTextField prenumeText;
                private JLabel cnpLabel;
                private JLabel adresaLabel;
                private JLabel emailLabel;
                private JLabel telefonLabel;
                private JLabel ibanLabel;
                private JLabel dataAngLabel;
                private JLabel functieLabel;
                private JLabel salariuLabel;
                private JLabel nrOreLabel;
                private JTextField cnpText;
                private JTextField adresaText;
                private JTextField emailText;
                private JTextField telefonText;
                private JTextField ibanText;
                private JTextField dataAngText;
                private JTextField functiaText;
                private JTextField salariuText;
                private JTextField nrOreText;
                private JButton adaugareButton;

                public AdaugaAngajatForm(Angajat angajat){

                    setTitle("AdaugaAngajatForm");
                    setContentPane(addPanel);
                    setMinimumSize(new Dimension(450, 429));
                    setSize(500, 500);
                    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                    homeButton.setFocusPainted(false);
                    homeButton.setBorderPainted(false);

                    adaugareButton.setFocusPainted(false);
                    adaugareButton.setBorderPainted(false);

                    homeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dispose();
                            AdministratorForm newAdmin = new AdministratorForm(angajat);
                        }
                    });

                    adaugareButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            java.sql.Date sqlDate = null;
                            Integer parsedInteger = null;
                            Integer parsedInteger2 = null;
                            try{
                                sqlDate = java.sql.Date.valueOf(dataAngText.getText());
                                parsedInteger = Integer.parseInt(salariuText.getText());
                                parsedInteger2 = Integer.parseInt(nrOreText.getText());
                            }
                            catch(IllegalArgumentException exc){
                                exc.printStackTrace();
                            }
                            adaugareAngajat(numeText.getText(), prenumeText.getText(), cnpText.getText(), adresaText.getText(), emailText.getText(),
                                    telefonText.getText(), ibanText.getText(), sqlDate, functiaText.getText(), parsedInteger, parsedInteger2);

                            if(functiaText.getText().equals("medic")){
                                openAdaugaMedicForm();
                            }
                            if(functiaText.getText().equals("asistent medical")){
                                openAdaugaAsistentForm();
                            }

                            numeText.setText(null);
                            prenumeText.setText(null);
                            cnpText.setText(null);
                            adresaText.setText(null);
                            emailText.setText(null);
                            telefonText.setText(null);
                            ibanText.setText(null);
                            dataAngText.setText(null);
                            functiaText.setText(null);
                            salariuText.setText(null);
                            nrOreText.setText(null);
                        }
                    });

                    setVisible(true);
                }

                public void adaugareAngajat(String nume, String prenume, String cnp, String adresa, String email, String telefon, String iban, Date dataAngajarii, String functia, Integer salariu, Integer nrOre){

                    final String DB_URL = "jdbc:mysql://localhost/mydb";
                    final String USERNAME = "root";
                    final String PASSWORD = "mysqlpass";

                    try(Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                        try(CallableStatement callableStatement = con.prepareCall("{call AdaugaAngajat(?,?,?,?,?,?,?,?,?,?,?)}")){
                            callableStatement.setString(1,cnp);
                            callableStatement.setString(2,nume);
                            callableStatement.setString(3,prenume);
                            callableStatement.setString(4,adresa);
                            callableStatement.setString(5,email);
                            callableStatement.setString(6,telefon);
                            callableStatement.setString(7,iban);
                            callableStatement.setDate(8,dataAngajarii);
                            callableStatement.setString(9,functia);
                            callableStatement.setInt(10,salariu);
                            callableStatement.setInt(11,nrOre);

                            ResultSet rs = callableStatement.executeQuery();
                            con.close();
                        }
                        catch(SQLException e){
                            e.printStackTrace();
                        }
                    }
                    catch(SQLException ex){
                        ex.printStackTrace();
                    }
                }

                public void openAdaugaMedicForm(){
                    SwingUtilities.invokeLater(()->{
                        AdaugaMedic adaugaMedic = new AdaugaMedic();
                        adaugaMedic.setVisible(true);
                    });
                }

                public void openAdaugaAsistentForm(){
                    SwingUtilities.invokeLater(()->{
                        AdaugaAsistent adaugaAsistent = new AdaugaAsistent();
                        adaugaAsistent.setVisible(true);
                    });
                }

                public static void main(String[] args) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            // Assuming you have a default constructor or modify as needed
                            AdaugaAngajatForm form = new AdaugaAngajatForm(new Angajat());
                            form.setVisible(true);
                        }
                    });
                }
            }
