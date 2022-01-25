package com.stopgroup.stopcar.captain.fragment;


import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.activity.RequestPaymentActivity;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.R;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class EaringFragment extends Fragment {
    View view;
    private TabLayout tabs;
    private ViewPager viewpager;
    RelativeLayout requestBtn ;
    TextView myCredit ;
    ProgressBar pb ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_earing, container, false);
        initView();
        return view;
    }

    private void initView() {
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        requestBtn = view.findViewById(R.id.requestBtn);
        myCredit = view.findViewById(R.id.myCredit);
        pb = view.findViewById(R.id.pb);
        make_tabs();
        requestBtn.setVisibility(View.VISIBLE);

//        if(LoginSession.hisCredit > 0) {
//            requestBtn.setVisibility(View.VISIBLE);
//            myCredit.setText(getString(R.string.my_credit)+" "+LoginSession.hisCredit+" "+getString(R.string.sar));
//        }
//        else {
//            requestBtn.setVisibility(View.GONE);
//                }

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getActivity() , RequestPaymentActivity.class);
                startActivity(i);
//                requestAdmin();
    }



    private  void requestAdmin() {
        APIModel.getMethod(getActivity(), "driver/request/admin", new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                pb.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFinish() {
                super.onFinish();
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, final String responseString, Throwable throwable) {
                try {
                    APIModel.handleFailure(getActivity(), statusCode, responseString, new APIModel.RefreshTokenListener() {
                        @Override
                        public void onRefresh() {
                            requestAdmin();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("555_request_admin", responseString+"" );
                Toast.makeText(getActivity(), R.string.request_done, Toast.LENGTH_SHORT).show();
            }
        });
    }
        });


    }
    private void make_tabs() {
        setupViewPager(viewpager);
        tabs.post(new Runnable() {
            @Override
            public void run() {
                tabs.setupWithViewPager(viewpager);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new TodayFragment(), getString(R.string.today));
        adapter.addFragment(new WaeklyFragment(), getString(R.string.weakly));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
