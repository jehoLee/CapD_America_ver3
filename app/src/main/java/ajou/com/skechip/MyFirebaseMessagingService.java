package ajou.com.skechip;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import ajou.com.skechip.Event.AlarmReceivedEvent;
import ajou.com.skechip.Event.GroupCreationEvent;
import ajou.com.skechip.Fragment.bean.AlarmEntity;
import ajou.com.skechip.Retrofit.api.RetrofitClient;
import ajou.com.skechip.Retrofit.models.DefaultResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final String TAG = "ssss.MainActivity";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // User 정보에 token도 같이 넣으면 좋을듯함.
        // sendRegistrationToServer(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Character alarmType = remoteMessage.getData().get("alarmType").charAt(0);
        String toToken = remoteMessage.getData().get("toToken");
        String fromToken = remoteMessage.getData().get("fromToken");

        Log.e("알람", toToken + " " + fromToken);

        Call<DefaultResponse> alarmCreate = RetrofitClient
                .getInstance()
                .getApi()
                .createAlarm(alarmType,toToken,fromToken);
        alarmCreate.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) { Log.e("plz",response.toString());}
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) { }
        });
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        AlarmEntity newAlarm = new AlarmEntity(1, remoteMessage.getData().get("alarmType").charAt(0)
                , fromToken, toToken);

        EventBus.getDefault().post(new AlarmReceivedEvent(newAlarm));

    }
}