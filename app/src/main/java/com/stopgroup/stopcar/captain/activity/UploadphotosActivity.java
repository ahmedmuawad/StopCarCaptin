package com.stopgroup.stopcar.captain.activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.adapter.UploadPhotoAdapter;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.Dialogs;
import com.stopgroup.stopcar.captain.helper.LoginSession;
import com.stopgroup.stopcar.captain.helper.camera.Camera;
import com.stopgroup.stopcar.captain.helper.camera.ImageConverter;
import com.stopgroup.stopcar.captain.modules.CatResponse;
import com.stopgroup.stopcar.captain.modules.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.stopgroup.stopcar.captain.helper.LoginSession.loginFile;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.getRealPathFromURI;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.myBitmap;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.selectedImagePath;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.sourceFile;
public class UploadphotosActivity extends AppCompatActivity {
    private ImageView back;
    private TextView title;
    private ImageView driverlincese;
    private ImageView vecillincese;
    private View continu;
    String image1 = "", image2 = "", image3 = "", image4 = "";
    int check = -1;
    private TextView vechileInsurance;
    private ImageView vecilinsurance;
    private LinearLayout checkUpLinear;
    private LinearLayout insuranceLinear;
    private TextView vechileCheckup;
    private TextView text1;
    private TextView text2;
    private ImageView vecilCheckup;
    UploadPhotoAdapter uploadPhotoAdapter;
    RecyclerView list;
    List<Settings.ResultBean.CategoriesBean> categoriesBeans = new ArrayList<>();
    List<Settings.ResultBean.CategoriesBean> uploadedSelectedImages = new ArrayList<>();
    int catId = 0;
    String images = "";
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
        list = findViewById(R.id.list);
        vecillincese = (ImageView) findViewById(R.id.vecillincese);
        continu = (View) findViewById(R.id.continu);
        title.setText(getString(R.string.uplaod_documents));
        Camera.activity = UploadphotosActivity.this;
        vechileInsurance =  findViewById(R.id.vechileInsurance);
        text1 =  findViewById(R.id.text1);
        text2 =  findViewById(R.id.text2);
        vecilinsurance = (ImageView) findViewById(R.id.vecilinsurance);
        checkUpLinear = (LinearLayout) findViewById(R.id.checkUpLinear);
        insuranceLinear = findViewById(R.id.insuranceLinear);
        vechileCheckup =  findViewById(R.id.vechileCheckup);
        vecilCheckup = (ImageView) findViewById(R.id.vecilCheckup);
        LinearLayoutManager layoutManage = new LinearLayoutManager(getApplicationContext());
        list.setLayoutManager(layoutManage);
        uploadPhotoAdapter = new UploadPhotoAdapter(categoriesBeans);
        list.setAdapter(uploadPhotoAdapter);
        uploadPhotoAdapter.setOnClickListener(new UploadPhotoAdapter.OnClickListener() {
            @Override
            public void onClick(int pos) {
                Camera.cameraOperation();
                check = pos + 1;
            }
        });
        if (getIntent().hasExtra("check")) {
            checkUpLinear.setVisibility(View.GONE);
            insuranceLinear.setVisibility(View.GONE);
            text1.setText(R.string.segel);
            text2.setText(R.string.baladaya);
            if (getIntent().getExtras().getInt("id") == 4) {
                text1.setText(R.string.estmart_sayara);
                text2.setText(R.string.driver_license);
                //  insuranceLinear.setVisibility(View.VISIBLE);
            }
        } else if (SelectcarActivity.TYPE == 1) {
            //      checkUpLinear.setVisibility(View.VISIBLE);
        } else {
            checkUpLinear.setVisibility(View.GONE);
        }
        catId = getIntent().getExtras().getInt("id");
        getcatogries();
        text1.setText(R.string.estmart_sayara);
        text2.setText(R.string.driver_license);
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
                check = 1;
            }
        });
        vecillincese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera.cameraOperation();
                check = 2;
            }
        });
        vecilinsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera.cameraOperation();
                check = 3;
            }
        });
        vecilCheckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera.cameraOperation();
                check = 4;
            }
        });
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  if (image1.equals("")) {
                //      Dialogs.showToast(getString(R.string.driver_lincese), UploadphotosActivity.this);
                //   }
                //   if (image2.equals("")) {
                //       Dialogs.showToast(getString(R.string.vecil_lincese), UploadphotosActivity.this);
                //    }
                //    if (SelectcarActivity.TYPE == 1) {
                // if (!image1.equals("") && !image2.equals("")) {
                register();
//                    Intent i = new Intent(UploadphotosActivity.this, LoginActivity.class);
//                    startActivity(i);
                //   } else {
                //       Toast.makeText(UploadphotosActivity.this, R.string.rest_pics, Toast.LENGTH_SHORT).show();
                //   }
                //    } else {
//&& !image1.equals("") && !image2.equals("")
                //      if (getIntent().hasExtra("check")) {
                //         register();
                //    }
// else if (!image1.equals("") && !image2.equals("")) {
//                        register();
////                    Intent i = new Intent(UploadphotosActivity.this, LoginActivity.class);
////                    startActivity(i);
//                    } else {
//                        Toast.makeText(UploadphotosActivity.this, R.string.rest_pics, Toast.LENGTH_SHORT).show();
//                    }
                // }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == Camera.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//-----------------------------------------------------------------------------------------------------------------------------------
                myBitmap = (Bitmap) data.getExtras().get("data");
                Uri tempUri = Camera.getImageUri(UploadphotosActivity.this, myBitmap);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                selectedImagePath = String.valueOf(finalFile);
                // sourceFile = new File(compressImage(selectedImagePath));
                sourceFile = new File(selectedImagePath);
            } else if (requestCode == Camera.PICK_PHOTO_FOR_AVATAR) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = Camera.getRealPathFromURI(selectedImageUri);
                sourceFile = new File((selectedImagePath));
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (myBitmap == null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inDither = true;
                myBitmap = BitmapFactory.decodeFile(selectedImagePath, options);
            }
            String image_base = ImageConverter.convert(myBitmap);
            sourceFile.delete();
            try {
                if (check == 1) {
                    image1 = image_base;
                    driverlincese.setImageBitmap(myBitmap);
                } else if (check == 2) {
                    image2 = image_base;
                    vecillincese.setImageBitmap(myBitmap);
                } else if (check == 3) {
                    image3 = image_base;
                    vecilinsurance.setImageBitmap(myBitmap);
                } else if (check == 4) {
                    image4 = image_base;
                    vecilCheckup.setImageBitmap(myBitmap);
                }
                try {
                    categoriesBeans.get(check - 1).bitmap = myBitmap;
                    uploadPhotoAdapter.notifyDataSetChanged();
                }catch (Exception e){

                }
            } catch (OutOfMemoryError a) {
                a.printStackTrace();
            } catch (NullPointerException a) {
                a.printStackTrace();
            } catch (RuntimeException a) {
                a.printStackTrace();
            }
            int pos = check - 1;
            if (pos < uploadPhotoAdapter.getHorizontalList().size()) {
                Settings.ResultBean.CategoriesBean object = new Settings.ResultBean.CategoriesBean();
                object.attachment_id = uploadPhotoAdapter.getHorizontalList().get(pos).id;
                object.img = image_base;
                boolean found = false;
                for (int i = 0; i < uploadedSelectedImages.size(); i++) {
                    if (uploadedSelectedImages.get(i).attachment_id == object.attachment_id) {
                        uploadedSelectedImages.get(i).img = object.img;
                        found = true;
                    }
                }
                if (!found) {
                    uploadedSelectedImages.add(object);
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(UploadphotosActivity.this,
                    R.string.camera_closed, Toast.LENGTH_SHORT)
                    .show();
        } else {
            // failed to capture image
            Toast.makeText(UploadphotosActivity.this,
                    R.string.failed_to_open_camera, Toast.LENGTH_SHORT)
                    .show();
        }
    }
    private void register() {
        if (SelectcarActivity.cat_price == null) {
            Toast.makeText(this, "رجاء قم اولا بتحديد نوع السياره", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SelectcarActivity.class));
            finish();
            return;
        }
        Dialogs.showLoadingDialog(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final RequestParams requestParams = new RequestParams();
                requestParams.put("category_id", SelectcarActivity.cat_id + "");
                if (getIntent().hasExtra("subcat")) {
                    Log.e("7asal_id_sub", "5555");
                    requestParams.put("sub_category_id", getIntent().getIntExtra("subcat", 65655) + "");
                }
                if (!SelectcarActivity.cat_price.equals("companies")) {
                    requestParams.put("brand_id", getIntent().getIntExtra("brand", 65655) + "");
                    requestParams.put("model_id", getIntent().getIntExtra("model", 65655) + "");
                    requestParams.put("color_id", getIntent().getIntExtra("color", 65655) + "");
                    requestParams.put("year", getIntent().getIntExtra("year", 65655) + "");
                    requestParams.put("plate_number", getIntent().getStringExtra("plate_number") + "");
                }
                if (getIntent().hasExtra("check")) {
                    Log.e("7asal", "5555");
                    requestParams.put("company_record_img", image1);
                    requestParams.put("company_license_img", image2);
                }
                requestParams.put("car_image", AddvechiclenameActivity.imagephoto);
                /** covert to json **/
                String json = new Gson().toJson(uploadedSelectedImages);
                requestParams.put("json", json); // insurance
                //images = images.substring(0, images.length() - 1);
                //requestParams.put("json", "["+images +"]"); // insurance
                if (SelectcarActivity.TYPE == 1) {
                    requestParams.put("check_up", image4); // fa7s
                }
                Log.e("parms", requestParams.toString());
                APIModel.postMethod(UploadphotosActivity.this, "driver/register/step2", requestParams, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Dialogs.dismissLoadingDialog();
                                Log.e("555_fail", statusCode + "--");
                                APIModel.handleFailure(UploadphotosActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                                    @Override
                                    public void onRefresh() {
                                        register();
                                    }
                                });
                            }
                        });
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Dialogs.dismissLoadingDialog();
                        Log.e("fdd", "vghvgh");
                        try {
                            JSONObject jsonObject = new JSONObject(responseString);
                            jsonObject.put("token_type", LoginSession.getlogindata(UploadphotosActivity.this).token_type);
                            jsonObject.put("access_token", LoginSession.getlogindata(UploadphotosActivity.this).access_token);
                            jsonObject.put("refresh_token", LoginSession.getlogindata(UploadphotosActivity.this).refresh_token);
                            jsonObject.put("statusCode", LoginSession.getlogindata(UploadphotosActivity.this).statusCode);
                            jsonObject.put("statusText", LoginSession.getlogindata(UploadphotosActivity.this).statusText);
                            loginFile = getSharedPreferences("LOGIN_FILE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = loginFile.edit();
                            editor.putString("json", jsonObject.toString());
                            editor.apply();
                            LoginSession.setdat(UploadphotosActivity.this);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Dialogs.dismissLoadingDialog();
                    }
                });
            }
        }).run();
    }
    private void getcatogries() {
        Dialogs.showLoadingDialog(this);
        APIModel.getMethod(UploadphotosActivity.this, "category/attachment?category_id=" + catId, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                APIModel.handleFailure(UploadphotosActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        getcatogries();
                    }
                });
                Dialogs.dismissLoadingDialog();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("data", responseString + "--");
                Type dataType = new TypeToken<CatResponse>() {
                }.getType();
                CatResponse data = new Gson().fromJson(responseString, dataType);
                categoriesBeans.addAll(data.result);
                uploadPhotoAdapter.notifyDataSetChanged();
                Dialogs.dismissLoadingDialog();
            }
        });
    }
}
