package ajou.com.skechip;

import ajou.com.skechip.Event.MeetingCreationEvent;
import ajou.com.skechip.Fragment.bean.MeetingEntity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.List;

import ajou.com.skechip.Fragment.bean.GroupEntity;

public class GroupDetailActivity extends AppCompatActivity {
    private final String TAG = "GroupDetailActivity";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMeetingCreationEvent(MeetingCreationEvent event){
        Log.d(TAG, "미팅 생성 이벤트 발생!");

        updateGroupEntity(event.getGroupEntityWithNewMeeting());
        updateMeetingAndRelatedView();
    }

    private Bundle bundle;
    private GroupEntity groupEntity;
    private List<MeetingEntity> meetingEntities;

    private RelativeLayout meetingAddView;
    private RelativeLayout meetingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        if (getIntent() != null) {
            bundle = getIntent().getBundleExtra("kakaoBundle");
            groupEntity = getIntent().getParcelableExtra("groupEntity");
//            kakaoUserID = kakaoBundle.getLong("kakaoUserID");
//            kakaoUserName = kakaoBundle.getString("kakaoUserName");
//            kakaoUserImg = kakaoBundle.getString("kakaoUserImg");
//            kakaoFriends = kakaoBundle.getParcelableArrayList("kakaoFriends");
//            friendsNickname_list = kakaoBundle.getStringArrayList("friendsNickname_list");
        }

        EventBus.getDefault().register(this);

        TextView groupNameText = findViewById(R.id.group_name_text);
        groupNameText.setText(groupEntity.getGroupTitle());

        meetingAddView = findViewById(R.id.initial_meeting_card);
        meetingView = findViewById(R.id.meeting_view);

        if(!groupEntity.isMeetingAdded()){

            meetingAddView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCreateMeeting();
                }
            });
            Button meetingAddBtn = findViewById(R.id.meeting_create_btn);
            meetingAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCreateMeeting();
                }
            });
        }
        else{
            updateMeetingAndRelatedView();
        }




//        InboxRecyclerView recyclerView = findViewById(R.id.inbox_recyclerview);
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);

//        RecyclerView.Adapter mAdapter = new GroupDetailAdapter(groupEntities);
//
//        recyclerView.setAdapter(mAdapter);
//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//
//                    }
//
//                    @Override
//                    public void onLongItemClick(View view, int position) {
//
//                    }
//                }));



    }

    public void updateGroupEntity(GroupEntity newGroup){
        groupEntity = newGroup;
    }

    public void updateMeetingAndRelatedView() {
        //TODO : 일단 카드뷰 하나 박아놓고 모임일정 띄워준다. 추후 모임일정 리스트 구현할 때 어댑터로 리사이클러뷰 구현하자
        meetingEntities = groupEntity.getMeetingEntities();

        //임시: 첫번째꺼만 보여줌
        MeetingEntity meetingEntity = meetingEntities.get(0);

        TextView meetingNameText = findViewById(R.id.meeting_name_text);
        TextView meetingLocText = findViewById(R.id.meeting_location_text);
        TextView meetingTimeText = findViewById(R.id.meeting_time_text);

        //임시: 첫번째꺼만 보여줌
        meetingNameText.setText(meetingEntity.getTitle());
        meetingLocText.setText(meetingEntity.getLocation());

        String time = meetingEntity.getMeetingTimeCells().get(0).getWeekofday() + " " + meetingEntity.getMeetingTimeCells().get(0).getStartTime();
        meetingTimeText.setText(time);

        meetingAddView.setVisibility(View.GONE);
        meetingView.setVisibility(View.VISIBLE);
    }


    public void startCreateMeeting(){
        Toast.makeText(getApplicationContext(), "start create meeting", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MeetingCreateActivity.class);

        intent.putExtra("kakaoBundle", bundle);
        intent.putExtra("groupEntity", groupEntity);

        startActivity(intent);
//        meetingAddView.setVisibility(View.GONE);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//
//        meetingCreateFragment = MeetingCreateFragment.newInstance(bundle);
//        transaction.add(R.id.frame_layout, meetingCreateFragment);
//        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().unregister(this);
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }





}
