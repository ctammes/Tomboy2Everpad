import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

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
    private JButton btnOpenDb;
    private JButton btnFileChooser;

    private Tomboy tomboyNote = null;
    private EverpadNotes everpad = null;

    public TomboyView() {
        txtTomboyDir.setText("/home/chris/.local/share/tomboy");
        tomboyNote = new Tomboy(txtTomboyDir.getText());

        //TODO aantal voorafgaande keuzes opslaan
        txtEverpadDir.setText("/home/chris/.everpad");  // dit is de echte
        txtEverpadDir.setText("/home/chris/IdeaProjects/java/Tomboy2Everpad");
        txtEverpadDb.setText("everpad.5.db");   // everpad.3.db is de oude versie, zonder share-kolommen

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
                    Tomboy2Everpad.log.severe(e.getMessage());
                }
            }
        });
        btnOpslaan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (everpad != null) {
                    if (everpad.zoekTitel(tomboyNote.getTitle()) == 0) {
                        everpad.schrijfNote(tomboyNote);
                    } else {
                        JOptionPane.showMessageDialog(null, "Record met deze titel bestaat al!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Database niet geopend!", "Fout", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        btnOpenDb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (everpad == null) {
                    everpad = new EverpadNotes(txtEverpadDir.getText(), txtEverpadDb.getText());
                    btnOpenDb.setText("SluitDb");
                } else {
                    everpad.sluitDb();
                    btnOpenDb.setText("OpenDb");
                    everpad = null;
                }
            }
        });

        btnFileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File(txtEverpadDir.getText()));
                fc.setDialogTitle("Selecteer Everpad database directory");
                fc.setDialogType(JFileChooser.OPEN_DIALOG);
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    if (fc.getFileSelectionMode() == JFileChooser.DIRECTORIES_ONLY) {
                        txtEverpadDir.setText(fc.getSelectedFile().toString());     // bij DIRECTORIES_ONLY
                    } else {
                        txtEverpadDir.setText(fc.getCurrentDirectory().toString()); // zonder DIRECTORIES_ONLY
                    }
                }
                else {
                    Tomboy2Everpad.log.info("No Selection ");
                }
            }
        });
    }

}
