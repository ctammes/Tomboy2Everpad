import com.sun.xml.internal.fastinfoset.stax.util.StAXParserWrapper;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.*;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 1-3-13
 * Time: 20:44
 * To change this template use File | Settings | File Templates.
 */
public class Tomboy2EverpadTest extends TestCase{

    private String dir = null;

    public void setUp() throws Exception {
        this.dir = "/home/chris/.local/share/tomboy";

    }

    // lees tomboy notitie
    public void testLeesTomboyTekst() {
        File file = new File(this.dir + "/abeec28c-1003-4239-9b6f-c4d70ac3b673.note");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder tekst = new StringBuilder();
            String regel = reader.readLine();
            while (regel != null) {
                tekst.append(regel);
                regel = reader.readLine();
            }
            System.out.println(tekst.toString());


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // testen van inlezen xml
    public void testLeesTomboyXml() {
//        String tomboyFile = this.dir + "/abeec28c-1003-4239-9b6f-c4d70ac3b673 (kopie).note";
        String tomboyFile = this.dir + "/abeec28c-1003-4239-9b6f-c4d70ac3b673.note";

        String text = null;
        try{
            text = new Scanner( new File(tomboyFile) ).useDelimiter("\\A").next();
            text.replace("<note-content version=\"0.1\">", "<note-content version=\"0.1\"><![CDATA[");
            text.replace("</note-content></text>", "]]></note-content></text>");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        File xmlFile = new File(tomboyFile);

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(xmlFile);
            Document doc = dBuilder.parse(text);

//            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName(doc.getDocumentElement().getNodeName());

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

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
                    for (String elem: elems) {
                        System.out.format("%s : %s\n", elem, eElement.getElementsByTagName(elem).item(0).getTextContent());
                    }

                    String title = eElement.getElementsByTagName("note-content").item(0).getTextContent();
                    System.out.println("note-content: " + title);
                    Document doc1 = dBuilder.parse(title);
                    doc1.getDocumentElement().normalize();
                    System.out.println("Root element :" + doc1.getDocumentElement().getNodeName());


                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // testen van de functie
    public void testLeesTomboyNote() {
        Tomboy tomboy = new Tomboy(this.dir);

        String tomboyFile = "/dab786f7-6c22-4249-9c8e-4fd72f3bf42f.note";
        HashMap<String, String> tomboyNote = tomboy.leesFile(tomboyFile);
        Set<String> keys = tomboyNote.keySet();
        for (String key: keys) {
            System.out.format("%s : %s\n", key, tomboyNote.get(key));
        }
    }


    // lees een Everpad notitie
    public void testEverpadLees() {
        String dir = "/home/chris/.everpad";
        String db = "everpad.3.db";
        EverpadNotes notes = new EverpadNotes(dir, db);
        if (notes.openDb()) {
            try{
                ResultSet rs = notes.leesNote(38L);
                while (rs.next()) {
                    System.out.println(rs.getString("title"));
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        notes.sluitDb();

    }

    // voorbeeld berekeningen
    public void testTomboyDatum() {
        Date nu = new Date();
        System.out.println("nu: "+nu.toString());

        long timestamp = System.currentTimeMillis()/1000;
        System.out.println("nu unix timestamp: " + timestamp);

        Date time=new java.util.Date(timestamp*1000);
        System.out.println("terug converteren: " + time);

        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String tomboyDatum = "2013-02-24T14:26:27.4496230+01:00";
            System.out.println("tomboy datum1: " + tomboyDatum);
            tomboyDatum = tomboyDatum.substring(0,19).replace("T"," ");
            System.out.println("tomboy datum2: " + tomboyDatum);
            Date parsedDate = dateFormat.parse(tomboyDatum);
            System.out.println(new java.sql.Timestamp(parsedDate.getTime()));
            timestamp = parsedDate.getTime();
            System.out.println("Everpad date: "+ timestamp);

            time=new Date(timestamp);
            System.out.println("Everpad datum: " + time);
            System.out.println(dateFormat.format(time));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

    }

    // 'serieuze' test via EverpadNotes class
    public void testEverpadDatum() {

        EverpadNotes notes = new EverpadNotes("", "");

        String format = "yyyy-MM-dd HH:mm:ss";
        String datum = "2013-02-24T14:26:27.4496230+01:00";
        long everpadDatum = notes.datumNaarEverpaddatum(datum, format);
        System.out.println("Everpad timestamp: " + everpadDatum);

        String dat = notes.everpaddatumNaarDatum(everpadDatum, format);
        System.out.println("Everpad datum: " + dat);

    }

    public void testLeesTomboyFilenamen() {
        Tomboy tomboy = new Tomboy(this.dir);
        String[] files = tomboy.leesAlleFilenamen();
        System.out.println(files.length + " files gevonden:");
        for (String file: files) {
            System.out.format("File: %s\n", file);
        }
    }
}
