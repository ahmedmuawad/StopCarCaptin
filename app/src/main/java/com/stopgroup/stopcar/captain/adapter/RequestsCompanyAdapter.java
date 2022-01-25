package com.stopgroup.stopcar.captain.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stopgroup.stopcar.captain.activity.CompletetripActivity;
import com.stopgroup.stopcar.captain.activity.OpenMapActivity;
import com.stopgroup.stopcar.captain.fragment.DialogOpenRequestFragment;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.modules.RequestsForCompany;
import com.stopgroup.stopcar.captain.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by سيد on 04/06/2017.
 */
public class RequestsCompanyAdapter extends RecyclerView.Adapter<RequestsCompanyAdapter.MyViewHolder> {
    private List<RequestsForCompany.ResultBean> historyArrayList;
    Activity activity;
    private RequestsForCompany.ResultBean currentRequest;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageUser;
        private TextView name;
        private TextView from;
        private TextView to;
        private TextView typeOfGoods;
        private TextView trip_delivery_note;
        private View openRequest;
        private TextView state;
        private TextView date;
        private LinearLayout linButtom;
        private TextView price;
        private TextView methodType;
        private View mainCard;
        private View collectCash;

        public MyViewHolder(View view) {
            super(view);

            imageUser = (CircleImageView) view.findViewById(R.id.image_user);
            name =  view.findViewById(R.id.name);
            from =  view.findViewById(R.id.from);
            to =  view.findViewById(R.id.to);
            typeOfGoods =  view.findViewById(R.id.type_of_goods);
            trip_delivery_note =  view.findViewById(R.id.trip_delivery_note);
            openRequest = (View) view.findViewById(R.id.open_request);
            state =  view.findViewById(R.id.state);
            date =  view.findViewById(R.id.date);
            linButtom = (LinearLayout) view.findViewById(R.id.lin_buttom);
            price =  view.findViewById(R.id.price);
            methodType =  view.findViewById(R.id.method_type);
            mainCard = view.findViewById(R.id.mainCard);
            collectCash = view.findViewById(R.id.collecting);
            collectCash.setVisibility(View.GONE);
        }
    }


    public RequestsCompanyAdapter(Activity activity, List<RequestsForCompany.ResultBean> historyArrayList, RequestsForCompany.ResultBean currentRequest) {
        this.historyArrayList = historyArrayList;
        this.activity = activity;
        this.currentRequest = currentRequest;
        if(currentRequest != null && currentRequest.id > 0){
            this.historyArrayList.add(0,currentRequest);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.name.setText(historyArrayList.get(position).client.first_name + " " + historyArrayList.get(position).client.last_name);
        holder.state.setText(historyArrayList.get(position).status_text);
        holder.from.setText(activity.getString(R.string.from) +" : "+historyArrayList.get(position).from_location);
        holder.trip_delivery_note.setText(activity.getString(R.string.notes) +" : "+historyArrayList.get(position).trip_delivery_note);

               if(!LoginSession.getlogindata(activity).result.driver.category_calculating_pricing.equals("tanks")){
            holder.from.setVisibility(View.VISIBLE);
               }
               else if (LoginSession.getlogindata(activity).result.driver.category_calculating_pricing.equals("tanks"))
               {
                   holder.from.setVisibility(View.GONE);
               }

               if (historyArrayList.get(position).status.equals("6")){
                   holder.collectCash.setVisibility(View.VISIBLE);
                   holder.mainCard.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           Intent i = new Intent(activity, CompletetripActivity.class);
                           i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                           i.putExtra("category_price",historyArrayList.get(position).category_calculating_pricing);
                           activity.startActivity(i);
                           activity.finish();

//                           Log.e("55555_status", historyArrayList.get(position).status +"");
//                           Intent i = new Intent(activity, ReviewTripActivity.class);
//                           i.putExtra("company","1") ;
//                           activity.startActivity(i);
                       }
                   });
               }



//        if (LoginSession.getlogindata(activity).result.driver.category_id > 5) {
//            holder.from.setVisibility(View.GONE);
//
//
//            holder.mainCard.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if(historyArrayList.get(position).from_lat!=0.0 || historyArrayList.get(position).to_lat!=0.0 || historyArrayList.get(position).from_lat!=0  || historyArrayList.get(position).to_lat!=0 ) {
//                        Intent i = new Intent(activity, OpenMapActivity.class);
//                        i.putExtra("from_lat", historyArrayList.get(position).from_lat + "");
//                        i.putExtra("from_lon", historyArrayList.get(position).from_lng + "");
//                        i.putExtra("to_lat", historyArrayList.get(position).to_lat + "");
//                        i.putExtra("to_lon", historyArrayList.get(position).to_lng + "");
//                        i.putExtra("status", historyArrayList.get(position).status + "");
//                        activity.startActivity(i);
//                    }
//                }
//            });
//
//        }
//        else


       if(LoginSession.getlogindata(activity).result.driver.category_id >2){
//            holder.from.setVisibility(View.VISIBLE);

            holder.from.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(activity, OpenMapActivity.class);
                    i.putExtra("from_lat", historyArrayList.get(position).from_lat + "");
                    i.putExtra("from_lon", historyArrayList.get(position).from_lng + "");
                    i.putExtra("to_lat", historyArrayList.get(position).from_lat + "");
                    i.putExtra("to_lon", historyArrayList.get(position).from_lng + "");
                    i.putExtra("status", historyArrayList.get(position).status + "");
                    activity.startActivity(i);
                }
            });


            holder.to.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(activity, OpenMapActivity.class);
                    i.putExtra("from_lat", historyArrayList.get(position).from_lat + "");
                    i.putExtra("from_lon", historyArrayList.get(position).from_lng + "");
                    i.putExtra("to_lat", historyArrayList.get(position).to_lat + "");
                    i.putExtra("to_lon", historyArrayList.get(position).to_lng + "");
                    i.putExtra("status", historyArrayList.get(position).status + "");
                    activity.startActivity(i);
                }
            });




        }

        holder.to.setText(activity.getString(R.string.to) +" : "+historyArrayList.get(position).to_location);
        holder.typeOfGoods.setText(historyArrayList.get(position).trip_special_description);
        holder.methodType.setText(historyArrayList.get(position).payment_method_text);
        holder.price.setText(historyArrayList.get(position).total_price);
        holder.date.setText(historyArrayList.get(position).created_at);

        if (historyArrayList.get(position).status.equals("2")){
            holder.state.setTextColor(Color.parseColor("#DC0000"));
        }else {
            holder.state.setTextColor(Color.parseColor("#35a73e"));
        }

        if (historyArrayList.get(position).status.equals("0") || historyArrayList.get(position).status.equals("1") || historyArrayList.get(position).status.equals("4")) {

            holder.openRequest.setVisibility(View.VISIBLE);
            holder.state.setVisibility(View.GONE);
        }
        else {
            holder.openRequest.setVisibility(View.GONE);
            holder.state.setVisibility(View.VISIBLE);
        }
        try {
            Picasso.get().load(historyArrayList.get(position).client.image).into(holder.imageUser);
        } catch (Exception e) {

        }
        holder.openRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogOpenRequestFragment openRequestFragment = new DialogOpenRequestFragment(historyArrayList.get(position)) ;
                openRequestFragment.show(activity.getFragmentManager()  , "");
            }
        });




    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }


}
