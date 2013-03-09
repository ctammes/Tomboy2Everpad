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
public class EverpadNotes {

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
    private int conflic_parent_id;

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

    public int getConflic_parent_id() {
        return conflic_parent_id;
    }

    public void setConflic_parent_id(int conflic_parent_id) {
        this.conflic_parent_id = conflic_parent_id;
    }

    private Sqlite sqlite = null;

    public EverpadNotes(String dir, String db) {
        this.sqlite = new Sqlite(dir, db);
    }

    public boolean openDb() {
        return sqlite.openDb();
    }

    public void sluitDb() {
        sqlite.sluitDb();
    }

    public ResultSet leesNote(Long id) {
        String sql = "select * from notes" +
                    " where id = " + Long.toString(id);
        return sqlite.execute(sql);
    }

    public boolean schrijfNote(Tomboy note) {
        this.guid = null;

        String values = String.format("null, '%s', '%s', %d, %d, null, 1, 0, 0, 0, null",
                        note.getTitle(), note.getNote_content(), datumNaarEverpaddatum(note.getCreate_date(), "yyyy-MM-dd HH:mm:ss"));
        String sql = "insert into notes" +
                    " (guid, title, content, created, updated, " +
                    " updated_local, notebook_id, pinnded, " +
                    " place_id, action, conflict_parent_id)" +
                    " values (" + values + ")";

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
            System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + e.getMessage());
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
    
}
