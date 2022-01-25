package com.stopgroup.stopcar.captain.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.fragment.AccountFragment;
import com.stopgroup.stopcar.captain.fragment.DialogOpenRequestFragment;
import com.stopgroup.stopcar.captain.fragment.EaringFragment;
import com.stopgroup.stopcar.captain.fragment.HomeFragment;
import com.stopgroup.stopcar.captain.fragment.RatingFragment;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.modules.CloseDialog;
import com.stopgroup.stopcar.captain.modules.Login;
import com.stopgroup.stopcar.captain.modules.MessageEvent;
import com.stopgroup.stopcar.captain.modules.Order;
import com.stopgroup.stopcar.captain.services.LocationUpdaterService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.stopgroup.stopcar.captain.fragment.HomeFragment.REQUEST_LOCATION;
import static com.stopgroup.stopcar.captain.helper.LoginSession.loginFile;

public class HomeActivity extends AppCompatActivity {

    private TabLayout tabs;
    public ViewPager viewpager;
    private SwitchCompat online;
    private TextView onlinetext;
    Dialog dialog;
    public static boolean isOpen = false;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        make_tabs();
        if (getIntent().hasExtra("CHECK")) {
            viewpager.setCurrentItem(1);
        }
        onclick();


    }

    private void initView() {
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        online = (SwitchCompat) findViewById(R.id.online);
        onlinetext =  findViewById(R.id.onlinetext);
        startService(new Intent(getApplicationContext(), LocationUpdaterService.class));
        handler = new Handler(Looper.getMainLooper());
//changeTabsFont();
//        if (LoginSession.getlogindata(HomeActivity.this).result.driver.category_id < 3 ||
//                LoginSession.getlogindata(HomeActivity.this).result.driver.category_calculating_pricing.toLowerCase().equals("delivery")) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    current_order();
                    Log.d("current_order", "current_order");
                    handler.postDelayed(this, 7000);
                }
            };


     //   }


        if (LoginSession.getlogindata(HomeActivity.this).result.active.equals("0"))
            online.setEnabled(false);
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


    private void changeTabsFont() {
//        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/"+ Constants.FontStyle);
        ViewGroup vg = (ViewGroup) tabs.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
//                    ( tabViewChild).setTypeface(font);
                    ( (TextView)tabViewChild).setTextSize(5);
                    ((TextView) tabViewChild).setAllCaps(false);


                }
            }
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), getString(R.string.home));
        adapter.addFragment(new EaringFragment(), getString(R.string.earnings));
        adapter.addFragment(new RatingFragment(), getString(R.string.ratings));
        adapter.addFragment(new AccountFragment(), getString(R.string.accounts));
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        if (handler != null && runnable != null)
            handler.postDelayed(runnable, 0);

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

        if (handler != null && runnable != null)
            handler.removeCallbacks(runnable);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        //   online.setChecked(true);
        online.setEnabled(event.active);

    }

    ;

    private void onclick() {
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginSession.getlogindata(HomeActivity.this).result.active.equals("1")) {
                    if (LoginSession.getlogindata(HomeActivity.this).result.driver.online) {
                        setRegister("0");
                        stopService(new Intent(getApplicationContext(), LocationUpdaterService.class));
                    } else {
                        setRegister("1");
                        startService(new Intent(getApplicationContext(), LocationUpdaterService.class));
                    }
                } else {
                    online.setChecked(LoginSession.getlogindata(HomeActivity.this).result.driver.online);
                }
            }
        });
    }

    private void setRegister(final String online1) {
        RequestParams requestParams = new RequestParams();
        if (online1.equals("1")) {
            requestParams.put("online", online1);
        }
        requestParams.put("category_id", LoginSession.getlogindata(HomeActivity.this).result.driver.category_id + "");
        Log.e("put", requestParams.toString());
        APIModel.putMethod(HomeActivity.this, "driver/update", requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                APIModel.handleFailure(HomeActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        setRegister(online1);
                    }
                });


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jo = new JSONObject(responseString);
                    JSONObject jsonObject = new JSONObject(responseString);
                    jsonObject.put("token_type", LoginSession.getlogindata(HomeActivity.this).token_type);
                    jsonObject.put("access_token", LoginSession.getlogindata(HomeActivity.this).access_token);
                    jsonObject.put("refresh_token", LoginSession.getlogindata(HomeActivity.this).refresh_token);
                    jsonObject.put("statusCode", LoginSession.getlogindata(HomeActivity.this).statusCode);
                    jsonObject.put("statusText", LoginSession.getlogindata(HomeActivity.this).statusText);
                    loginFile = getSharedPreferences("LOGIN_FILE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = loginFile.edit();
                    editor.putString("json", jsonObject.toString());
                    editor.apply();
                    LoginSession.setdata(HomeActivity.this);
                    Login login = LoginSession.getlogindata(HomeActivity.this);
                    if (login.result.driver.online) {
                        online.setChecked(true);
                        onlinetext.setText(getString(R.string.online));
                    } else {
                        online.setChecked(false);
                        onlinetext.setText(getString(R.string.offline));
                    }

                    if (login.result.active.equals("1"))
                        online.setEnabled(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void current_order() {
        APIModel.getMethod(HomeActivity.this, "trips/driver/current", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

//                APIModel.handleFailure(HomeActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
//                    @Override
//                    public void onRefresh() {
//                        current_order();
//                    }
//                });

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Log.e("5555555_data1", responseString);
                if (!responseString.equals("{\n" +
                        "    \"result\": {},\n" +
                        "    \"statusCode\": 200,\n" +
                        "    \"statusText\": \"OK\"\n" +
                        "}")) {
                    try {
                        Type dataType = new TypeToken<Order>() {
                        }.getType();
                        Order data = new Gson().fromJson(responseString, dataType);


                        if (data.result == null || data.result.id == 0) {
                            EventBus.getDefault().post(new CloseDialog());
                        }
                        if(data.result.category_calculating_pricing.equals("companies") || data.result.category_calculating_pricing.equals("truck_water")
                                || data.result.category_calculating_pricing.equals("tanks")) {
                            if (data.result.status == 0) {
                                DialogOpenRequestFragment openRequestFragment = new DialogOpenRequestFragment(data.result) ;
                                openRequestFragment.mode = 1;
                                if (!isOpen){
                                    isOpen = true;
                                    openRequestFragment.show(getFragmentManager()  , "");
                                }

                            } else if (data.result.status == 6) {
                                Intent i = new Intent(getApplicationContext(), CompletetripActivity.class);
                                i.putExtra("data", responseString);
                                startActivity(i);
                            } else {
                                if (data.result.status != -1) {
                                    Intent i = new Intent(getApplicationContext(), TripActivity.class);
                                    i.putExtra("data", responseString);
                                    startActivity(i);
                                    finish();
                                }
                            }

                        } else {
                            if (data.result.status == 0) {
                                Intent i = new Intent(getApplicationContext(), MapActivity.class);
                                i.putExtra("data", responseString);
                                if (!isOpen)
                                    startActivity(i);
                            } else if (data.result.status == 6) {
                                Intent i = new Intent(getApplicationContext(), CompletetripActivity.class);
                                i.putExtra("data", responseString);
                                startActivity(i);
                            } else {
                                if (data.result.status != -1 && data.result.status != 2) {
                                    Intent i = new Intent(getApplicationContext(), TripActivity.class);
                                    i.putExtra("data", responseString);
                                            startActivity(i);
                                    finish();
                                }
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Login login = LoginSession.getlogindata(this);
        if (login.result.driver != null) {
            if (login.result.driver.online) {
                online.setChecked(true);
                onlinetext.setText(getString(R.string.online));
            } else {
                online.setChecked(false);
                onlinetext.setText(getString(R.string.offline));
            }
        } else {
            online.setChecked(false);
            onlinetext.setText(getString(R.string.offline));
        }
    }


    //callback method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:


                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                overridePendingTransition(0, 0);
                                finish();
                                overridePendingTransition(0, 0);
                                Intent i = new Intent(HomeActivity.this, HomeActivity.class);
                                overridePendingTransition(0, 0);
                                startActivity(i);
                                overridePendingTransition(0, 0);

                            }
                        }, 1000);
                        // All required changes were successfully made
//
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
//                        Toast.makeText(getActivity(), "Gps Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }


//        finish();


    }
}
