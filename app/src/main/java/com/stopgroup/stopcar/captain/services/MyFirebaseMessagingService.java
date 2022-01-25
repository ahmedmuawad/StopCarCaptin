package com.stopgroup.stopcar.captain.services;

/**
 * Created by TareQ on 11/15/2016.
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.activity.CompletetripActivity;
import com.stopgroup.stopcar.captain.activity.Splash;
import com.stopgroup.stopcar.captain.activity.MapActivity;
import com.stopgroup.stopcar.captain.activity.ReviewTripActivity;
import com.stopgroup.stopcar.captain.activity.TripActivity;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.modules.Order;
import com.stopgroup.stopcar.captain.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    public static boolean b = false;

    String body;
    String title;
    String type;
    String username;
    String msg_id;
    Intent intent;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("noti", remoteMessage.getData().toString() + "--");
        JSONObject jo = new JSONObject(remoteMessage.getData());

            if (remoteMessage.getData().toString().contains("\"type\":10")) {
                type = "10" ;
            }else {
                type = "0";
            }

                Log.e("type", type+"" );

        try {

//                if (!foregrounded()) {
                    buildNotif(jo.getString("title"), jo.getString("body"));
//                } else {
//                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
//                }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    void buildNotif(String title, String desc) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notificationIntent = null;
        if (type.equals("10")){
            notificationIntent = new Intent(getApplicationContext(), CompletetripActivity.class);
        }else {
            notificationIntent = new Intent(getApplicationContext(), Splash.class);
        }
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = null;

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.pin)
                    .setLargeIcon(bitmap1)
                    .setContentTitle(title)
                    .setContentText(desc)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(contentIntent);
        } else {
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.pin)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(contentIntent);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        Random random = new Random();
        int notificationID = random.nextInt(9999 - 1000) + 1000;
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }

    //        noti = getSharedPreferences("noti",0);
//        loginFile = getSharedPreferences(
//                "USER_DATA", 0);
//        userId = loginFile.getString("USER_ID","");
//
//
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean foregrounded() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE);
    }

    private void current_order(final String type) {
        APIModel.getMethod1(getApplicationContext(), "trips/driver/current", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure((Activity) getApplicationContext(), statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        current_order(type);
                    }
                });

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (!responseString.equals("{\n" +
                        "    \"result\": {},\n" +
                        "    \"statusCode\": 200,\n" +
                        "    \"statusText\": \"OK\"\n" +
                        "}")) {
                    try {
                        if (type == "0") {
                            Type dataType = new TypeToken<Order>() {
                            }.getType();
                            Order data = new Gson().fromJson(responseString, dataType);
                            if (data.result.status == 0) {
                                Intent i = new Intent(getApplicationContext(), MapActivity.class);
                                i.putExtra("data", responseString);
                                startActivity(i);
                            } else if (data.result.status == 6) {
                                Intent i = new Intent(getApplicationContext(), CompletetripActivity.class);
                                i.putExtra("data", responseString);
                                startActivity(i);
                            } else if (data.result.status == 7) {
                                Intent i = new Intent(getApplicationContext(), ReviewTripActivity.class);
                                i.putExtra("data", responseString);
                                startActivity(i);
                            } else {
                                if (data.result.status != -1) {
                                    Intent i = new Intent(getApplicationContext(), TripActivity.class);
                                    i.putExtra("data", responseString);
                                    startActivity(i);
                                }
                            }
                        } else {
                            Type dataType = new TypeToken<Order>() {
                            }.getType();
                            Order data = new Gson().fromJson(responseString, dataType);
                            Intent i;
                            if (data.result.status == 0) {
                                i = new Intent(getApplicationContext(), MapActivity.class);
                                i.putExtra("data", responseString);
                                startActivity(i);
                            } else if (data.result.status == 6) {
                                 i = new Intent(getApplicationContext(), CompletetripActivity.class);
                                i.putExtra("data", responseString);
                                startActivity(i);
                            } else if (data.result.status == 7) {
                                i = new Intent(getApplicationContext(), ReviewTripActivity.class);
                                i.putExtra("data", responseString);
                                startActivity(i);
                            } else {
                                if (data.result.status != -1) {
                                     i = new Intent(getApplicationContext(), TripActivity.class);
                                    i.putExtra("data", responseString);
                                    startActivity(i);
                                }
                            }
                        }
                    } catch (Exception e) {
e.printStackTrace();
                    }
                }

            }
        });
    }
}

//