import nl.ctammes.common.Sqlite;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 2-3-13
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
public class EverpadNotes extends Sqlite {

    private Long id;
    private String guid;
    private String title;
    private String content;
    private Long created;
    private Long updated;
    private Long updated_local;
    private int notebook_id;
    private String pinnded;
    private int place_id;
    private int action;
    private int conflict_parent_id;
    // deze voor Everpad versie 5
    private int share_date;
    private int share_status;
    private String share_url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public Long getUpdated_local() {
        return updated_local;
    }

    public void setUpdated_local(Long updated_local) {
        this.updated_local = updated_local;
    }

    public int getNotebook_id() {
        return notebook_id;
    }

    public void setNotebook_id(int notebook_id) {
        this.notebook_id = notebook_id;
    }

    public String getPinnded() {
        return pinnded;
    }

    public void setPinnded(String pinnded) {
        this.pinnded = pinnded;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getConflict_parent_id() {
        return conflict_parent_id;
    }

    public void setConflict_parent_id(int conflict_parent_id) {
        this.conflict_parent_id = conflict_parent_id;
    }

    public int getShare_date() {
        return share_date;
    }

    public void setShare_date(int share_date) {
        this.share_date = share_date;
    }

    public int getShare_status() {
        return share_status;
    }

    public void setShare_status(int share_status) {
        this.share_status = share_status;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public EverpadNotes(String dir, String db) {
        super(dir, db);
        openDb();
    }

    public void sluitDb() {
        super.sluitDb();
    }

    public ResultSet leesNote(Long id) {
        String sql = "select * from notes" +
                    " where id = " + Long.toString(id);
        return execute(sql);
    }

    public boolean schrijfNote(Tomboy note) {
        this.guid = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nu = dateFormat.format(new Date());

        String titel = note.getTitle().replaceAll("'", "''");;
        String tekst = note.getNote_content().replaceAll("'", "''");;
        String values = String.format("null, '%s', '%s', %d, %d, %d, 1, 0, null, 1, null, null, 0, null",
                        titel, tekst, datumNaarEverpaddatum(note.getCreate_date()),
                        datumNaarEverpaddatum(note.getLast_change_date()),
                        datumNaarEverpaddatum(nu));
        String sql = "insert into notes" +
                    " (guid, title, content, created, updated, " +
                    " updated_local, notebook_id, pinnded, " +
                    " place_id, action, conflict_parent_id, " +
                    " share_date, share_status, share_url)" +
                    " values (" + values + ")";
        Tomboy2Everpad.log.fine("sql: " + sql);
        executeNoResult(sql);
        return false;

    }

    /**
     * Converteer een datum naar Everpad formaat (Java timestamp??)
     * @param datum als string
     * @param format
     * @return timestamp
     */
    public long datumNaarEverpaddatum(String datum, String format) {

        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            datum = datum.substring(0,19).replace("T"," ");
            Date parsedDate = dateFormat.parse(datum);
            long timestamp = parsedDate.getTime();
            return timestamp;
        } catch (ParseException e) {
            Tomboy2Everpad.log.severe(e.getMessage());
            return 0;
        }
    }

    /**
     * Converteer een datum naar Everpad formaat (Java timestamp??)
     * Default datumformaat: "yyyy-MM-dd HH:mm:ss"
     * @param datum
     * @return
     */
    public long datumNaarEverpaddatum(String datum) {

        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            datum = datum.substring(0,19).replace("T"," ");
            Date parsedDate = dateFormat.parse(datum);
            long timestamp = parsedDate.getTime();
            return timestamp;
        } catch (ParseException e) {
            Tomboy2Everpad.log.severe(e.getMessage());
            return 0;
        }
    }

    /**
     * Zet Everpad datum om in 'normale' datum
     * @param timestamp
     * @param format
     * @return datum als string
     */
    public String everpaddatumNaarDatum(long timestamp, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date time=new Date(timestamp);
        return dateFormat.format(time);

    }

    /**
     * Zoekt record met titel
     * @param titel
     * @return id
     */
    public Long zoekTitel(String titel) {
        String sql = String.format("select id from notes where lower(title) = '%s'",
            titel.toLowerCase());
        Tomboy2Everpad.log.fine("sql: " + sql);
        try {
            ResultSet rs = execute(sql);
            if (rs.next()) {
                return rs.getLong("id");
            } else {
                return 0L;
            }
        } catch (Exception e) {
            Tomboy2Everpad.log.severe(e.getMessage());
            return 0L;
        }
    }
    
}
