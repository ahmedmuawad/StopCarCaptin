package com.stopgroup.stopcar.captain.activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.helper.LoginSession;

import java.util.Locale;
public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LoginSession.setdat(Splash.this);

        YoYo.with(Techniques.Tada)
                .duration(4000)
                .repeat(100)
                .playOn(findViewById(R.id.carlogo));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LoginSession.isLogin) {
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);

                    if (getIntent().hasExtra("trip_id")) {

                        i.putExtra("trip_id",(getIntent().getStringExtra("trip_id")));
                    }


                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), AdActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 4000);
    }

}
