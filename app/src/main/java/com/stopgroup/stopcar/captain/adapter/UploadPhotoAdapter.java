package com.stopgroup.stopcar.captain.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stopgroup.stopcar.captain.modules.Settings;
import com.stopgroup.stopcar.captain.R;

import java.util.List;


/**
 * Created by سيد on 04/06/2017.
 */
public class UploadPhotoAdapter extends RecyclerView.Adapter<UploadPhotoAdapter.MyViewHolder> {
    Context context;
    private List<Settings.ResultBean.CategoriesBean> horizontalList;
    int id;
    public int pos = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView des;
        public ImageView check;
        public ImageView img;
        public View card;

        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.title);
            img = view.findViewById(R.id.img);
        }
    }


    public UploadPhotoAdapter(List<Settings.ResultBean.CategoriesBean> horizontalList) {
        this.horizontalList = horizontalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_upload_photo, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(horizontalList.get(position).name);

        if (horizontalList.get(position).bitmap != null)
            holder.img.setImageBitmap(horizontalList.get(position).bitmap);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(position);
            }
        });
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int pos);
    }

    public List<Settings.ResultBean.CategoriesBean> getHorizontalList() {
        return horizontalList;
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }


}
