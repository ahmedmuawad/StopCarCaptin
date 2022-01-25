package com.stopgroup.stopcar.captain.activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.Gdata;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.helper.camera.Camera;
import com.stopgroup.stopcar.captain.helper.camera.ImageConverter;
import com.stopgroup.stopcar.captain.modules.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.stopgroup.stopcar.captain.helper.LoginSession.loginFile;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.compressImage;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.getRealPathFromURI;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.myBitmap;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.selectedImagePath;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.sourceFile;


public class EditActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;
    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private Spinner spin;
    private EditText phone;
    private EditText password;
    private View next;
    private ProgressBar progress;
    private ArrayList<Settings.ResultBean.CountriesBean> countries = new ArrayList<>();
    int country_id;
    private CircleImageView img;
    String image = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
        onclick();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        title =  findViewById(R.id.title);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        email = (EditText) findViewById(R.id.email);
        spin = (Spinner) findViewById(R.id.spin);
        phone = (EditText) findViewById(R.id.phone);
        next = (View) findViewById(R.id.next);
        img = (CircleImageView) findViewById(R.id.img);
        progress = (ProgressBar) findViewById(R.id.progress);
        title.setText(getString(R.string.edit_profile));
        getcountry();
        firstname.setText(LoginSession.getlogindata(EditActivity.this).result.first_name);
        lastname.setText(LoginSession.getlogindata(EditActivity.this).result.last_name);
        phone.setText(LoginSession.getlogindata(EditActivity.this).result.mobile);
        email.setText(LoginSession.getlogindata(EditActivity.this).result.email);
        Picasso.get().load(LoginSession.getlogindata(EditActivity.this).result.image).into(img);
        Camera.activity = EditActivity.this;
    }

    private void onclick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstname.getText().toString().equals("")) {
                    firstname.setError(getString(R.string.required));
                }
                if (lastname.getText().toString().equals("")) {
                    lastname.setError(getString(R.string.required));
                }
                if (phone.getText().toString().equals("")) {
                    phone.setError(getString(R.string.required));
                }
                if (email.getText().toString().equals("")) {
                    email.setError(getString(R.string.required));
                }
                if (phone.getText().toString().length() == 0) {
                    phone.setError(getString(R.string.ph));
                }
                if (!Gdata.emailValidator(email.getText().toString().trim())) {
                    email.setError(getString(R.string.emailnotcorrect));
                }
                if ( Gdata.emailValidator(email.getText().toString().trim()) && !firstname.getText().toString().trim().equals("") && !lastname.getText().toString().trim().equals("") && !email.getText().toString().trim().equals("") && !phone.getText().toString().trim().equals("")) {
                    register();
                }
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera.cameraOperation();
            }
        });
    }

    private void register() {
        progress.setVisibility(View.VISIBLE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("email", email.getText().toString());
        requestParams.put("first_name", firstname.getText().toString());
        requestParams.put("last_name", lastname.getText().toString());
        requestParams.put("mobile", phone.getText().toString());
        requestParams.put("country_id", country_id + "");
        requestParams.put("image", image);
        APIModel.putMethod(EditActivity.this, "driver/update", requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(EditActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        register();
                    }
                });

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("fdd", "vghvgh");
                try {
                    JSONObject jo = new JSONObject(responseString);
                    JSONObject jsonObject = new JSONObject(responseString);
                    jsonObject.put("token_type", LoginSession.getlogindata(EditActivity.this).token_type);
                    jsonObject.put("access_token", LoginSession.getlogindata(EditActivity.this).access_token);
                    jsonObject.put("refresh_token", LoginSession.getlogindata(EditActivity.this).refresh_token);
                    jsonObject.put("statusCode", LoginSession.getlogindata(EditActivity.this).statusCode);
                    jsonObject.put("statusText", LoginSession.getlogindata(EditActivity.this).statusText);
                    loginFile = getSharedPreferences("LOGIN_FILE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = loginFile.edit();
                    editor.putString("json", jsonObject.toString());
                    editor.apply();
                    LoginSession.setdat(EditActivity.this);
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getcountry() {
        APIModel.getMethod(EditActivity.this, "configs", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(EditActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        getcountry();
                    }
                });

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Type dataType = new TypeToken<Settings>() {
                }.getType();
                Settings data = new Gson().fromJson(responseString, dataType);
                countries.addAll(data.result.countries);
                ArrayAdapter<Settings.ResultBean.CountriesBean> adapter = new ArrayAdapter<Settings.ResultBean.CountriesBean>(EditActivity.this, android.R.layout.simple_spinner_dropdown_item, countries) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.item_code, null, false);
                        TextView txtTitle =  convertView.findViewById(R.id.code);
                        ImageView imageView = (ImageView) convertView.findViewById(R.id.img);
                        if (countries.size() > 0) {
                            try {
                                txtTitle.setText(countries.get(position).code);
                                try {
                                    Picasso.get().load(countries.get(position).image).into(imageView);
                                } catch (Exception e) {

                                }

                                country_id = countries.get(position).id;
                            } catch (IndexOutOfBoundsException e) {

                            }


                        }
                        return convertView;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.item_code1, null, false);
                        TextView txtTitle =  convertView.findViewById(R.id.code);
                        ImageView imageView = (ImageView) convertView.findViewById(R.id.img);
                        if (countries.size() > 0) {
                            try {
                                txtTitle.setText(countries.get(position).code);
                                try {
                                    Picasso.get().load(countries.get(position).image).into(imageView);
                                } catch (Exception e) {

                                }

                                country_id = countries.get(position).id;
                            } catch (IndexOutOfBoundsException e) {

                            }


                        }
                        return convertView;
                    }


                };

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(adapter);
                for (int i = 0; i < countries.size(); i++) {
                    if (countries.get(i).id == LoginSession.getlogindata(EditActivity.this).result.country_id) {
                        spin.setSelection(i);
                        break;
                    }
                }
                progress.setVisibility(View.GONE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//-----------------------------------------------------------------------------------------------------------------------------------


                myBitmap = (Bitmap) data.getExtras().get("data");
                Uri tempUri = Camera.getImageUri(EditActivity.this, myBitmap);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                selectedImagePath = String.valueOf(finalFile);
                sourceFile = new File(compressImage(selectedImagePath));
//                myBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
                image = ImageConverter.convert(myBitmap);
                img.setImageBitmap(myBitmap);



            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(EditActivity.this,
                        R.string.camera_closed, Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(EditActivity.this,
                        R.string.failed_to_open_camera, Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == Camera.PICK_PHOTO_FOR_AVATAR && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(EditActivity.this,
                        " Failed to select picture", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            Uri selectedImageUri = data.getData();
            try {
                myBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Setting the Bitmap to ImageView
            selectedImagePath = Camera.getRealPathFromURI(selectedImageUri);
            sourceFile = new File((selectedImagePath));
        }
        try {
          image = ImageConverter.convert(myBitmap);
            img.setImageBitmap(myBitmap);
        } catch (OutOfMemoryError a) {
            a.printStackTrace();
        } catch (NullPointerException a) {
            a.printStackTrace();
        } catch (RuntimeException a) {
            a.printStackTrace();
        }
    }
}
