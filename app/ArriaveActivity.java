package com.stopgroup.stopcar.captain.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.stopgroup.stopcar.captain.Api.APIModel;
import com.stopgroup.stopcar.captain.Helper.Closure;
import com.stopgroup.stopcar.captain.Helper.CurrentActivity;
import com.stopgroup.stopcar.captain.Helper.HttpHelper;
import com.stopgroup.stopcar.captain.Helper.RouteHelper;
import com.stopgroup.stopcar.captain.Modules.EmergencyContacts;
import com.stopgroup.stopcar.captain.Modules.Order;
import com.stopgroup.stopcar.captain.Modules.Settings;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.adapter.Choose1Adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class ArriaveActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "dd";
    private GoogleMap mMap;
    private Marker mMarcadorActual;
    private FusedLocationProviderClient mFusedLocationClient;
    private ImageView back;
    private TextView title;
    private LinearLayout lin1;
    private CircleImageView img;
    private TextView name;
    private RatingBar rate;
    private TextView num;
    private TextView model;
    private TextView status;
    private ImageView call;
    private TextView line;
    private LinearLayout lin;
    private TextView loc1;
    private TextView titlepayment;
    private TextView changepayment;
    private View Confirm;
    private LinearLayout sos;
    private TextView locfrom;
    private TextView locto;
    Order data = new Order();
    private boolean firstCall = true;
    String phone = "", lat = "", lon = "";
    Marker marker, marker1;
    private int mInterval = 15000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    Handler handler = new Handler(Looper.getMainLooper());
    int x = 0;
    Dialog dialog;
    ArrayList<Settings.ResultBean.CancelReasonsBean> cancelReasonsBean = new ArrayList<>();
    Choose1Adapter chooseAdapter;
    private View card;
    private ProgressBar progress;
    private LinearLayout lin2;
    int count = 0;
    private boolean canBack = false;
    private boolean checkReview = false;
    private Timer timer = new Timer();
    private Double currentLat = 0.0;
    private Double currentLng = 0.0;
    private RouteHelper routeHelper = new RouteHelper();
    private ImageView more;
    private View pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurrentActivity.setInstance(this);
        setContentView(R.layout.activity_arriave);
        initView();
        onclick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        title =  findViewById(R.id.title);
        lin1 = (LinearLayout) findViewById(R.id.lin1);
        img = (CircleImageView) findViewById(R.id.img);
        name =  findViewById(R.id.name);
        rate = (RatingBar) findViewById(R.id.rate);
        num =  findViewById(R.id.num);
        model =  findViewById(R.id.model);
        status =  findViewById(R.id.status);
        call = (ImageView) findViewById(R.id.call);
        line =  findViewById(R.id.line);
        lin = (LinearLayout) findViewById(R.id.lin);
        loc1 =  findViewById(R.id.loc1);
        titlepayment =  findViewById(R.id.titlepayment);
        changepayment =  findViewById(R.id.changepayment);
        Confirm = (View) findViewById(R.id.Confirm);
        sos = (LinearLayout) findViewById(R.id.sos);
        locfrom =  findViewById(R.id.locfrom);
        locto =  findViewById(R.id.locto);
        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ArriaveActivity.this);
        title.setText(getString(R.string.pickuparriave));
        card = (View) findViewById(R.id.card);
        progress = (ProgressBar) findViewById(R.id.progress);
        lin2 = (LinearLayout) findViewById(R.id.lin2);
        more = (ImageView) findViewById(R.id.more);
        pay = (View) findViewById(R.id.pay);
    }

    public void timer() {
        try {
            if (timer == null) {
                timer = new Timer();
            }
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            current_order();
                        }
                    });


                }
            }, 0, 10000);//put here time 1000 milliseconds=1 second
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void current_order() {
        APIModel.getMethod(ArriaveActivity.this, "trips/client/current", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(ArriaveActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        current_order();
                    }
                });

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Order order = new Order();
                    Log.e("current_order", responseString);
                    Type dataType = new TypeToken<Order>() {
                    }.getType();
                    if (firstCall) {
                        data = new Gson().fromJson(responseString, dataType);
                        firstCall = false;
                    } else {
                        order = new Gson().fromJson(responseString, dataType);
                        checkReview = true;
                    }
                    if ((data.result == null || data.result.id == 0)) {
                        canBack = true;
                        return;
                    }
                    if (data.result.status == 6 && data.result.payment_method == 2 && data.result.payment_paid == false) {
                        pay.setVisibility(View.VISIBLE);
                    }else if (data.result.status == 6 && data.result.payment_method == 2 && data.result.payment_paid == true) {
                        pay.setVisibility(View.GONE);
                    } else {
                        if (data.result.status >= 5 && checkReview && (order.result == null || order.result.id == 0)) {
                            Intent i = new Intent(ArriaveActivity.this, ReviewTripActivity.class);
                            i.putExtra("data", responseString);
                            String json = new Gson().toJson(data);
                            i.putExtra("json", json);
                            startActivity(i);
                            finish();
                            return;
                        } else if (order.result != null && order.result.id != 0) {
                            data = order;
                        }
                    }


                    phone = data.result.driver.mobile;
                    locfrom.setText(data.result.from_location);
                    locto.setText(data.result.to_location);
                    name.setText(data.result.driver.first_name + " " + data.result.driver.last_name);
                    status.setText(data.result.status_text);
                    try {
                        rate.setRating(Float.parseFloat(data.result.driver.driver.rate));
                        ;
                        num.setText("(" + data.result.driver.driver.rate + ")");
                    } catch (Exception e) {
                        rate.setRating(0);
                        num.setText("(0)");
                    }
                    if (data.result.category_id != 3) {
                        try {
                            model.setText(data.result.driver.driver.car.brand_name + " - " + data.result.driver.driver.car.model_name + " - " + data.result.driver.driver.car.color_name);
                        } catch (Exception e) {
                            model.setText("");
                        }
                    } else {
                        model.setVisibility(View.GONE);
                    }
                    Picasso.with(getApplicationContext()).load(data.result.driver.image).into(img);
                    titlepayment.setText(data.result.payment_method_text);
                    loc1.setText(data.result.from_location);
                    if (data.result.status == 4) {
                        Confirm.setVisibility(View.GONE);
                        loc1.setText(data.result.to_location);
                    } else if (data.result.status == 5) {
                        call.setVisibility(View.GONE);
                        loc1.setText(data.result.to_location);
                        marker.remove();
                    }
                    if (!lat.equals("")) {
                        marker1.remove();
                    }

                    if (data.result.driver.driver.lat != null) {
                        LatLng latLng = new LatLng(data.result.driver.driver.lat, data.result.driver.driver.lng);
                        BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.drawable.carr);
                        MarkerOptions options;
                        if (data.result.status < 4) {
                            options = new MarkerOptions()
                                    .position(latLng)
                                    .icon(map_client)
                                    .title(data.result.statistics.arriving_minutes);
                            try {
                                if (data.result.type.equals("SPECIAL_TYPE")) {
                                    LatLng from = new LatLng(data.result.driver.driver.lat, data.result.driver.driver.lng);
                                    LatLng to = new LatLng(Double.parseDouble(data.result.to_lat), Double.parseDouble(data.result.to_lng));
                                    routeHelper.drawRoad(from, to, mMap);
                                } else {
                                    if (data.result.from_lat != 0) {
                                        LatLng from = new LatLng(currentLat, currentLng);
                                        LatLng to = new LatLng(data.result.driver.driver.lat, data.result.driver.driver.lng);
                                        routeHelper.drawRoad(from, to, mMap);
                                    } else {
                                        LatLng from = new LatLng(data.result.driver.driver.lat, data.result.driver.driver.lng);
                                        LatLng to = new LatLng(Double.parseDouble(data.result.to_lat), Double.parseDouble(data.result.to_lng));
                                        routeHelper.drawRoad(from, to, mMap);
                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else if (data.result.status < 5) {
                            options = new MarkerOptions()
                                    .position(latLng)
                                    .icon(map_client);
                            try {
                                routeHelper.removeRoad();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (data.result.status < 6) {
                            options = new MarkerOptions()
                                    .position(latLng)
                                    .icon(map_client);
                            try {
                                LatLng from = new LatLng(data.result.driver.driver.lat, data.result.driver.driver.lng);
                                LatLng to = new LatLng(Double.parseDouble(data.result.to_lat), Double.parseDouble(data.result.to_lng));
                                routeHelper.drawRoad(from, to, mMap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            options = new MarkerOptions()
                                    .position(latLng)
                                    .icon(map_client);

                        }

                        marker1 = mMap.addMarker(options);
                        // move the camera zoom to the location
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        lat = String.valueOf(data.result.driver.driver.lat);
                    }
                    if (count == 0) {

                    }

                    lin2.setVisibility(View.VISIBLE);
                    card.setVisibility(View.VISIBLE);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(ArriaveActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mMap.clear();
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.drawable.pin);
                            MarkerOptions options = new MarkerOptions()
                                    .position(latLng)
                                    .icon(map_client)
                                    .title("my location");

                            marker = mMap.addMarker(options);

                            currentLat = latLng.latitude;
                            currentLng = latLng.longitude;
// move the camera zoom to the location
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                            // Logic to handle location object
                        }
                    }
                });


    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {

            } finally {
//                getdescussion();
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    @Override
    public void onStop() {
        stopRepeatingTask();
        super.onStop();
        stopRepeatingTask();
        Log.e("dd", "cdcd");
    }

    @Override
    public void onPause() {
        stopRepeatingTask();
        super.onPause();
        stopRepeatingTask();
        Log.e("dd", "cdcd");
        timer.cancel();
        timer = null;
    }

    @Override
    public void onDestroy() {
        stopRepeatingTask();
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    void stopRepeatingTask() {
        handler.removeMessages(0);
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        if(data.result.type.equals("SPECIAL_TYPE")) {
            Intent history = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(history);
            return;
        }
        if (canBack) {
            super.onBackPressed();
        }
    }

    private void onclick() {
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WebviewActivity.class);
                i.putExtra("id", data.result.id);
                startActivity(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
        changepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ChangepaymentActivity.class);
                i.putExtra("trip_id",data.result.id);
                i.putExtra("payment",data.result.payment_method_text);
                startActivity(i);
            }
        });
        lin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (x == 0) {
                    lin.setVisibility(View.VISIBLE);
                    x = 1;
                } else {
                    lin.setVisibility(View.GONE);
                    x = 0;
                }
            }
        });
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callingSos();
            }
        });

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(ArriaveActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_choose);
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int width = display.getWidth();
                dialog.getWindow().setLayout((width * 17 / 20), LinearLayout.LayoutParams.WRAP_CONTENT);
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                RecyclerView choose_recycler = dialog.findViewById(R.id.choose_recycler);
                TextView cancel =  dialog.findViewById(R.id.cancel);
                TextView applycode =  dialog.findViewById(R.id.applycode);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                LinearLayoutManager layoutManage = new LinearLayoutManager(getApplicationContext());
                choose_recycler.setLayoutManager(layoutManage);
                chooseAdapter = new Choose1Adapter(ArriaveActivity.this, cancelReasonsBean);
                choose_recycler.setAdapter(chooseAdapter);
                getreason();
                applycode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        canceltrip();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        locto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EditdestinationActivity.class);
                try {
                    i.putExtra("lat", data.result.to_lat);
                    i.putExtra("lon", data.result.to_lng);
                } catch (Exception e) {

                }

                startActivity(i);
            }
        });
    }

    private void getreason() {
        APIModel.getMethod(ArriaveActivity.this, "configs", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(ArriaveActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        getreason();
                    }
                });

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Type dataType = new TypeToken<Settings>() {
                }.getType();
                Settings data = new Gson().fromJson(responseString, dataType);
                cancelReasonsBean.addAll(data.result.cancel_reasons);
                chooseAdapter.notifyDataSetChanged();
            }
        });
    }

    private void canceltrip() {
        HttpHelper.httpHelper.paramters.put("cancel_reason_id", chooseAdapter.id);
        HttpHelper.httpHelper.get("trips/cancel", new Closure<String>() {
            @Override
            public void run(String args) {
                super.run(args);
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                dialog.dismiss();
            }
        });

    }

    private void callingSos() {
        this.sos(new Closure<EmergencyContacts>() {
            @Override
            public void run(EmergencyContacts args) {
                super.run(args);
                if (args.getResult().size() > 0) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
            }
        });
    }

    private void sos(final Closure<EmergencyContacts> closure) {
        HttpHelper.httpHelper.paramters.put("default_contact", 1);
        HttpHelper.httpHelper.get("client_contacts", new Closure<String>() {
            @Override
            public void run(String args) {
                super.run(args);
                Type dataType = new TypeToken<EmergencyContacts>() {
                }.getType();
                EmergencyContacts data = new Gson().fromJson(args, dataType);
                closure.run(data);
            }
        });
    }

}
