package ajou.com.skechip.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kakao.friends.response.model.AppFriendInfo;

import java.util.ArrayList;
import java.util.List;

import ajou.com.skechip.Adapter.Alarm_Recycler_Adapter;
import ajou.com.skechip.Fragment.bean.AlarmEntity;
import ajou.com.skechip.R;
import ajou.com.skechip.RecyclerItemClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmFragment  extends Fragment {
    private final String TAG = "#FriendListFragment: ";
    private List<String> friendsNickname_list = new ArrayList<>();
    private String kakaoUserImg;
    private String kakaoUserName;
    private Long kakaoUserID;
    private List<AppFriendInfo> kakaoFriends;
    private ArrayList<AlarmEntity> alarmEntities = new ArrayList<>();
    private Bundle bundle;
    private View view;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static AlarmFragment newInstance(Bundle bundle) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if(bundle != null){
            kakaoUserID = bundle.getLong("kakaoUserID");
            kakaoUserName = bundle.getString("kakaoUserName");
            kakaoUserImg = bundle.getString("kakaoUserImg");
            kakaoFriends = bundle.getParcelableArrayList("kakaoFriends");
            friendsNickname_list = bundle.getStringArrayList("friendsNickname_list");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarm_list, container, false);
//        refresh_list();
        return view;
    }
    public void addAlarmEntity(AlarmEntity alarmEntity) {
        alarmEntities.add(alarmEntity);
    }

    public void updatealarmEntityOnMeetingCreate(AlarmEntity alarmEntity){
        for(int i = 0; i < alarmEntities.size(); i++){
            if(alarmEntities.get(i).equals(alarmEntity.getAlarmTitle())){//TODO:추후 그룹id로 변경하기
                alarmEntities.set(i, alarmEntity);//update
            }
        }
    }
    public void updateAlarmListView() {
        if (!alarmEntities.isEmpty()) {
            view.findViewById(R.id.initial_alarm_card).setVisibility(View.GONE);

            mRecyclerView = view.findViewById(R.id.alarm_card_list_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new Alarm_Recycler_Adapter(alarmEntities);

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Toast.makeText(getActivity(), position + "번 째 아이템 클릭 : " + alarmEntities.get(position).getAlarmTitle(), Toast.LENGTH_SHORT).show();
                            //TODO : 해당 알림과 관련된 액티비티 이동

                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            Toast.makeText(getActivity(), position + "번 째 아이템 롱 클릭", Toast.LENGTH_SHORT).show();
                        }
                    }));

        }
    }
//    public void refresh_list(){
//        Call<AvailableMeetingTimesResponse> call = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getAvailableMeetingTimes(list.toString());
//        call.enqueue(new Callback<AvailableMeetingTimesResponse>() {
//            @Override
//            public void onResponse(Call<AvailableMeetingTimesResponse> call, Response<AvailableMeetingTimesResponse> response) {
//
//            }
//        }
//    }

//    private void createNotificationChannel() {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_application)
//                .setContentTitle("My notification")
//                .setContentText("Much longer text that cannot fit one line...")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.app_name);
//            String description = getString(R.string.app_name);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
}
