package ajou.com.skechip.Retrofit.models;

import java.util.List;

public class AlarmResponse {
    private Boolean error;
    private List<Alarm> alarmList;

    public AlarmResponse(boolean error, List<Alarm> alarmList) {
        this.error = error;
        this.alarmList = alarmList;
    }

    public boolean isError() {
        return error;
    }

    public List<Alarm> getAlarmList() {
        return alarmList;
    }
}