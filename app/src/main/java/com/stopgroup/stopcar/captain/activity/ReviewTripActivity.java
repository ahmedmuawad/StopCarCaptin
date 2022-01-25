package com.stopgroup.stopcar.captain.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.Dialogs;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.modules.CurrentOrder;
import com.stopgroup.stopcar.captain.modules.Rate;
import com.stopgroup.stopcar.captain.R;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.fancybuttons.FancyButton;
public class ReviewTripActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;
    private RatingBar rateBar;
    private View needHelpBtn;
    private FancyButton rateBtn;
    private TextView totalprice;
    private TextView distance;
    private CircleImageView image;
    private TextView name;
    private TextView time;
    private TextView distance1;
    private EditText comment;
    private View rateCard;
    Rate data;
    CurrentOrder data1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_trip);
        initView();
        onclick();
    }

    public void initView() {
        TextView title = findViewById(R.id.title);
        title.setText(R.string.review_your_trip);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        back = (ImageView) findViewById(R.id.back);
        title =  findViewById(R.id.title);
        rateBar = (RatingBar) findViewById(R.id.rate_bar);
        needHelpBtn = (FancyButton) findViewById(R.id.need_help_btn);
        rateBtn = (FancyButton) findViewById(R.id.rate_btn);
        totalprice =  findViewById(R.id.totalprice);
        comment = (EditText) findViewById(R.id.comment);
        distance = findViewById(R.id.distance);
        image = findViewById(R.id.image);
        name =  findViewById(R.id.name);
        time = findViewById(R.id.time);
        distance1 = findViewById(R.id.distance1);
        current_order();
        rateCard = findViewById(R.id.rateCard);
        title.setText(getString(R.string.review_your_trip));

        if (getIntent().hasExtra("company")) {
            rateCard.setVisibility(View.GONE);
            rateBtn.setText(getString(R.string.collecting));
            title.setText(R.string.collecting);
        }

        needHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReviewTripActivity.this, HelpActivity.class);
                startActivity(i);
            }
        });
    }

    private void current_order() {
        APIModel.getMethod(ReviewTripActivity.this, "trips/driver/current", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(ReviewTripActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        current_order();
                    }
                });

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Type dataType;

                Log.e("5555_", responseString);
                if (getIntent().hasExtra("company")) {
                    dataType = new TypeToken<CurrentOrder>() {
                    }.getType();
                    data1 = new Gson().fromJson(responseString, dataType);
                } else {
                    dataType = new TypeToken<Rate>() {
                    }.getType();
                    data = new Gson().fromJson(responseString, dataType);
                }


                if (getIntent().hasExtra("company")) {


                    try {
                        Picasso.get().load(data1.result.client.image).into(image);
                    } catch (Exception a) {
                        a.printStackTrace();
                    }
                    String date = "";
                    SimpleDateFormat spf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    Date newDate = null;
                    try {
                        newDate = spf.parse(data1.result.created_at);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (Exception a) {
                        a.printStackTrace();
                    }
                    spf = new SimpleDateFormat("hh:mm aa");
                    date = spf.format(newDate);
                    time.setText(date + "");
                    name.setText(data1.result.client.first_name + " " + data1.result.client.last_name);
                    distance.setText(data1.result.distance + "");
                    distance1.setText(data1.result.distance + "");

                    Double totalPrice =  (data1.result.total_price )  - data1.result.discount ;

                    totalprice.setText(totalPrice + " " + LoginSession.getlogindata(ReviewTripActivity.this).result.currency);


                } else {

                    try {
                        Picasso.get().load(data.rate_last_trip.client.image).into(image);
                    } catch (Exception a) {
                        a.printStackTrace();
                    }
                    String date = "";
                    SimpleDateFormat spf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    Date newDate = null;
                    try {
                        newDate = spf.parse(data.rate_last_trip.created_at);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (Exception a) {
                        a.printStackTrace();
                    }
                    spf = new SimpleDateFormat("hh:mm aa");
                    date = spf.format(newDate);
                    time.setText(date + "");
                    name.setText(data.rate_last_trip.client.first_name + " " + data.rate_last_trip.client.last_name);
                    distance.setText(data.rate_last_trip.distance + "");
                    distance1.setText(data.rate_last_trip.distance + "");

                    Double totalPrice =  (data.rate_last_trip.total_price )  - data.rate_last_trip.discount ;

                    totalprice.setText(totalPrice + " " + LoginSession.getlogindata(ReviewTripActivity.this).result.currency );

                }
            }
        });
    }

    //|| comment.getText().toString().trim().equals("")
    private void make_rate() {
        if (rateBar.getRating() == 0) {
            Dialogs.showToast(getString(R.string.complete_rate_data), ReviewTripActivity.this);
        } else {
            ratetrip();
        }
    }

    private void collect() {
        Dialogs.showLoadingDialog(this);
        APIModel.getMethod(ReviewTripActivity.this, "trips/collecting", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(ReviewTripActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        collect();
                    }
                });

            }

            @Override
            public void onFinish() {
                Dialogs.dismissLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Toast.makeText(ReviewTripActivity.this, R.string.collected, Toast.LENGTH_LONG);
                finish();

            }
        });
    }

    private void ratetrip() {
        APIModel.getMethod(ReviewTripActivity.this, String.format("driver/trip/rate/%s?rate=%s&comment=%s", data.rate_last_trip.id, rateBar.getRating(), comment.getText().toString()), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(ReviewTripActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        ratetrip();
                    }
                });

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

    private void onclick() {
        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().hasExtra("company")) {
                    collect();
                } else {
                    make_rate();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
