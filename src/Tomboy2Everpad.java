import javax.swing.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 1-3-13
 * Time: 20:32
 * To change this template use File | Settings | File Templates.
 */
public class Tomboy2Everpad {

    public static Logger log = null;

    public static void main(String[] args) {
        try {
            FileHandler hand = new FileHandler("Tomboy2Everpad.log");
            log = Logger.getLogger("log_file");
            log.addHandler(hand);
        } catch (Exception e) {
            System.out.println("logger: " + e.getMessage());
        }

        JFrame frame = new JFrame("TomboyView");
        frame.setContentPane(new TomboyView().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
