package com.stopgroup.stopcar.captain.helper;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.activity.Splash;
import com.stopgroup.stopcar.captain.activity.TripActivity;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.modules.Order;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
/**
 * Created by H on 08/06/2016.
 */
public class LocationStoreHelperBackground extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private   LocationStoreHelper locationStore;
    private int tripID = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public static final String CHANNEL_ID = "map_task";
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, Splash.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("رحلة ستوب كار")
                .setContentText("هناك رحلة قيد التقدم")
                .setSmallIcon(R.drawable.placeholder)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        super.onCreate();

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        if (intent != null) {
            tripID = intent.getIntExtra("tripID", 0);
            if (checkPlayServices()) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API).build();
                mGoogleApiClient.connect();
            }
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(4000);
            mLocationRequest.setFastestInterval(4000);
            mLocationRequest.setSmallestDisplacement(100);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationStore = new LocationStoreHelper(this,tripID);

            setupLocationManager();
        }
        return START_STICKY;
    }
    private void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    @Override
    public void onDestroy() {
        stopLocationUpdates();

        super.onDestroy();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //To check whether location settings are good to proceed or not.
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                if (task.isSuccessful()) {
                    if (ActivityCompat.checkSelfPermission(LocationStoreHelperBackground.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationStoreHelperBackground.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, LocationStoreHelperBackground.this);
                    startGettingLocation();
                }
            }
        });
    }
    @SuppressLint("MissingPermission")
    public void startGettingLocation() {
        setLocation(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
    }
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    @Override
    public void onLocationChanged(Location location) {
        setLocation(location);
    }
    public void OnGetLocation(Double latitude, Double longitude) {

        Log.e("location_api_service:", latitude + " , " + longitude + "");
        try {
            APIModel.getMethod(LocationStoreHelperBackground.this, "trips/driver/current", new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                APIModel.handleFailure(HomeActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
//                    @Override
//                    public void onRefresh() {
//                        current_order();
//                    }
//                });
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    //Log.e("trip_data1", responseString);
                    if (!responseString.equals("{\n" +
                            "    \"result\": {},\n" +
                            "    \"statusCode\": 200,\n" +
                            "    \"statusText\": \"OK\"\n" +
                            "}")) {
                        try {
                            Type dataType = new TypeToken<Order>() {
                            }.getType();
                            Order dataResult = new Gson().fromJson(responseString, dataType);
                            if (dataResult.result.status == 5) {
                                locationStore.calculateDistance(latitude, longitude, tripID);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
        }
        Log.e("distance IS: ==", locationStore.getDistance() + "");
        /** updatemap marker **/
        try {
            TripActivity.activity.listenOnLocationUpdated(latitude, latitude);
        } catch (Exception e) {
        }
    }
    @SuppressLint("MissingPermission")
    public void setupLocationManager() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            Location bestLocation = null;
            try {
                Criteria mFineCriteria = new Criteria();
                mFineCriteria.setAccuracy(Criteria.ACCURACY_FINE);
                mFineCriteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
                mFineCriteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
                mFineCriteria.setBearingRequired(true);
                String provider = locationManager.getBestProvider(mFineCriteria, true);
                if (provider != null) {
                    bestLocation = locationManager.getLastKnownLocation(provider);
                }
            } catch (Exception e) {
            }
            if (bestLocation == null) {
                List<String> list = locationManager.getAllProviders();
                if (list.isEmpty()) {
                    Toast.makeText(LocationStoreHelperBackground.this, "رجاء قم بالاتصال بال wifi او تاكد من تغطية ال gps لمنطقتك", Toast.LENGTH_SHORT).show();
                } else {
                    for (String provider : list) {
                        Location i = locationManager.getLastKnownLocation(provider);
                        if (i == null) {
                            continue;
                        }
                        if (bestLocation == null || i.getAccuracy() < bestLocation.getAccuracy()) {
                            bestLocation = i;
                        }
                    }
                }
            }
            if (bestLocation != null) {
                OnGetLocation(bestLocation.getLatitude(), bestLocation.getLongitude());
            }
        }
    }
    private boolean checkPlayServices() {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        return resultCode == ConnectionResult.SUCCESS;
    }
    boolean showLocatedToast = true;
    public void setLocation(Location location) {
        if (location != null) {
            if (showLocatedToast) {
                //Toast.makeText(this, "تهانينا تم تحديد الموقع بنجاح", Toast.LENGTH_SHORT).show();
                showLocatedToast = false;
            }
            OnGetLocation(location.getLatitude(),location.getLongitude());
        }
    }
}