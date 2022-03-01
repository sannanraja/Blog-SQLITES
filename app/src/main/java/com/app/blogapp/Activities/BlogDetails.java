package com.app.blogapp.Activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.app.blogapp.DatabaseHelper.DatabaseHelper;
import com.app.blogapp.Models.Blog;
import com.app.blogapp.R;
import com.bumptech.glide.Glide;

import java.io.File;

public class BlogDetails extends AppCompatActivity {
    Blog blog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);
        blog = (Blog) getIntent().getSerializableExtra("blog");
        ((TextView)findViewById(R.id.et_title)).setText(blog.getBlogTitle());
        ((TextView)findViewById(R.id.et_description)).setText(blog.getBlogDescription());
        ((TextView)findViewById(R.id.tv_published_date)).setText(blog.getDate());

        Glide.with(this).load(blog.getImagePath()).asBitmap().into(((ImageView) findViewById(R.id.iv_image)));
        findViewById(R.id.iv_back).setOnClickListener(view -> startActivity(new Intent(BlogDetails.this, MainActivity.class)));
        (findViewById(R.id.btn_edit)).setOnClickListener(view -> {
            Intent intent = new Intent(BlogDetails.this,EditBlog.class);
            intent.putExtra("blog",blog);
            startActivity(intent);
        });

        (findViewById(R.id.btn_delete)).setOnClickListener(view -> {
           DatabaseHelper databaseHelper = new DatabaseHelper(this);
           databaseHelper.getReadableDatabase().delete(
                   DatabaseHelper.TABLE_NAME,
                   DatabaseHelper.ID+"=?",
                   new String[] {
                           String.valueOf(blog.getID())
                        }
                   );
           startActivity((new Intent(BlogDetails.this, MainActivity.class)));
        });

        findViewById(R.id.iv_share).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            File file = new File(blog.getImagePath());
            File f = new File(new ContextWrapper(BlogDetails.this).getDir("images", Context.MODE_PRIVATE),file.getName());
            Uri imageUri = FileProvider.getUriForFile(
                    BlogDetails.this,
                    BlogDetails.this.getPackageName()+".provider", //(use your app signature + ".provider" )
                    f);
            intent.setDataAndType(imageUri,"*/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.putExtra(Intent.EXTRA_SUBJECT, blog.getBlogTitle());
            intent.putExtra(Intent.EXTRA_TEXT, blog.getBlogDescription());
            startActivity(Intent.createChooser(intent, "Share Image using"));
        });

        findViewById(R.id.iv_gmail).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            File file = new File(blog.getImagePath());
            File f = new File(new ContextWrapper(BlogDetails.this).getDir("images", Context.MODE_PRIVATE),file.getName());
            Uri imageUri = FileProvider.getUriForFile(
                    BlogDetails.this,
                    BlogDetails.this.getPackageName()+".provider", //(use your app signature + ".provider" )
                    f);
            intent.setDataAndType(imageUri,"message/rfc822");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.putExtra(Intent.EXTRA_SUBJECT, blog.getBlogTitle());
            intent.putExtra(Intent.EXTRA_TEXT, blog.getBlogDescription());
            startActivity(Intent.createChooser(intent, "Choose App"));
        });
    }
}