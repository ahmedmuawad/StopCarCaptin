package com.stopgroup.stopcar.captain.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.modules.CloseDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.stopgroup.stopcar.captain.activity.HomeActivity.isOpen;

public class OpenMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private View reject;
    private View accept;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_map);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
//        int width = display.getWidth();
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        initView();
        onclick();
    }

    private void initView() {
        reject = findViewById(R.id.reject);
        accept = findViewById(R.id.accept);
        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        isOpen = false;
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void onclick() {
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open google map
                String fromLat = getIntent().getStringExtra("from_lat");
                String fromLon = getIntent().getStringExtra("from_lon");
                String toLat = getIntent().getStringExtra("to_lat");
                String toLon = getIntent().getStringExtra("to_lon");

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse("https://maps.google.com/maps?saddr="+fromLat+","+fromLon+"&daddr="+toLat+","+toLon));
                        Uri.parse("https://maps.google.com/maps?daddr=" + toLat + "," + toLon));
                startActivity(intent);


            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CloseDialog event) {
        //   online.setChecked(true);

        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap = googleMap;
        mMap.clear();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(Double.parseDouble(getIntent().getStringExtra("to_lat")), Double.parseDouble(getIntent().getStringExtra("to_lon")));
        BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.drawable.pin);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .icon(map_client);

        mMap.addMarker(options);
// move the camera zoom to the location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
    }




}
