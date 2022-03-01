package com.app.blogapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.blogapp.Activities.BlogDetails;
import com.app.blogapp.Activities.SelectionActivity;
import com.app.blogapp.Models.Blog;
import com.app.blogapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.MyHolder> {
    public static List<Blog> blogList;
    Context context;
    public BlogAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.list_item_blog, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Blog blog = blogList.get(position);
        holder.tvBlogTitle.setText(blog.getBlogTitle());
        holder.tvBlogDescription.setText(blog.getBlogDescription());
        holder.tvBlogDatePublished.setText(blog.getDate());
        Glide.with(context).load(blog.getImagePath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.tvBlogImage);
        holder.itemView.setOnClickListener(view -> {

                Intent intent = new Intent(context, BlogDetails.class);
                intent.putExtra("blog", blog);
                context.startActivity(intent);
        });
        holder.itemView.setOnLongClickListener(view -> {
            blogList.get(holder.getAdapterPosition()).setMarkedForDeletion(true);
            Intent intent = new Intent(context, SelectionActivity.class);
            context.startActivity(intent);
            return false;
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
        BlogAdapter.blogList = blogList;
    }
}
