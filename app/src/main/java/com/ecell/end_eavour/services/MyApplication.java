package com.ecell.end_eavour.services;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {

    String userToken;

    //---( Profile Update Trigger )---//
    boolean profileUpdateTrigger = false;
    public boolean isProfileUpdateTrigger() {
        return profileUpdateTrigger;
    }
    public void setProfileUpdateTrigger(boolean profileUpdateTrigger) {
        this.profileUpdateTrigger = profileUpdateTrigger;
    }

    //---( Get Event ID Temp )---//
    String tempEventId = "";
    public String getTempEventId() {
        return tempEventId;
    }
    public void setTempEventId(String tempEventId) {
        this.tempEventId = tempEventId;
    }

    //---( Get Pass Trigger )---//
    boolean getPassTrigger = true;
    public boolean isGetPassTrigger() {
        return getPassTrigger;
    }
    public void setGetPassTrigger(boolean getPassTrigger) {
        this.getPassTrigger = getPassTrigger;
    }

    //---( Logout to Login )---//
    boolean logoutSuccess = false;
    public boolean isLogoutSuccess() {
        return logoutSuccess;
    }
    public void setLogoutSuccess(boolean logoutSuccess) {
        this.logoutSuccess = logoutSuccess;
    }

    //---( Profile Missing Msg )---//
    boolean profileMissing = false;
    public boolean isProfileMissing() {
        return profileMissing;
    }
    public void setProfileMissing(boolean profileMissing) {
        this.profileMissing = profileMissing;
    }

    //---( Profile Updated Success )---//
    boolean profileSuccess = false;
    public boolean isProfileSuccess() {
        return profileSuccess;
    }
    public void setProfileSuccess(boolean profileSuccess) {
        this.profileSuccess = profileSuccess;
    }

    //---( To Profile Msg )---//
    boolean toProfile = false;
    public boolean isToProfile() {
        return toProfile;
    }
    public void setToProfile(boolean toProfile) {
        this.toProfile = toProfile;
    }

    //---( Signup Success Msg )---//
    boolean signUpSuccess = false;
    public boolean isSignUpSuccess() {
        return signUpSuccess;
    }
    public void setSignUpSuccess(boolean signUpSuccess) {
        this.signUpSuccess = signUpSuccess;
    }

    //---( Login Success Msg )---//
    boolean loginSuccess = false;
    public boolean isLoginSuccess() {
        return loginSuccess;
    }
    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    //---( Login Expired Status )---//
    boolean loginExpired = false;
    public boolean isLoginExpired() {
        return loginExpired;
    }
    public void setLoginExpired(boolean loginExpired) {
        this.loginExpired = loginExpired;
    }

    //---( User Schema )---//
    public String userName;
    public String userEmail;
    public boolean userProfile;
    public String userPhoneNumber;
    public String userEndvrId;
    public String userId;
    public String libraryId;
    public String branch;
    public String semester;
    public String collegeName;
    public String discord;

    public MyApplication() {
    }

    //---( User Schema Getter and Setter )---//

    public String getDiscord() {
        return discord;
    }
    public void setDiscord(String discord) {
        this.discord = discord;
    }
    public String getLibraryId() {
        return libraryId;
    }
    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }
    public String getBranch() {
        return branch;
    }
    public void setBranch(String branch) {
        this.branch = branch;
    }
    public String getSemester() {
        return semester;
    }
    public void setSemester(String semester) {
        this.semester = semester;
    }
    public String getCollegeName() {
        return collegeName;
    }
    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public boolean isUserProfile() {
        return userProfile;
    }
    public void setUserProfile(boolean userProfile) {
        this.userProfile = userProfile;
    }
    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }
    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
    public String getUserEndvrId() {
        return userEndvrId;
    }
    public void setUserEndvrId(String userEndvrId) {
        this.userEndvrId = userEndvrId;
    }

    //---( User Token Getter and Setter )---//
    public String getUserToken() {
        return userToken;
    }
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    //---( Firebase Persistance )---//
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    //---( To check Network Connections )---//
    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    //---( To Save User Token to Device )---//
    public void saveUserToken(Context context,String value) {
        String key = "userToken";
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value).apply();
    }

    //---( To Remove User Token )---//
    public void removeUserToken(Context context) {
        String key = "userToken";
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key).apply();
    }

    //---( Set User Endvr Id For Notification )---//
    public void setUserUserIdForNotification(Context context,String value) {
        String key = "userUserID";
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value).apply();
    }

    //---( To Remove Endvr Id For Notification )---//
    public void removeUserIdForNotification(Context context){
        String key = "userUserID";
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key).apply();
    }
}
