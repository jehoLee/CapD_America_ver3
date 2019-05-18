package ajou.com.skechip;

import ajou.com.skechip.Event.GroupCreationEvent;
import ajou.com.skechip.Event.MeetingCreationEvent;
import ajou.com.skechip.Fragment.SelectFriendsFragment;
import ajou.com.skechip.Fragment.SelectMeetingTimeFragment;
import ajou.com.skechip.Fragment.bean.Cell;
import ajou.com.skechip.Fragment.bean.GroupEntity;
import ajou.com.skechip.Fragment.bean.MeetingEntity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.friends.response.model.AppFriendInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MeetingCreateActivity extends AppCompatActivity {

    private GroupEntity groupEntity;
    private List<Cell> meetingTimeCells;
    private List<AppFriendInfo> selectedMembers;

    private Bundle bundle;
    private SelectMeetingTimeFragment selectMeetingTimeFragment;
    private SelectFriendsFragment selectFriendsFragment;

    private String meetingType;

    private RelativeLayout infoEnterView;
    private FrameLayout frameLayout;
    private Button createMeetingBtn;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_create);
        if (getIntent() != null) {
            bundle = getIntent().getBundleExtra("kakaoBundle");
            groupEntity = getIntent().getParcelableExtra("groupEntity");
        }

        infoEnterView = findViewById(R.id.info_enter_view);
        setMeetingTypeSpinner();

        frameLayout = findViewById(R.id.frame_layout);
        fragmentManager = getSupportFragmentManager();

        bundle.putBoolean("isForMeetingCreate", true);
        bundle.putParcelable("groupEntity", groupEntity);
        selectFriendsFragment = SelectFriendsFragment.newInstance(bundle);

        frameLayout.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction()
                .add(R.id.frame_layout, selectFriendsFragment)
                .commit();

        createMeetingBtn = findViewById(R.id.create_meeting_button);

        createMeetingBtn.setTextColor(getResources().getColor(R.color.trans_gray));
        createMeetingBtn.setClickable(false);

        Button getAvailableTimeBtn = findViewById(R.id.get_available_time_btn);
        getAvailableTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                infoEnterView.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);

//                RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.addRule(RelativeLayout.BELOW, R.id.appbarLayout);
//                frameLayout.setLayoutParams(params);

                selectMeetingTimeFragment = SelectMeetingTimeFragment.newInstance(bundle);

                fragmentManager.beginTransaction()
                        .add(R.id.frame_layout, selectMeetingTimeFragment)
                        .commit();
            }
        });


        createMeetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //멤버들, 제목, 타입, 장소, 일정시간

                EditText meetingTitleEdit = findViewById(R.id.meeting_title_edit);
                String meetingTitle = meetingTitleEdit.getText().toString();

                EditText meetingLocEdit = findViewById(R.id.meeting_location_edit);
                String meetingLocation = meetingLocEdit.getText().toString();

                for(Cell cell : meetingTimeCells){
                    cell.setSubjectName(meetingTitle);
                    cell.setPlaceName(meetingLocation);
                }

                Toast.makeText(getApplicationContext(), meetingTitle +
                        "\n" + meetingLocation + "\n" + meetingType + "\n" +
                        meetingTimeCells.get(0).getSubjectName() + "에서\n" +
                        meetingTimeCells.get(0).getStartTime() + meetingTimeCells.get(0).getWeekofday() + "에\n" +
                        selectedMembers.get(0).getProfileNickname() + "와 함께"
                        , Toast.LENGTH_LONG).show();

                MeetingEntity meetingEntity = new MeetingEntity(
                        meetingTitle, meetingLocation, meetingType, meetingTimeCells, selectedMembers);

                //TODO : server put

                //TODO : onMeetingCreateEvent in EP_fragment <- main activity
                //TODO : onMeetingCreateEvent in MeetingFragment <- main activity
                //TODO : onMeetingCreateEvent in GroupDetailActivity

                EventBus.getDefault().post(new MeetingCreationEvent(meetingEntity));

//                EventBus.getDefault().postSticky(new adawdawadwad(meetingEntity));
                finish();
            }
        });





    }


    public void onSelectMembersFinishedEvent(List<AppFriendInfo> members){
        selectedMembers = members;

        LinearLayout selectedParticipantsView = findViewById(R.id.participants_layout);
        TextView selectedParticipantsNum = findViewById(R.id.participants_num);

        String text = "일정에 참여하는 " + Integer.toString(selectedMembers.size()) + "명의 친구";
        selectedParticipantsNum.setText(text);

        for(AppFriendInfo friendEntity : selectedMembers){
            final TextView name = new TextView(this);
            name.setText(friendEntity.getProfileNickname());
            name.setTextColor(getResources().getColor(R.color.text_dark1));
            selectedParticipantsView.addView(name);
        }

        frameLayout.setVisibility(View.GONE);
        fragmentManager.beginTransaction().remove(selectFriendsFragment).commit();

    }


    public void onSelectTimesFinishedEvent(List<Cell> SELECTED_CELLS){
        meetingTimeCells = SELECTED_CELLS;

        //Do somethin with meetingTimeCells

        //update selected time view
        TextView selectedTimeText = findViewById(R.id.selected_time_text);
        selectedTimeText.setText(meetingTimeCells.get(0).getWeekofday() + " " + meetingTimeCells.get(0).getStartTime());

        createMeetingBtn.setTextColor(getResources().getColor(R.color.black));
        createMeetingBtn.setClickable(true);

        frameLayout.setVisibility(View.GONE);
        fragmentManager.beginTransaction().remove(selectMeetingTimeFragment).commit();

        findViewById(R.id.get_time_view).setVisibility(View.GONE);
        findViewById(R.id.selected_time_view).setVisibility(View.VISIBLE);
        infoEnterView.setVisibility(View.VISIBLE);

    }



//    private void setCreateMeetingBtnClickListener(){
//        createMeetingBtn.setTextColor(getResources().getColor(R.color.black));
//        createMeetingBtn.setClickable(true);
//        createMeetingBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//    }




    private void setMeetingTypeSpinner() {
        String[] meetingTypes = new String[]{"일정 타입을 선택하세요","매주 정기적인 일정", "이번 주만 일시적인 일정"};
        Spinner meetingTypeSpinner = findViewById(R.id.meeting_type_spinner);

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, meetingTypes) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        meetingTypeSpinner.setAdapter(spinnerArrayAdapter);

        meetingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
//                    Toast.makeText
//                            (getActivity(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                            .show();
                    meetingType = selectedItemText;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }







    @Override
    public void onBackPressed() {
//        if(){
//            infoEnterView.setVisibility(View.VISIBLE);
//            frameLayout.setVisibility(View.GONE);
//            fragmentManager.beginTransaction().remove(selectMeetingTimeFragment).commit();
//        }
        if(frameLayout.getVisibility() == View.VISIBLE){
            if(Objects.equals(fragmentManager.findFragmentById(R.id.frame_layout), selectMeetingTimeFragment)){
                infoEnterView.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                fragmentManager.beginTransaction().remove(selectMeetingTimeFragment).commit();
            }
            else{
                super.onBackPressed();
            }
        }
        else{
            super.onBackPressed();
            //additional code
        }
    }


}
