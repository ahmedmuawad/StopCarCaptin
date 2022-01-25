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
import com.stopgroup.stopcar.captain.adapter.Reviewweaklyadapter;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.modules.History;
import com.stopgroup.stopcar.captain.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaeklyFragment extends Fragment {


    LinearLayout lin;

    View view;
    private TextView price;
    private TextView time;
    private TextView num;
    private TextView title;
    private RecyclerView tripsRecycler;
    private ProgressBar progress;
    Reviewweaklyadapter reviewtodayadapter;
    ArrayList<History.ResultBean.WeekBean.TripsBeanX> todayBeans = new ArrayList<>();
    private TextView nashmiPrice;
    private TextView myPrice;
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
        lin = view.findViewById(R.id.lin);
        time =  view.findViewById(R.id.time);
        num =  view.findViewById(R.id.num);
        title =  view.findViewById(R.id.title);
        tripsRecycler = (RecyclerView) view.findViewById(R.id.trips_recycler);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        LinearLayoutManager layoutManage = new LinearLayoutManager(getContext());
        tripsRecycler.setLayoutManager(layoutManage);
        reviewtodayadapter = new Reviewweaklyadapter(todayBeans);
        tripsRecycler.setAdapter(reviewtodayadapter);
        title.setText(getString(R.string.weak_trip));
        nashmiPrice = view.findViewById(R.id.nashmiPrice);
        myPrice = view.findViewById(R.id.myPrice);
        lin1 = view.findViewById(R.id.lin1);
        lin1.setVisibility(View.VISIBLE);
    }

    private void getcatogries() {
        progress.setVisibility(View.VISIBLE);
        APIModel.getMethod(getActivity(), "driver/history", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                try {
                    APIModel.handleFailure(getActivity(), statusCode, responseString, new APIModel.RefreshTokenListener() {
                        @Override
                        public void onRefresh() {
                            getcatogries();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("5555_weekly", responseString + "");
                Type dataType = new TypeToken<History>() {
                }.getType();
                todayBeans.clear();
                History data = new Gson().fromJson(responseString, dataType);
                todayBeans.addAll(data.result.week.trips);
                reviewtodayadapter.notifyDataSetChanged();
                num.setText(data.result.week.statistics.count_trips + "");
                time.setText(data.result.week.statistics.spend_time);

                price.setText(data.result.week.statistics.total_earned + "");
                nashmiPrice.setText(data.result.week.statistics.weeklyAdmin + " " + LoginSession.getlogindata(getActivity()).result.currency);
                myPrice.setText((data.result.week.statistics.total_earned - data.result.week.statistics.weeklyAdmin) + " " + LoginSession.getlogindata(getActivity()).result.currency);
                // myPrice.setText(data.result.week.statistics.weeklyDriver + " " + LoginSession.getlogindata(getActivity()).result.currency);
                progress.setVisibility(View.GONE);
                lin.setVisibility(View.VISIBLE);
            }
        });
    }
}
