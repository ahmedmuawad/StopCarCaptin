package com.stopgroup.stopcar.captain.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stopgroup.stopcar.captain.modules.RatesResponse;
import com.stopgroup.stopcar.captain.R;

import java.util.ArrayList;


/**
 * Created by سيد on 04/06/2017.
 */
public class RateAdapter extends RecyclerView.Adapter<RateAdapter.MyViewHolder> {
    private ArrayList<RatesResponse.ResultBean.RatesBean> historyArrayList;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView reviewTxv , nameTxv , dateTxv;
        public ImageView imageImv;
        public RatingBar ratingBar;
        public MyViewHolder(View view) {
            super(view);
            nameTxv = view.findViewById(R.id.name_txv);
            reviewTxv = view.findViewById(R.id.review_txv);
            dateTxv = view.findViewById(R.id.date_txv);
            imageImv = view.findViewById(R.id.image_imv);
            ratingBar = view.findViewById(R.id.rate_bar);
        }
    }


    public RateAdapter(Activity activity, ArrayList<RatesResponse.ResultBean.RatesBean> historyArrayList) {
        this.historyArrayList = historyArrayList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_review, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.nameTxv.setText(historyArrayList.get(position).client_name);
        holder.ratingBar.setRating(historyArrayList.get(position).rate);
        holder.reviewTxv.setText(historyArrayList.get(position).comment);
        holder.dateTxv.setText(historyArrayList.get(position).end_date);
        if ( historyArrayList.get(position).client_image != null && historyArrayList.get(position).client_image.length() != 0)
            Picasso.get().load(historyArrayList.get(position).client_image);

    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }


}
