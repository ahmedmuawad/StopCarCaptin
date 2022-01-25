package com.stopgroup.stopcar.captain.api;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.helper.Dialogs;
import com.stopgroup.stopcar.captain.helper.ErrorResponse;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.stopgroup.stopcar.captain.helper.LoginSession.loginFile;


public class APIModel {
    public final static int take = 10;
    public static String BASE_URL = "https://uberclone.club/api/v1/";
    // when app version is old
    public final static int FORCE_UPDATE = 451;
    // when user blocked
    public final static int BLOCK = 456;
    // when token expired
    public final static int REFRESH_TOKEN = 401;
    public final static int SUCCESS = 200;
    public final static int CREATED = 201;
    public final static int Failer = 422;
    public final static int BAD_REQUEST = 400;
    public final static int UNAUTHORIZE = 401;
    public final static int SERVER_ERROR = 500;
    public final static int USER_DELETE = 403;
    public final static int Not_FOUND = 404;
    public final static int Error = 409;
    public static String version = "v1";
    public static String device_type = "1";
    public static String methodRunning = "";

    public static void handleFailure(final Activity activity, int statusCode, String errorResponse, final RefreshTokenListener listener) {
       if (activity==null){
           return;
       }
        Log.e("fail", statusCode + "--" + errorResponse);
        Type dataType = new TypeToken<ErrorResponse>() {
        }.getType();
        ErrorResponse responseBody = new ErrorResponse();
        try {
            if (statusCode != SERVER_ERROR)
                responseBody = new Gson().fromJson(errorResponse, dataType);

        } catch (Exception e) {
        }
        switch (statusCode) {
            case BAD_REQUEST:
                if(!methodRunning.equals("update")){
                    if (responseBody.getMessage().equals("")) {
                        Dialogs.showToast(responseBody.getMsg() != null ?
                                responseBody.getMsg() : "", activity);
                    } else {
                        Dialogs.showToast(responseBody.getMessage() != null ?
                                responseBody.getMessage() : "", activity);
                    }
                }

                break;
            case Not_FOUND:
                if (responseBody.getMessage().equals("")) {
                    Dialogs.showToast(responseBody.getMsg() != null ?
                            responseBody.getMsg() : "", activity);
                } else {
                    Dialogs.showToast(responseBody.getMessage() != null ?
                            responseBody.getMessage() : "", activity);
                }
                ((Activity) activity).finish();
                break;
            case Failer:
            case Error:
                if (responseBody.getMessage().equals("")) {
                    Dialogs.showToast(responseBody.getMsg() != null ?
                            responseBody.getMsg() : "", activity);
                } else {
                    Dialogs.showToast(responseBody.getMessage() != null ?
                            responseBody.getMessage() : "", activity);
                }
                break;
            case USER_DELETE:
                loginFile = activity.getSharedPreferences("LOGIN_FILE", 0);
                LoginSession.clearData((Activity) activity);
                Toast.makeText(activity, R.string.user_deleted, Toast.LENGTH_SHORT).show();
                break;
            case REFRESH_TOKEN:
                RequestParams requestParams = new RequestParams();
                requestParams.put("refresh_token",LoginSession.refresh_token);
                APIModel.postMethod( activity, "refresh/token",requestParams, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        if (statusCode==401){
                            LoginSession.clearData((Activity) activity);
                        }

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseString);

                            String x = loginFile.getString("json", "");
                            x = x.replaceAll(LoginSession.token, jsonObject.getString("access_token"));
                            x = x.replaceAll(LoginSession.refresh_token, jsonObject.getString("refresh_token"));
                            x = x.replaceAll(Long.toString(LoginSession.expired), Long.toString(jsonObject.getLong("expires_in")));
//                                JSONObject jsonObject1 = new JSONObject(x);
//                                JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
//                                jsonObject.put("data", jsonObject2.toString());
                            SharedPreferences.Editor editor = loginFile.edit();
                            editor.putString("json", x);
                            editor.putString("type", "3");
                            editor.apply();
                            LoginSession.setdata((Activity) activity);
                            listener.onRefresh();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
            default:
                Dialogs.showToast(activity.getString(R.string.no_network), activity);
        }
    }

