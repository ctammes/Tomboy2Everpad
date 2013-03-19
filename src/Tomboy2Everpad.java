import nl.ctammes.common.MijnLog;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 1-3-13
 * Time: 20:32
 * To change this template use File | Settings | File Templates.
 */
public class Tomboy2Everpad {

    // initialiseer logger
    public static Logger log = Logger.getLogger(Tomboy2Everpad.class.getName());

    public static void main(String[] args) {
        String logDir = ".";
        String logNaam = "Tomboy2Everpad.log";

        try {
            MijnLog mijnlog = new MijnLog(logDir, logNaam, true);
            log = mijnlog.getLog();
            log.setLevel(Level.INFO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        JFrame frame = new JFrame("TomboyView");
        frame.setContentPane(new TomboyView().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
