package com.stopgroup.stopcar.captain.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stopgroup.stopcar.captain.activity.SelectcarActivity;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.modules.History;
import com.stopgroup.stopcar.captain.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by سيد on 04/06/2017.
 */
public class Reviewweaklyadapter extends RecyclerView.Adapter<Reviewweaklyadapter.MyViewHolder> {
    Context context;
    private List<History.ResultBean.WeekBean.TripsBeanX> horizontalList;
    SelectcarActivity addressActivity = new SelectcarActivity();
    int id;
    public int pos = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView name;
        private TextView time;
        private TextView distance1;
        private TextView type;

        public MyViewHolder(View view) {
            super(view);
            image = (CircleImageView) view.findViewById(R.id.image);
            name =  view.findViewById(R.id.name);
            time =  view.findViewById(R.id.time);
            distance1 =  view.findViewById(R.id.distance1);
            type =  view.findViewById(R.id.type);
        }
    }


    public Reviewweaklyadapter(List<History.ResultBean.WeekBean.TripsBeanX> horizontalList) {
        this.horizontalList = horizontalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_trip, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(horizontalList.get(position).client.first_name+ " " + horizontalList.get(position).client.last_name);
        String date = "";
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = spf.parse(horizontalList.get(position).created_at);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("hh:mm aa");
        date = spf.format(newDate);
        holder.time.setText(date);

        double totalPrice =  (horizontalList.get(position).total_price )  -  horizontalList.get(position).discount ;


        holder.distance1.setText(totalPrice + " "+ LoginSession.getlogindata(context).result.currency);
        holder.type.setText(horizontalList.get(position).payment_method_text);

    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }


}
