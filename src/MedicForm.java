import javax.swing.*;
import java.awt.*;

public class MedicForm extends JFrame{
    private JLabel medicLabel;
    private JPanel medicPanou;

    public MedicForm(){
        setTitle("MedicForm");
        setContentPane(medicPanou);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
