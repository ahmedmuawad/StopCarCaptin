package com.stopgroup.stopcar.captain.activity;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.LocationStoreHelper;
import com.stopgroup.stopcar.captain.helper.LocationStoreHelperBackground;
import com.stopgroup.stopcar.captain.helper.OpenGps;
import com.stopgroup.stopcar.captain.helper.parser;
import com.stopgroup.stopcar.captain.modules.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.fancybuttons.FancyButton;
public class TripActivity extends ChatActivity implements OnMapReadyCallback {
    Polyline polylineFinal;
    PolylineOptions lineOptions = null;
    LatLng myLatLng;
    private ImageView back;
    private TextView title;
    private TextView trips;
    private TextView totalhours;
    private TextView earning;
    private FancyButton trip;
    private TextView note_txv;
    // private TextView pick_up;
    private TextView titleloc;
    private TextView loc;
    private View callBtn;
    private TextView deliverBtn;
    private View cancelBtn;
    private CircleImageView img;
    private String clientPhone = "";
    private LatLng clientLocation;
    public Order data;
    GoogleMap mMap;
    Marker marker, marker1;
    int status;
    private FusedLocationProviderClient mFusedLocationClient;
    private LinearLayout lin;
    private View card;
    private ProgressBar progress;
    Handler handler;
    Runnable runnable;

