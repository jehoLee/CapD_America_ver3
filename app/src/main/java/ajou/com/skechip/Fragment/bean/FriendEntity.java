package ajou.com.skechip.Fragment.bean;

//import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FriendEntity implements Serializable {
    private Long userID;
    private String userName;
    private String userImgPath;

    public FriendEntity(Long userID, String userName, String userImgPath) {
        this.userID = userID;
        this.userName = userName;
        this.userImgPath = userImgPath;
    }

    public Long getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImgPath() {
        return userImgPath;
    }
}
