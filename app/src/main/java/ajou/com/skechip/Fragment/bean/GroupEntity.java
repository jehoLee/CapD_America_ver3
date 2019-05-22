package ajou.com.skechip.Fragment.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.kakao.friends.response.model.AppFriendInfo;

import java.util.ArrayList;
import java.util.List;

public class GroupEntity implements Parcelable {
    private Integer groupID;
    private Integer groupManager;
    private String groupTitle;
    //    private int groupImg;
    private String groupTag;
    private int groupMemberNum;
    private List<AppFriendInfo> groupMembers;
    private List<MeetingEntity> meetingEntities = new ArrayList<>();

    public GroupEntity(Integer groupID, String groupTitle, String groupTag, Integer groupManager){
        this.groupID = groupID;
        this.groupTitle = groupTitle;
        this.groupTag = groupTag;
        this.groupManager = groupManager;
    }

    public GroupEntity(String groupTitle, String groupTag, int groupMemberNum, List<AppFriendInfo> groupMembers){
        this.groupTitle = groupTitle;
        this.groupTag = groupTag;
        this.groupMemberNum = groupMemberNum;
        this.groupMembers = groupMembers;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

//    public int getGroupImg() {
//        return groupImg;
//    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

//    public void setGroupImg(int groupImg) {
//        this.groupImg = groupImg;
//    }


    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public Integer getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(Integer groupManager) {
        this.groupManager = groupManager;
    }

    public String getGroupTag() {
        return groupTag;
    }

    public void setGroupTag(String groupTag) {
        this.groupTag = groupTag;
    }

    public int getGroupMemberNum() {
        return groupMemberNum;
    }

    public void setGroupMemberNum(int groupMemberNum) {
        this.groupMemberNum = groupMemberNum;
    }

    public List<AppFriendInfo> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<AppFriendInfo> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public List<MeetingEntity> getMeetingEntities() {
        return meetingEntities;
    }

//    public void setMeetingEntities(List<MeetingEntity> meetingEntities) {
//        this.meetingEntities = meetingEntities;
//    }

    public void addMeetingEntity(MeetingEntity meetingEntity){
        this.meetingEntities.add(meetingEntity);
    }

    public GroupEntity(Parcel in) {
        groupTitle = in.readString();
//        groupImg = in.readInt();
        groupTag = in.readString();
        groupMemberNum = in.readInt();
        groupMembers = in.createTypedArrayList(AppFriendInfo.CREATOR);
    }

    public static final Creator<GroupEntity> CREATOR = new Creator<GroupEntity>() {
        @Override
        public GroupEntity createFromParcel(Parcel in) {
            return new GroupEntity(in);
        }

        @Override
        public GroupEntity[] newArray(int size) {
            return new GroupEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupTitle);
//        dest.writeInt(groupImg);
        dest.writeString(groupTag);
        dest.writeInt(groupMemberNum);
        dest.writeTypedList(groupMembers);
    }
}

//
//public class RoomContext implements Serializable {
//
//    public RoomContext (){
//
//    }
//
//    public RoomContext(String room_name, Vote vote){
//        this.room_name = room_name;
//        this.vote = vote;
//    }
//
//    @SerializedName("room_name")
//    private String room_name;
//
//    @SerializedName("vote")
//    private Vote vote;
//
//    @SerializedName("rtmp_url")
//    private String streaming_url;
//
//    @SerializedName("xmpp_host")
//    private String XMPPHost;
//
//    @SerializedName("xmpp_service_name")
//    private String XMPPService;
//
//    @SerializedName("xmpp_room_name")
//    private String XMPPRoomName;
//
//    @SerializedName("room_idx")
//    private String roomId;
//
//    //for sqlLite
//    private int id;
//
//    public String getRoomId() { return roomId; }
//
//    public String getXMPPHost() { return XMPPHost; }
//
//    public String getXMPPService() { return XMPPService; }
//
//    public String getXMPPRoomName() { return XMPPRoomName; }
//
//    public String getStreaming_url() {
//        return streaming_url;
//    }
//
//    public Vote getVote() {
//        return vote;
//    }
//
//    public void setVote(Vote vote) {
//        this.vote = vote;
//    }
//
//    public String getRoom_name() {
//        return room_name;
//    }
//
//    public void setRoom_name(String room_name) {
//        this.room_name = room_name;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public void setRoomId(String roomId) {
//        this.roomId = roomId;
//    }
//
//}