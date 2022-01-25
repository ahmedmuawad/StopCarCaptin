package com.stopgroup.stopcar.captain.helper;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class PaginatorData {


    public int page = 1;
    public boolean loading = false;
    public boolean empty = false;
    public SwipeRefreshLayout refreshLayout ;
    public LinearLayout noResultLayout ;
    public RecyclerView recyclerView ;

    public RelativeLayout progressRel;
    public RelativeLayout progressMoreDataRel ;
}