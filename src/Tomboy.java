import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Scanner;
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

    private String filenaam = null;     // misschien gebruiken voor guid bij Everpad?
    private String title = null;
    private String note_content = null;
    private String create_date = null;
    private String last_change_date = null;
    private String last_metadata_change_date = null;
    private int cursor_position = 0;
    private int selection_bound_position = 0;
    private int width = 0;
    private int height = 0;
    private int x = 0;
    private int y = 0;

    public String getFilenaam() {
        return filenaam;
    }

    public String getTitle() {
        return title;
    }

    public String getNote_content() {
        return note_content;
    }

    public String getCreate_date() {
        return create_date;
    }

    public String getLast_change_date() {
        return last_change_date;
    }

    public String getLast_metadata_change_date() {
        return last_metadata_change_date;
    }

    public int getCursor_position() {
        return cursor_position;
    }

    public int getSelection_bound_position() {
        return selection_bound_position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Geef de inhoud van een Tomboy notitie terug
     * @param file
     * @return
     */
    public void leesFile(String file) {
        file = this.tomboyDir + "/" + file;

        String text = null;
        ByteArrayInputStream text1 = null;
        try{
            text = new Scanner( new File(file) ).useDelimiter("\\A").next();
            // hiermee bewaar je de html tags in de tekst
            text = text.replaceFirst("(<note-content version=\".+\">)", "$1<![CDATA[");
            text = text.replace("</note-content></text>", "]]></note-content></text>");

            // belangrijk!!
            text1 = new ByteArrayInputStream(text.getBytes("UTF8"));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(text1);

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

            filenaam = file;
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    for (String elem: elems) {
                        String tekst = eElement.getElementsByTagName(elem).item(0).getTextContent();
                        if (elem == "title") {
                            title = tekst;
                        } else if (elem == "note-content") {
                            tekst = vertaal(tekst);
                            System.out.println(file + ": " + tekst);
                            note_content = tekst;
                        } else if (elem == "create-date") {
                            create_date = tekst;
                        } else if (elem == "last-change-date") {
                            last_change_date = tekst;
                        } else if (elem == "last-metadata-change-date") {
                            last_metadata_change_date = tekst;
                        } else if (elem == "cursor-position") {
                            cursor_position = Integer.parseInt(tekst);
                        } else if (elem == "selection-bound-position") {
                            selection_bound_position = Integer.parseInt(tekst);
                        } else if (elem == "width") {
                            width = Integer.parseInt(tekst);
                        } else if (elem == "height") {
                            height= Integer.parseInt(tekst);
                        } else if (elem == "x") {
                            x =Integer.parseInt(tekst);
                        } else if (elem == "y") {
                            y = Integer.parseInt(tekst);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception - " + file + ": " + e.getMessage());
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

    /**
     * Vertaal Tomboy tags naar standaard HTML
     * @param tekst
     * @return
     */
    private String vertaal(String tekst) {
        tekst = tekst.replaceAll(" (?= )|(?<= ) ","&nbsp;");    // vervang aaneengesloten spaties
        tekst = tekst.replace("\"", "&quot;");
        tekst = tekst.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        tekst = tekst.replace("bold>", "b>");
        tekst = tekst.replace("italic>", "i>");
        tekst = tekst.replace("strikethrough>", "del>");
        tekst = tekst.replace("monospace>", "tt>");
        tekst = tekst.replace("list>", "ul>");
        tekst = tekst.replace("list-item>", "li>");
        tekst = tekst.replace("\n", "<br />");

        return tekst;
    }



}

