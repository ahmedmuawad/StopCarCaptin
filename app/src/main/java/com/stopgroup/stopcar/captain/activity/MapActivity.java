package com.stopgroup.stopcar.captain.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.Dialogs;
import com.stopgroup.stopcar.captain.modules.CloseDialog;
import com.stopgroup.stopcar.captain.modules.Order;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;
import mehdi.sakout.fancybuttons.FancyButton;

import static com.stopgroup.stopcar.captain.activity.HomeActivity.isOpen;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private TextView num;
    private TextView notes_txv;
    private TextView monu;
    private TextView loc;
    private TextView locFrom;
    private TextView notes;
    private TextView reject;
    private FancyButton accept;
    private GoogleMap mMap;
    Order data;
    private EditText price;
    MediaPlayer mp;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        getWindow().setLayout((width * 17 / 20), LinearLayout.LayoutParams.WRAP_CONTENT);
        initView();
        onclick();

        mp = MediaPlayer.create(this, R.raw.alarm);
        mp.start();
        mp.setLooping(true);
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if (mp != null && mp.isPlaying())
                    mp.stop();
                finish();
            }
        };

        handler.postDelayed(runnable, 18000);

        isOpen = true;
    }

    private void initView() {
        num =  findViewById(R.id.num);
        monu =  findViewById(R.id.monu);
        loc =  findViewById(R.id.loc);
        locFrom =  findViewById(R.id.locFrom);
        notes =  findViewById(R.id.notes);
        notes_txv =  findViewById(R.id.notes_txv);
        reject =  findViewById(R.id.reject);
        TextView currency =  findViewById(R.id.currency);
        //    reject.setText(LoginSession.getlogindata(this).result.currency);
        accept =  findViewById(R.id.accept);
        price = (EditText) findViewById(R.id.price);
        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Type dataType = new TypeToken<Order>() {
        }.getType();
        data = new Gson().fromJson(getIntent().getStringExtra("data"), dataType);
        num.setText(data.result.time_estimation.substring(0, data.result.time_estimation.indexOf("Min")));
        monu.setText(data.result.trip_price + "");

        if(data.result.from_location.isEmpty()) {
            locFrom.setVisibility(View.GONE);
        } else {
            locFrom.setText(getString(R.string.from)+" "+data.result.from_location);
        }

        loc.setText(getString(R.string.to)+" "+data.result.to_location);

        if (data.result.category_calculating_pricing.equals("delivery")) {
            notes.setVisibility(View.VISIBLE);
            notes.setText(data.result.trip_delivery_note);
        }// else
            //notes_txv.setText(data.result.trip_special_notes);

        if (data.result.category_calculating_pricing.equals("truck_water")
                || data.result.category_calculating_pricing.equals("companies")
                || data.result.category_calculating_pricing.equals("tanks")) {
            price.setVisibility(View.VISIBLE);
        }
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
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);

        }
        stopAlert();

    }
    private void stopAlert() {
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
    }

    private void onclick() {
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAlert();
                if (data.result.category_calculating_pricing.equals("private_car") || data.result.category_calculating_pricing.equals("truck")
                        || data.result.category_calculating_pricing.equals("tok_tok") || data.result.category_calculating_pricing.equals("save_car")
                        || data.result.category_calculating_pricing.equals("delivery")) {
                    acceptttrip();
                } else {
                    if (price.getText().toString().equals("")) {
                        price.setError(getString(R.string.required));
                    } else {
                        acceptttripwithprice();
                    }
                }
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAlert();
                refusetrip();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CloseDialog event) {
        //   online.setChecked(true);

        finish();
    }

    ;

    private void    acceptttrip() {
        Dialogs.showLoadingDialog(this);
        APIModel.getMethod(MapActivity.this, "trips/accept", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(MapActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
//                        acceptttrip();
                    }
                });

            }

            @Override
            public void onFinish() {
                Dialogs.dismissLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //current_order();
                //  startActivity(new Intent(MapActivity.this,TripActivity.class));
            }
        });
    }

    private void acceptttripwithprice() {
        Dialogs.showLoadingDialog(this);
        APIModel.getMethod(MapActivity.this, "trips/accept?price=" + price.getText().toString(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(MapActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        acceptttripwithprice();
                    }
                });

            }

            @Override
            public void onFinish() {
                Dialogs.dismissLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    private void refusetrip() {
        Dialogs.showLoadingDialog(this);
        APIModel.getMethod(MapActivity.this, "trips/reject", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(MapActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        refusetrip();
                    }
                });

            }

            @Override
            public void onFinish() {
                Dialogs.dismissLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //  googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap = googleMap;

        mMap.clear();
        LatLng latLng = new LatLng(data.result.from_lat, data.result.from_lng);
        BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.drawable.pin);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .icon(map_client)
                .title("my location");

        mMap.addMarker(options);
// move the camera zoom to the location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
    }

    private void current_order() {
        APIModel.getMethod(MapActivity.this, "trips/driver/current", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(MapActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        current_order();
                    }
                });

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("map_data1", responseString);
                if (!responseString.equals("{\n" +
                        "    \"result\": {},\n" +
                        "    \"statusCode\": 200,\n" +
                        "    \"statusText\": \"OK\"\n" +
                        "}")) {
                    try {
                        Type dataType = new TypeToken<Order>() {
                        }.getType();
                        Order data = new Gson().fromJson(responseString, dataType);
                        if (data.result.status == 0) {
                            Intent i = new Intent(getApplicationContext(), MapActivity.class);
                            i.putExtra("data", responseString);
                            startActivity(i);
                            finish();
                        } else if (data.result.status == 6) {
                            Intent i = new Intent(getApplicationContext(), CompletetripActivity.class);
                            i.putExtra("data", responseString);
                            startActivity(i);
                            finish();
                        } else {
                            if (data.result.status != -1) {
                                Intent i = new Intent(getApplicationContext(), TripActivity.class);
                                i.putExtra("data", responseString);
                                startActivity(i);
                                finish();
                            }
                        }
                    } catch (Exception e) {

                    }
                }

            }
        });
    }


}
