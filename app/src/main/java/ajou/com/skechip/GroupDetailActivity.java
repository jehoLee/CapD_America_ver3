package ajou.com.skechip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ajou.com.skechip.Fragment.bean.GroupEntity;

public class GroupDetailActivity extends AppCompatActivity {
//    private List<String> friendsNickname_list = new ArrayList<>();
//    private String kakaoUserImg;
//    private String kakaoUserName;
//    private Long kakaoUserID;
//    private List<AppFriendInfo> kakaoFriends;


    private RelativeLayout meetingAddView;

    private Bundle bundle;
    private GroupEntity groupEntity;

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

        TextView groupNameText = findViewById(R.id.group_name_text);
        groupNameText.setText(groupEntity.getGroupTitle());

        meetingAddView = findViewById(R.id.initial_meeting_card);
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

        super.onBackPressed();


    }





}
