package kg.auca.doodle.content;

public class DoodleItem {
    private long id;
    private String title;
    private String path;

    DoodleItem(String title, String path) {
        this(-1, title, path);
    }

    DoodleItem(long id, String title, String path) {
        this.id = id;
        this.title = title;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return title;
    }

}
