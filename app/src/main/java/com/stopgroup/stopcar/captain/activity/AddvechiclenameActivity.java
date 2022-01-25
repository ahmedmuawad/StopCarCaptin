package com.stopgroup.stopcar.captain.activity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.stopgroup.stopcar.captain.R;
import com.stopgroup.stopcar.captain.api.APIModel;
import com.stopgroup.stopcar.captain.helper.Dialogs;
import com.stopgroup.stopcar.captain.helper.camera.Camera;
import com.stopgroup.stopcar.captain.helper.camera.ImageConverter;
import com.stopgroup.stopcar.captain.modules.Model;
import com.stopgroup.stopcar.captain.modules.Settings;
import com.vistrav.ask.Ask;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.stopgroup.stopcar.captain.helper.camera.Camera.compressImage;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.getRealPathFromURI;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.myBitmap;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.selectedImagePath;
import static com.stopgroup.stopcar.captain.helper.camera.Camera.sourceFile;

public class AddvechiclenameActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;
    private ImageView image;
    private Spinner brand;
    private Spinner model;
    private EditText year,plate_number;
    private Spinner color;
    private View continu;
    private ProgressBar progress;
    ArrayList<Settings.ResultBean.BrandsBean> brands = new ArrayList<>();
    ArrayList<Settings.ResultBean.ColorsBean> colors = new ArrayList<>();
    ArrayList<Model.ResultBean> models = new ArrayList<>();
    int brand_id, color_id, model_id, cat_it;
    public static String imagephoto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvechiclename);
        initView();
        onclick();

        getdata();
        Camera.activity = AddvechiclenameActivity.this;
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        title =  findViewById(R.id.title);
        image = (ImageView) findViewById(R.id.image);
        brand = (Spinner) findViewById(R.id.brand);
        model = (Spinner) findViewById(R.id.model);
        year = (EditText) findViewById(R.id.year);
        plate_number = (EditText) findViewById(R.id.plate_number);
        color = (Spinner) findViewById(R.id.color);
        continu = (View) findViewById(R.id.continu);
        progress = (ProgressBar) findViewById(R.id.progress);
        title.setText(getString(R.string.Add_Vehicle_details));
        Ask.on(this)
                .forPermissions(Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_NETWORK_STATE
                        , Manifest.permission.CALL_PHONE
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.CAMERA)
                .withRationales("Call permission need for call",
                        "In order to save file you will need to grant storage permission"
                        , "In order to Read file you will need to grant storage permission"
                        , "allow access location"
                        , "allow access GET ACCOUNTS"
                        , "allow access location"
                        , "allow access network"
                ) //optional
                .go();

        try {
            if (getIntent().hasExtra("image") && getIntent().getStringExtra("image").equals("")) {
                image.setBackgroundResource(R.drawable.car);
            }
            Picasso.get().load(getIntent().getStringExtra("image")).into(image);
            imagephoto = getIntent().getStringExtra("image");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getdata() {
        progress.setVisibility(View.VISIBLE);
        String x = "";
        if (getIntent().hasExtra("subcat")) {
            x = "configs?category_id=" + SelectcarActivity.cat_id + "&sub_category_id=" + getIntent().getIntExtra("subcat", 65655) + "";
        } else {
            x = "configs?category_id=" + SelectcarActivity.cat_id;
        }

        Log.e("test_x", x);
        APIModel.getMethod(AddvechiclenameActivity.this, x, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(AddvechiclenameActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        getdata();
                    }
                });

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Type dataType = new TypeToken<Settings>() {
                }.getType();
                Settings data = new Gson().fromJson(responseString, dataType);
                brands.addAll(data.result.brands);
                colors.addAll(data.result.colors);
                ArrayAdapter<Settings.ResultBean.BrandsBean> adapter = new ArrayAdapter<Settings.ResultBean.BrandsBean>(AddvechiclenameActivity.this, android.R.layout.simple_spinner_dropdown_item, brands) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.item_brand, null, false);
                        TextView txtTitle =  convertView.findViewById(R.id.code);
                        if (brands.size() > 0) {
                            try {
                                txtTitle.setText(brands.get(position).name);
                                brand_id = brands.get(position).id;
                            } catch (IndexOutOfBoundsException e) {

                            }

                        }
                        return convertView;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.item_brand1, null, false);
                        TextView txtTitle =  convertView.findViewById(R.id.code);
                        if (brands.size() > 0) {
                            try {
                                txtTitle.setText(brands.get(position).name);

                            } catch (IndexOutOfBoundsException e) {

                            }


                        }
                        return convertView;
                    }


                };

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brand.setAdapter(adapter);
                ArrayAdapter<Settings.ResultBean.ColorsBean> adapter1 = new ArrayAdapter<Settings.ResultBean.ColorsBean>(AddvechiclenameActivity.this, android.R.layout.simple_spinner_dropdown_item, colors) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.item_brand, null, false);
                        TextView txtTitle =  convertView.findViewById(R.id.code);
                        if (colors.size() > 0) {
                            try {
                                txtTitle.setText(colors.get(position).name);
                                color_id = colors.get(position).id;
                            } catch (IndexOutOfBoundsException e) {

                            }


                        }
                        return convertView;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.item_brand1, null, false);
                        TextView txtTitle =  convertView.findViewById(R.id.code);
                        if (colors.size() > 0) {
                            try {
                                txtTitle.setText(colors.get(position).name);

                            } catch (IndexOutOfBoundsException e) {

                            }


                        }
                        return convertView;
                    }


                };

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                color.setAdapter(adapter1);
                progress.setVisibility(View.GONE);
            }
        });
    }

    private void onclick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera.cameraOperation();
            }
        });
        brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getmodel(brands.get(i).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (year.getText().toString().equals("")) {
                    year.setError(getString(R.string.required));
                }
                if (plate_number.getText().toString().equals("")) {
                    plate_number.setError(getString(R.string.required));
                }
                if (imagephoto.equals("")) {
                    Dialogs.showToast("upload car1 photo", AddvechiclenameActivity.this);
                }
                if (!plate_number.getText().toString().equals("") &&!year.getText().toString().equals("") && !imagephoto.equals("")) {
                    Intent i = new Intent(AddvechiclenameActivity.this, UploadphotosActivity.class);
                    i.putExtra("brand", brand_id);
                    i.putExtra("model", model_id);
                    i.putExtra("year", year.getText().toString());
                    i.putExtra("plate_number", plate_number.getText().toString());
                    i.putExtra("color", color_id);
                    i.putExtra("id", SelectcarActivity.cat_id);
                    if (getIntent().hasExtra("subcat")) {
                        i.putExtra("subcat", getIntent().getIntExtra("subcat", 6555));

                    }
                    startActivity(i);
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
                Uri tempUri = Camera.getImageUri(AddvechiclenameActivity.this, myBitmap);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                selectedImagePath = String.valueOf(finalFile);
                sourceFile = new File(compressImage(selectedImagePath));
//                myBitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
                imagephoto = ImageConverter.convert(myBitmap);
                image.setImageBitmap(myBitmap);



            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(AddvechiclenameActivity.this,
                        R.string.camera_closed, Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(AddvechiclenameActivity.this,
                        R.string.failed_to_open_camera, Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == Camera.PICK_PHOTO_FOR_AVATAR && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(AddvechiclenameActivity.this,
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
           imagephoto = ImageConverter.convert(myBitmap);
            image.setImageBitmap(myBitmap);
        } catch (OutOfMemoryError a) {
            a.printStackTrace();
        } catch (NullPointerException a) {
            a.printStackTrace();
        } catch (RuntimeException a) {
            a.printStackTrace();
        }
    }

    private void getmodel(final int id) {
        progress.setVisibility(View.VISIBLE);
        APIModel.getMethod(AddvechiclenameActivity.this, "models/" + id, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                APIModel.handleFailure(AddvechiclenameActivity.this, statusCode, responseString, new APIModel.RefreshTokenListener() {
                    @Override
                    public void onRefresh() {
                        getmodel(id);
                    }
                });

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Type dataType = new TypeToken<Model>() {
                }.getType();
                Model data = new Gson().fromJson(responseString, dataType);
                models.clear();
                models.addAll(data.result);
                ArrayAdapter<Model.ResultBean> adapter = new ArrayAdapter<Model.ResultBean>(AddvechiclenameActivity.this, android.R.layout.simple_spinner_dropdown_item, models) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.item_brand, null, false);
                        TextView txtTitle =  convertView.findViewById(R.id.code);
                        if (models.size() > 0) {
                            try {
                                txtTitle.setText(models.get(position).name);
                                model_id = models.get(position).id;
                            } catch (IndexOutOfBoundsException e) {

                            }


                        }
                        return convertView;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.item_brand1, null, false);
                        TextView txtTitle =  convertView.findViewById(R.id.code);
                        if (models.size() > 0) {
                            try {
                                txtTitle.setText(models.get(position).name);

                            } catch (IndexOutOfBoundsException e) {

                            }


                        }
                        return convertView;
                    }


                };

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                model.setAdapter(adapter);

                progress.setVisibility(View.GONE);
            }
        });
    }
}
