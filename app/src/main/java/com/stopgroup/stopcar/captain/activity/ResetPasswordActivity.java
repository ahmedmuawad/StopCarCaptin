package com.stopgroup.stopcar.captain.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
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

public class ResetPasswordActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;
    private EditText code;
    private EditText password;
    private EditText confPassword;
    private View btnSubmit;
    private ProgressBar progress;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();

        email  = getIntent().getStringExtra("email");
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        title =  findViewById(R.id.title);
        code = (EditText) findViewById(R.id.code);
        password = (EditText) findViewById(R.id.password);
        confPassword = (EditText) findViewById(R.id.conf_password);
        btnSubmit = (View) findViewById(R.id.btnSubmit);
        progress = (ProgressBar) findViewById(R.id.progress);


        title.setText(R.string.reset_password);
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
        if (code.getText().toString().equals("") || password.getText().toString().equals("") || confPassword.getText().toString().equals("") ) {
            Toast.makeText(this, R.string.plz_fill_data, Toast.LENGTH_SHORT).show();
        } else if (!password.getText().toString().equals(confPassword.getText().toString())) {
            Toast.makeText(this, R.string.password_not_match, Toast.LENGTH_SHORT).show();
        } else {
            RequestParams arg = new RequestParams();
            arg.put("token", code.getText().toString());
            arg.put("email", email);
            arg.put("password", password.getText().toString());
            arg.put("password_confirmation", confPassword.getText().toString());
            APIModel.postMethod(this, "password/verify", arg, new TextHttpResponseHandler() {

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
                    APIModel.handleFailure(ResetPasswordActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                        @Override
                        public void onRefresh() {
                            forgetPass();
                        }
                    });
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Intent i = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                }
            });

        }
    }
}
