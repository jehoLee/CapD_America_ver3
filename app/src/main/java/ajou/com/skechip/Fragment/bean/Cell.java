package ajou.com.skechip.Fragment.bean;

//import android.os.Parcel;
//import android.os.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhouchaoyuan on 2017/1/14.
 */

public class Cell implements Parcelable {

    private int status;// 과목
    private int position;
    private String PlaceName;//
    private String SubjectName;//
    private String startTime;
    private String weekofday;

    public Cell() {}

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String PlaceName) {
        this.PlaceName = PlaceName;
    }

    public String getSubjectName() { return SubjectName; }

    public void setSubjectName(String SubjectName) {
        this.SubjectName =SubjectName;
    }

    public String getStartTime() { return startTime; }

    public void setStartTime(String StartTime) { this.startTime = StartTime; }

    public String getWeekofday() { return weekofday; }

    public void setWeekofday(String weekofday) { this.weekofday = weekofday; }

    public void setPosition(int position){this.position = position; }

    public int getPosition(){ return position; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeString(PlaceName);
        dest.writeString(SubjectName);
        dest.writeString(startTime);
        dest.writeString(weekofday);
    }

    protected Cell(Parcel in) {
        status = in.readInt();
        PlaceName = in.readString();
        SubjectName = in.readString();
        startTime = in.readString();
        weekofday = in.readString();
    }

    public static final Parcelable.Creator<Cell> CREATOR = new Parcelable.Creator<Cell>() {
        @Override
        public Cell createFromParcel(Parcel in) {
            return new Cell(in);
        }

        @Override
        public Cell[] newArray(int size) {
            return new Cell[size];
        }
    };
}