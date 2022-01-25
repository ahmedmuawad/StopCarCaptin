package com.stopgroup.stopcar.captain.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.R;

import cz.msebera.android.httpclient.Header;

public class ForgetPassActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;
    private TextView text;
    private EditText email;
    private View btnSubmit;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        title =  findViewById(R.id.title);
        text =  findViewById(R.id.text);
        email = (EditText) findViewById(R.id.email);
        btnSubmit = (View) findViewById(R.id.btnSubmit);
        progress = (ProgressBar) findViewById(R.id.progress);

        title.setText(R.string.forget_pass_str);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPass();
            }
        });

    }

    public void forgetPass(){
        if (email.getText().toString().equals("") ) {
            Toast.makeText(this, R.string.plz_fill_data, Toast.LENGTH_SHORT).show();
        } else {
            RequestParams arg = new RequestParams();
            arg.put("email", email.getText().toString());
            APIModel.postMethod(this, "password/reset", arg, new TextHttpResponseHandler() {

                @Override
                public void onStart() {
                    super.onStart();
                    progress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    APIModel.handleFailure(ForgetPassActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                        @Override
                        public void onRefresh() {
                            forgetPass();
                        }
                    });
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Intent i = new Intent(ForgetPassActivity.this, ResetPasswordActivity.class);
                    i.putExtra("email",email.getText().toString());
                    startActivity(i);
                    finish();

                }
            });

        }
    }
}
