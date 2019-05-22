package ajou.com.skechip.Retrofit.api;

import java.util.List;

import ajou.com.skechip.Retrofit.models.AsManyUserAsAvailableResponse;
import ajou.com.skechip.Retrofit.models.AvailableMeetingTimesResponse;
import ajou.com.skechip.Retrofit.models.DefaultResponse;
import ajou.com.skechip.Retrofit.models.GroupResponse;
import ajou.com.skechip.Retrofit.models.Kakao;
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
            @Field("kakaoId") long kakaoId,
            @Field("name") String name,
            @Field("member") int member
    );

    @FormUrlEncoded
    @POST("createfriend")
    Call<DefaultResponse> createFriend(
            @Field("userKakakoId") long userKakakoId,
            @Field("friendKakakoId") long friendKakakoId
    );

    @FormUrlEncoded
    @POST("createtimetable")
    Call<DefaultResponse> createTimeTable(
            @Field("kakaoId") long kakaoId,
            @Field("type") Character type,
            @Field("title") String title,
            @Field("place") String place,
            @Field("cellPosition") int cellPosition
    );

    @FormUrlEncoded
    @POST("createGroup")
    Call<DefaultResponse> createGroup(
            @Query("kakaoIdList") String kakaoIdList,
            @Field("manager") Long manager,
            @Field("title") String title,
            @Field("tag") String tag
    );

    @GET("getuser")
    Call<UserResponse> getUser(
            @Query("kakaoId") long kakaoId
    );

    @GET("gettimetables")
    Call<TimeTablesResponse> getTimeTables(
            @Query("kakaoId") long kakaoId
    );

    @GET("getGroup")
    Call<GroupResponse> getGroup(
            @Query("kakaoId") long kakaoId
    );

    @GET("getUserByGroupId")
    Call<UserByGroupIdResponse> getUserByGroupId(
            @Query("groupId") long groupId
    );


    @GET("getAvailableMeetingTimes")
    Call<AvailableMeetingTimesResponse> getAvailableMeetingTimes(
            @Query("kakaoIds") String kakaoIdList
    );

    @GET("getAsManyUserAsAvailable")
    Call<AsManyUserAsAvailableResponse> getAsManyUserAsAvailable(
            @Query("kakaoIds") String kakaoIdList
    );

    @FormUrlEncoded
    @PUT("updatetimetable/{kakaoId}")
    Call<TimeTableResponse> updateTimeTable(
            @Path("kakaoId") long kakaoId,
            @Field("type") Character type,
            @Field("title") String title,
            @Field("place") String place,
            @Field("cellPosition") int cellPosition
    );

    @DELETE("deletetimetable/{kakaoId}/{cellPosition}")
    Call<TimeTableResponse> deleteTimeTable(
            @Path("kakaoId") long kakaoId,
            @Path("cellPosition") int cellPosition
    );

    @DELETE("deleteAllTimeTable/{kakaoId}/{cellPosition}")
    Call<DefaultResponse> deleteAllTimeTable(
            @Path("kakaoId") long kakaoId
    );

}