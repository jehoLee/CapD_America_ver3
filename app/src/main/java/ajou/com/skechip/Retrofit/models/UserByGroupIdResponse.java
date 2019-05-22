package ajou.com.skechip.Retrofit.models;

import java.util.List;

public class UserByGroupIdResponse {
    private boolean error;

    private List<Kakao> kakaoList;

    public UserByGroupIdResponse(boolean error, List<Kakao> kakaoList) {
        this.error = error;
        this.kakaoList = kakaoList;
    }

    public boolean isError() {
        return error;
    }

    public List<Kakao> getKakaoList() {
        return kakaoList;
    }

}
