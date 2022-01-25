package com.stopgroup.stopcar.captain.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stopgroup.stopcar.captain.adapter.Subsubcatogaryadapter;
import com.stopgroup.stopcar.captain.helper.Dialogs;
import com.stopgroup.stopcar.captain.modules.Subcatogary;
import com.stopgroup.stopcar.captain.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubwaterActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;
    private TextView titleproduct;
    private ImageView img;
    private TextView des;
    private RecyclerView list;
    private View continu;
    Subsubcatogaryadapter catogaryadapter;
    ArrayList<Subcatogary.ResultBean.ChildsBean> childsBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subwater);
        initView();
        onclick();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        title =  findViewById(R.id.title);
        titleproduct =  findViewById(R.id.titleproduct);
        img = (ImageView) findViewById(R.id.img);
        des =  findViewById(R.id.des);
        list = (RecyclerView) findViewById(R.id.list);
        continu = (View) findViewById(R.id.continu);
        title.setText(getString(R.string.select_car_type));
        LinearLayoutManager layoutManage = new LinearLayoutManager(getApplicationContext());
        list.setLayoutManager(layoutManage);
        catogaryadapter = new Subsubcatogaryadapter(childsBeans);
        list.setAdapter(catogaryadapter);
        try {
            Picasso.get().load(getIntent().getStringExtra("img")).into(img);

        } catch (Exception e) {
        }
        titleproduct.setText(getIntent().getStringExtra("name"));
        des.setText(getIntent().getStringExtra("des"));
        try {
            JSONArray jsonArray = new JSONArray(getIntent().getStringExtra("array"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                Subcatogary.ResultBean.ChildsBean childsBean = new Subcatogary.ResultBean.ChildsBean();
                childsBean.id = jo.getInt("id");
                childsBean.fixed_price = jo.getInt("fixed_price");
//                childsBean.image = jo.getString("image");
                childsBean.name = jo.getString("name");
                childsBean.parent_id = jo.getInt("parent_id");
                childsBeans.add(childsBean);
            }
            catogaryadapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                if (catogaryadapter.pos == -1) {
                    Dialogs.showToast(getString(R.string.select_car_type), SubwaterActivity.this);
                } else {
                    Intent i = new Intent(getApplicationContext(), AddvechiclenameActivity.class);

                    String img = getIntent().getStringExtra("img");
                    if (img.equals("")) {
                        i.putExtra("image", childsBeans.get(catogaryadapter.pos).image);
                    } else {
                        i.putExtra("image", img);
                    }
                    i.putExtra("subcat", childsBeans.get(catogaryadapter.pos).id);
                    startActivity(i);
                }
            }
        });
    }
}
