import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 1-3-13
 * Time: 20:17
 * To change this template use File | Settings | File Templates.
 */
public class TomboyView {
    protected JPanel mainPanel;

    private JTextField txtTomboyDir;
    private JButton btnLeesDir;
    private JComboBox cmbTomboyFiles;
    private JButton btnVerwerkTomboy;
    private JTextField txtEverpadTitel;
    private JButton btnOpslaan;
    private JTextArea txtTomboyTekst;
    private JTextField txtEverpadDb;
    private JTextField txtEverpadDir;
    private JEditorPane txtEverpadTekst;

    private Tomboy tomboyNote = null;
    private EverpadNotes everpad = null;

    public TomboyView() {
        txtTomboyDir.setText("/home/chris/.local/share/tomboy");
        tomboyNote = new Tomboy(txtTomboyDir.getText());

        txtEverpadDir.setText("/home/chris/.everpad");
        txtEverpadDb.setText("everpad.3.db");

        btnLeesDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String[] files = tomboyNote.leesAlleFilenamen();
                cmbTomboyFiles.removeAll();
                for (String file: files) {
                    cmbTomboyFiles.addItem(file);
                }
            }
        });

        btnVerwerkTomboy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        cmbTomboyFiles.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                try {
                    String naam = cmbTomboyFiles.getItemAt(cmbTomboyFiles.getSelectedIndex()).toString();
                    tomboyNote.leesFile(naam);
                    txtTomboyTekst.setText(tomboyNote.getNote_content());
                    txtEverpadTitel.setText(tomboyNote.getTitle());
                    txtEverpadTekst.setFont(Font.getFont("DejaVu Sans Mono"));
                    txtEverpadTekst.setText(tomboyNote.getNote_content());
                } catch (Exception e) {
                    LoggingExceptions.logException(e);
                }
            }
        });
        btnOpslaan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                everpad.schrijfNote(tomboyNote);
            }
        });
    }

}
