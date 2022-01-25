package com.stopgroup.stopcar.captain.helper;

import android.text.TextUtils;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by hp on 3/25/2018.
 */

public class Gdata {
    public static String type = "";
    public static String Address1 = "";
    public static String Address2 = "";
    public static String City = "";
    public static String State = "";
    public static String Country = "";
    public static String County = "";
    public static String PIN = "";

    public static boolean emailValidator(String mail) {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }

    public static void getAddress1(Double lat, Double lon, final TextView data) {
        Address1 = "";
        Address2 = "";
        City = "";
        Country = "";
        County = "";
        PIN = "";
        State = "";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + ","
                + lon + "&key=AIzaSyBYFoqMrU8982wmwFFyAj3IAPTaTKZkmaI&language=" + Locale.getDefault().getLanguage(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObj = new JSONObject(responseString);
                    String Status = jsonObj.getString("status");
                    if (Status.equalsIgnoreCase("OK")) {
                        JSONArray Results = jsonObj.getJSONArray("results");
                        JSONObject zero = Results.getJSONObject(0);
                        JSONArray address_components = zero.getJSONArray("address_components");

                        for (int i = 0; i < address_components.length(); i++) {
                            JSONObject zero2 = address_components.getJSONObject(i);
                            String long_name = zero2.getString("long_name");
                            JSONArray mtypes = zero2.getJSONArray("types");
                            String Type = mtypes.getString(0);

                            if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                                if (Type.equalsIgnoreCase("street_number")) {
                                    Address1 = long_name + " ";
                                } else if (Type.equalsIgnoreCase("route")) {
                                    Address1 = Address1 + long_name;
                                } else if (Type.equalsIgnoreCase("sublocality")) {
                                    Address2 = long_name;
                                } else if (Type.equalsIgnoreCase("locality")) {
                                    // Address2 = Address2 + long_name + ", ";
                                    City = long_name;
                                } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                                    County = long_name;
                                } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                    State = long_name;
                                } else if (Type.equalsIgnoreCase("country")) {
                                    Country = long_name;
                                } else if (Type.equalsIgnoreCase("postal_code")) {
                                    PIN = long_name;
                                }
                            }
                        }

                    }
                    data.setText(Address1 + " " + Address2 + " " + City + " " + County + " " + State + " " + Country);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
