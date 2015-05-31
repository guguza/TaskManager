package viktoriia.vihriian.taskmanager;

public class Note {
    private String name;
    private String text;
    private long date;
    private boolean complite;

    public Note(String name, String text, long date, boolean complite) {
        this.setName(name);
        this.setText(text);
        this.setDate(date);
        this.setComplite(complite);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isComplite() {
        return complite;
    }

    public void setComplite(boolean complite) {
        this.complite = complite;
    }
}
