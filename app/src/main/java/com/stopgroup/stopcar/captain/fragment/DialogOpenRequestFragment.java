package com.stopgroup.stopcar.captain.fragment;


import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.activity.HomeActivity;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.modules.Order;
import com.stopgroup.stopcar.captain.modules.RequestsForCompany;
import com.stopgroup.stopcar.captain.R;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogOpenRequestFragment extends DialogFragment {
   public DialogOpenRequestFragment(){

    }

    View view;
    RequestsForCompany.ResultBean bean;
    Order.ResultBean orderBean;
    public int mode = 0;
    private TextView location1;
    private TextView typesGoods;
    private LinearLayout priceLin;
    private TextView location2;
    private TextView statFrom;
    private TextView typeOfGood;
    private TextView notes;
    private TextView stat_rdod;
    private TextView rdod;
    private EditText price;
    private View send;
    private View cancel;
    private ProgressBar progressBar;

    @SuppressLint("ValidFragment")
    public DialogOpenRequestFragment(RequestsForCompany.ResultBean resultBean) {
        this.bean = resultBean;
    }
    @SuppressLint("ValidFragment")
    public DialogOpenRequestFragment(Order.ResultBean order) {
        this.orderBean = order;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dialog_open_request, container, false);
        setupModeOrder();
        initView(view);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HomeActivity.isOpen = false;
    }

    private void setupModeOrder() {
        if(mode == 1) {
            HomeActivity.isOpen = true;
            bean = new RequestsForCompany.ResultBean();
            bean.from_location = orderBean.from_location;
            bean.to_location = orderBean.to_location;
            bean.trip_special_notes = orderBean.trip_special_notes;
            bean.trip_special_description = orderBean.trip_special_description;
            bean.status = String.valueOf(orderBean.status);
        }
    }
    private void initView(View view) {
        location1 =  view.findViewById(R.id.location1);
        TextView currency =  view.findViewById(R.id.currency);
        currency.setText(LoginSession.getlogindata(getActivity()).result.currency);
        location2 =  view.findViewById(R.id.location2);
        typesGoods =  view.findViewById(R.id.typesGoods);
        statFrom =  view.findViewById(R.id.statFrom);
        typeOfGood =  view.findViewById(R.id.type_of_good);
        notes =  view.findViewById(R.id.notes);
        price = (EditText) view.findViewById(R.id.price);
        stat_rdod = view.findViewById(R.id.stat_rdod);
        rdod = view.findViewById(R.id.rodod);
        send = (View) view.findViewById(R.id.send);
        cancel = (View) view.findViewById(R.id.cancel);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        priceLin = view.findViewById(R.id.priceLin);
        //Log.e("555_tag", bean.trip_special_notes + "---");
        location1.setText(bean.from_location);
        location2.setText(bean.to_location);

        try {
            if (!bean.trip_special_notes.isEmpty())
                notes.setText((bean.trip_special_notes + ""));
        } catch (NullPointerException a) {
            notes.setText("");
            a.printStackTrace();
        }
        typeOfGood.setText(bean.trip_special_description);
        if (!bean.status.equals("0")) {
            priceLin.setVisibility(View.GONE);
            price.setVisibility(View.GONE);
            send.setVisibility(View.GONE);
        }
        if (LoginSession.getlogindata(getActivity()).result.driver.category_calculating_pricing.equals("tanks")) {
            statFrom.setVisibility(View.GONE);
            location1.setVisibility(View.GONE);
            typesGoods.setText(R.string.requireddd);
            typeOfGood.setHint(R.string.requireddd);
        }
        if (LoginSession.getlogindata(getActivity()).result.driver.category_calculating_pricing.equals("companies")) {
            rdod.setVisibility(View.VISIBLE);
            stat_rdod.setVisibility(View.VISIBLE);
            rdod.setText(bean.trip_special_reassemble + "");
        } else {
            rdod.setVisibility(View.GONE);
            stat_rdod.setVisibility(View.GONE);
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (price.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.Please_enter_the_price), Toast.LENGTH_SHORT).show();
                } else
                    accept();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean.status.equals("0")) {
                    reject();
                } else {
                    dismiss();
                }
            }
        });

    }

    private void accept() {
        HomeActivity.isOpen = false;
        progressBar.setVisibility(View.VISIBLE);
        APIModel.getMethod(getActivity(), "trips/accept?price=" + price.getText().toString(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(getActivity(), statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        accept();
                    }
                });

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("accept", responseString);

                dismiss();
            }
        });

    }

    private void reject() {
        HomeActivity.isOpen = false;

        progressBar.setVisibility(View.VISIBLE);
        APIModel.getMethod(getActivity(), "trips/reject", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(getActivity(), statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        reject();
                    }
                });

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("reject", responseString);

                dismiss();
            }
        });

    }


}
