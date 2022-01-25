package com.stopgroup.stopcar.captain.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.adapter.Reviewtodayadapter;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.modules.History;
import com.stopgroup.stopcar.captain.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {
    View view;
    private TextView price;
    private TextView time;
    private TextView num;
    private TextView title;
    private RecyclerView tripsRecycler;
    private ProgressBar progress;
    private LinearLayout lin;
    Reviewtodayadapter reviewtodayadapter;
    ArrayList<History.ResultBean.TodayBean.TripsBean> todayBeans = new ArrayList<>();
    private LinearLayout lin1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_today, container, false);
        initView();
        getcatogries();
        return view;
    }

    private void initView() {
        price =  view.findViewById(R.id.price);
        time =  view.findViewById(R.id.time);
        num = view.findViewById(R.id.num);
        title =  view.findViewById(R.id.title);
        lin1 = view.findViewById(R.id.lin1);
        tripsRecycler =  view.findViewById(R.id.trips_recycler);
        progress = view.findViewById(R.id.progress);
        LinearLayoutManager layoutManage = new LinearLayoutManager(getContext());
        tripsRecycler.setLayoutManager(layoutManage);
        reviewtodayadapter = new Reviewtodayadapter(todayBeans);
        tripsRecycler.setAdapter(reviewtodayadapter);
        title.setText(getString(R.string.today_trip));
        lin = (LinearLayout) view.findViewById(R.id.lin);
        lin1 = (LinearLayout) view.findViewById(R.id.lin1);
        lin1.setVisibility(View.GONE);

    }

    private void getcatogries() {
        progress.setVisibility(View.VISIBLE);
        APIModel.getMethod(getActivity(), "driver/history", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(getActivity(), statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        getcatogries();
                    }
                });

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("5555_today", responseString + "" );
                Type dataType = new TypeToken<History>() {
                }.getType();
                todayBeans.clear();
                History data = new Gson().fromJson(responseString, dataType);
                todayBeans.addAll(data.result.today.trips);
                reviewtodayadapter.notifyDataSetChanged();
                num.setText(data.result.today.statistics.count_trips + "");
                time.setText(data.result.today.statistics.spend_time);
                price.setText(data.result.today.statistics.total_earned + "");
                progress.setVisibility(View.GONE);

                lin.setVisibility(View.VISIBLE);
            }
        });
    }
}
