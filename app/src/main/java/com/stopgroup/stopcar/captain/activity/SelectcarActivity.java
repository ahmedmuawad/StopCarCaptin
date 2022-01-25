package com.stopgroup.stopcar.captain.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.adapter.Catogaryadapter;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.Dialogs;
import com.stopgroup.stopcar.captain.modules.Settings;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SelectcarActivity extends AppCompatActivity {

    public static int TYPE = -1;
    private ImageView back;
    private TextView title;
    private RecyclerView list;
    private View continu;
    Catogaryadapter catogaryadapter;
    ArrayList<Settings.ResultBean.CategoriesBean> categoriesBeans = new ArrayList<>();
    private ProgressBar progress;
    public static int cat_id;
    public static String cat_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcar);
        if (getIntent().getExtras()!=null){
            canBack = getIntent().getExtras()   .getBoolean("canBack", true);
        }
        initView();
        getcatogries();
        onclick();

    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        title =  findViewById(R.id.title);
        list = (RecyclerView) findViewById(R.id.list);
        continu = (View) findViewById(R.id.continu);
        title.setText(getString(R.string.select_car_type));
        LinearLayoutManager layoutManage = new LinearLayoutManager(getApplicationContext());
        list.setLayoutManager(layoutManage);
        catogaryadapter = new Catogaryadapter(categoriesBeans);
        list.setAdapter(catogaryadapter);
        progress = (ProgressBar) findViewById(R.id.progress);
        if(!canBack){
            back.setVisibility(View.GONE);
        }
    }
    public boolean canBack = false;
    @Override
    public void onBackPressed() {
        if(canBack){
            super.onBackPressed();
        }

    }
    private void onclick() {
        back.setVisibility(View.GONE);
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (catogaryadapter.pos == -1) {
                    Dialogs.showToast(getString(R.string.select_car_type), SelectcarActivity.this);
                } else {
                    cat_id = categoriesBeans.get(catogaryadapter.pos).id;
                    cat_price = categoriesBeans.get(catogaryadapter.pos).calculating_pricing;
                    if (!categoriesBeans.get(catogaryadapter.pos).calculating_pricing.equals("companies")) {
                        if (categoriesBeans.get(catogaryadapter.pos).has_sub) {
                            TYPE = -1;
                            Intent i = new Intent(getApplicationContext(), SubcarActivity.class);
                            i.putExtra("id", categoriesBeans.get(catogaryadapter.pos).id);
                            startActivity(i);
                        } else {
                            TYPE = 1;
                            Intent i = new Intent(getApplicationContext(), AddvechiclenameActivity.class);
                            i.putExtra("id", categoriesBeans.get(catogaryadapter.pos).id);
                            i.putExtra("image", categoriesBeans.get(catogaryadapter.pos).img);
                            startActivity(i);
                        }
                    } else {
                        TYPE = -1;
                        Intent i = new Intent(getApplicationContext(), UploadphotosActivity.class);
                        i.putExtra("id", categoriesBeans.get(catogaryadapter.pos).id);
                        i.putExtra("check", -2);
                        startActivity(i);
                    }
                }
            }
        });
    }

    private void getcatogries() {
        progress.setVisibility(View.VISIBLE);
        APIModel.getMethod(SelectcarActivity.this, "configs", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(SelectcarActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        getcatogries();
                    }
                });

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("data", responseString + "--");
                Type dataType = new TypeToken<Settings>() {
                }.getType();
                Settings data = new Gson().fromJson(responseString, dataType);
                categoriesBeans.addAll(data.result.categories);

                int i = 0;
                while (i < categoriesBeans.size()) {
                    if (categoriesBeans.get(i).id == 3) {
                        categoriesBeans.remove(i);
                        i = 0;
                        continue;
                    }
                    i++;
                }

                catogaryadapter.notifyDataSetChanged();
                progress.setVisibility(View.GONE);
            }
        });
    }
}
