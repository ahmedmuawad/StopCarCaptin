package com.stopgroup.stopcar.captain.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.stopgroup.stopcar.captain.helper.GlobalVariables;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Created by H on 08/06/2016.
 */
public class GetdistanceService extends Service {
    public static final int TWO_MINUTES = 5000; // 120 seconds
    public static Boolean isRunning = false;

    public LocationManager mLocationManager;
    public LocationUpdaterListener mLocationListener;
    public Location previousBestLocation = null;
    SharedPreferences storedata;
    private static String filename = "mlogin";
    String id,type ;
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    PowerManager.WakeLock wakeLock;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationUpdaterListener();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire();
        super.onCreate();
    }

    Handler mHandler = new Handler();
    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) {
                startListening();
            }
            mHandler.postDelayed(mHandlerTask, TWO_MINUTES);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandlerTask.run();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopListening();
        mHandler.removeCallbacks(mHandlerTask);
//        wakeLock.release();
//        Gdata.mSocket.connect();
        super.onDestroy();
    }

    private void startListening() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, mLocationListener);

        if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mLocationListener);
//        }
        isRunning = true;
    }

    private void stopListening() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.removeUpdates(mLocationListener);
        // }
        isRunning = false;
    }

    public class LocationUpdaterListener implements LocationListener {
        @Override
        public void onLocationChanged(final Location location) {
            if (isBetterLocation(location, previousBestLocation)) {
                previousBestLocation = location;
                if (latitude==0.0){
                    GlobalVariables.distance  = 0.0;
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }else {
                    Location loc1 = new Location("");
                    loc1.setLatitude(location.getLatitude());
                    loc1.setLongitude(location.getLongitude());

                    Location loc2 = new Location("");
                    loc2.setLatitude(latitude);
                    loc2.setLongitude(longitude);

                    float distanceInMeters = loc1.distanceTo(loc2);
                    Double d = Double.parseDouble(String.valueOf(distanceInMeters)) ;
                   GlobalVariables.distance = GlobalVariables.distance + d ;
                }

                Log.e("latitude", location.getLatitude() + "");
                Log.e("longitude", location.getLongitude() + "");

            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            stopListening();
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

//    private Socket mSocket;
//
//    {
//        try {
//            Gdata.mSocket = IO.socket("https://192.168.1.17:800");
//        } catch (URISyntaxException e) {
//        }
//    }


    public  String excutePost(String targetURL, String urlParameters) {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast t = Toast.makeText(getApplicationContext(), "تاكد من اتصالك بالانترنت", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
            });

            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}