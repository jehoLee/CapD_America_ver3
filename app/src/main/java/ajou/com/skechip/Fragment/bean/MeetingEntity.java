package ajou.com.skechip.Fragment.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.kakao.friends.response.model.AppFriendInfo;
import java.util.List;

public class MeetingEntity implements Parcelable {

    private String title;
    private String location;
    private String type;
    private List<Cell> meetingTimeCells;
    private List<AppFriendInfo> selectedMembers;

    public MeetingEntity(String title, String location, String type, List<Cell> meetingTimeCells, List<AppFriendInfo> selectedMembers) {
        this.title = title;
        this.location = location;
        this.type = type;
        this.meetingTimeCells = meetingTimeCells;
        this.selectedMembers = selectedMembers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Cell> getMeetingTimeCells() {
        return meetingTimeCells;
    }

    public void setMeetingTimeCells(List<Cell> meetingTimeCells) {
        this.meetingTimeCells = meetingTimeCells;
    }

    public List<AppFriendInfo> getSelectedMembers() {
        return selectedMembers;
    }

    public void setSelectedMembers(List<AppFriendInfo> selectedMembers) {
        this.selectedMembers = selectedMembers;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(location);
        dest.writeString(type);
        dest.writeTypedList(meetingTimeCells);
        dest.writeTypedList(selectedMembers);
    }

    protected MeetingEntity(Parcel in) {
        title = in.readString();
        location = in.readString();
        type = in.readString();
        meetingTimeCells = in.createTypedArrayList(Cell.CREATOR);
        selectedMembers = in.createTypedArrayList(AppFriendInfo.CREATOR);
    }

    public static final Creator<MeetingEntity> CREATOR = new Creator<MeetingEntity>() {
        @Override
        public MeetingEntity createFromParcel(Parcel in) {
            return new MeetingEntity(in);
        }

        @Override
        public MeetingEntity[] newArray(int size) {
            return new MeetingEntity[size];
        }
    };




}
