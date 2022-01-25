package com.stopgroup.stopcar.captain.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.modules.Settings;
import com.stopgroup.stopcar.captain.R;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

public class AboutActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;
    private TextView text;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        getabout();
        onclick();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        title =  findViewById(R.id.title);
        text =  findViewById(R.id.text);
        progress = (ProgressBar) findViewById(R.id.progress);
        title.setText(getString(R.string.about));
    }
    private void getabout() {
        progress.setVisibility(View.VISIBLE);
        APIModel.getMethod(AboutActivity.this, "configs", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(AboutActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
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
                Settings data = new Gson().fromJson(responseString, dataType);
                text.setText(data.result.config.about_us);
                progress.setVisibility(View.GONE);
            }
        });
    }
    private void onclick(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
