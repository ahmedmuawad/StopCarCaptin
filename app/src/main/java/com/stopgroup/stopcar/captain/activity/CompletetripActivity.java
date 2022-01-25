package com.stopgroup.stopcar.captain.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.Dialogs;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.modules.Order;
import com.stopgroup.stopcar.captain.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;
import mehdi.sakout.fancybuttons.FancyButton;

import static com.stopgroup.stopcar.captain.helper.LoginSession.loginFile;

public class CompletetripActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;
    private TextView totalFareValueTextView;
    private TextView totalDistanceValueTextView;
    private TextView pickUpLocationValueTextView;
    private TextView destinationLocationValueTextView;
    private TextView tripfare;
    private TextView riderdiscount;
    private TextView outstanding;
    private TextView total;
    private FancyButton trip;
    private ProgressBar progress;
    private LinearLayout lin;
    private TextView tripType;
    private TextView tripdate;
    private TextView nashmiTax;
    private TextView billTax;
    private TextView tripTimeEst;
    private TextView delayTime;
    private TextView TripID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initView();
        onclick();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        back.setVisibility(View.GONE);
        try {
            String categoryPrice = getIntent().getStringExtra("category_price");
            if(categoryPrice.equals("tanks") || categoryPrice.equals("companies") || categoryPrice.equals("truck_water")) {
                back.setVisibility(View.VISIBLE);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
            }
        } catch (Exception e) {

        }
        title =  findViewById(R.id.title);
        totalFareValueTextView =  findViewById(R.id.total_fare_value_text_view);
        totalDistanceValueTextView =  findViewById(R.id.total_distance_value_text_view);
        pickUpLocationValueTextView =  findViewById(R.id.pick_up_location_value_text_view);
        destinationLocationValueTextView =  findViewById(R.id.destination_location_value_text_view);
        tripfare =  findViewById(R.id.tripfare);
        riderdiscount =  findViewById(R.id.riderdiscount);
        outstanding =  findViewById(R.id.outstanding);
        total =  findViewById(R.id.total);
        trip = (FancyButton) findViewById(R.id.trip);
        progress = (ProgressBar) findViewById(R.id.progress);
        tripTimeEst = findViewById(R.id.trip_time_estimation);
        delayTime = findViewById(R.id.trip_delay_time);
        title.setText(getString(R.string.Collechcash));
        current_order();

        lin = (LinearLayout) findViewById(R.id.lin);

        tripType = findViewById(R.id.tripType);
        tripdate = findViewById(R.id.tripdate);
        nashmiTax = findViewById(R.id.nashmiTax);
        billTax = findViewById(R.id.billTax);
        TripID = findViewById(R.id.TripID);
    }

    private void onclick() {
        trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endtrip();

            }
        });

    }


    private void endtrip() {
        Dialogs.showLoadingDialog(this);
        APIModel.getMethod(CompletetripActivity.this, "trips/collecting", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(CompletetripActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        endtrip();
                    }
                });

            }

            @Override
            public void onFinish() {
                Dialogs.dismissLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // setRegister("1");
                Log.e("data", responseString);
                Intent i = new Intent(getApplicationContext(), ReviewTripActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

            }
        });
    }


    private void current_order() {
        APIModel.getMethod(CompletetripActivity.this, "trips/driver/current", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(CompletetripActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        current_order();
                    }
                });

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Type dataType = new TypeToken<Order>() {
                }.getType();
                Order data = new Gson().fromJson(responseString, dataType);

                Double totalPrice =  (data.result.total_price )  - data.result.discount ;

                totalFareValueTextView.setText(totalPrice + " " + LoginSession.getlogindata(CompletetripActivity.this).result.currency );
                totalDistanceValueTextView.setText(data.result.distance + " " + getString(R.string.km));
                tripfare.setText(data.result.trip_price + " " + LoginSession.getlogindata(CompletetripActivity.this).result.currency );
                riderdiscount.setText(data.result.discount + " " + LoginSession.getlogindata(CompletetripActivity.this).result.currency );
                outstanding.setText(data.result.client.client_credit + " " + LoginSession.getlogindata(CompletetripActivity.this).result.currency );
                pickUpLocationValueTextView.setText(data.result.from_location + "");
                destinationLocationValueTextView.setText(data.result.to_location + "");
                total.setText(data.result.total_price + " " + LoginSession.getlogindata(CompletetripActivity.this).result.currency);

                tripType.setText(data.result.sub_category_name == null ? data.result.category_name : data.result.sub_category_name);
                tripdate.setText(data.result.start_date + "");
                nashmiTax.setText(data.result.tax + "");
                billTax.setText(data.result.country_tax + "");
                tripTimeEst.setText(data.result.time_estimation + "");
                delayTime.setText(data.result.delay_time + "");
                TripID.setText(data.result.id + "");
                lin.setVisibility(View.VISIBLE);


            }
        });
    }


}
