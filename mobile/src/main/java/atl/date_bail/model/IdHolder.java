package atl.date_bail.model;

/**
 * Created by Alan on 12/02/2016.
 */
public class IdHolder {
    private static IdHolder instance = null;
    private long lastId;
    private String saveDate;
    private String saveTime;

    private IdHolder() {
        lastId = -1;
        saveDate = "";
        saveTime = "";
    }

    public static IdHolder getInstance() {
        if (instance == null) {
            instance = new IdHolder();
        }
        return instance;
    }

    public long getLastId() {
        return lastId;
    }

    public void setLastId(long id) {
        lastId = id;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }
}
