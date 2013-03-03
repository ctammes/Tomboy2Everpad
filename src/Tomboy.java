import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 2-3-13
 * Time: 20:28
 * To change this template use File | Settings | File Templates.
 */
public class Tomboy {
    private String tomboyDir = null;

    public Tomboy(String dir) {
        this.tomboyDir = dir;
    }

    /**
     * Geef de inhoud van een Tomboy notitie terug
     * @param file
     * @return
     */
    public HashMap<String, String> leesFile(String file) {
        file = this.tomboyDir + "/" + file;

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName(doc.getDocumentElement().getNodeName());

            String[] elems = {"title",
                            "note-content",
                            "create-date",
                            "last-change-date",
                            "last-metadata-change-date",
                            "cursor-position",
                            "selection-bound-position",
                            "width",
                            "height",
                            "x",
                            "y"};
            HashMap<String, String> tomboyInhoud = new HashMap<String, String>();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    for (String elem: elems) {
                        tomboyInhoud.put(elem, eElement.getElementsByTagName(elem).item(0).getTextContent());
                    }
                }
            }
            return tomboyInhoud;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Lees alle filenamen uit de Tomboy directory
     * @return
     */
    public String[] leesAlleFilenamen() {
        File map = new File(this.tomboyDir);
        String[] files = map.list(new FilenameFilter() {
            @Override
            public boolean accept(File map, String fileName) {
                return Pattern.matches(".*\\.note", fileName.toLowerCase());
            }
        });

        return files;

    }



}
