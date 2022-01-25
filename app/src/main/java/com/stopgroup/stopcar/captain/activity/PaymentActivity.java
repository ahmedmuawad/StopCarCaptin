package com.stopgroup.stopcar.captain.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.modules.History;
import com.stopgroup.stopcar.captain.R;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

public class PaymentActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;
    private TextView priceTxt;
    private TextView btnPaymentDetails;
    private TextView btnRecentTransactions;
    private TextView btnMakeTransAction;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment2);
        initView();
        getcatogries();
    }

    private void initView() {
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        priceTxt = findViewById(R.id.priceTxt);

        btnPaymentDetails = findViewById(R.id.btnPaymentDetails);
        btnRecentTransactions = findViewById(R.id.btnRecentTransactions);
        btnMakeTransAction = findViewById(R.id.btnMakeTransAction);
        progressBar = findViewById(R.id.progress);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        title.setText(R.string.payments);

        btnPaymentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentActivity.this,HomeActivity.class) ;
                i.putExtra("CHECK","1") ;
                startActivity(i);
            }
        });


        btnMakeTransAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentActivity.this,RequestPaymentActivity.class) ;
                startActivity(i);
            }
        });

        btnRecentTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentActivity.this,TransactionActivity.class) ;
                startActivity(i);
            }
        });
    }


    private void getcatogries() {
        progressBar.setVisibility(View.VISIBLE);
        APIModel.getMethod(PaymentActivity.this, "driver/history", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                try {
                    APIModel.handleFailure(PaymentActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                        @Override
                        public void onRefresh() {
                            getcatogries();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("5555_weekly", responseString + "");
                Type dataType = new TypeToken<History>() {
                }.getType();

                History data = new Gson().fromJson(responseString, dataType);
                priceTxt.setText(data.result.week.statistics.total_earned + " " + LoginSession.getlogindata(PaymentActivity.this).result.currency );

                progressBar.setVisibility(View.GONE);

            }
        });
    }

}
