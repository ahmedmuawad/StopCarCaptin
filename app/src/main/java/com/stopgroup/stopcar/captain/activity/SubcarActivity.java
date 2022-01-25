package com.stopgroup.stopcar.captain.activity;

import android.content.Intent;
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
import com.stopgroup.stopcar.captain.adapter.Subcatogaryadapter;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.Dialogs;
import com.stopgroup.stopcar.captain.modules.Subcatogary;
import com.stopgroup.stopcar.captain.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SubcarActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;
    private RecyclerView list;
    private View continu;
    private ProgressBar progress;
    Subcatogaryadapter catogaryadapter;
    ArrayList<Subcatogary.ResultBean> categoriesBeans = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcar);
        initView();
        onclick();
        getcatogries();
    }
    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        title =  findViewById(R.id.title);
        list = (RecyclerView) findViewById(R.id.list);
        continu = (View) findViewById(R.id.continu);
        progress = (ProgressBar) findViewById(R.id.progress);
        title.setText(getString(R.string.select_truck_type));
        LinearLayoutManager layoutManage = new LinearLayoutManager(getApplicationContext());
        list.setLayoutManager(layoutManage);
        catogaryadapter = new Subcatogaryadapter(categoriesBeans);
        list.setAdapter(catogaryadapter);
        progress = (ProgressBar) findViewById(R.id.progress);
    }
    private void onclick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (catogaryadapter.pos== -1){
                    Dialogs.showToast(getString(R.string.select_car_type),SubcarActivity.this);
                }else {
                    if (categoriesBeans.get(catogaryadapter.pos).childs.size()>0) {
                        String data = new Gson().toJson(categoriesBeans.get(catogaryadapter.pos).childs);
                        Intent i = new Intent(getApplicationContext(), SubwaterActivity.class);
                        i.putExtra("id",categoriesBeans.get(catogaryadapter.pos).id);
                        i.putExtra("name",categoriesBeans.get(catogaryadapter.pos).name);
                        i.putExtra("des",categoriesBeans.get(catogaryadapter.pos).description);
                        i.putExtra("img",categoriesBeans.get(catogaryadapter.pos).image);
                        i.putExtra("array",data);
                        startActivity(i);
                    }else {
                        Intent i = new Intent(getApplicationContext(), AddvechiclenameActivity.class);
                        i.putExtra("subcat",categoriesBeans.get(catogaryadapter.pos).id);
                        i.putExtra("image",categoriesBeans.get(catogaryadapter.pos).image);
                        startActivity(i);
                    }
                }
            }
        });
    }
    private void getcatogries() {
        progress.setVisibility(View.VISIBLE);
        APIModel.getMethod(SubcarActivity.this, "sub_categories/"+getIntent().getIntExtra("id",6555), new TextHttpResponseHandler() {

            @Override
            public void onStart() {

                Log.e("id", getIntent().getIntExtra("id",6555) +"");
                super.onStart();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", statusCode+"---");
                APIModel.handleFailure(SubcarActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        getcatogries();
                    }
                });

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Log.e("respp", responseString );
                Type dataType = new TypeToken<Subcatogary>() {
                }.getType();
                Subcatogary data = new Gson().fromJson(responseString, dataType);
                categoriesBeans.addAll(data.result);
                catogaryadapter.notifyDataSetChanged();
                progress.setVisibility(View.GONE);
            }
        });
    }
}
