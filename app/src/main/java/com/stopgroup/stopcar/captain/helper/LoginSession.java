package com.stopgroup.stopcar.captain.helper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stopgroup.stopcar.captain.activity.Splash;
import com.stopgroup.stopcar.captain.modules.Login;

import java.lang.reflect.Type;


/**
 * Created by pc on 01/06/2017.
 */

public class LoginSession {
    public static String token = "";
    public static String refresh_token = "";
    public static double hisCredit ;

    public static Long expired;
    public static boolean isLogin;
    public static SharedPreferences loginFile;
    public static int id;
    public static String email;
    public static String mobile;
    public static String fname;
    public static String lastname;
    public static String image;
    public static String county_id;
    public static String code;
    public static String currency;

    public static void setdata(Activity activity) {

        loginFile = activity.getSharedPreferences("LOGIN_FILE", 0);
        String x = loginFile.getString("json", "");
        Log.e("data",x);
        if (!x.equals("")) {
            Type dataType = new TypeToken<Login>() {
            }.getType();
            Login data = new Gson().fromJson(x, dataType);
            email = data.result.email;
            mobile = data.result.mobile;
            fname = data.result.first_name;
            lastname = data.result.last_name;
            image = data.result.image ;
            county_id = String.valueOf(data.result.country_id);
            code = data.result.country_code;
            currency = data.result.currency;
            token = data.access_token;
            refresh_token = data.refresh_token;
            expired = data.expires_in;
            isLogin = true;
        } else {
            isLogin = false;
        }
    }
    public static Login getlogindata(Activity activity) {
        if(activity==null){
            return  null;
        }
        loginFile = activity.getSharedPreferences("LOGIN_FILE", 0);
        String x = loginFile.getString("json", "");
        Login data = null;
        Log.e("data",x);
        if (!x.equals("")) {
            Type dataType = new TypeToken<Login>() {
            }.getType();
            data = new Gson().fromJson(x, dataType);
        } else {
            isLogin = false;
        }
        return data ;
    }
    public static Login getlogindata(Context activity) {
        loginFile = activity.getSharedPreferences("LOGIN_FILE", 0);
        String x = loginFile.getString("json", "");
        Login data = null;
        Log.e("data",x);
        if (!x.equals("")) {
            Type dataType = new TypeToken<Login>() {
            }.getType();
            data = new Gson().fromJson(x, dataType);
        } else {
            isLogin = false;
        }
        return data ;
    }
    public static Login getlogindata1(Context activity) {
        loginFile = activity.getSharedPreferences("LOGIN_FILE", 0);
        String x = loginFile.getString("json", "");
        Login data = null;
        Log.e("data",x);
        if (!x.equals("")) {
            Type dataType = new TypeToken<Login>() {
            }.getType();
            data = new Gson().fromJson(x, dataType);
        } else {
            isLogin = false;
        }
        return data ;
    }
    public static void setdat(Context activity) {

        loginFile = activity.getSharedPreferences("LOGIN_FILE", 0);
        String x = loginFile.getString("json", "");
        Log.e("data",x);
        if (!x.equals("")&& x.contains("online")) {
            Type dataType = new TypeToken<Login>() {
            }.getType();
            Login data = new Gson().fromJson(x, dataType);
            email = data.result.email;
            mobile = data.result.mobile;
            fname = data.result.first_name;
            lastname = data.result.last_name;
            image = data.result.image ;
            county_id = String.valueOf(data.result.country_id);
            code = data.result.country_code;
            currency = data.result.currency;
            token = data.access_token;
            refresh_token = data.refresh_token;
            expired = data.expires_in;
            isLogin = true;
            hisCredit = data.result.driver.his_credit ;
        } else {
            isLogin = false;
        }
    }
    public static void clearData(Activity activity) {



        if (activity!= null){
            loginFile = activity.getSharedPreferences("LOGIN_FILE", 0);

            loginFile.edit().clear().apply();

            Intent i = new Intent(activity, Splash.class);
            activity.startActivity(i);
            activity.finish();

        }
    }

    public static void AddToSharedPreferences(Activity activity, String Name, String USERNAME, String phone, String Email, String IMAGE, String password, String lat, String lon, String token, String expired ,int isCredit) {
        loginFile = activity.getSharedPreferences("LOGIN_FILE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = loginFile.edit();
        editor.putString("NAME", Name);
        editor.putString("USERNAME", USERNAME);
        editor.putString("PHONE", phone);
        editor.putString("EMAIL", Email);
        editor.putString("IMAGE", IMAGE);
        editor.putString("password", password);
        editor.putBoolean("LOGIN", true);
        editor.putString("lat", lat);
        editor.putString("lon", lon);
        editor.putString("token", token);
        editor.putString("expired", expired);
        editor.putInt("hisCredit", isCredit);

        editor.apply();

    }

    public static void AddTotokenSharedPreferences(Activity activity, String token, String expired) {
        loginFile = activity.getSharedPreferences("LOGIN_FILE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = loginFile.edit();
        editor.putString("token", token);
        editor.putString("expired", expired);

        editor.apply();

    }

    public static void AddTotokenSharedpassword(Activity activity, String password) {
        loginFile = activity.getSharedPreferences("LOGIN_FILE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = loginFile.edit();
        editor.putString("password", password);

        editor.apply();

    }

//    public static void plsGoLogin(final Activity activity) {
//        final AlertDialog.Builder alert = new AlertDialog.Builder(
//                activity);
//        alert.setTitle(R.string.Error);
//        alert.setMessage(R.string.plz_login_first);
//        alert.setPositiveButton(R.string.cancel,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        alert.setNegativeButton(R.string.login,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent i = new Intent(activity, LoginActivity.class);
//                        activity.startActivity(i);
//                    }
//                });
//        alert.show();
//    }
}
