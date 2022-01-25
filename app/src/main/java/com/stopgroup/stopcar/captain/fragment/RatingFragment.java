package com.stopgroup.stopcar.captain.fragment;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.adapter.RateAdapter;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.PaginatorData;
import com.stopgroup.stopcar.captain.modules.RatesResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {
    private LinearLayout lin;
    private RatingBar rateBar;
    private ProgressBar progress5;
    private ProgressBar progress4;
    private ProgressBar progress3;
    private ProgressBar progress2;
    private ProgressBar progress1;
    private RelativeLayout progrsMoreDataLayout;
    private RelativeLayout progrsDataLayout;
    public RatingFragment() {
        // Required empty public constructor
    }
    RecyclerView resultsRecycler;
    RateAdapter resultsAdapter;
    ArrayList<RatesResponse.ResultBean.RatesBean> results = new ArrayList<>();
    PaginatorData paginatorData = new PaginatorData();
    TextView rateTxv, totalTxv;
    TextView total5Txv, total4Txv, total3Txv, total2Txv, total1Txv;
    RatingBar rate_bar;
    ProgressBar rating1, rating2, rating3, rating4, rating5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        initView(view);
        listeners();
        return view;
    }
    public void initView(View view) {
        total5Txv = view.findViewById(R.id.total5_txv);
        total4Txv = view.findViewById(R.id.total4_txv);
        total3Txv = view.findViewById(R.id.total3_txv);
        total2Txv = view.findViewById(R.id.total2_txv);
        total1Txv = view.findViewById(R.id.total1_txv);
        rateTxv = view.findViewById(R.id.rate_txv);
        totalTxv = view.findViewById(R.id.total_txv);
        rate_bar = view.findViewById(R.id.rate_bar);
        rating1 = view.findViewById(R.id.progress1);
        rating2 = view.findViewById(R.id.progress2);
        rating3 = view.findViewById(R.id.progress3);
        rating4 = view.findViewById(R.id.progress4);
        rating5 = view.findViewById(R.id.progress5);
        resultsRecycler = view.findViewById(R.id.results_recycler);
        resultsAdapter = new RateAdapter(getActivity(), results);
        resultsRecycler.setHasFixedSize(true);
        resultsRecycler.setAdapter(resultsAdapter);
        paginatorData.progressRel = view.findViewById(R.id.progrsData_layout);
        lin = (LinearLayout) view.findViewById(R.id.lin);
    }
    public void listeners() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        resultsRecycler.setLayoutManager(linearLayoutManager);
//        resultsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int visibleItemCount = linearLayoutManager.getChildCount();
//                int totalItemCount = linearLayoutManager.getItemCount();
//                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
//
//                if (!paginatorData.loading && !paginatorData.empty) {
//                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                            && firstVisibleItemPosition >= 0
//                            && totalItemCount >= 10) {
//                        getResults(getActivity(), paginatorData);
//                    }
//                }
//
//            }
//        });
        getResults(getActivity(), paginatorData);
    }
    public void getResults(final Activity activity, final PaginatorData paginator) {
        paginator.progressRel.setVisibility(View.VISIBLE);
        APIModel.getMethod(activity, "driver/rates", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                APIModel.handleFailure(activity, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        getResults(activity, paginator);
                    }
                });
                paginator.progressRel.setVisibility(View.GONE);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    if (getActivity() != null) {
                        Type dataType = new TypeToken<RatesResponse>() {
                        }.getType();
                        Log.e("555_response", responseString);
                        RatesResponse response = new Gson().fromJson(responseString, dataType);
                        results.addAll(response.result.rates);
                        rating1.setProgress(response.result.statistics.rate1);
                        rating2.setProgress(response.result.statistics.rate2);
                        rating3.setProgress(response.result.statistics.rate3);
                        rating4.setProgress(response.result.statistics.rate4);
                        rating5.setProgress(response.result.statistics.rate5);
                        totalTxv.setText(response.result.count_rates + " " + getString(R.string.total));
                        rateTxv.setText(response.result.avg_rate + "");
                        rate_bar.setRating(response.result.avg_rate);
                        total1Txv.setText(response.result.statistics.rate1 + "");
                        total4Txv.setText(response.result.statistics.rate2 + "");
                        total3Txv.setText(response.result.statistics.rate3 + "");
                        total2Txv.setText(response.result.statistics.rate4 + "");
                        total5Txv.setText(response.result.statistics.rate5 + "");
                        paginator.progressRel.setVisibility(View.GONE);
                        lin.setVisibility(View.VISIBLE);
                        resultsAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }
        });
    }
}
