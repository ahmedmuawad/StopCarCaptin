package com.stopgroup.stopcar.captain.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.helper.camera.Camera;
import com.stopgroup.stopcar.captain.helper.camera.ImageConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

import static com.stopgroup.stopcar.captain.helper.camera.Camera.compressImage;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.getRealPathFromURI;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.myBitmap;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.selectedImagePath;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.sourceFile;

public class UpdatephotosActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;
    private ImageView driverlincese;
    private ImageView vecillincese;
    private View continu;
    String image1 = "", image2 = "", check;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadphotos);
        initView();
        onclick();

    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        title =  findViewById(R.id.title);
        driverlincese = (ImageView) findViewById(R.id.driverlincese);
        vecillincese = (ImageView) findViewById(R.id.vecillincese);
        continu = (View) findViewById(R.id.continu);
        title.setText(getString(R.string.update_documents));
        progress = (ProgressBar) findViewById(R.id.progress);
        Camera.activity = UpdatephotosActivity.this;


    }

    private void onclick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        driverlincese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera.cameraOperation();
                check = "1";
            }
        });
        vecillincese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera.cameraOperation();
                check = "2";
            }
        });
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (image1.equals("") && image2.equals("")) {
                    onBackPressed();
                } else {
                    register();
                }
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
                Uri tempUri = Camera.getImageUri(UpdatephotosActivity.this, myBitmap);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                selectedImagePath = String.valueOf(finalFile);
                sourceFile = new File(compressImage(selectedImagePath));
//                myBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
                if (check == "1") {
                    image1 = ImageConverter.convert(myBitmap);
                    driverlincese.setImageBitmap(myBitmap);
                } else if (check == "2") {
                    image2 = ImageConverter.convert(myBitmap);
                    vecillincese.setImageBitmap(myBitmap);
                }
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(UpdatephotosActivity.this,
                        R.string.camera_closed, Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(UpdatephotosActivity.this,
                        R.string.failed_to_open_camera, Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == Camera.PICK_PHOTO_FOR_AVATAR && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(UpdatephotosActivity.this,
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
            if (check == "1") {
                image1 = ImageConverter.convert(myBitmap);
                driverlincese.setImageBitmap(myBitmap);
            } else if (check == "2") {
                image2 = ImageConverter.convert(myBitmap);
                vecillincese.setImageBitmap(myBitmap);
            }
        } catch (OutOfMemoryError a) {
            a.printStackTrace();
        } catch (NullPointerException a) {
            a.printStackTrace();
        } catch (RuntimeException a) {
            a.printStackTrace();
        }
    }


    private void register() {
        if(progress.getVisibility()==View.VISIBLE) {

            return;
        }
        progress.setVisibility(View.VISIBLE);
        final RequestParams requestParams = new RequestParams();
        if (!image1.equals("")) {
            requestParams.put("driving_license_img", image1);
        }
        if (!image2.equals("")) {
            requestParams.put("car_license_img", image2);
        }
        if (LoginSession.getlogindata1(getApplicationContext()).result.driver.online) {
            requestParams.put("online", "1");
        }
        requestParams.put("category_id", LoginSession.getlogindata1(getApplicationContext()).result.driver.category_id + "");
        Log.e("parms", requestParams.toString());
        APIModel.putMethod(UpdatephotosActivity.this, "driver/update", requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(UpdatephotosActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        register();
                    }
                });

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    Intent i = new Intent(UpdatephotosActivity.this, HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progress.setVisibility(View.GONE);
            }
        });
    }


}
