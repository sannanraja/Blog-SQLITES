package com.app.blogapp.Activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.blogapp.DatabaseHelper.DatabaseHelper;
import com.app.blogapp.Models.Blog;
import com.app.blogapp.R;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditBlog extends AppCompatActivity {
    private ImageView ivImage;
    private Bitmap bitmap;
    RelativeLayout rl;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_blog);
        EditText etTitle = findViewById(R.id.et_title);
        EditText etDescription = findViewById(R.id.et_description);

        databaseHelper = new DatabaseHelper(this);

        ivImage = findViewById(R.id.iv_image);

        rl = findViewById(R.id.rl_pic_container);

        Blog blog = (Blog) getIntent().getSerializableExtra("blog");
        final File[] f = {null};

        etTitle.setText(blog.getBlogTitle());
        etDescription.setText(blog.getBlogDescription());
        Glide.with(this).load(blog.getImagePath()).asBitmap().into(ivImage);

        findViewById(R.id.iv_back).setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
        findViewById(R.id.button_cancel).setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
        findViewById(R.id.button_ok).setOnClickListener(view -> {
            try {
                FileOutputStream fos;

                if (bitmap != null) {
                    /* path to /data/data/your app/app_data/imageDir */
                    // Create imageDir
                    String FILENAME = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".jpeg";
                    f[0] = new File(new ContextWrapper(getApplicationContext()).getDir("images", Context.MODE_PRIVATE), FILENAME);
                    fos = new FileOutputStream(f[0]);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();
                }


                boolean etTitleValidation = validateText(etTitle);
                boolean etDescriptionValidation = validateText(etDescription);

                if (etTitleValidation && etDescriptionValidation) {
                    String title = etTitle.getText().toString().trim();
                    String description = etDescription.getText().toString().trim();

                    if (bitmap != null) {

                        databaseHelper.updateImage(databaseHelper.getReadableDatabase(), blog.getID(),title, description, f[0].getPath(), blog.getDate());
                    }
                    else {
                        databaseHelper.updateImage(databaseHelper.getReadableDatabase(), blog.getID(),title, description, blog.getImagePath(), blog.getDate());
                    }
                    databaseHelper.close();
                    Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();


                    startActivity(new Intent(this, MainActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        rl.setOnClickListener(v -> CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(EditBlog.this)
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    Glide.with(this).load(resultUri).asBitmap().into(ivImage);
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

        if (text.isEmpty()) {
            field.setError("Field cannot be empty");
            noError = false;
        }

        return noError;
    }
}