package atl.date_bail.model;

public class DateInfo {
    private Long id;
    private String name;
    private String img;
    private String time;
    private String date;
    private String location;
    private String bailouts;
    private String notes;

    public DateInfo(Long id, String name, String img, String time, String date, String location, String bailouts, String notes) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.time = time;
        this.date = date;
        this.location = location;
        this.bailouts = bailouts;
        this.notes = notes;
    }

    public DateInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBailouts() {
        return bailouts;
    }

    public void setBailouts(String bailouts) {
        this.bailouts = bailouts;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
