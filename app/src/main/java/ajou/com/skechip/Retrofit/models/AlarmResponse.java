package ajou.com.skechip.Retrofit.models;

import java.util.List;

public class AlarmResponse {
    private Boolean error;
    private List<Alarm> alarms;

    public AlarmResponse(boolean error, List<Alarm> alarms) {
        this.error = error;
        this.alarms = alarms;
    }

    public boolean isError() {
        return error;
    }

    public List<Alarm> getAlarmList() {
        return alarms;
    }
}