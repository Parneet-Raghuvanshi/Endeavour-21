package com.ecell.end_eavour.serverfiles;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    String authHead = "Authorization";

    @POST("auth/login")
    Call<ResponseBody> loginApi(@Body Map<String,Object> params);

    @POST("user/contactUs")
    Call<ResponseBody> contactUsApi(@Body Map<String,Object> params);

    @POST("auth/forgotpassword")
    Call<ResponseBody> sendForgotPassMail(@Body Map<String,Object> params);

    @POST("auth/signup")
    Call<ResponseBody> signUpApi(@Body Map<String,Object> params);

    @POST("user/updateProfile")
    Call<ResponseBody> setProfileApi(@Header(authHead) String userToken , @Body Map<String,Object> params);

    @GET("user/getUser")
    Call<ResponseBody> getRegisteredStatusApi(@Header(authHead) String userToken);

    @GET("user/getUser")
    Call<ResponseBody> verifyUserTokenApi(@Header(authHead) String userToken);

    @GET("event/getEvent/{eventId}")
    Call<ResponseBody> getEventApi(@Header(authHead) String userToken,@Path("eventId") String eventId);

    @POST("user/register/{eventId}")
    Call<ResponseBody> registerEvent(@Header(authHead) String userToken,@Body Map<String,Object> params,@Path("eventId") String eventId);

    @GET("event/getTeam/{teamId}")
    Call<ResponseBody> getTeamDetail(@Header(authHead) String userToken,@Path("teamId") String teamId);

}
