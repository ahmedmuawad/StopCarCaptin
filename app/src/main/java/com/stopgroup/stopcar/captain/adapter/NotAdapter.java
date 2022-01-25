package com.stopgroup.stopcar.captain.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.stopgroup.stopcar.captain.modules.Not;
import com.stopgroup.stopcar.captain.R;

import java.util.ArrayList;


/**
 * Created by سيد on 04/06/2017.
 */
public class NotAdapter extends RecyclerView.Adapter<NotAdapter.MyViewHolder> {
    private ArrayList<Not.ResultBean> historyArrayList;
    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView body;

        public MyViewHolder(View view) {
            super(view);
            title =  view.findViewById(R.id.title);
            body =  view.findViewById(R.id.body);
        }
    }


    public NotAdapter(Activity activity, ArrayList<Not.ResultBean> historyArrayList) {
        this.historyArrayList = historyArrayList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_not, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.title.setText(historyArrayList.get(position).title);
        holder.body.setText(historyArrayList.get(position).body);

    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }


}
