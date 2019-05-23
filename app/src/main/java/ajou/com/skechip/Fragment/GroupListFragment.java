package ajou.com.skechip.Fragment;

import android.content.Intent;
import android.os.Bundle;

import ajou.com.skechip.Fragment.bean.Cell;
import ajou.com.skechip.Fragment.bean.ColTitle;
import ajou.com.skechip.Fragment.bean.MeetingEntity;
import ajou.com.skechip.Retrofit.api.RetrofitClient;
import ajou.com.skechip.Retrofit.models.GroupResponse;
import ajou.com.skechip.Retrofit.models.Kakao;
import ajou.com.skechip.Retrofit.models.MeetingResponse;
import ajou.com.skechip.Retrofit.models.TimeTablesResponse;
import ajou.com.skechip.Retrofit.models.UserByGroupIdResponse;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.util.helper.log.Logger;

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
import java.util.Arrays;
import java.util.List;

import static ajou.com.skechip.Fragment.EP_Fragment.ROW_SIZE;

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
    private List<Kakao> kakaoFriends;
    private ArrayList<FriendEntity> friendEntities;

    private Bundle bundle;
    private View view;

    private GroupEntity curGroup;
    private int calledGroupCount = 0;


    private Handler handler;

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

        updateGroupList();

        return view;
    }

    private void updateGroupList() {
        Call<GroupResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getGroup(kakaoUserID);

        call.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                int groupNum = response.body().getTotalCount();

                if (groupNum != 0) {
                    GroupResponse groupResponse = response.body();

                    List<Integer> groupIDs = groupResponse.getIdList();
                    List<Integer> groupManagers = groupResponse.getManagerList();
                    List<String> groupTags = groupResponse.getTagList();
                    List<String> groupTitles = groupResponse.getTitleList();

                    for (int i = 0; i < groupNum; i++) {
                        GroupEntity groupEntity = new GroupEntity(
                                groupIDs.get(i)
                                , groupTitles.get(i)
                                , groupTags.get(i)
                                , groupManagers.get(i));

                        groupEntities.add(groupEntity);
                    }

                    for (GroupEntity group : groupEntities) {
                        Call<UserByGroupIdResponse> call_ = RetrofitClient
                                .getInstance()
                                .getApi()
                                .getUserByGroupId(group.getGroupID());

                        final Call<MeetingResponse> call1 = RetrofitClient
                                .getInstance()
                                .getApi()
                                .getMeeting(group.getGroupID());

                        call_.enqueue(new Callback<UserByGroupIdResponse>() {
                            @Override
                            public void onResponse(Call<UserByGroupIdResponse> call, Response<UserByGroupIdResponse> response) {
                                List<Kakao> members = response.body().getKakaoList();
                                Integer curGroupID = response.body().getGroupId();

                                Logger.e("떠라!!!!!!");
                                for (int i = 0; i < members.size(); i++) {
                                    Kakao member = members.get(i);
                                    if (member.getUserId().equals(1050039103L)) {
                                        Logger.e("member는 " + member.getProfileNickname() + " " + member.getProfileThumbnailImage());
                                        member.setProfileThumbnailImage("https://mud-th-p-talk.kakao.com/th/talkp/wlblmvm7er/55K9YxabmKvluvOL7w1z7K/6ex0kx_110x110_c.jpg");
                                        members.set(i, member);
                                    } else if (member.getUserId().equals(1048797678L)) {
                                        Logger.e("member는 " + member.getProfileNickname() + " " + member.getProfileThumbnailImage());
                                        member.setProfileThumbnailImage("https://k.kakaocdn.net/dn/ddOTX0/btqtNZze7hu/LgGjR5sPSiNxmV82a5HaR1/profile_640x640s.jpg");
                                        members.set(i, member);
                                    } else if (member.getUserId().equals(1050029407L)) {
                                        Logger.e("member는 " + member.getProfileNickname() + " " + member.getProfileThumbnailImage());
                                        member.setProfileThumbnailImage("https://mud-th-p-talk.kakao.com/th/talkp/wlb5J4cTjd/lmvfuiKrper0QiasA3Tbpk/p814so_110x110_c.jpg");
                                        members.set(i, member);
                                    } else if (member.getUserId().equals(1050033491L)) {
                                        Logger.e("member는 " + member.getProfileNickname() + " " + member.getProfileThumbnailImage());
                                        member.setProfileThumbnailImage("");
                                        members.set(i, member);
                                    }
                                }

                                for (int i = 0; i < groupEntities.size(); i++) {
                                    GroupEntity groupEntity = groupEntities.get(i);
                                    if (groupEntity.getGroupID().equals(curGroupID)) {
                                        groupEntity.setGroupMembers(members);
                                        groupEntities.set(i, groupEntity);
                                    }
                                }

                                call1.enqueue(new Callback<MeetingResponse>() {
                                    @Override
                                    public void onResponse(Call<MeetingResponse> call, Response<MeetingResponse> response) {
                                        MeetingResponse meetingResponse = response.body();
                                        Integer curGroupID = response.body().getGroupId();
                                        List<Integer> cellPositions = meetingResponse.getCellPositionList();

                                        if (!meetingResponse.getIdList().isEmpty()) {
                                            Integer meetingID = meetingResponse.getIdList().get(0);
                                            Integer meetingManager = meetingResponse.getManagerList().get(0);
                                            String meetingPlace = meetingResponse.getPlaceList().get(0);
                                            String meetingTitle = meetingResponse.getTitleList().get(0);
                                            Integer meetingType = meetingResponse.getTypeList().get(0);

                                            //TODO : 미팅 타임셀 저장해야함
                                            List<Cell> cells = new ArrayList<>();
                                            for (Integer position : cellPositions) {
                                                Cell cell = new Cell();
                                                cell.setStatus(2);
                                                cell.setPlaceName(meetingPlace);
                                                cell.setSubjectName(meetingTitle);
                                                cell.setPosition(position);
                                                cell.setStartTime(getTimeString(position));
                                                cell.setWeekofday(getWeekDayString(position));

                                                cells.add(cell);
                                            }

                                            List<MeetingEntity> meetingEntities = new ArrayList<>();

                                            meetingEntities.add(new MeetingEntity(meetingTitle, meetingPlace
                                                    , meetingType, meetingID, meetingManager, cells));

                                            for (int i = 0; i < groupEntities.size(); i++) {
                                                GroupEntity groupEntity = groupEntities.get(i);
                                                if (groupEntity.getGroupID().equals(curGroupID)) {
                                                    groupEntity.addMeetingEntity(meetingEntities.get(0));
                                                    groupEntities.set(i, groupEntity);
                                                }
                                            }



//                                            calledGroupCount++;
//                                            if(calledGroupCount == groupEntities.size()){
//                                                updateGroupListView();
//                                                calledGroupCount = 0;
//                                            }


                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<MeetingResponse> call, Throwable t) {

                                    }
                                });


                                calledGroupCount++;
                                if (calledGroupCount == groupEntities.size()) {
                                    updateGroupListView();
                                    calledGroupCount = 0;
                                }


                            }

                            @Override
                            public void onFailure(Call<UserByGroupIdResponse> call, Throwable t) {

                            }
                        });

                    }


                } else {
                    //no group entities
                    groupCreateBtn = view.findViewById(R.id.meeting_create_btn);
                    groupCreateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startGroupCreate();
                        }
                    });
                }


            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
            }
        });
    }

    private String getTimeString(int cellPosition){
        List<ColTitle> colTitles = new ArrayList<>();
        int hour = 9;
        List<String> minute = new ArrayList<String>();
        minute.add(":00");
        minute.add(":30");

        for (int i = 0; i < ROW_SIZE; i++) {
            ColTitle colTitle = new ColTitle();
            String c = new Character((char) (i + 65)).toString();
            colTitle.setRoomNumber(c);
            if (i % 2 == 0) {
                String str = hour + minute.get(0) + "~";
                hour++;
                str += hour + minute.get(1);
                colTitle.setRoomTypeName(str);
            } else {
                String str = hour + minute.get(1) + "~";
                hour += 2;
                str += hour + minute.get(0);
                colTitle.setRoomTypeName(str);
            }
            colTitles.add(colTitle);
        }

        String s = colTitles.get(cellPosition/5).getTimeRangeName().split("~")[0];
        return s;
    }

    private String getWeekDayString(int cellPosition){
        List<String> weekofday = Arrays.asList(new String[]{"월","화","수","목","금"});
        return weekofday.get(cellPosition%5)+"요일";
    }


    public void addGroupEntity(GroupEntity groupEntity) {
        groupEntities.add(groupEntity);
    }

    public void updateGroupEntityOnMeetingCreate(GroupEntity groupEntity) {
        groupEntities.clear();
        updateGroupList();
    }

//    public void updateGroupEntityOnMeetingCreate(GroupEntity groupEntity){
//        for(int i = 0; i < groupEntities.size(); i++){
//            if(groupEntities.get(i).getGroupTitle().equals(groupEntity.getGroupTitle())){//TODO:추후 그룹id로 변경하기
//                groupEntities.set(i, groupEntity);//update
//            }
//        }
//
//        updateGroupListView();
//    }

    public void updateGroupListView() {
//        if (groupEntities.isEmpty()) {
//            groupCreateBtn = view.findViewById(R.id.meeting_create_btn);
//            groupCreateBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startGroupCreate();
//                }
//            });
//
//        } else {
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

                        intent.putParcelableArrayListExtra("meetingEntities", (ArrayList<? extends Parcelable>) groupEntities.get(position).getMeetingEntities());

                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
//                            Toast.makeText(getActivity(), position + "번 째 아이템 롱 클릭", Toast.LENGTH_SHORT).show();
                    }
                }));

//        }
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
