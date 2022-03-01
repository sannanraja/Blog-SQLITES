package com.app.blogapp.Activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.blogapp.DatabaseHelper.DatabaseHelper;
import com.app.blogapp.R;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddBlog extends AppCompatActivity {
    private TextView tvAddPicture;
    private ImageView ivImage;
    private Bitmap bitmap;
    RelativeLayout rl;
    Button okButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);
        ivImage = findViewById(R.id.iv_image);
        tvAddPicture = findViewById(R.id.tv_add_picture);
        rl = findViewById(R.id.rl_pic_container);

        okButton = findViewById(R.id.button_ok);
        okButton.setOnClickListener(view -> {
            try {
                /* path to /data/data/your app/app_data/imageDir */
                FileOutputStream fos;
                // Create imageDir
                String FILENAME = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".jpeg";
                File f = new File(new ContextWrapper(getApplicationContext()).getDir("images", Context.MODE_PRIVATE),FILENAME);
                fos = new FileOutputStream(f);
                if (bitmap!=null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                } else {
                    tvAddPicture.setError("Field should not be empty");
                    rl.setBackground(getResources().getDrawable(R.drawable.dotted_border_error));
                }

                fos.close();

                DatabaseHelper databaseHelper = new DatabaseHelper(this);

                EditText etTitle = findViewById(R.id.et_title);
                EditText etDescription = findViewById(R.id.et_description);
                boolean etTitleValidation = validateText(etTitle);
                boolean etDescriptionValidation = validateText(etDescription);

                if (etTitleValidation && etDescriptionValidation && bitmap != null) {
                    String title = etTitle.getText().toString().trim();
                    String description = etDescription.getText().toString().trim();

                    databaseHelper.addImage(databaseHelper.getReadableDatabase(), title, description, f.getPath());
                    databaseHelper.close();
                    Toast.makeText(this,"Data added successfully", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(this, MainActivity.class));
                }
            } catch (Exception e ) {
                e.printStackTrace();
            }
        });
        rl.setOnClickListener(v -> CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(AddBlog.this)
        );

        findViewById(R.id.iv_back).setOnClickListener(view ->{
            startActivity(new Intent(this, MainActivity.class));
        });
        findViewById(R.id.button_cancel).setOnClickListener(view ->{
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                try {

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    Glide.with(this).load(resultUri).asBitmap().into(ivImage);
                    tvAddPicture.setVisibility(View.GONE);
                    rl.setBackground(getResources().getDrawable(R.drawable.dotted_border));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private boolean validateText(@NonNull EditText field) {
        String text = field.getText().toString().trim();
        boolean noError = true;

        if(text.isEmpty()) {
            field.setError("Field cannot be empty");
            noError = false;
        }

        return noError;
    }
}