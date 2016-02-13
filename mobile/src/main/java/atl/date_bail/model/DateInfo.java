package atl.date_bail.model;

public class DateInfo {
    private Long id;
    private String name;
    private String time;
    private String date;
    private String location;
    private String bailouts;
    private String notes;

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
