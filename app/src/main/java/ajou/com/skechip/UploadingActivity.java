package ajou.com.skechip;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class UploadingActivity extends AppCompatActivity {
    /*
    * TODO: 시간표 업로드 액티비티에서 해야할 것
    *
    * 충희-> 갤러리이미지 불러오고 시간표 이미지 선택한 뒤 시간표 이미지 분석
    *
    * 제호-> 1. 시간표 업로드 끝나면 메인 액티비티의 ep_fragment의 레이아웃 fragment_time_table 로 업데이트 하기
    *       2. 메인 액티비티 SavePreference 해서 timetableUploaded(boolean) true 로 save 해두기
    *
    * 건형-> DB 저장(서버)
    *
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading);
    }
}
