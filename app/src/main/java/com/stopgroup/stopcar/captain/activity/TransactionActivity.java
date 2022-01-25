package com.stopgroup.stopcar.captain.activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.adapter.TransactionAdapter;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.PaginatorData;
import com.stopgroup.stopcar.captain.modules.Transaction;
import com.stopgroup.stopcar.captain.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TransactionActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;
    private RecyclerView list;
    private ProgressBar progress;
    TransactionAdapter resultsAdapter;
    ArrayList<Transaction.ResultBean> results = new ArrayList<>();
    PaginatorData paginatorData = new PaginatorData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        initView();
        listeners();
        onclick();

    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        title =  findViewById(R.id.title);
        list = (RecyclerView) findViewById(R.id.list);
        progress = (ProgressBar) findViewById(R.id.progress);
        title.setText(getString(R.string.recent_transactions));

    }

    private void getcatogries(final Activity activity, final PaginatorData paginator) {
        progress.setVisibility(View.VISIBLE);
        APIModel.getMethod(TransactionActivity.this, "driver/transactions", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(TransactionActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        getcatogries(activity,paginator);
                    }
                });

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("gg",responseString);
                Type dataType = new TypeToken<Transaction>() {
                }.getType();
                Transaction data = new Gson().fromJson(responseString, dataType);
                results.addAll(data.result);
                resultsAdapter.notifyDataSetChanged();
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
    public void listeners() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(linearLayoutManager);
        resultsAdapter = new TransactionAdapter(this,results);
        list.setAdapter(resultsAdapter);
        getcatogries(TransactionActivity.this, paginatorData);
    }
}