    public static void userResponses(final Activity activity, int code) {
        switch (code) {
            case FORCE_UPDATE:
                //  IntentClass.goToActivityAndClear(activity, ForceUpdateActivity.class, null);
                break;
            case BLOCK:
                //  IntentClass.goToActivityAndClear(activity, BlockActivity.class, null);
                break;
            case REFRESH_TOKEN:
//                SharedPref sharedPref = new SharedPref(activity);
//  sharedPref.setToken(response.body().getTokenResponse().getToken());
                try {
//                    func.notify();
                } catch (Exception e) {
                }
                break;

        }
    }

    public static AsyncHttpClient getMethod(Context currentActivity, String url, TextHttpResponseHandler textHttpResponseHandler) {
        methodRunning = "";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(99999999);
        client.addHeader("Authorization", "Bearer " + LoginSession.token);
        client.addHeader("version", version + "");
        client.addHeader("lang", Locale.getDefault().getLanguage());
        Log.e("Authorization", "Bearer " + LoginSession.token);

//        client.addHeader("token", FirebaseInstanceId.getInstance().getToken());

        client.get(BASE_URL + url, textHttpResponseHandler);

        return client;
    }
    public static AsyncHttpClient getMethod1(Context currentActivity, String url, TextHttpResponseHandler textHttpResponseHandler) {
        methodRunning = "";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(99999999);
        client.addHeader("Authorization", "Bearer " + LoginSession.token);
        client.addHeader("version", version + "");
        client.addHeader("lang", Locale.getDefault().getLanguage());
        Log.e("data", "Bearer " + LoginSession.token);

//        client.addHeader("token", FirebaseInstanceId.getInstance().getToken());

        client.get(BASE_URL + url, textHttpResponseHandler);

        return client;
    }

    public static AsyncHttpClient postMethod(Activity currentActivity, String url, RequestParams params, TextHttpResponseHandler textHttpResponseHandler) {
        methodRunning = "";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(9999999);
        client.setResponseTimeout(9999999);
        client.setTimeout(9999999);

        client.addHeader("Authorization", "Bearer " + LoginSession.token);

        client.addHeader("version", version + "");
        client.addHeader("lang", Locale.getDefault().getLanguage());
        client.post(BASE_URL + url, params, textHttpResponseHandler);

        return client;
    }

    public static AsyncHttpClient putMethod(Activity currentActivity, String url, RequestParams params, TextHttpResponseHandler textHttpResponseHandler) {
        methodRunning = "update";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(9999999);
        client.addHeader("Authorization", "Bearer " + LoginSession.token);

        client.addHeader("version", version + "");
        client.addHeader("lang", Locale.getDefault().getLanguage());
        client.put(BASE_URL + url, params, textHttpResponseHandler);
        return client;
    }
    public static AsyncHttpClient putMethods(Context currentActivity, String url, RequestParams params, TextHttpResponseHandler textHttpResponseHandler) {
        methodRunning = "update";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(20000);
        SharedPreferences loginFile;
        loginFile = currentActivity.getSharedPreferences("LOGIN_FILE", 0);
        client.addHeader("Authorization", "Bearer " + LoginSession.token);
        Log.e("data", "Bearer " + LoginSession.token);
        client.addHeader("version", version + "");
        client.addHeader("lang", Locale.getDefault().getLanguage());
        client.put(BASE_URL + url, params, textHttpResponseHandler);
        return client;
    }
    public static AsyncHttpClient deleteMethod(Activity currentActivity, String url, TextHttpResponseHandler textHttpResponseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(20000);
        SharedPreferences loginFile;
        loginFile = currentActivity.getSharedPreferences("LOGIN_FILE", 0);

        client.addHeader("Authorization", "Bearer " + LoginSession.token);
        client.addHeader("version", version + "");
        client.addHeader("lang", Locale.getDefault().getLanguage());
        client.delete(BASE_URL + url, textHttpResponseHandler);

        return client;
    }


    public static AsyncHttpClient tokenPost(Activity currentActivity, String url, RequestParams params, TextHttpResponseHandler textHttpResponseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("version", version + "");
        client.post(BASE_URL + url, params, textHttpResponseHandler);

        return client;
    }

    public interface RefreshTokenListener {
        void onRefresh();
    }

}