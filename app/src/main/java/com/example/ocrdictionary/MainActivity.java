package com.example.ocrdictionary;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocrdictionary.entity.SearchResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "HEHEHE";
    public static final int REQ_CAMERA = 1000;
    public static final int REQ_GALLERY = 9999;

    Bitmap takenPhoto;
    Button btnCapture;
    TextView tvResult;
    TextView tvText;
    ImageView ivImageCaptured;
    Button btnGallery;
    String filePath = "";
    String postUrl = "https://nmai-nhtq.herokuapp.com/upload-image";
    OkHttpClient client = new OkHttpClient();
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        findViewById();

        onClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CAMERA:
                takenPhoto = (Bitmap) data.getExtras().get("data");
                ivImageCaptured.setImageBitmap(takenPhoto);
                storeImage(takenPhoto);
                Toast.makeText(MainActivity.this, "Translating...", Toast.LENGTH_LONG).show();
                tvResult.setText("");
                post(postUrl);
                break;
            case REQ_GALLERY:
                Uri returnUri = data.getData();
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore
                            .Images
                            .Media
                            .getBitmap(MainActivity.this.getContentResolver(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivImageCaptured.setImageBitmap(bitmapImage);
                storeImage(bitmapImage);
                tvResult.setText("");
                Toast.makeText(MainActivity.this, "Translating...", Toast.LENGTH_LONG).show();
                post(postUrl);
        }
    }

    private void storeImage(Bitmap image) {
        try {
            String mImageName = "hehe.png";
            FileOutputStream fos = openFileOutput(mImageName, MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            filePath = Environment.getDataDirectory() + "/data/com.example.ocrdictionary/files/" + mImageName;
            Log.d(TAG, "storeImage: " +filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void findViewById() {
        tvResult = findViewById(R.id.tvResult);
        btnCapture = findViewById(R.id.btnCapture);
        ivImageCaptured = findViewById(R.id.ivImageCaptured);
        btnGallery = findViewById(R.id.btnGallery);
        tvText = findViewById(R.id.tvText);
    }

    void onClick() {
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQ_CAMERA);
            }
        });
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery();
            }
        });
    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQ_GALLERY);
        }
    }

    void post(String url) {

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "hehe.png",
                        RequestBody.create(MEDIA_TYPE_PNG, new File(filePath)))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: Fail call");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                        try {
                            String json = response.body().string();
                            Gson gson = new Gson();
                            SearchResponse searchResponse = gson.fromJson(json, SearchResponse.class);
                            tvText.setText(searchResponse.getReadText());
                            if (searchResponse.getSearchText().size() != 0) {
                                tvResult.setText(searchResponse.getSearchText().get(0).getMeaning().get(0));
                            } else {
                                tvResult.setText("Can not translate this word!");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"Permission is granted");
            } else {
                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"Permission is granted");
            } else {
                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        if(ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2000);
        }
    }

}