    public static TripActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_trip);
        initView();
        onclick();
        OpenGps.checkgps(TripActivity.this);
        Log.e("myLoc_1", myLatLng + "");
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                current_order(TripActivity.this);
                Log.d("current_order", "current_order");
                handler.postDelayed(this, 7000);
            }
        };

        activity = this;


        Intent i = new Intent(getApplicationContext(), LocationStoreHelperBackground.class);
        i.putExtra("tripID", TripActivity.activity.data.result.id);
        startService(i);
    }
    @Override
    public void onStart() {
        super.onStart();
        if (handler != null && runnable != null) {
            handler.postDelayed(runnable, 0);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }

    public void listenOnLocationUpdated(Double lat , Double lng) {
        //The last location in the list is the newest
        myLatLng = new LatLng(lat, lng);

        try {
            marker1.setPosition(myLatLng);
        } catch (Exception e) {
        }
// move the camera zoom to the location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 12));
    }
    //0 = pending
//1 = accepted
//4 = arrived
//5 = start
//6 = complete
//7 = collecting
    private void current_order(final Activity activity) {
        APIModel.getMethod(activity, "trips/driver/current", new TextHttpResponseHandler() {
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
                        data = dataResult;
                        if (data.result == null || data.result.id == 0) {
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            Toast.makeText(activity, getText(R.string.trip_canceld), Toast.LENGTH_LONG).show();
                        } else {
                            status = data.result.status;
                            clientPhone = data.result.client.mobile;
                            try {
                                clientLocation = new LatLng(Double.parseDouble(data.result.to_lat), Double.parseDouble(data.result.to_lng));
                            } catch (Exception a) {
                                clientLocation = new LatLng(data.result.from_lat, data.result.from_lng);
                                a.printStackTrace();
                            }
                            Picasso.get().load(data.result.client.image).into(img);
                            trips.setText(data.result.client.first_name + " " + data.result.client.last_name);
                            String date = "";
                            SimpleDateFormat spf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                            Date newDate = null;
                            try {
                                newDate = spf.parse(data.result.created_at);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            spf = new SimpleDateFormat("hh:mm aa");
                            date = spf.format(newDate);
                            totalhours.setText(date);
                            earning.setText(data.result.distance + " " + "Km\nDistance");
                            /** check cancel **/
                            cancelBtn();
                            /** **/
                            if (status == 1) {
                                trip.setText(getString(R.string.iarriaved));
                                loc.setText(data.result.from_location);
                            } else if (status == 4) {
                                trip.setText(getString(R.string.start_trip));
                                loc.setText(data.result.to_location);
                            } else if (status == 5) {
                                trip.setText(getString(R.string.end_trip));
                                loc.setText(data.result.to_location);
                            }
                            try {
                                if (!data.result.to_location.equals("")) {
                                    //Log.e("_0", "onSuccess:");
                                    String url = getDirectionsUrl(new LatLng(data.result.from_lat, data.result.from_lng), new LatLng(Double.parseDouble(data.result.to_lat), Double.parseDouble(data.result.to_lng)));
                                    DownloadTask downloadTask = new DownloadTask();
                                    downloadTask.execute(url);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //Log.e("_1", "onSuccess: ");
//                                String url = getDirectionsUrl(new LatLng(myLatLng.latitude, myLatLng.longitude), new LatLng(data.result.from_lat, data.result.from_lng));
//                                DownloadTask downloadTask = new DownloadTask();
//                                downloadTask.execute(url);
                            }
                        }
                        String myUid = "driver_" + data.result.id;
                        String myName = "Me";
                        String myImage = "https://www.klrealty.com.au/wp-content/uploads/2018/11/user-image-.png";
                        String hisUid = "user_" + data.result.id;
                        String hisName = data.result.client.first_name + " " + data.result.client.last_name;
                        String hisImage = data.result.client.image;
                        startChat(myUid, myName, myImage, hisUid, hisName, hisImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
    }
    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        callBtn = findViewById(R.id.callBtn);
        deliverBtn = findViewById(R.id.deliverBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        title =  findViewById(R.id.title);
        trips =  findViewById(R.id.trips);
        totalhours =  findViewById(R.id.totalhours);
        earning =  findViewById(R.id.earning);
        trip =  findViewById(R.id.trip);
        note_txv =  findViewById(R.id.note_txv);
        // pick_up =  findViewById(R.id.pick_up);
        titleloc =  findViewById(R.id.titleloc);
        loc =  findViewById(R.id.loc);
        img = (CircleImageView) findViewById(R.id.img);
        lin = (LinearLayout) findViewById(R.id.lin);
        card = (View) findViewById(R.id.card);
        progress = (ProgressBar) findViewById(R.id.progress);
        title.setText(getString(R.string.On_Trip));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(TripActivity.this);
        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Type dataType = new TypeToken<Order>() {
        }.getType();
        data = new Gson().fromJson(getIntent().getStringExtra("data"), dataType);
        status = data.result.status;
        Picasso.get().load(data.result.client.image).into(img);
        trips.setText(data.result.client.first_name + " " + data.result.client.last_name);
        String date = "";
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = spf.parse(data.result.created_at);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("hh:mm aa");
        date = spf.format(newDate);
        totalhours.setText(date);
        earning.setText(data.result.distance + " " + "Km\nDistance");
        if (status == 1) {
            trip.setText(getString(R.string.iarriaved));
            loc.setText(data.result.from_location);
        } else if (status == 4) {
            trip.setText(getString(R.string.start_trip));
            loc.setText(data.result.to_location);
        } else if (status == 5) {
            trip.setText(getString(R.string.end_trip));
            loc.setText(data.result.to_location);
        }
        try {
            if (!data.result.to_location.equals("")) {
                String url = getDirectionsUrl(new LatLng(data.result.from_lat, data.result.from_lng), new LatLng(Double.parseDouble(data.result.to_lat), Double.parseDouble(data.result.to_lng)));
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            }
        } catch (Exception e) {
        }
        if (data.result.category_calculating_pricing.equals("delivery") || !TextUtils.isEmpty(data.result.trip_special_notes)) {
            note_txv.setVisibility(View.VISIBLE);
            //pick_up.setVisibility(View.GONE);
        }
        note_txv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.result.category_calculating_pricing.equals("delivery"))
                    shownotes(TripActivity.this, data.result.trip_delivery_note);
                if (!TextUtils.isEmpty(data.result.trip_special_notes))
                    shownotes(TripActivity.this, data.result.trip_special_notes);
            }
        });
        lin.setVisibility(View.VISIBLE);
        card.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        cancelBtn();
    }
    private void cancelBtn() {
        if (data.result.status == 1) {
            cancelBtn.setVisibility(View.VISIBLE);
        } else {
            cancelBtn.setVisibility(View.GONE);
        }
    }
    private void onClickCancel() {
        String url = "trips/driver/cancel";
        APIModel.getMethod(TripActivity.this, url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                APIModel.handleFailure(TripActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        onClickCancel();
                    }
                });
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("data", responseString);
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
    }
    public static void shownotes(Activity activity, String notes) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_notes);
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        dialog.getWindow().setLayout((width * 17 / 20), LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView notesTxv = dialog.findViewById(R.id.notes);
        notesTxv.setText(notes);
        dialog.show();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap = googleMap;
        mMap.clear();
        LatLng latLng = new LatLng(data.result.from_lat, data.result.from_lng);
        BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.drawable.pin);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .icon(map_client)
                .title("my location");
        marker = mMap.addMarker(options);
// move the camera zoom to the location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(TripActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.drawable.car1);
                            MarkerOptions options = new MarkerOptions()
                                    .position(myLatLng)
                                    .icon(map_client)
                                    .title("my location");
                            marker1 = mMap.addMarker(options);
