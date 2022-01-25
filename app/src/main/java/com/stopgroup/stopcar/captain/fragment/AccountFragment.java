package com.stopgroup.stopcar.captain.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.stopgroup.stopcar.captain.activity.AboutActivity;
import com.stopgroup.stopcar.captain.activity.ContactUsActivity;
import com.stopgroup.stopcar.captain.activity.EditActivity;
import com.stopgroup.stopcar.captain.activity.HelpActivity;
import com.stopgroup.stopcar.captain.activity.HomeActivity;
import com.stopgroup.stopcar.captain.activity.NotActivity;
import com.stopgroup.stopcar.captain.activity.PaymentActivity;
import com.stopgroup.stopcar.captain.activity.UpdatephotosActivity;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.R;

import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    View view;
    private ImageView carimage;
    private LinearLayout edit;
    private CircleImageView image;
    private TextView name;
    private LinearLayout logout;
    private LinearLayout waybill;
    private LinearLayout documents;
    private LinearLayout setting;
    private LinearLayout about;
    private LinearLayout help;
    private LinearLayout contactUs;
    private LinearLayout paymentDetails;
    private LinearLayout historyDetails;
    String lang = "";
    private LinearLayout not;
    public static boolean isHistoryEnable = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        initView();
        onclick();
        return view;
    }

    private void initView() {
        carimage = (ImageView) view.findViewById(R.id.carimage);
        edit = (LinearLayout) view.findViewById(R.id.edit);
        image = (CircleImageView) view.findViewById(R.id.image);
        name =  view.findViewById(R.id.name);
        logout = (LinearLayout) view.findViewById(R.id.logout);
        Picasso.get().load(LoginSession.getlogindata(getActivity()).result.image).into(carimage);
        name.setText(LoginSession.getlogindata(getActivity()).result.first_name + " " + (LoginSession.getlogindata(getActivity()).result.last_name == null ? "" : LoginSession.getlogindata(getActivity()).result.last_name ));

        waybill =  view.findViewById(R.id.waybill);
        documents = view.findViewById(R.id.documents);
        setting = (LinearLayout) view.findViewById(R.id.setting);
        about = (LinearLayout) view.findViewById(R.id.about);
        help = (LinearLayout) view.findViewById(R.id.help);
        not = (LinearLayout) view.findViewById(R.id.not);
        contactUs = (LinearLayout) view.findViewById(R.id.contactUs);
        paymentDetails = (LinearLayout) view.findViewById(R.id.paymentDetails);
        historyDetails = view.findViewById(R.id.historyDetails);
    }

    private void onclick() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EditActivity.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        waybill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity homeActivity = (HomeActivity) getActivity();
                homeActivity.viewpager.setCurrentItem(1);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AboutActivity.class);
                startActivity(i);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), HelpActivity.class);
                startActivity(i);
            }
        });


        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(i);
            }
        });


        documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UpdatephotosActivity.class);
                startActivity(i);
            }
        });
        not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NotActivity.class);
                startActivity(i);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLanguageDialog();
            }
        });
        paymentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PaymentActivity.class));
            }
        });
        historyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isHistoryEnable = true;
                Intent i = new Intent(getActivity(), HomeActivity.class);
                i.putExtra("mode","history");
                startActivity(i);
            }
        });
    }

    private void logout() {
        APIModel.getMethod(getActivity(), "logout", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                APIModel.handleFailure(getActivity(), statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        logout();
                    }
                });

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LoginSession.clearData(getActivity());
            }
        });
    }

    void openLanguageDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lang_dialog);
        dialog.setCancelable(true);
        View okBtn = (View) dialog.findViewById(R.id.ok_button);
        View cancel = (View) dialog.findViewById(R.id.cancel_button);

        final RadioButton arabic = (RadioButton) dialog.findViewById(R.id.radio_arabic);
        RadioButton english = (RadioButton) dialog.findViewById(R.id.radio_english);


        arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang = "ar";
            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang = "en";
            }
        });

        if (Locale.getDefault().getLanguage().equals("en")) {
            arabic.setGravity(Gravity.LEFT | Gravity.CENTER);
        } else if (Locale.getDefault().getLanguage().equals("ar")) {

        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lang.equals("")) {
                    Toast.makeText(getContext(), R.string.plz_select_lang, Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    Locale locale = new Locale(lang);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;

                    getContext().getResources().updateConfiguration(config, null);

                    prefs.edit().putString("lang", lang).commit();

                    Intent i = new Intent(getContext(), HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    ((Activity) getContext()).finish();
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
