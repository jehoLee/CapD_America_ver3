package ajou.com.skechip.Event;

import ajou.com.skechip.Fragment.bean.MeetingEntity;

public class MeetingCreationEvent {

    private MeetingEntity newMeeting;

    public MeetingCreationEvent(MeetingEntity meetingEntity){
        this.newMeeting = meetingEntity;
    }

    public MeetingEntity getNewMeeting() {
        return newMeeting;
    }

    public void setNewMeeting(MeetingEntity newMeeting) {
        this.newMeeting = newMeeting;
    }

}
