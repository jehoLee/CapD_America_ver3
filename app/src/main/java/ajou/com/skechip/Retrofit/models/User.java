package ajou.com.skechip.Retrofit.models;

public class User {

    private int id, member;
    private long kakaoid;
    private String name, place;

    public User(int id, long kakaoid, String name, int member) {
        this.id = id;
        this.kakaoid = kakaoid;
        this.name = name;
        this.member = member;
    }

    public int getId() {
        return id;
    }

    public long getKakaoid() {
        return kakaoid;
    }

    public String getName() {
        return name;
    }

    public int getMember() {
        return member;
    }
}
