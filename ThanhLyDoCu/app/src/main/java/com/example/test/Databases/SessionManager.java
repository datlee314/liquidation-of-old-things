package com.example.test.Databases;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    //Variables
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_PHONENUMBER = "phoneNo";
    public static final String KEY_PROFILEIMAGE = "profileImg";
    public static final String KEY_DATE = "date";

    public SessionManager(Context _context) {
        context = _context;
        usersSession = _context.getSharedPreferences("userProfileSession", Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createLoginSession(String fullName, String address, String username, String profileImg, String date, String gender, String phoneNo) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_FULLNAME, fullName);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_PHONENUMBER, phoneNo);
        editor.putString(KEY_PROFILEIMAGE, profileImg);
        editor.putString(KEY_DATE, date);

        editor.commit();

    }

    public HashMap<String, String> getUserDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_FULLNAME, usersSession.getString(KEY_FULLNAME, null));
        userData.put(KEY_USERNAME, usersSession.getString(KEY_USERNAME, null));
        userData.put(KEY_ADDRESS, usersSession.getString(KEY_ADDRESS, null));
        userData.put(KEY_GENDER, usersSession.getString(KEY_GENDER, null));
        userData.put(KEY_PHONENUMBER, usersSession.getString(KEY_PHONENUMBER, null));
        userData.put(KEY_PROFILEIMAGE, usersSession.getString(KEY_PROFILEIMAGE, null));
        userData.put(KEY_DATE, usersSession.getString(KEY_DATE, null));

        return userData;
    }

    public boolean checkLogin() {
        if (usersSession.getBoolean(IS_LOGIN, true)) {
            return true;
        } else
            return false;
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }

}
