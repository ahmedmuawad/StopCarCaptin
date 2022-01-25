package com.stopgroup.stopcar.captain.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stopgroup.stopcar.captain.modules.Transaction;
import com.stopgroup.stopcar.captain.R;

import java.util.ArrayList;


/**
 * Created by سيد on 04/06/2017.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    private ArrayList<Transaction.ResultBean> historyArrayList;
    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView body;
        private TextView date;

        public MyViewHolder(View view) {
            super(view);
            title =  view.findViewById(R.id.title);
            body =  view.findViewById(R.id.body);
            date =  view.findViewById(R.id.date);
        }
    }


    public TransactionAdapter(Activity activity, ArrayList<Transaction.ResultBean> historyArrayList) {
        this.historyArrayList = historyArrayList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_transaction, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.title.setText(historyArrayList.get(position).type_text);
        holder.body.setText(historyArrayList.get(position).driver_credit+"");
        holder.date.setText(historyArrayList.get(position).created_at);

    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }


}
