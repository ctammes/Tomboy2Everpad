import junit.framework.Assert;
import nl.ctammes.common.MijnLog;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 1-3-13
 * Time: 20:44
 * To change this template use File | Settings | File Templates.
 */
public class Tomboy2EverpadTest {

    private static String dir = null;
    private static String everpadDir = null;
    private static String everpadDb = null;

    static String logDir = "./tests";
    static String logNaam = "test.log";
    static Logger log = null;

    @BeforeClass
    public static void setUp() throws Exception {
        dir = "/home/chris/.local/share/tomboy";
        everpadDir = "/home/chris/IdeaProjects/java/Tomboy2Everpad";
        everpadDb = "everpad.5.db";

        if (log == null) {
            try {
                MijnLog mijnlog = new MijnLog(logDir, logNaam, true);
                log = mijnlog.getLog();
                log.setLevel(Level.INFO);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    // lees tomboy notitie
    @Test
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
    @Test
    public void testLeesTomboyXml() {
//        String tomboyFile = this.dir + "/abeec28c-1003-4239-9b6f-c4d70ac3b673 (kopie).note";
        String tomboyFile = this.dir + "/abeec28c-1003-4239-9b6f-c4d70ac3b673.note";

        String text = null;
        try{
            text = new Scanner( new File(tomboyFile) ).useDelimiter("\\A").next();
            text = text.substring(text.indexOf("<note"));
            text = text.replace("<note-content version=\"0.1\">", "<note-content version=\"0.1\"><![CDATA[");
            text = text.replace("</note-content></text>", "]]></note-content></text>");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

//        File xmlFile = new File(tomboyFile);

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(xmlFile);

            Document doc = dBuilder.parse(new InputSource(new StringReader(text)));

            doc.getDocumentElement().normalize();

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
//                        if (elem == "note-content") {
//                            System.out.println(eElement.getChildNodes().toString());
//                        }
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


    /**
     * Dit zou de definitieve functie moeten zijn
     */
    @Test
    public void testXmlFunctie() {
        String tomboyFile = this.dir + "/abeec28c-1003-4239-9b6f-c4d70ac3b673.note";

        String text = null;
        ByteArrayInputStream text1 = null;
        try{
            text = new Scanner( new File(tomboyFile)).useDelimiter("\\A").next();
            text = text.replace("<note-content version=\"0.1\">", "<note-content version=\"0.1\"><![CDATA[");
            text = text.replace("</note-content></text>", "]]></note-content></text>");
            text1 = new ByteArrayInputStream(text.getBytes("UTF8"));

        } catch (Exception e) {
            System.out.println("1: "+ e.getMessage());
        }

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(text1);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName(doc.getDocumentElement().getNodeName());

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

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
                        String tekst = eElement.getElementsByTagName(elem).item(0).getTextContent();
                        if (elem == "note-content") {
                            tekst = vertaal(tekst);
                        }
                        System.out.format("%s : %s\n", elem, tekst);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("2: " + e.getMessage() );
        }
    }


    /**
     * Vertaal Tomboy tags naar standaard HTML
     * @param tekst
     * @return
     */
    private String vertaal(String tekst) {
        Pattern pat = Pattern.compile(" (?= )|(?<= ) ");
        tekst = tekst.replaceAll(" (?= )|(?<= ) ","&nbsp;");
//        tekst = tekst.replace("\b{2,}", "&nbsp;");   // eigenlijk alleen indien meer dan 1 space
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


    // werkt zo niet
    @Test
    public void testXml() {
        String xml = null;

        xml = "<version>\r\n" + //
                "    <name>2.0.2</name>\r\n" + //
                "    <description>\r\n" + //
                "-Stop hsql database after close fist <br />\r\n" + //
                "-Check for null category name before adding it to the categories list  <br />\r\n" + //
                "-Fix NPE bug if there is no updates  <br />\r\n" + //
                "-add default value for variable, change read bytes filter, and description of propertyFile  <br />\r\n" + //
                "-Change HTTP web Proxy (the “qcProxy” field ) to http://web-proxy.isr.hp.com:8080  <br />\r\n" + //
                "</description>\r\n" + //
                "    <fromversion>>=2.0</fromversion>\r\n" + //
                "</version>";

        xml = "<note version=\"0.3\" xmlns:link=\"http://beatniksoftware.com/tomboy/link\" xmlns:size=\"http://beatniksoftware.com/tomboy/size\" xmlns=\"http://beatniksoftware.com/tomboy\">\n" +
                "  <title>Partitie aankoppelen</title>\n" +
                "  <text xml:space=\"preserve\"><note-content version=\"0.1\">Partitie aankoppelen\n" +
                "\n" +
                "<bold>Toevoegen van de oude homedir:</bold>\n" +
                "\n" +
                "<italic>In terminal</italic>:\n" +
                "<monospace>sudo mkdir <link:url>/media/home_oud</link:url></monospace><monospace>\n" +
                "sudo chown -R chris:chris <link:url>/media/home_oud</link:url></monospace><monospace>\n" +
                "sudo chmod -R 755 <link:url>/media/home_oud</link:url></monospace>\n" +
                "\n" +
                "<italic>In <link:url>/etc/fstab</link:url></italic><italic> (voor <link:url>/dev/sdb4)<bold>:</bold></link:url></italic><bold /><bold>\n" +
                "</bold>UUID=08aa3d19-1e6f-4415-b690-c0d8ff0f9033    <link:url>/media/home_oud</link:url>    auto    defaults    0     2\n" +
                "\n" +
                "<italic>Permanente koppeling (symlink) maken (let op laatste backslash!!)</italic>\n" +
                "<monospace>mkdir <link:url>~/chris1</link:url></monospace><monospace>\n" +
                "ln -s <link:url>/media/home_oud/chris/</link:url></monospace><monospace> <link:url>~/chris1</link:url></monospace>\n" +
                "\n" +
                "<monospace>chris@chris-HP-Compaq-dc7900:~/chris1$ ls -l\n" +
                "totaal 0\n" +
                "lrwxrwxrwx 1 chris chris 22 dec 25 11:59 chris -&gt; <link:url>/media/home_oud/chris/</link:url></monospace>\n" +
                "\n" +
                "<italic>Symlink verwijderen (vanuit chris1):</italic>\n" +
                "<monospace>rm chris</monospace><strikethrough>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "</strikethrough><italic>Oorspronkelijke fstab:</italic>\n" +
                "# &lt;file system&gt; &lt;mount point&gt;   &lt;type&gt;  &lt;options&gt;       &lt;dump&gt;  &lt;pass&gt;\n" +
                "proc            /proc           proc    nodev,noexec,nosuid 0       0\n" +
                "# / was on <link:url>/dev/sda5</link:url> during installation\n" +
                "UUID=83d2e014-4525-42e6-baa6-e17382b3a837 /               ext4    errors=remount-ro 0       1\n" +
                "# /home was on <link:url>/dev/sda3</link:url> during installation\n" +
                "UUID=4534abe2-bc5d-4745-92cb-cd197264b698 /home           ext4    defaults        0       2\n" +
                "# swap was on <link:url>/dev/sda6</link:url> during installation\n" +
                "UUID=425946c9-6208-47e3-8caa-d07d0a67d16f none            swap    sw              0       0\n" +
                "\n" +
                "<italic>regel toegevoegd:</italic>\n" +
                "# oude home partitie na toevoegen oude harddisk\n" +
                "UUID=08aa3d19-1e6f-4415-b690-c0d8ff0f9033 <link:url>/media/home_oud</link:url>   auto    defaults    0     2\n" +
                "</note-content></text>\n" +
                "  <last-change-date>2012-12-25T12:03:42.7522200+01:00</last-change-date>\n" +
                "  <last-metadata-change-date>2013-01-20T20:50:29.0067230+01:00</last-metadata-change-date>\n" +
                "  <create-date>2012-12-25T11:21:36.4312910+01:00</create-date>\n" +
                "  <cursor-position>295</cursor-position>\n" +
                "  <selection-bound-position>295</selection-bound-position>\n" +
                "  <width>742</width>\n" +
                "  <height>360</height>\n" +
                "  <x>582</x>\n" +
                "  <y>129</y>\n" +
                "  <open-on-startup>False</open-on-startup>\n" +
                "</note>";

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes());
            Document doc = docBuilder.parse(bis);

            // XPath to retrieve the <version>/<description> tag
            XPath xpath = XPathFactory.newInstance().newXPath();
//            XPathExpression expr = xpath.compile("/version/description");
            XPathExpression expr = xpath.compile("/note/@version");
            Node descriptionNode = (Node) expr.evaluate(doc, XPathConstants.NODE);

            // Transformer to convert the XML Node to String equivalent
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(descriptionNode), new StreamResult(sw));
//            String description = sw.getBuffer().toString().replaceAll("</?description>", "");
            String description = sw.getBuffer().toString().replaceAll("</?note-content>", "");
            System.out.println(description);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // testen van de functie
    @Test
    public void testLeesTomboyNote() {
        Tomboy tomboy = new Tomboy(this.dir);

        String tomboyFile = "/dab786f7-6c22-4249-9c8e-4fd72f3bf42f.note";
        tomboy.leesFile(tomboyFile);
        //TODO uitwerken verder
    }


    // lees een Everpad notitie
    @Test
    public void testEverpadLees() {
        String dir = "/home/chris/.everpad";
        String db = "everpad.5.db";
        EverpadNotes notes = new EverpadNotes(dir, db);
        if (notes != null) {
            try{
                ResultSet rs = notes.leesNote(23L);
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
    @Test
    public void testTomboyDatum() {
        Date nu = new Date();
        System.out.println("nu: "+nu.toString());

        long timestamp = System.currentTimeMillis()/1000;
        System.out.println("nu unix timestamp: " + timestamp);

        Date time=new java.util.Date(timestamp*1000);
        System.out.println("terug converteren: " + time);

        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String vandaag = dateFormat.format(new Date());
            System.out.println("vandaag: "+vandaag);

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
    @Test
    public void testEverpadDatum() {

        EverpadNotes notes = new EverpadNotes("", "");

        String format = "yyyy-MM-dd HH:mm:ss";
        String datum = "2013-02-24T14:26:27.4496230+01:00";
        long everpadDatum = notes.datumNaarEverpaddatum(datum, format);
        System.out.println("Everpad timestamp: " + everpadDatum);

        String dat = notes.everpaddatumNaarDatum(everpadDatum, format);
        System.out.println("Everpad datum: " + dat);

    }

    @Test
    public void testLeesTomboyFilenamen() {
        Tomboy tomboy = new Tomboy(this.dir);
        String[] files = tomboy.leesAlleFilenamen();
        System.out.println(files.length + " files gevonden:");
        Assert.assertTrue("aantal files", files.length == 145);
        for (String file: files) {
            System.out.format("File: %s\n", file);
        }
    }

    @Test
    public void testZoekTitel() {
        EverpadNotes notes = new EverpadNotes(everpadDir, everpadDb);
        Assert.assertTrue("TesSie", notes.zoekTitel("TesSie") == 4);
        System.out.println(notes.zoekTitel("TesSie"));

        Assert.assertTrue("flauwekul", notes.zoekTitel("flauwekul") == 0);
        System.out.println(notes.zoekTitel("flauwekul"));
    }

    @Test
    public void testReplace() {
        String tekst = "dit is 'een' test";
        System.out.println(tekst);
        log.info(tekst);
        tekst = tekst.replace("'", "''");
        System.out.println(tekst);
        log.info(tekst);
        tekst = tekst.replaceAll("'", "''");
        System.out.println(tekst);
        log.info(tekst);
    }
}
