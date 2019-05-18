package ajou.com.skechip.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.friends.response.model.AppFriendInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ajou.com.skechip.R;

public class FriendListAdapter extends BaseAdapter{

    private Context activityContext;

    public List<AppFriendInfo> getSelectedFriends() {
        return selectedFriends;
    }

    private List<AppFriendInfo> friendEntities;
    private List<AppFriendInfo> selectedFriends = new ArrayList<>();


    private int selectedFriendsNum = 0;
    private TextView selectedFriendsNumberText;
    private LinearLayout selectedFriendsView;

    private List<TextView> selectedNames = new ArrayList<>();

    public FriendListAdapter(Context activityContext, List<AppFriendInfo> friendEntities, RelativeLayout layout) {
        this.activityContext = activityContext;
        this.friendEntities = friendEntities;
//        LayoutInflater mInflater = (LayoutInflater)
//                activityContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        View view = mInflater.inflate(R.layout.fragment_select_friends, null);
//        selectedFriendsNumberText = view.findViewById(R.id.selected_friends_number);

        selectedFriendsNumberText = layout.findViewById(R.id.selected_friends_number);
//        selectedFriendsScrollView = layout.findViewById(R.id.friends_scroll_view);
        selectedFriendsView = layout.findViewById(R.id.selected_friends_view);
    }

    @Override
    public int getCount() {
        return friendEntities.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < getCount()) {
            return friendEntities.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    activityContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.friend_entity_block, null);
        }

        final AppFriendInfo friendEntity = friendEntities.get(position);

        TextView friendName = convertView.findViewById(R.id.friend_name);
        friendName.setText(friendEntity.getProfileNickname());

        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
//                    Toast.makeText(activityContext, friendEntity.getProfileNickname() + ": checked!", Toast.LENGTH_SHORT).show();
                    selectedFriends.add(friendEntity);
                    selectedFriendsNumberText.setText(Integer.toString(++selectedFriendsNum));

                    final TextView name = new TextView(activityContext);
                    name.setText(friendEntity.getProfileNickname());

//                    ShapeDrawable sd = new ShapeDrawable();
//                    sd.setShape(new RectShape());
//                    sd.getPaint().setColor(Color.BLACK);
//                    sd.getPaint().setStrokeWidth(1f);
//                    sd.getPaint().setStyle(Paint.Style.STROKE);
//                    name.setBackground(sd);
//                    name.setPadding(10, 5, 10, 5);
                    name.setTextColor(activityContext.getResources().getColor(R.color.text_dark1));


                    selectedFriendsView.addView(name);
                    selectedNames.add(name);

                }else{
//                    Toast.makeText(activityContext, friendEntity.getProfileNickname() + ": unchecked!", Toast.LENGTH_SHORT).show();
                    selectedFriends.remove(friendEntity);
                    selectedFriendsNumberText.setText(Integer.toString(--selectedFriendsNum));

                    for(TextView target : selectedNames){
                        if((target.getText().toString()).equals(friendEntity.getProfileNickname()))
                            selectedFriendsView.removeView(target);
                    }

                }
            }
        });

        return convertView;
    }


}
























