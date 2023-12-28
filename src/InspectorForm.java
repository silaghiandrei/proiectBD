import javax.swing.*;
import java.awt.*;

public class InspectorForm extends JFrame{
    private JLabel inspectorLabel;
    private JPanel inspectorPanou;

    public InspectorForm(Angajat angajat){
        setTitle("Inspector form");
        setContentPane(inspectorPanou);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
