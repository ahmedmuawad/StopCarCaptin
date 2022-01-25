package com.stopgroup.stopcar.captain.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.Dialogs;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.modules.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.stopgroup.stopcar.captain.helper.LoginSession.loginFile;


public class LoginActivity extends AppCompatActivity {


    private TextView title;

    private EditText username;
    private EditText password;
    private View btnLogin;
    private View forgetpassBtn;

    private TextView policyText;
    AppCompatCheckBox policyCheck;

    private static ProgressBar progress;
    private ImageView back;

    private String TAG = "printKeyHash";

    public static String social_email;
    public static String social_id;
    public static String social_name;
    public static LoginActivity loginActivity;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivity = this;
        printKeyHash();
        printHashKey(this);
//        OpenGps.checkgps(this);

        initView();



        mAuth = FirebaseAuth.getInstance();

        forgetpassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgetPassActivity.class);
                startActivity(i);
            }
        });
    }


    private void printKeyHash() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.stopgroup.stopcar.captain", PackageManager.GET_SIGNATURES);


            for (Signature signature : info.signatures) {
                MessageDigest md;

                md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }




    void login(final Activity activity) {

        if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
            Toast.makeText(this, R.string.plz_fill_data, Toast.LENGTH_SHORT).show();
        } else {
            RequestParams arg = new RequestParams();
            arg.put("username", username.getText().toString());
            arg.put("password", password.getText().toString());
            arg.put("device_type", "1");
            arg.put("device_token", FirebaseInstanceId.getInstance().getToken());
            APIModel.postMethod(this, "driver/login", arg, new TextHttpResponseHandler() {

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
                    APIModel.handleFailure(LoginActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                        @Override
                        public void onRefresh() {
                            login(activity);
                        }
                    });
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    loginFile = getSharedPreferences("LOGIN_FILE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = loginFile.edit();
                    editor.putString("json", responseString);
                    editor.apply();
                    LoginSession.setdata(LoginActivity.this);

                    Type dataType = new TypeToken<Login>() {
                    }.getType();
                    Login login = new Gson().fromJson(responseString, dataType);

                    if (login.result.driver != null) {
                        Intent i = new Intent(activity, HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(i);
                        activity.finish();
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(responseString);
                        Intent i = new Intent(activity, VerifyMobileActivity.class);
                            i.putExtra("pass", password.getText().toString());
                            i.putExtra("code", jsonObject.getJSONObject("result").getString("activation_code"));
                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }
            });

        }
    }

    private void initView() {
        title =  findViewById(R.id.title);
        back = findViewById(R.id.back);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (View) findViewById(R.id.btnLogin);
        forgetpassBtn = findViewById(R.id.forgetpassBtn);
        policyText =  findViewById(R.id.textPolicy);
        policyCheck = findViewById(R.id.checkBox);

        policyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoginActivity.this, HelpActivity.class);
                startActivity(i);
            }
        });

        if (Locale.getDefault().getLanguage().equals("ar")) {
            username.setGravity(Gravity.CENTER | Gravity.RIGHT);
            password.setGravity(Gravity.CENTER | Gravity.RIGHT);
        }
        progress = findViewById(R.id.progress);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                if (!policyCheck.isChecked()){
//                    Toast.makeText(LoginActivity.this, R.string.must_accept_policy, Toast.LENGTH_SHORT).show();
//                }
//                else {
                login(LoginActivity.this);
//                }
            }
        });



    }




    @Override
    protected void onStart() {
        //firebaseAurh.addAuthStateListener(authStateListener);
        super.onStart();
    }

    @Override
    protected void onStop() {

//        if (authStateListener != null) {
//            firebaseAurh.removeAuthStateListener(authStateListener);
//
//        }
        super.onStop();
    }



    private void getData(JSONObject object) {

        Log.e("opjectFace", object + "");


        String faceEmail = null;
        try {
            URL url = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?width=250&height=250");

            faceEmail = object.getString("email");

            String FaceName = object.getString("name");
            String first_name = object.getString("first_name");
            String last_name = object.getString("last_name");


            Log.e("faceEmail", faceEmail);
            Log.e("name", FaceName);
            Log.e("first_name", first_name);
            Log.e("last_name", last_name);


            social_id = object.getString("id");
            social_email = faceEmail;
            social_name = FaceName;


            RequestParams params = new RequestParams();
            params.put("social_id", social_id);
            params.put("email", social_email);
            params.put("name", social_name);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }


}
