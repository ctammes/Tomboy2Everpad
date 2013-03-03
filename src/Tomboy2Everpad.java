import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 1-3-13
 * Time: 20:32
 * To change this template use File | Settings | File Templates.
 */
public class Tomboy2Everpad {

    public static void main(String[] args) {
        JFrame frame = new JFrame("TomboyView");
        frame.setContentPane(new TomboyView().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