// move the camera zoom to the location
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 12));
                            // Logic to handle location object
                        }
                    }
                });
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                try {
                    //marker1.remove();
                } catch (Exception e) {
                }
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.drawable.car1);
//                MarkerOptions options = new MarkerOptions()
//                        .position(latLng)
//                        .icon(map_client)
//                        .title(getString(R.string.my_location));
//
//                marker1 = mMap.addMarker(options);
//// move the camera zoom to the location
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            }
        });
    }
    private void arriave() {
        APIModel.getMethod(TripActivity.this, "trips/arrive", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                APIModel.handleFailure(TripActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        arriave();
                    }
                });
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                status = 4;
                if (data.result.type.equals("delivery"))
                    status = 5;
                if (status == 1) {
                    deliverBtn.setVisibility(View.GONE);
                    trip.setText(getString(R.string.iarriaved));
                    loc.setText(data.result.from_location);
                } else if (status == 4) {
                    deliverBtn.setVisibility(View.VISIBLE);
                    trip.setText(getString(R.string.start_trip));
                    loc.setText(data.result.to_location);
                } else if (status == 5) {
                    deliverBtn.setVisibility(View.VISIBLE);
                    trip.setText(getString(R.string.end_trip));
                    loc.setText(data.result.to_location);
                }
            }
        });
    }
    private void starttrip() {
        APIModel.getMethod(TripActivity.this, "trips/start", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                APIModel.handleFailure(TripActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        starttrip();
                    }
                });
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    marker.remove();
                } catch (Exception e) {
                }
                status = 5;
                if (status == 1) {
                    trip.setText(getString(R.string.iarriaved));
                    loc.setText(data.result.from_location);
                } else if (status == 4) {
                    trip.setText(getString(R.string.start_trip));
                    try {
                        if (!data.result.to_location.equals("")) {
                            loc.setText(data.result.to_location);
                        } else {
                            loc.setText(getString(R.string.no_loc));
                        }
                    } catch (Exception e) {
                        loc.setText(getString(R.string.no_loc));
                    }
                } else if (status == 5) {
                    trip.setText(getString(R.string.end_trip));
                    try {
                        if (!data.result.to_location.equals("")) {
                            loc.setText(data.result.to_location);
                        } else {
                            loc.setText(getString(R.string.no_loc));
                        }
                    } catch (Exception e) {
                        loc.setText(getString(R.string.no_loc));
                    }
                }
            }
        });
    }
    private void endtrip() {
        double lat;
        double lng;
        double distance;
        double delay;
        try {
            LocationStoreHelper    locationHelper  = new LocationStoreHelper(this ,TripActivity.activity.data.result.id);
            Location location = locationHelper.getLastPoint();
            distance =locationHelper.getDistance();
            delay = locationHelper.getDelayInMinutes();
            lat = location.getLatitude();
            lng = location.getLongitude();
        } catch (Exception e) {
            lat = myLatLng.latitude;
            lng = myLatLng.longitude;
            distance = 0;
            delay = 0;
        }
        String url = String.format("trips/complete?to_lat=%s&to_lng=%s&distance=%s&delay=%s", lat, lng, distance, delay);
        APIModel.getMethod(TripActivity.this, url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                APIModel.handleFailure(TripActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        endtrip();
                    }
                });
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //Log.e("data", responseString);
                Intent i = new Intent(getApplicationContext(), CompletetripActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
    }
    private void onclick() {
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clientPhone.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + clientPhone));
                    startActivity(intent);
                }
            }
        });
        deliverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clientLocation != null) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("https://maps.google.com/maps?saddr=" + "&daddr=" + clientLocation.latitude + "," + clientLocation.longitude));
                    startActivity(intent);
                } else {
                    if (clientLocation != null) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("https://maps.google.com/maps?saddr=" + "&daddr=" + myLatLng.latitude + "," + myLatLng.longitude));
                        startActivity(intent);
                    }
                }
            }
        });
        cancelBtn.setOnClickListener(v -> {
            onClickCancel();
        });
        trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == 1) {
                    arriave();
                } else if (status == 4) {
                    starttrip();
                } else if (status == 5) {
                    endtrip();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // onBackPressed();
            }
        });
    }
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + ","
                + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String key = "key=AIzaSyBYFoqMrU8982wmwFFyAj3IAPTaTKZkmaI";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + key;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters;
        return url;
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            // Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            final JSONObject json;
            try {
                json = new JSONObject(result);
                JSONArray routeArray = json.getJSONArray("routes");
                JSONObject routes = routeArray.getJSONObject(0);
                JSONArray newTempARr = routes.getJSONArray("legs");
                JSONObject newDisTimeOb = newTempARr.getJSONObject(0);
                JSONObject distOb = newDisTimeOb.getJSONObject("distance");
                JSONObject timeOb = newDisTimeOb.getJSONObject("duration");
                Log.i("Diatance :", distOb.getString("text"));
                Log.i("Time :", timeOb.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ParserTask3 parserTask = new ParserTask3();
            parserTask.execute(result);
        }
    }
    private class ParserTask3 extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                parser parser = new parser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            try {
                polylineFinal.remove();
            } catch (NullPointerException a) {
                a.printStackTrace();
            }
            //Log.e("results", result + "");
            try {
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = result.get(i);
                    //Log.e("points", path + "");
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        if (j == 0) {
                        } else if (j == 1) {
                        } else if (j > 1) {
                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            points.add(position);
                        }
                    }
                    lineOptions.addAll(points);
                    lineOptions.width(5);
                    lineOptions.color(Color.parseColor("#0000FF"));
                }
                polylineFinal = mMap.addPolyline(lineOptions);
            } catch (NullPointerException e) {
            }
        }
    }
}
