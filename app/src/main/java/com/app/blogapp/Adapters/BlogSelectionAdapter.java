package com.app.blogapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.blogapp.Models.Blog;
import com.app.blogapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class BlogSelectionAdapter extends RecyclerView.Adapter<BlogSelectionAdapter.MyHolder> {
    private List<Blog> blogList;
    Context context;

    public BlogSelectionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BlogSelectionAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BlogSelectionAdapter.MyHolder(LayoutInflater.from(context).inflate(R.layout.list_item_blog, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BlogSelectionAdapter.MyHolder holder, int position) {
        Blog blog = blogList.get(position);
        holder.tvBlogTitle.setText(blog.getBlogTitle());
        holder.tvBlogDescription.setText(blog.getBlogDescription());
        holder.tvBlogDatePublished.setText(blog.getDate());
        Glide.with(context).load(blog.getImagePath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.tvBlogImage);
        if (blog.getMarkedForDeletion()) {
            holder.ivCheckMark.setVisibility(View.VISIBLE);
        }
        else {
            holder.ivCheckMark.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view -> {
           if (!blog.getMarkedForDeletion()) {
               holder.ivCheckMark.setVisibility(View.VISIBLE);
           }
           else {
               holder.ivCheckMark.setVisibility(View.GONE);
           }
           blog.setMarkedForDeletion(!blog.getMarkedForDeletion());
        });
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tvBlogTitle;
        public TextView tvBlogDescription;
        public TextView tvBlogDatePublished;
        public ImageView tvBlogImage;
        public CardView cvItem;
        public ImageView ivCheckMark;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cvItem = itemView.findViewById(R.id.cv_item);
            tvBlogImage = itemView.findViewById(R.id.iv_image);
            tvBlogDescription = itemView.findViewById(R.id.tv_blog_description);
            tvBlogTitle = itemView.findViewById(R.id.tv_blog);
            tvBlogDatePublished = itemView.findViewById(R.id.tv_date);
            ivCheckMark = itemView.findViewById(R.id.iv_check_mark);
        }
    }

    public void setData(List<Blog> blogList) {
        this.blogList = blogList;
    }
}