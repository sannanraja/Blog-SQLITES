package com.app.blogapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.app.blogapp.Adapters.BlogAdapter;
import com.app.blogapp.Adapters.BlogSelectionAdapter;
import com.app.blogapp.DatabaseHelper.DatabaseHelper;
import com.app.blogapp.Models.Blog;
import com.app.blogapp.R;

import java.util.List;

public class SelectionActivity extends AppCompatActivity {
    boolean allSelected = false;
    RecyclerView recyclerView;
    List<Blog> blogList;
    BlogSelectionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        recyclerView = findViewById(R.id.recycler_view);

        adapter = new BlogSelectionAdapter(this);
        blogList = BlogAdapter.blogList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (blogList.size() == 0) {
            startActivity(new Intent(this, MainActivity.class));
        }
        findViewById(R.id.iv_delete).setOnClickListener(view -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(SelectionActivity.this);
            for (Blog blog : blogList) {
                if (blog.getMarkedForDeletion()) {
                    databaseHelper.getReadableDatabase().delete(
                            DatabaseHelper.TABLE_NAME,
                            DatabaseHelper.ID+"=?",
                            new String[] {
                                    String.valueOf(blog.getID())
                            }
                    );
                }
            }
            startActivity(new Intent (SelectionActivity.this, MainActivity.class));
        });
        findViewById(R.id.iv_select_all).setOnClickListener(view ->{
            if (!allSelected) {
                for (Blog blog : blogList) {
                    blog.setMarkedForDeletion(true);
                }
                allSelected = true;
            }
            else {
                for (Blog blog : blogList) {
                    blog.setMarkedForDeletion(false);
                }
                allSelected = false;
            }
            adapter.setData(blogList);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setData(blogList);
    }
}