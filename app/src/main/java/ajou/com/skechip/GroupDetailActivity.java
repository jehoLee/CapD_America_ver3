package ajou.com.skechip;

import ajou.com.skechip.Event.MeetingCreationEvent;
import ajou.com.skechip.Fragment.bean.Cell;
import ajou.com.skechip.Fragment.bean.MeetingEntity;
import ajou.com.skechip.Retrofit.api.RetrofitClient;
import ajou.com.skechip.Retrofit.models.Kakao;
import ajou.com.skechip.Retrofit.models.UserByGroupIdResponse;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.util.helper.log.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import ajou.com.skechip.Fragment.bean.GroupEntity;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetailActivity extends AppCompatActivity {
    private final String TAG = "GroupDetailActivity";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMeetingCreationEvent(MeetingCreationEvent event) {
        Log.d(TAG, "미팅 생성 이벤트 발생!");

        updateGroupEntity(event.getGroupEntityWithNewMeeting());
        updateMeetingAndRelatedView();
    }

    private Bundle bundle;
    private GroupEntity groupEntity;
    private List<MeetingEntity> meetingEntities;
    private RelativeLayout meetingAddView;
    private RelativeLayout meetingView;
    private ImageButton groupSettingButton;
    private Bitmap bitmap;
    private Kakao memberIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        if (getIntent() != null) {
            bundle = getIntent().getBundleExtra("kakaoBundle");
            groupEntity = getIntent().getParcelableExtra("groupEntity");
            meetingEntities = getIntent().getParcelableArrayListExtra("meetingEntities");
        }

        EventBus.getDefault().register(this);

        TextView groupNameText = findViewById(R.id.group_name_text);
        TextView groupTagText = findViewById(R.id.group_tag_text);
        TextView memberNumText = findViewById(R.id.member_num_text);

        groupNameText.setText(groupEntity.getGroupTitle());
        groupTagText.setText(groupEntity.getGroupTag());
        String memberNum = String.valueOf(groupEntity.getGroupMemberNum());
        memberNumText.setText(memberNum);

        meetingAddView = findViewById(R.id.initial_meeting_card);
        meetingView = findViewById(R.id.meeting_view);

        groupSettingButton = findViewById(R.id.group_setting_button);

        if (meetingEntities.isEmpty()) {
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
            setClickListenerForSettingBtn(false);
        } else {
            updateMeetingAndRelatedView();
            setClickListenerForSettingBtn(true);
        }

        setMembersView();

    }

    private void setMembersView() {
        Long managerID = groupEntity.getGroupManager();
        LinearLayout membersView = findViewById(R.id.members_view);

        for(Kakao member : groupEntity.getGroupMembers()){
            memberIndex = member;

            CircleImageView imageView = new CircleImageView(this);
            Thread thread = new Thread(){
                @Override
                public void run(){
                    try{
                        if(memberIndex.getProfileThumbnailImage().isEmpty()){
                            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.defalt_thumb_nail_image);
                        }
                        else {
                            URL url = new URL(memberIndex.getProfileThumbnailImage());
                            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                            connection.setDoInput(true);
                            connection.connect();

                            InputStream inputStream = connection.getInputStream();
                            bitmap = BitmapFactory.decodeStream(inputStream);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();

            try {
                thread.join();
                imageView.setImageBitmap(bitmap);
                LinearLayout friendView = new LinearLayout(this);
                friendView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                friendView.setOrientation(LinearLayout.VERTICAL);
                friendView.addView(imageView);

                LinearLayout name = new LinearLayout(this);
                name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                name.setOrientation(LinearLayout.VERTICAL);
                if(managerID.equals(memberIndex.getUserId())){
                    final TextView nameText = new TextView(this);
                    String managerName = member.getProfileNickname() + "(매니저)";
                    nameText.setText(managerName);
                    nameText.setTextColor(getApplicationContext().getResources().getColor(R.color.CapD_color_main));
                    name.addView(nameText);
                    friendView.addView(name);
                    membersView.addView(friendView, 0);
                }
                else {
                    final TextView nameText = new TextView(this);
                    nameText.setText(member.getProfileNickname());
                    nameText.setTextColor(getApplicationContext().getResources().getColor(R.color.text_dark1));
                    name.addView(nameText);
                    friendView.addView(name);
                    membersView.addView(friendView);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateGroupEntity(GroupEntity newGroup) {
        groupEntity = newGroup;
        meetingEntities = groupEntity.getMeetingEntities();
    }

    private void setClickListenerForSettingBtn(Boolean isMeetingAdded){
        if(isMeetingAdded){
            groupSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO : 모임과 일정 모두 수정 가능
                }
            });
        }else{
            groupSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO : 모임만 수정 가능
                }
            });
        }
    }

    public void updateMeetingAndRelatedView() {
        //임시: 첫번째꺼만 보여줌
        MeetingEntity meetingEntity = meetingEntities.get(0);

        TextView meetingNameText = findViewById(R.id.meeting_name_text);
        TextView meetingLocText = findViewById(R.id.meeting_location_text);
        TextView meetingTimeText = findViewById(R.id.meeting_time_text);

        //임시: 첫번째꺼만 보여줌
        meetingNameText.setText(meetingEntity.getTitle());
        meetingLocText.setText(meetingEntity.getLocation());

        if(!meetingEntity.getMeetingTimeCells().isEmpty()) {
            String time = meetingEntity.getMeetingTimeCells().get(0).getWeekofday() + " " + meetingEntity.getMeetingTimeCells().get(0).getStartTime();
            meetingTimeText.setText(time);
        }

        meetingAddView.setVisibility(View.GONE);
        meetingView.setVisibility(View.VISIBLE);
    }


    public void startCreateMeeting() {
        Intent intent = new Intent(this, MeetingCreateActivity.class);
        intent.putExtra("kakaoBundle", bundle);
        intent.putExtra("groupEntity", groupEntity);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().unregister(this);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
