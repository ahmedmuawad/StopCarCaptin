package com.stopgroup.stopcar.captain.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.adapter.PaymentAdapter;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.IntentClass;
import com.stopgroup.stopcar.captain.helper.LoginSession;

import com.stopgroup.stopcar.captain.modules.History;
import com.stopgroup.stopcar.captain.modules.RequestPaymentModel;
import com.stopgroup.stopcar.captain.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import mehdi.sakout.fancybuttons.FancyButton;
public class RequestPaymentActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;

    private TextView date;
    private TextView date2;
    private TextView balance;
    private FancyButton requestBtn;

    private RecyclerView list;
    private ProgressBar progress;
    PaymentAdapter paymentAdapter;
    ArrayList<RequestPaymentModel.ResultBean> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_request_payment);
        initView();
//        getData();
        getcatogries();
    }

    private void initView() {
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title.setText(getString(R.string.make_transactions));
//        list = findViewById(R.id.list);
        progress = findViewById(R.id.progress);

        date = findViewById(R.id.date);
        date2 = findViewById(R.id.date2);
        balance = findViewById(R.id.balance);
        requestBtn = findViewById(R.id.requestBtn);


//        LinearLayoutManager layoutManage = new LinearLayoutManager(getApplicationContext());
//        list.setLayoutManager(layoutManage);
//        paymentAdapter = new PaymentAdapter(this,results);
//        list.setAdapter(paymentAdapter);
    }

    History data;

    private void getcatogries() {
        progress.setVisibility(View.VISIBLE);
        APIModel.getMethod(RequestPaymentActivity.this, "driver/history", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                try {
                    APIModel.handleFailure(RequestPaymentActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                        @Override
                        public void onRefresh() {
                            getcatogries();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("5555_weekly", responseString + "");
                Type dataType = new TypeToken<History>() {
                }.getType();
                data = new Gson().fromJson(responseString, dataType);

//                price.setText(data.result.week.statistics.total_earned + "");
//                nashmiPrice.setText(data.result.week.statistics.weeklyAdmin + " " + getString(R.string.sar));
//                myPrice.setText(data.result.week.statistics.weeklyDriver + " " + getString(R.string.sar));
//

                if (data.result.week.statistics.weeklyAdmin > data.result.week.statistics.weeklyDriver) {
                    requestBtn.setText(getString(R.string.send_admin));
                    balance.setText("- " + (data.result.week.statistics.weeklyAdmin + "0") + " " + LoginSession.getlogindata(RequestPaymentActivity.this).result.currency);

                    // to admin
                    requestAdmin("driver/payto/admin");
                } else {
                    requestBtn.setText(getString(R.string.request_admin));
                    balance.setText((data.result.week.statistics.weeklyDriver + "0") + " " + LoginSession.getlogindata(RequestPaymentActivity.this).result.currency);

                    // from adimn
                    requestAdmin("driver/request/admin");
                }


                progress.setVisibility(View.GONE);

            }
        });
    }


    void requestAdmin(final String url) {
        APIModel.getMethod(this, url, new TextHttpResponseHandler() {


            @Override
            public void onStart() {
                super.onStart();
                progress.setVisibility(View.VISIBLE);
            }


            @Override
            public void onFinish() {
                super.onFinish();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                APIModel.handleFailure(RequestPaymentActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        requestAdmin(url);
                    }
                });
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("44444_", responseString + "");


                try {
                    JSONObject jo = new JSONObject(responseString);
                    final String id = jo.getJSONObject("result").getString("id");
                    Log.e("type", id + "");


                    requestBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final Dialog dialog = new Dialog(RequestPaymentActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_choose_payment);
                            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            dialog.setCancelable(false);
                            dialog.show();

                            dialog.findViewById(R.id.close_imv).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.findViewById(R.id.bank_transfer).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    IntentClass.goToActivity(RequestPaymentActivity.this, BankAccountActivity.class, null);
                                }
                            });
                            dialog.findViewById(R.id.visa).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                   checkPaymentOnlineCountry(id);
                                }
                            });
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void checkPaymentOnlineCountry(String id) {
     /*
        if(LoginSession.code.contains("02") || LoginSession.code.contains("+2")) {
            Intent i = new Intent(RequestPaymentActivity.this, FawryActivity.class);
            i.putExtra("price", (data.result.week.statistics.weeklyAdmin));
            i.putExtra("requestID", id);
            startActivity(i);
        } else {
            Intent i = new Intent(RequestPaymentActivity.this, WebViewActivity.class);
            i.putExtra("URL", "https://nashmit.com/clone/payment/transaction/requestAdmin/process?request_id=" + id);
            startActivity(i);
        }


      */
        Toast.makeText(this, "لم يتم إعداد منصة الدفع بعد", Toast.LENGTH_SHORT).show();
    }

    void getData() {
        APIModel.getMethod(this, "driver/transactions", new TextHttpResponseHandler() {


            @Override
            public void onStart() {
                super.onStart();
                progress.setVisibility(View.VISIBLE);
            }


            @Override
            public void onFinish() {
                super.onFinish();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                APIModel.handleFailure(RequestPaymentActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        getData();
                    }
                });
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("44444_", responseString + "");
                results.clear();
                Type dataType = new TypeToken<RequestPaymentModel>() {
                }.getType();
                RequestPaymentModel data = new Gson().fromJson(responseString, dataType);
                results.addAll(data.result);
                paymentAdapter.notifyDataSetChanged();

            }
        });
    }
}
