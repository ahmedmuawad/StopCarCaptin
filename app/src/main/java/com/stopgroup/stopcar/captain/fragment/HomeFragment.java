package com.stopgroup.stopcar.captain.fragment;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.adapter.RequestsCompanyAdapter;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.modules.History;
import com.stopgroup.stopcar.captain.modules.RequestsForCompany;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;
/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final int REQUEST_LOCATION = 001;
    public static GoogleApiClient googleApiClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    PendingResult<LocationSettingsResult> pendingResult;
    public void mEnableGps() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        mLocationSetting();
    }
    public void mLocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);
        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        mResult();
    }
    public void mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    //callback method
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Toast.makeText(getActivity(), "Gps enabled", Toast.LENGTH_SHORT).show();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(this).attach(this).commit();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(getActivity(), "Gps Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
//   Toast.makeText(getActivity(),"onnconnected_5555",Toast.LENGTH_SHORT);
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    private static final String TAG = "dd";
    private GoogleMap mMap;
    private Marker mMarcadorActual;
    private FusedLocationProviderClient mFusedLocationClient;
    String lat = "", lon = "", location = "";
    View view;
    private TextView trips;
    private TextView totalhours;
    private TextView earning;
    private LinearLayout lin;
    String category_id, category_name;
    private FrameLayout frameMap;
    private FrameLayout linOrders;
    private RecyclerView list;
    private TextView textNoRequests;
    ProgressBar progressBar;
    SupportMapFragment mapFragment;
    Handler handler;
    Runnable runnable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        category_id = LoginSession.getlogindata(this.getActivity()).result.driver.category_id + "";
        category_name = LoginSession.getlogindata(this.getActivity()).result.driver.category_calculating_pricing + "";
        initView();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e(TAG, "gps enabled");
        } else {
            mEnableGps();
        }
//        OpenGps.checkgps(getActivity());
        return view;
    }
    private void initView() {
        trips =  view.findViewById(R.id.trips);
        totalhours =  view.findViewById(R.id.totalhours);
        earning =  view.findViewById(R.id.earning);
        lin = (LinearLayout) view.findViewById(R.id.lin);
        frameMap = (FrameLayout) view.findViewById(R.id.frame_map);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        linOrders = (FrameLayout) view.findViewById(R.id.lin_orders);
        list = (RecyclerView) view.findViewById(R.id.list);
        textNoRequests =  view.findViewById(R.id.text_no_requests);
        mapFragment =
                (SupportMapFragment)
                        getChildFragmentManager().findFragmentById(R.id.map);
        isModeHistory();
    }
    public void isModeHistory() {
        try {
            String mode = getActivity().getIntent().getStringExtra("mode");
            if (mode.equals("history")) {
                list.setVisibility(View.VISIBLE);
                handler = new Handler(Looper.getMainLooper());
                String category = LoginSession.getlogindata(getActivity()).result.driver.category_calculating_pricing;
                if (category.equals("companies") || category.equals("truck_water") || category.equals("tanks")) {
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            getHomeCompany();
                            handler.postDelayed(this, 7000);
                        }
                    };
                    handler.postDelayed(runnable, 7000);
                }
            } else {
                list.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            list.setVisibility(View.GONE);
        }
    }
    public boolean checkModeHistory() {
        try {
            String mode = getActivity().getIntent().getStringExtra("mode");
            if (mode.equals("history")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.clear();
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    setLocationNow(location);
                }
            }
        }, Looper.getMainLooper());
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        setLocationNow(location);
                    }
                });
    }
    public void setLocationNow(Location location) {
        // Got last known location. In some rare situations this can be null.
        if (location != null) {
            mMap.clear();
            lat = String.valueOf(location.getLatitude());
            lon = String.valueOf(location.getLongitude());
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.drawable.car1);
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .icon(map_client)
                    .title("my location");
            mMap.addMarker(options);
// move the camera zoom to the location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            // Logic to handle location object
        }
    }
    @Override
    public void onResume() {
//        OpenGps.checkgps(getActivity());
        if (AccountFragment.isHistoryEnable) {
            AccountFragment.isHistoryEnable = false;
            frameMap.setVisibility(View.GONE);
            lin.setVisibility(View.GONE);
            linOrders.setVisibility(View.VISIBLE);
            getHomeCompany();
        } else {
            frameMap.setVisibility(View.VISIBLE);
            mapFragment.getMapAsync(this);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            linOrders.setVisibility(View.GONE);
        }
//        if (category_name.equals("private_car") || category_name.equals("truck") || category_name.equals("tok_tok") || category_name.equals("save_car") || category_name.toLowerCase().equals("delivery")) {
//            frameMap.setVisibility(View.VISIBLE);
//            mapFragment.getMapAsync(this);
//            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
//            linOrders.setVisibility(View.GONE);
//            //gethistory();
//
//        }
//        else {
//            frameMap.setVisibility(View.GONE);
//            lin.setVisibility(View.GONE);
//            linOrders.setVisibility(View.VISIBLE);
//            getHomeCompany();
//        }
        super.onResume();
    }
    private void gethistory() {
        progressBar.setVisibility(View.VISIBLE);
        APIModel.getMethod(getActivity(), "driver/history", new TextHttpResponseHandler() {
            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                APIModel.handleFailure(getActivity(), statusCode, responseString, new APIModel.RefreshTokenListener() {
//                    @Override
//                    public void onRefresh() {
//                        gethistory();
//                    }
//                });
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("gethistory", responseString);
                Type dataType = new TypeToken<History>() {
                }.getType();
                History data = new Gson().fromJson(responseString, dataType);
                earning.setText(data.result.today.statistics.total_earned + " " + LoginSession.getlogindata(getActivity()).result.currency);
                totalhours.setText(data.result.today.statistics.spend_time + " " + getString(R.string.online));
                trips.setText(data.result.today.statistics.count_trips + " " + getString(R.string.trips));
                lin.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void getHomeCompany() {
        progressBar.setVisibility(View.VISIBLE);
        APIModel.getMethod(getActivity(), "driver/home", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                APIModel.handleFailure(getActivity(), statusCode, responseString, new APIModel.RefreshTokenListener() {
//                    @Override
//                    public void onRefresh() {
//                        gethistory();
//                    }
//                });
            }
            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("gethistory", responseString);
                Type dataType = new TypeToken<RequestsForCompany>() {
                }.getType();
                RequestsForCompany data = new Gson().fromJson(responseString, dataType);
                if (data.result.size() > 0) {
                    RequestsCompanyAdapter adapter = new RequestsCompanyAdapter(getActivity(), data.result, data.current_request);
                    LinearLayoutManager layoutManage = new LinearLayoutManager(getContext());
                    list.setLayoutManager(layoutManage);
                    list.setAdapter(adapter);
                } else if (data.current_request != null) {
                    RequestsCompanyAdapter adapter = new RequestsCompanyAdapter(getActivity(), data.result, data.current_request);
                    LinearLayoutManager layoutManage = new LinearLayoutManager(getContext());
                    list.setLayoutManager(layoutManage);
                    list.setAdapter(adapter);
                } else
                    textNoRequests.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
