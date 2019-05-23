package ajou.com.skechip.Retrofit.models;

public class User {

    private Integer id, member;
    private Long kakaoid;
    private String name;

    public User(Integer id, Long kakaoid, String name, Integer member) {
        this.id = id;
        this.kakaoid = kakaoid;
        this.name = name;
        this.member = member;
    }

    public Integer getId() {
        return id;
    }

    public Long getKakaoid() {
        return kakaoid;
    }

    public String getName() {
        return name;
    }

    public Integer getMember() {
        return member;
    }
}