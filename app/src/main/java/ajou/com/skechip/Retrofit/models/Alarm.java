package ajou.com.skechip.Retrofit.models;

public class Alarm {
    private Integer id;
    private Character type;
    private String from;
    private String time;

    public Alarm(Integer id, Character type, String from, String time) {
        this.id = id;
        this.type = type;
        this.from = from;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public Character getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public String getTime() {
        return time;
    }
}