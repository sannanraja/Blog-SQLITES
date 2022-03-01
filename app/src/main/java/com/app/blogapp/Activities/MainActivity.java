package com.app.blogapp.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.blogapp.Adapters.BlogAdapter;
import com.app.blogapp.DatabaseHelper.DatabaseHelper;
import com.app.blogapp.Models.Blog;
import com.app.blogapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    List<Blog> blogList;
    BlogAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        databaseHelper = new DatabaseHelper(this);

        adapter = new BlogAdapter(this);
        blogList = new ArrayList<>();
        setData();

        findViewById(R.id.fab).setOnClickListener(view-> {
            recyclerView.removeAllViews();
            databaseHelper.close();
            Intent intent = new Intent(MainActivity.this, AddBlog.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        if (blogList.size() > 0) {
            recyclerView.setOnLongClickListener(view -> {
                startActivity(new Intent(MainActivity.this, SelectionActivity.class));
                return false;
            });
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    public void setData() {

        Cursor cursor = databaseHelper.fetchAllData(databaseHelper.getReadableDatabase());

        while(cursor.moveToNext()) {
            int blogIdIndex = cursor.getColumnIndex(DatabaseHelper.ID);
            int blogTitleIndex = cursor.getColumnIndex(DatabaseHelper.BLOG_TITLE);
            int blogDescriptionIndex = cursor.getColumnIndex(DatabaseHelper.BLOG_DESCRIPTION);
            int blogPublishedDateIndex = cursor.getColumnIndex(DatabaseHelper.BLOG_PUBLISHED_DATE);

            try {
                int imagePathIndex = cursor.getColumnIndex(DatabaseHelper.BLOG_IMAGE_PATH);

                blogList.add(new Blog(
                        cursor.getInt(blogIdIndex),
                        cursor.getString(blogTitleIndex),
                        cursor.getString(blogDescriptionIndex),
                        cursor.getString(imagePathIndex),
                        cursor.getString(blogPublishedDateIndex)
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        adapter.setData(blogList);
    }
}