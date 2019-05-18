
package ajou.com.skechip;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

import ajou.com.skechip.Fragment.GroupInfoEnterFragment;
import ajou.com.skechip.Fragment.SelectFriendsFragment;

public class GroupCreateActivity extends AppCompatActivity {
    private final String TAG = "GroupCreateActivity";

    private FragmentManager fragmentManager;
    private SelectFriendsFragment selectFriendsFragment;
    private GroupInfoEnterFragment groupInfoEnterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        Bundle bundle = new Bundle();
        if (getIntent() != null) {
            bundle = getIntent().getBundleExtra("kakaoBundle");
        }

        fragmentManager = getSupportFragmentManager();
        selectFriendsFragment = SelectFriendsFragment.newInstance(bundle);
        fragmentManager.beginTransaction()
                .add(R.id.frame_layout, selectFriendsFragment)
//                .addToBackStack(null)
                //TODO : 백스택 추가하여 백버튼 눌렀을 때 다시 친구 선택할 수 있게 해주기
                .commit();


    }

    public void replaceFragment(Bundle bundle){
        groupInfoEnterFragment = GroupInfoEnterFragment.newInstance(bundle);

        fragmentManager.beginTransaction()
                .add(R.id.frame_layout, groupInfoEnterFragment)
                .commit();
    }

    public void finishActivity(){
        fragmentManager.beginTransaction().remove(selectFriendsFragment).commit();
        finish();
    }


    @Override
    public void onBackPressed() {
        if(Objects.equals(fragmentManager.findFragmentById(R.id.frame_layout), groupInfoEnterFragment)){
            fragmentManager.beginTransaction().remove(groupInfoEnterFragment).commit();
        }else{
            super.onBackPressed();
            //additional code
        }
//        int count = fragmentManager.getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//            Toast.makeText(this, "backstack entry = 0", Toast.LENGTH_SHORT).show();
//
//        } else {
//            fragmentManager.beginTransaction().remove(groupInfoEnterFragment).commit();
////            fragmentManager.popBackStackImmediate();
//            Toast.makeText(this, "backstack entry > 0", Toast.LENGTH_SHORT).show();
//        }

    }





}
