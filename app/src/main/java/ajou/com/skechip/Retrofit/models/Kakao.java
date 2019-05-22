package ajou.com.skechip.Retrofit.models;

public class Kakao {
    private long userId;
    private String profileNickname;
    private String profileThumbnailImage;

    public Kakao(long userId, String profileNickname) {
        this.userId = userId;
        this.profileNickname = profileNickname;
    }

    public long getUserId() {
        return userId;
    }

    public String getProfileNickname() {
        return profileNickname;
    }
}

