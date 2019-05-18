package ajou.com.skechip.Fragment;

import android.os.Bundle;

import ajou.com.skechip.Fragment.bean.FriendEntity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import ajou.com.skechip.Adapter.FriendListAdapter;
import ajou.com.skechip.R;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kakao.friends.response.model.AppFriendInfo;

import java.util.ArrayList;
import java.util.List;

public class FriendListFragment extends Fragment {
    private FriendListFragment tmp = this;
    private final String TAG = "#FriendListFragment: ";
//    private FriendTimetableFragment friend_timetable = new FriendTimetableFragment();
    private List<String> friendsNickname_list = new ArrayList<>();
    private String kakaoUserImg;
    private String kakaoUserName;
    private Long kakaoUserID;
    private List<AppFriendInfo> kakaoFriends;
    private List<FriendEntity> friendEntities = new ArrayList<>();
    private FragmentManager fragmentManager;
    private Boolean isForGroupCreation;

    private FriendListAdapter friendListAdapter;

    public static FriendListFragment newInstance(Bundle bundle) {
        FriendListFragment fragment = new FriendListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        if(bundle != null){
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
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
        Log.d(TAG, "onCreateView");
        fragmentManager = getActivity().getSupportFragmentManager();

        View view;
        view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, friendsNickname_list);
        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String item = String.valueOf(parent.getItemAtPosition(position));
//                        FragmentTransaction transaction = fragmentManager.beginTransaction();
//                        transaction.add(R.id.frame_layout, friend_timetable).hide(tmp);
//                        transaction.commit();
                    }
                }
        );


        return view;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }




}
