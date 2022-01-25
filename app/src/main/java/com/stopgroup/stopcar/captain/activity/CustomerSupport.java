package com.stopgroup.stopcar.captain.activity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.modules.Login;

import org.json.JSONObject;

import java.net.URLEncoder;
public class CustomerSupport extends AppCompatActivity {
    WebView webView;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);
        webView = findViewById(R.id.webView);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(
                        CustomerSupport.this);
                alert.setMessage("هل انت متأكد من مغادرة الجلسه ؟");
                alert.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.show();

            }
        });

      TextView  title =  findViewById(R.id.title);

        title.setText(getString(R.string.contactus));

        String url;
        try {
            Login.ResultBean resultBean = LoginSession.getlogindata(this).result;
            JSONObject jsonObject = new JSONObject();
            String fullName = resultBean.first_name + " " + resultBean.last_name;
            jsonObject.put("full_name", fullName);
            String mobile = resultBean.mobile;
            jsonObject.put("phone_number", mobile);
            String email = resultBean.email;
            jsonObject.put("email", email);

            url = "https://www.livehelpnow.net/lhn/livechatvisitor.aspx?lhnid=39552&zzwindow=0&d=48500&custom1=&custom2=&custom3=&pcv=" + URLEncoder.encode(jsonObject.toString(), "UTF-8");
        } catch (Exception e) {
            url = "https://www.livehelpnow.net/lhn/livechatvisitor.aspx?lhnid=39552&zzwindow=0&d=48500&custom1=&custom2=&custom3";
        }
        webView.getSettings().setJavaScriptEnabled(true);
        ProgressBar progressBar = findViewById(R.id.progress);
        webView.setWebViewClient(new AppWebViewClients(progressBar) {
            @Override
            public void onLoadResource(WebView view, String url) {
                if (url.equals("https://www.livehelpnow.net/lhn/lc.aspx")) {
                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                         //   finish();
                        }
                    }, 3000);
                }
                super.onLoadResource(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("livehelpnow")) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }

        });
        webView.loadUrl(url);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        run("session_ended();");
    }
    public void run(final String scriptSrc) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:" + scriptSrc);
            }
        });
    }

    public class AppWebViewClients extends WebViewClient {
        private ProgressBar progressBar;
        public AppWebViewClients(ProgressBar progressBar) {
            this.progressBar = progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

}
