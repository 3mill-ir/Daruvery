package com.startup.hezare.startup;

/**
 * Created by rf on 01/08/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManagment {
    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences pref2;
    // Editor for Shared preferences
    Editor editor;
    // Editor for Shared preferences
    Editor editor2;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Login_Credentials";

    private static final String PREF_NAME2 = "Sign_up_Credentials";

    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String IS_SignedUp = "IsSignedUp";

    // User name (make variable public to access from outside)
    public static final String KEY_PHONE = "phone";

    // Email address (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    public static final String KEY_NAME = "name";

    public static final String KEY_FAMILY = "family";

    public static final String KEY_ADDRESS = "address";

    public static final String KEY_PATH = "path";


    public static final String KEY_PHONE_SIGNUP = "phone_sign_up";


    public static final String KEY_PASSWORD_SIGNUP = "password_sign_up";


    public static final String KEY_LOGGED = "key";
    // Constructor
    public SessionManagment(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        pref2 = _context.getSharedPreferences(PREF_NAME2, PRIVATE_MODE);
        editor2 = pref2.edit();
    }

    //check for more than 5 password cganges
    public void Increase_Times(int i)
    {
        editor2.putInt("legal", i);
        editor2.commit();
    }
    public boolean IS_LEGAL(){

        if(pref2.getInt("legal", 0)>5)
        return false;
        else
        return true;
    }
    public int get_Times(){
        return  pref2.getInt("legal", 0);
    }


    //check for more than 5 forgotten passwords
    public void Increase_Times1(int i)
    {
        editor2.putInt("plegal", i);
        editor2.commit();
    }
    public boolean IS_LEGAL1(){

        if(pref2.getInt("plegal", 0)>5)
            return false;
        else
            return true;
    }
    public int get_Times1(){
        return  pref2.getInt("plegal", 0);
    }

    //check for more than 5 forgotten passwords
    public void Increase_Times2(int i)
    {
        editor2.putInt("plegal1", i);
        editor2.commit();
    }
    public boolean IS_LEGAL2(){

        if(pref2.getInt("plegal1", 0)>5)
            return false;
        else
            return true;
    }
    public int get_Times2(){
        return  pref2.getInt("plegal1", 0);
    }


    /**
     * Create login session
     * */
    public void createLoginSession(String phone, String password){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing phone in pref
        editor.putString(KEY_PHONE, phone);

        // Storing password in pref
        editor.putString(KEY_PASSWORD, password);

        // commit changes
        editor.commit();
    }


    /**
     * Create Signup session
     * */
    public void createSignUpSession(String phone,String password){
        // Storing login value as TRUE
        editor2.putBoolean(IS_SignedUp, true);

        // Storing phone in pref
        editor2.putString(KEY_PHONE_SIGNUP, phone);
        // Storing password in pref
        editor2.putString(KEY_PASSWORD_SIGNUP, password);
        // commit changes
        editor2.commit();
    }
    public HashMap<String, String> getSignedUpSession(){
        HashMap<String, String> user = new HashMap<String, String>();
        // phone
        user.put(KEY_PHONE_SIGNUP, pref2.getString(KEY_PHONE_SIGNUP, null));
        // password
        user.put(KEY_PASSWORD_SIGNUP, pref2.getString(KEY_PASSWORD_SIGNUP, null));
        // return user
        return user;
    }

    public boolean isSigedUp(){
        return pref2.getBoolean(IS_SignedUp, false);
    }
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // phone
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));

        // password
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, false);
        //editor.clear();
        editor.commit();
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void createUserDetails(String name, String family,String address){

        editor.putString(KEY_NAME , name);

        editor.putString(KEY_FAMILY, family);

        editor.putString(KEY_ADDRESS,address);

        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getUserDetailsname(){
        HashMap<String, String> user = new HashMap<String, String>();
        // phone
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // password
        user.put(KEY_FAMILY, pref.getString(KEY_FAMILY, null));

        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        // return user
        return user;
    }

    public void createpathSession(String path){
        // Storing phone in pref
        editor.putString(KEY_PATH, path);
        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getpath(){
        HashMap<String, String> path = new HashMap<String, String>();
        // path
        path.put(KEY_PATH, pref.getString(KEY_PATH, null));

        return path;
    }
    public void set_splash(boolean splash)
    {
        // Storing phone in pref
        editor2.putBoolean("splash_show", splash);
        // commit changes
        editor2.commit();
    }

    public boolean show_splash()
    {

        return pref2.getBoolean("splash_show",true);
    }


}
