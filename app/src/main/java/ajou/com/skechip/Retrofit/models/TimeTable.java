package ajou.com.skechip.Retrofit.models;

public class TimeTable {

    private int id, userId, cellPosition;
    private char type;
    private String title, place;

    public TimeTable(int id, int userId, char type, String title, String place, int cellPosition) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.place= place;
        this.cellPosition = cellPosition;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public char getType(){ return type; }

    public String getTitle() {
        return title;
    }

    public String getPlace() {
        return place;
    }

    public int getCellPosition() {
        return cellPosition;
    }
}
