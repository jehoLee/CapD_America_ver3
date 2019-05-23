package ajou.com.skechip;

import ajou.com.skechip.Event.GroupCreationEvent;
import ajou.com.skechip.Event.TimeTableImageUploadEvent;
import ajou.com.skechip.Fragment.bean.GroupEntity;
import ajou.com.skechip.Retrofit.conn.CallMethod;
import androidx.appcompat.app.AppCompatActivity;
import ajou.com.skechip.Fragment.bean.Cell;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import com.kakao.friends.response.model.AppFriendInfo;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }



    private Mat img_input;
    private Mat img_output;
    ImageView imageVIewOuput;
    int[][] Schedul;
    private List<Cell> scheduleCells;
    private CallMethod con =  new CallMethod();
    private GroupEntity groupEntity;
    private Bundle bundle;
    private String kakaoUserName;
    private Long kakaoUserID;
    private static final String TAG = "sdk";
    private final int GET_GALLERY_IMAGE = 200;

    boolean isReady = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading);
        bundle = getIntent().getBundleExtra("kakaoBundle");
        kakaoUserID = bundle.getLong("kakaoUserID");
        imageVIewOuput = (ImageView)findViewById(R.id.imageView);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GET_GALLERY_IMAGE);
        imageprocess_and_showResult();
    }

    @Override
    protected void onResume() {
        super.onResume();

        isReady = true;
    }

    public native void imageprocessing(long inputImage, long outputImage);

    private void imageprocess_and_showResult() {
        scheduleCells = new ArrayList<Cell>();
        int k = 0;
        int pastheight=0;
        int pastweight=0;
        int enoughpoit = 0;
        int cellhighpoint=0;
        int celllowpoint=0;
        if (isReady==false) return;

        if (img_output == null)
            img_output = new Mat();
        imageprocessing(img_input.getNativeObjAddr(), img_output.getNativeObjAddr());
        Schedul = new int[7][5];

        Bitmap bitmapOutput = Bitmap.createBitmap(img_output.cols(), img_output.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img_output, bitmapOutput);
        //imageVIewOuput.setImageBitmap(bitmapOutput);

        for(int j=0 ;j<bitmapOutput.getWidth();j++){
            for(int i= 0;i<bitmapOutput.getHeight();i++){
                if ((bitmapOutput.getPixel(j,i) & 0x00ff00) >> 8==0) {
                    Log.e("1", "no " + i + " value " + j);
                    k++;

                    if (i-pastheight==1)
                        enoughpoit++;
                    if (enoughpoit>3){
                        enoughpoit = 0 ;
                        pastweight = j+1;
                        k=20;

                        break;
                    }
                    pastheight = i;

                }
                if (i == bitmapOutput.getHeight()/2) {
                    break;
                }
            }
            if (k==20)
                break;
        }
        for (int i = pastheight+100; i>pastheight;i--){
            if ((bitmapOutput.getPixel(pastweight+2,i) & 0x00ff00) >> 8==0){
                celllowpoint = i;
                break;
            }
        }
        for (int i = pastheight+100; i<pastheight+200;i++){
            if ((bitmapOutput.getPixel(pastweight+2,i) & 0x00ff00) >> 8==0){
                cellhighpoint = i;
                break;
            }
        }

        int cellheight = cellhighpoint - celllowpoint;
        Log.e("1",  " cell " + cellheight );
        Log.e("1","height " + pastheight);
        for(int i=0;i<6;i++) {
            for(int j=0;j<5;j++){
               if ((bitmapOutput.getPixel((3*pastweight)+
                                ((bitmapOutput.getWidth()
                                        -(3*pastweight))/10)*(2*j+1),
                        pastheight+((((cellheight)/2)+(cellheight))/2)*(2*i+1) ) & 0x00ff00) >> 8 != 255 ){
                    Schedul[i][j]= 1;
                    Cell cell = new Cell();
                    cell.setPosition(5*i+j);
                    cell.setPlaceName("");
                    scheduleCells.add(cell);
                }
                else
                    Schedul[i][j]= 0;
               // Log.e("1","value " + ((bitmapOutput.getPixel(100+200*j,232+100*i ) & 0xff0000) >> 16) +" " +
                        //((bitmapOutput.getPixel(100+200*j,232+100*i ) & 0x00ff00) >> 8) + " " +
                        //((bitmapOutput.getPixel(100+200*j,232+100*i ) & 0x0000ff) >> 0));
            }
        }
        Log.e("1"," "+ Schedul[0][0]+" "+ Schedul[0][1]+" "+ Schedul[0][2]+" "+ Schedul[0][3]+" "+ Schedul[0][4]);
        Log.e("1"," "+ Schedul[1][0]+" "+ Schedul[1][1]+" "+ Schedul[1][2]+" "+ Schedul[1][3]+" "+ Schedul[1][4]);
        Log.e("1"," "+ Schedul[2][0]+" "+ Schedul[2][1]+" "+ Schedul[2][2]+" "+ Schedul[2][3]+" "+ Schedul[2][4]);
        Log.e("1"," "+ Schedul[3][0]+" "+ Schedul[3][1]+" "+ Schedul[3][2]+" "+ Schedul[3][3]+" "+ Schedul[3][4]);
        Log.e("1"," "+ Schedul[4][0]+" "+ Schedul[4][1]+" "+ Schedul[4][2]+" "+ Schedul[4][3]+" "+ Schedul[4][4]);
        Log.e("1"," "+ Schedul[5][0]+" "+ Schedul[5][1]+" "+ Schedul[5][2]+" "+ Schedul[5][3]+" "+ Schedul[5][4]);
        //con.delete_server(scheduleCells,kakaoUserID);
        con.append_server(scheduleCells,kakaoUserID,'c');

        //TODO : UI update
        //2. 시간표 업데이트

        EventBus.getDefault().post(new TimeTableImageUploadEvent());
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if (requestCode == GET_GALLERY_IMAGE) {
                if (data.getData() != null) {
                    Uri uri = data.getData();

                    try {
                        String path = getRealPathFromURI(uri);
                        int orientation = getOrientationOfImage(path); // 런타임 퍼미션 필요
                        Bitmap temp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        Bitmap bitmap = getRotatedBitmap(temp, orientation);
                        imageVIewOuput.setImageBitmap(bitmap);
                        img_input = new Mat();
                        Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Utils.bitmapToMat(temp, img_input);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                imageprocess_and_showResult();
            }
        }
        else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        return cursor.getString(column_index);
    }

    // 출처 - http://snowdeer.github.io/android/2016/02/02/android-image-rotation/
    public int getOrientationOfImage(String filepath) {
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException e) {
            Log.d("@@@", e.toString());
            return -1;
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

        if (orientation != -1) {
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        }

        return 0;
    }

    public Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) throws Exception {
        if(bitmap == null) return null;
        if (degrees == 0) return bitmap;

        Matrix m = new Matrix();
        m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }






}