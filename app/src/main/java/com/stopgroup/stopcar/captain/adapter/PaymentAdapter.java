package com.stopgroup.stopcar.captain.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stopgroup.stopcar.captain.activity.WebViewActivity;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.modules.RequestPaymentModel;
import com.stopgroup.stopcar.captain.R;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;
/**
 * Created by سيد on 04/06/2017.
 */
public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {
    private ArrayList<RequestPaymentModel.ResultBean> historyArrayList;
    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView date2;
        private TextView balance;
        private FancyButton requestBtn;

        public MyViewHolder(View view) {
            super(view);
            date =  view.findViewById(R.id.date);
            date2 =  view.findViewById(R.id.date2);
            balance =  view.findViewById(R.id.balance);
            requestBtn =  view.findViewById(R.id.requestBtn);
        }
    }


    public PaymentAdapter(Activity activity, ArrayList<RequestPaymentModel.ResultBean> historyArrayList) {
        this.historyArrayList = historyArrayList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request_payment, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        if (historyArrayList.get(position).admin_credit > 0) {
            holder.requestBtn.setText(String.valueOf(R.string.send_admin));
            holder.balance.setText("- " + historyArrayList.get(position).admin_credit + " " + LoginSession.getlogindata(activity).result.currency);
            ;
        } else {
            holder.requestBtn.setText(String.valueOf(R.string.request_admin));
            holder.balance.setText(historyArrayList.get(position).driver_credit + " " + LoginSession.getlogindata(activity).result.currency);
            ;
        }


        holder.requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebViewActivity.class);
                i.putExtra("URL", "https://nashmit.com/clone/payment/transaction/requestAdmin/process?request_id=" + historyArrayList.get(position).id);
                activity.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }


}
