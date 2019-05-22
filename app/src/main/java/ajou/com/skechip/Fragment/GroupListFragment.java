package ajou.com.skechip.Fragment;

import android.content.Intent;
import android.os.Bundle;

import ajou.com.skechip.Fragment.bean.Cell;
import ajou.com.skechip.Retrofit.api.RetrofitClient;
import ajou.com.skechip.Retrofit.models.GroupResponse;
import ajou.com.skechip.Retrofit.models.Kakao;
import ajou.com.skechip.Retrofit.models.TimeTablesResponse;
import ajou.com.skechip.Retrofit.models.UserByGroupIdResponse;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kakao.friends.response.model.AppFriendInfo;

import ajou.com.skechip.Adapter.GroupEntity_Recycler_Adapter;
import ajou.com.skechip.Fragment.bean.FriendEntity;
import ajou.com.skechip.Fragment.bean.GroupEntity;
import ajou.com.skechip.GroupCreateActivity;
import ajou.com.skechip.GroupDetailActivity;
import ajou.com.skechip.R;
import ajou.com.skechip.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class GroupListFragment extends Fragment {
    private final String TAG = "GroupListFragment";

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onGroupCreationEvent(GroupCreationEvent event){
//        Log.d(TAG, "이벤트 발생!!");
//        addGroupEntity(event.getNewGroup());
//        updateGroupListView();
//    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<GroupEntity> groupEntities = new ArrayList<>();
    private Button groupCreateBtn;

    private List<String> friendsNickname_list = new ArrayList<>();
    private String kakaoUserImg;
    private String kakaoUserName;
    private Long kakaoUserID;
    private List<AppFriendInfo> kakaoFriends;
    private ArrayList<FriendEntity> friendEntities;

    private Bundle bundle;
    private View view;

    public static GroupListFragment newInstance(Bundle bundle) {
        GroupListFragment fragment = new GroupListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (bundle != null) {
            kakaoUserID = bundle.getLong("kakaoUserID");
            kakaoUserName = bundle.getString("kakaoUserName");
            kakaoUserImg = bundle.getString("kakaoUserImg");
            kakaoFriends = bundle.getParcelableArrayList("kakaoFriends");
            friendsNickname_list = bundle.getStringArrayList("friendsNickname_list");
        }
//
//        groupEntities.add(new GroupEntity("캡디아메리카", R.drawable.sample1));
//        groupEntities.add(new GroupEntity("모임모임모임모임모임모임모임", R.drawable.sample));
//        groupEntities.add(new GroupEntity("1111", R.drawable.sample));
//        groupEntities.add(new GroupEntity("2222", R.drawable.sample));
//        groupEntities.add(new GroupEntity("3333", R.drawable.sample));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group_list, container, false);

        ImageButton groupCreateBarBtn = view.findViewById(R.id.group_create_bar_btn);
        groupCreateBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGroupCreate();
            }
        });






        Call<GroupResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getGroup(kakaoUserID);

        call.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                int groupNum = response.body().getTotalCount();
                GroupResponse groupResponse = response.body();

                List<Integer> groupIDs = groupResponse.getIdList();
                List<Integer> groupManagers = groupResponse.getManagerList();
                List<String> groupTags = groupResponse.gettagList();
                List<String> groupTitles = groupResponse.getTitleList();

                for(int i = 0; i < groupNum; i++){
                    GroupEntity groupEntity = new GroupEntity(
                            groupIDs.get(i)
                            ,groupTitles.get(i)
                            ,groupTags.get(i)
                            ,groupManagers.get(i));
                    groupEntities.add(groupEntity);
                }



                for(GroupEntity curGroup : groupEntities){
                    Call<UserByGroupIdResponse> call_ = RetrofitClient
                            .getInstance()
                            .getApi()
                            .getUserByGroupId(curGroup.getGroupID());

                    call_.enqueue(new Callback<UserByGroupIdResponse>() {
                        @Override
                        public void onResponse(Call<UserByGroupIdResponse> call, Response<UserByGroupIdResponse> response) {

                            List<Kakao> members = response.body().getKakaoList();

                            List<AppFriendInfo> groupMembers = new ArrayList<>();
                            for(Kakao member : members){
//                                AppFriendInfo addMember = new AppFriendInfo(
//                                        member.getUserId(), member.getProfileNickname(), member.ge );
                            }

//                            curGroup.setGroupMembers();

                        }

                        @Override
                        public void onFailure(Call<UserByGroupIdResponse> call, Throwable t) {

                        }
                    });
                }






            }
            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
            }
        });























        updateGroupListView();

        return view;
    }

    public void addGroupEntity(GroupEntity groupEntity) {
        groupEntities.add(groupEntity);
    }

    public void updateGroupEntityOnMeetingCreate(GroupEntity groupEntity){
        for(int i = 0; i < groupEntities.size(); i++){
            if(groupEntities.get(i).getGroupTitle().equals(groupEntity.getGroupTitle())){//TODO:추후 그룹id로 변경하기
                groupEntities.set(i, groupEntity);//update
            }
        }

        Toast.makeText(getActivity(), "미팅 : " + groupEntities.get(0).getMeetingEntities().get(0).getTitle(), Toast.LENGTH_SHORT).show();

        updateGroupListView();
    }

    public void updateGroupListView() {
        if (groupEntities.isEmpty()) {
            groupCreateBtn = view.findViewById(R.id.meeting_create_btn);
            groupCreateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGroupCreate();
                }
            });

        } else {
            view.findViewById(R.id.initial_card).setVisibility(View.GONE);

            mRecyclerView = view.findViewById(R.id.group_card_list_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new GroupEntity_Recycler_Adapter(groupEntities);

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
                            intent.putExtra("kakaoBundle", bundle);
                            intent.putExtra("groupEntity", groupEntities.get(position));
                            intent.putParcelableArrayListExtra("meetingEntities", (ArrayList<? extends Parcelable>)groupEntities.get(position).getMeetingEntities());

                            startActivity(intent);

                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
//                            Toast.makeText(getActivity(), position + "번 째 아이템 롱 클릭", Toast.LENGTH_SHORT).show();
                        }
                    }));

        }
    }

    private void startGroupCreate() {
        Intent intent = new Intent(getActivity(), GroupCreateActivity.class);
        intent.putExtra("kakaoBundle", bundle);
        startActivity(intent);
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }


}
