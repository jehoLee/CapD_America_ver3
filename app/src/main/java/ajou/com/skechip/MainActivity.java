package ajou.com.skechip;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;

import ajou.com.skechip.Event.MeetingCreationEvent;
import ajou.com.skechip.Fragment.bean.Cell;
import ajou.com.skechip.Fragment.bean.GroupEntity;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import ajou.com.skechip.Event.GroupCreationEvent;
import ajou.com.skechip.Fragment.EP_Fragment;
import ajou.com.skechip.Fragment.FriendListFragment;
import ajou.com.skechip.Fragment.GroupListFragment;
import ajou.com.skechip.Retrofit.api.RetrofitClient;
import ajou.com.skechip.Retrofit.models.DefaultResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.kakao.friends.AppFriendContext;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "ssss.MainActivity";

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onGroupCreationEvent(GroupCreationEvent event){
//        Log.d(TAG, "이벤트 발생!!");
//        groupListFragment.addGroupEntity(event.getNewGroup());
//        groupListFragment.updateGroupListView();
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMeetingCreationEvent(MeetingCreationEvent event) {
        Log.d(TAG, "미팅 생성 이벤트 발생 !");

        GroupEntity groupWithNewMeeting = event.getGroupEntityWithNewMeeting();
        ArrayList<Cell> cells = (ArrayList<Cell>) groupWithNewMeeting.getMeetingEntities().get(0).getMeetingTimeCells();

        epFragment.Onmeeting_created(cells);

        groupListFragment.updateGroupEntityOnMeetingCreate(groupWithNewMeeting);
        groupListFragment.updateGroupListView();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onGroupCreationEvent(GroupCreationEvent event) {
        Log.d(TAG, "이벤트 발생!!");
        groupListFragment.addGroupEntity(event.getNewGroup());
        groupListFragment.updateGroupListView();

        EventBus.getDefault().removeStickyEvent(event);
    }

    //for Fragment
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FriendListFragment friendListFragment;
    private GroupListFragment groupListFragment;
    private EP_Fragment epFragment;

    private Fragment curActivatedFragment;

    //for kakao user info
    private Long kakaoUserID;
    private String kakaoUserImg;
    private String kakaoUserName;
    private MeV2Response kakaoUserInfo;

    //for friend list
    final AppFriendContext friendContext = new AppFriendContext(true, 0, 10, "asc");
    private List<AppFriendInfo> kakaoFriends = new ArrayList<>();
    private List<String> friendsNickname_list = new ArrayList<>();
//    private ArrayList<FriendEntity> friendEntities = new ArrayList<>();

    //for Gallery - 충희
    private int GET_GALLERY_IMAGE = 200;
    private Mat img_input;
    private Mat img_output;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        init_Main();
    }


    /*
     * init_Main() 함수
     * 1. kakao 계정의 친구 목록을 불러온다
     * 2. kakao 계정의 유저 정보를 불러온다
     * 3. 위 정보를 불러온 뒤에, fragment를 초기화하고 navigationView를 등록한다.
     * */
    public void init_Main() {
        KakaoTalkService.getInstance().requestAppFriends(friendContext,
                new TalkResponseCallback<AppFriendsResponse>() {
                    @Override
                    public void onNotKakaoTalkUser() {
                        //KakaoToast.makeToast(getApplicationContext(), "not a KakaoTalk user", Toast.LENGTH_SHORT).show();
                        Logger.e("onNotKakao");
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        //redirectLoginActivity();
                        Logger.e("onSessionClosed");
                    }

                    @Override
                    public void onNotSignedUp() {
                        //redirectSignupActivity();
                        Logger.e("onNotSignededup");
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Logger.e("onFailure: " + errorResult.toString());
                    }

                    @Override
                    public void onSuccess(AppFriendsResponse result) {
                        // 친구 목록
                        Logger.e("onSucess " + result.getFriends().toString());

                        Iterator iter = result.getFriends().iterator();
                        while (iter.hasNext()) {
                            AppFriendInfo next = (AppFriendInfo) iter.next();
                            friendsNickname_list.add(next.getProfileNickname());
                            kakaoFriends.add(next);
//                            friendEntities.add(new FriendEntity(next.getId(), next.getProfileNickname(), next.getProfileThumbnailImage()));
                        }

                        if (friendContext.hasNext()) {
                            init_Main();
                        } else {
                            Logger.e("No next pages");
                            // 모든 페이지 요청 완료.

                            List<String> keys = new ArrayList<>();
                            keys.add("properties.nickname");
                            keys.add("properties.profile_image");
                            keys.add("kakao_account.email");

                            UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
                                @Override
                                public void onFailure(ErrorResult errorResult) {
                                    String message = "failed to get user info. msg=" + errorResult;
                                    Logger.d(message);
                                }

                                @Override
                                public void onSessionClosed(ErrorResult errorResult) {

                                }

                                @Override
                                public void onSuccess(MeV2Response response) {
                                    kakaoUserInfo = response;
                                    saveUser(kakaoUserInfo);

                                    for (AppFriendInfo friend : kakaoFriends) {
                                        saveUser(friend);
                                        saveFriendRelationShip(kakaoUserInfo, friend);
                                    }

                                    initiateFragmentsAndNavigation();
                                }
                            });
                        }
                    }
                });
    }

    private void initiateFragmentsAndNavigation() {
        //for 8 between MainActivity <-> Fragments
        Bundle bundle = new Bundle();

        bundle.putString("kakaoUserProfileImg", kakaoUserInfo.getProfileImagePath());
        bundle.putString("kakaoUserName", kakaoUserInfo.getNickname());
        bundle.putLong("kakaoUserID", kakaoUserInfo.getId());

        bundle.putParcelableArrayList("kakaoFriends", (ArrayList<? extends Parcelable>) kakaoFriends);
        bundle.putStringArrayList("friendsNickname_list", (ArrayList<String>) friendsNickname_list);

        groupListFragment = GroupListFragment.newInstance(bundle);
        friendListFragment = FriendListFragment.newInstance(bundle);

        boolean timeTableUploaded = getPreferencesBoolean("timeTableUploaded");
        bundle.putBoolean("timeTableUploaded", timeTableUploaded);

        epFragment = EP_Fragment.newInstance(bundle);

        //for bottomNavigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame_layout, groupListFragment).hide(groupListFragment);
        transaction.add(R.id.frame_layout, friendListFragment).hide(friendListFragment);
        transaction.add(R.id.frame_layout, epFragment);
        transaction.commit();

        curActivatedFragment = epFragment;

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_timetable:
                        transaction.hide(curActivatedFragment).show(epFragment);
                        transaction.commit();
                        curActivatedFragment = epFragment;
                        break;
                    case R.id.navigation_meeting:
                        transaction.hide(curActivatedFragment).show(groupListFragment);
                        transaction.commit();
                        curActivatedFragment = groupListFragment;
                        break;
                    case R.id.navigation_friends:
                        transaction.hide(curActivatedFragment).show(friendListFragment);
                        transaction.commit();
                        curActivatedFragment = friendListFragment;
                        break;
                }
                return true;
            }
        });
    }

    private void saveUser(final AppFriendInfo user) {
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(user.getId(), user.getProfileNickname(), 0);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
//                if (response.code() == 201) {
//                    DefaultResponse dr = response.body();
//                    Toast.makeText(MainActivity.this, user.getProfileNickname() + " created successfully", Toast.LENGTH_LONG).show();
//                } else if (response.code() == 422) {
//                    Toast.makeText(MainActivity.this, user.getProfileNickname() + " already exist", Toast.LENGTH_LONG).show();
//                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveUser(final MeV2Response user) {
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(user.getId(), user.getNickname(), 1);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

//                if (response.code() == 201) {
//                    DefaultResponse dr = response.body();
//                    Toast.makeText(MainActivity.this, user.getNickname() + " created successfully", Toast.LENGTH_LONG).show();
//                } else if (response.code() == 422) {
//                    Toast.makeText(MainActivity.this, user.getNickname() + " already exist", Toast.LENGTH_LONG).show();
//                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void saveFriendRelationShip(final MeV2Response user, final AppFriendInfo friend) {
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createFriend(user.getId(), friend.getId());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
//                if (response.code() == 201) {
//                    DefaultResponse dr = response.body();
//                    Toast.makeText(MainActivity.this, user.getNickname() + "과"+ friend.getProfileNickname() + "의 Friend relationship created successfully", Toast.LENGTH_LONG).show();
//                } else if (response.code() == 422) {
//                    Toast.makeText(MainActivity.this, user.getNickname() + "과"+ friend.getProfileNickname() + "의 Friend relationship already exist", Toast.LENGTH_LONG).show();
//                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void savePreferencesBoolean(String key, boolean value) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value).apply();
    }

    public boolean getPreferencesBoolean(String key) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //다른 액티비티가 main 액티비티 위에 올라갔을 때
    }


    @Override
    protected void onResume() {
        super.onResume();
        //다시 main 액티비티가 화면에 보일 때

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
