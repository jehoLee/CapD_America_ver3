package ajou.com.skechip.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import ajou.com.skechip.Fragment.bean.MeetingEntity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.kakao.friends.response.model.AppFriendInfo;


import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import ajou.com.skechip.Event.GroupCreationEvent;
import ajou.com.skechip.Fragment.bean.GroupEntity;
import ajou.com.skechip.GroupCreateActivity;
import ajou.com.skechip.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class GroupInfoEnterFragment extends Fragment {
    private List<AppFriendInfo> selectedFriends;
    private String groupName;
    private String groupTag;
    private int groupMemberNum;

    private AppFriendInfo curFriend;
    private Bitmap bitmap;

    public static GroupInfoEnterFragment newInstance(Bundle bundle) {
        GroupInfoEnterFragment fragment = new GroupInfoEnterFragment();
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedFriends = bundle.getParcelableArrayList("selectedFriends");
        }
        groupMemberNum = selectedFriends.size();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_group_info_enter, container, false);

        setSelectedFriendsView(view);

        setGroupTagSpinner(view);

        final EditText groupNameText = view.findViewById(R.id.group_name_edit);

        Button createGroupButton = view.findViewById(R.id.create_group_button);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "생성버튼클릭드", Toast.LENGTH_SHORT).show();

                //TODO - UI : 필수 입력 사항 체크하고 dialog 띄워서 최종 확인시키기
                groupName = groupNameText.getText().toString();//제목 썼는지 체크하기


                //TODO - DB/server : 모임 생성 API 호출 + 모임 정보 DB 저장

                GroupEntity newGroup = new GroupEntity(groupName, groupTag, groupMemberNum, selectedFriends);

                EventBus.getDefault().postSticky(new GroupCreationEvent(newGroup));
                ((GroupCreateActivity)getActivity()).finishActivity();
            }
        });


        return view;
    }

    private void setGroupTagSpinner(View view) {
        String[] groupTags = new String[]{"#", "#동아리", "#대외활동", "#강의팀플", "#여행", "#친목", "#기타"};
        Spinner groupTagSpinner = view.findViewById(R.id.group_tag_spinner);
//        groupTagSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, groupTags));


        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, groupTags) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
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
        groupTagSpinner.setAdapter(spinnerArrayAdapter);

        groupTagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    groupTag = selectedItemText;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSelectedFriendsView(View view) {

        LinearLayout selectedFriendsView = view.findViewById(R.id.friends_layout);
        TextView selectedFriendsNum = view.findViewById(R.id.participants_num);

        String text = Integer.toString(selectedFriends.size()) + "명의 친구";
        selectedFriendsNum.setText(text);


        for(AppFriendInfo friendEntity : selectedFriends){
            curFriend = friendEntity;
            bitmap = null;
            final TextView name = new TextView(getActivity());
            name.setText(friendEntity.getProfileNickname());
            name.setTextColor(getResources().getColor(R.color.text_dark1));

            final CircleImageView imageView = new CircleImageView(getActivity());
            Thread thread = new Thread(){
                @Override
                public void run(){
                    try{
                        if(curFriend.getProfileThumbnailImage().isEmpty()){
                            bitmap = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.defalt_thumb_nail_image);
                        }
                        else {
                            URL url = new URL(curFriend.getProfileThumbnailImage());
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
                LinearLayout friendView = new LinearLayout(getActivity());
                friendView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                friendView.setOrientation(LinearLayout.VERTICAL);
                friendView.addView(imageView);
                friendView.addView(name);
                selectedFriendsView.addView(friendView);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


//        for(AppFriendInfo friendEntity : selectedFriends){
//            final TextView name = new TextView(getActivity());
//            name.setText(friendEntity.getProfileNickname());
//            name.setTextColor(getResources().getColor(R.color.text_dark1));
//            selectedFriendsView.addView(name);
//        }

    }


}
