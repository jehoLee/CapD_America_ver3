package ajou.com.skechip.Retrofit.api;

import ajou.com.skechip.Retrofit.models.MeetingResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FirebaseApi {
    @GET("getAlarmToken")
    Call<MeetingResponse> getfirebase(
            @Query("groupId") Integer groupId
    );
}