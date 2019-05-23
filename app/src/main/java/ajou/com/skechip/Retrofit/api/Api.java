package ajou.com.skechip.Retrofit.api;

import java.util.List;

import ajou.com.skechip.Retrofit.models.AlarmResponse;
import ajou.com.skechip.Retrofit.models.AsManyUserAsAvailableResponse;
import ajou.com.skechip.Retrofit.models.AvailableMeetingTimesResponse;
import ajou.com.skechip.Retrofit.models.DefaultResponse;
import ajou.com.skechip.Retrofit.models.GroupResponse;
import ajou.com.skechip.Retrofit.models.Kakao;
import ajou.com.skechip.Retrofit.models.MeetingResponse;
import ajou.com.skechip.Retrofit.models.TimeTableResponse;
import ajou.com.skechip.Retrofit.models.TimeTablesResponse;
import ajou.com.skechip.Retrofit.models.UserByGroupIdResponse;
import ajou.com.skechip.Retrofit.models.UserResponse;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @POST("createuser")
    Call<DefaultResponse> createUser(
            @Field("kakaoId") Long kakaoId,
            @Field("name") String name,
            @Field("member") Integer member
    );

    @FormUrlEncoded
    @POST("createfriend")
    Call<DefaultResponse> createFriend(
            @Field("userKakakoId") Long userKakakoId,
            @Field("friendKakakoId") Long friendKakakoId
    );

    @FormUrlEncoded
    @POST("createtimetable")
    Call<DefaultResponse> createTimeTable(
            @Field("kakaoId") Long kakaoId,
            @Field("type") Character type,
            @Field("title") String title,
            @Field("place") String place,
            @Field("cellPosition") Integer cellPosition
    );

    @FormUrlEncoded
    @POST("createGroup")
    Call<DefaultResponse> createGroup(
            @Query("kakaoIdList") String kakaoIdList,
            @Field("manager") Long manager,
            @Field("title") String title,
            @Field("tag") String tag
    );

    @FormUrlEncoded
    @POST("createMeeting")
    Call<DefaultResponse> createMeeting(
            @Query("kakaoIdList") String kakaoIdList,
            @Field("cellPositionList") String cellPositionList,
            @Field("groupId") Integer groupId,
            @Field("type") Integer type,
            @Field("manager") Integer manager,
            @Field("title") String title,
            @Field("place") String place
    );

    @FormUrlEncoded
    @POST("createAlarm")
    Call<DefaultResponse> createAlarm(
            @Field("type") Character type,
            @Field("from") String from,
            @Field("time") String time
    );

    @GET("getuser")
    Call<UserResponse> getUser(
            @Query("kakaoId") Long kakaoId
    );

    @GET("gettimetables")
    Call<TimeTablesResponse> getTimeTables(
            @Query("kakaoId") Long kakaoId
    );

    @GET("getGroup")
    Call<GroupResponse> getGroup(
            @Query("kakaoId") Long kakaoId
    );

    @GET("getUserByGroupId")
    Call<UserByGroupIdResponse> getUserByGroupId(
            @Query("groupId") Integer groupId
    );

    @GET("getMeeting")
    Call<MeetingResponse> getMeeting(
            @Query("groupId") Integer groupId
    );

    @GET("getUserByMeetingId")
    Call<UserByGroupIdResponse> getUserByMeetingId(
            @Query("meetingId") Integer meetingId
    );

    @GET("getAvailableMeetingTimes")
    Call<AvailableMeetingTimesResponse> getAvailableMeetingTimes(
            @Query("kakaoIds") String kakaoIdList
    );

    @GET("getAsManyUserAsAvailable")
    Call<AsManyUserAsAvailableResponse> getAsManyUserAsAvailable(
            @Query("kakaoIds") String kakaoIdList
    );

    @GET("getAllAlarm")
    Call<AlarmResponse> getAllAlarm();

    @FormUrlEncoded
    @PUT("updatetimetable/{kakaoId}")
    Call<TimeTableResponse> updateTimeTable(
            @Path("kakaoId") long kakaoId,
            @Field("type") Character type,
            @Field("title") String title,
            @Field("place") String place,
            @Field("cellPosition") Integer cellPosition
    );

    @DELETE("deletetimetable/{kakaoId}/{cellPosition}")
    Call<TimeTableResponse> deleteTimeTable(
            @Path("kakaoId") Long kakaoId,
            @Path("cellPosition") Integer cellPosition
    );

    @DELETE("deleteAllTimeTable/{kakaoId}/{cellPosition}")
    Call<DefaultResponse> deleteAllTimeTable(
            @Path("kakaoId") Long kakaoId
    );

    @DELETE("deleteAlarm/{id}")
    Call<DefaultResponse> deleteAlarm(
            @Path("id") Integer id
    );

}