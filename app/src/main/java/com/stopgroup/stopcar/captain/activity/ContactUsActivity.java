package com.stopgroup.stopcar.captain.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.IntentClass;
import com.stopgroup.stopcar.captain.modules.Settings;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

public class ContactUsActivity extends AppCompatActivity {

    private ImageView back;
    private TextView text;
    private ProgressBar progress;
    private TextView whatsapp;
    private TextView web;
    private TextView email;
    private TextView phone;
    private TextView location;
    Activity activity ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        TextView title = findViewById(R.id.title);
        title.setText(R.string.contactus);


        activity = this ;
        back = findViewById(R.id.back);

        progress = findViewById(R.id.progress);

        whatsapp = findViewById(R.id.whatsapp);
        web = findViewById(R.id.web);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        location = findViewById(R.id.location);
        getabout();
        onclick();
    }


    Settings data = new Settings() ;
    private void getabout() {
        progress.setVisibility(View.VISIBLE);
        APIModel.getMethod(ContactUsActivity.this, "configs", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(ContactUsActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        getabout();
                    }
                });

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Type dataType = new TypeToken<Settings>() {
                }.getType();
              data = new Gson().fromJson(responseString, dataType);

                whatsapp.setText(data.result.config.mobile);
                web.setText("ather.com");
                email.setText(data.result.config.email);
                phone.setText(data.result.config.mobile);
                location.setText(data.result.config.address);




                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentClass.goTodialPhoneNumber(activity,data.result.config.mobile);
                    }
                });
                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentClass.goToEmail(activity, data.result.config.email , "");
                    }
                });
                web.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentClass.goToLink(activity, "ather.com");
                    }
                });
                whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentClass.goTodialPhoneNumber(activity,data.result.config.mobile);
                    }
                });


                progress.setVisibility(View.GONE);
            }
        });
    }

    private void onclick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void chatNow(View view) {
        startActivity(new Intent(this , CustomerSupport.class));
    }
}
