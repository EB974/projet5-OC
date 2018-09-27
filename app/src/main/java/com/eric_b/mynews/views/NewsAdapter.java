package com.eric_b.mynews.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.eric_b.mynews.R;
import com.eric_b.mynews.models.Multimedium;
import com.eric_b.mynews.models.Result;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<Result> mItems;
    private PostItemListener mItemListener;
    private RequestManager glide;

    public NewsAdapter(List<Result> results, RequestManager glide, PostItemListener itemListener) {
        mItemListener = itemListener;
        mItems = results;
        this.glide = glide;
    }

    public interface Listeners {
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        PostItemListener mItemListener;
        ImageView imageTv;
        TextView titleTv;
        TextView dateTv;

        ViewHolder(View newsView, PostItemListener postItemListener) {
            super(newsView);
            titleTv = newsView.findViewById(R.id.news_item_title);
            dateTv = newsView.findViewById(R.id.news_item_date);
            imageTv = newsView.findViewById(R.id.news_item_image);
            this.mItemListener = postItemListener;
            newsView.setOnClickListener(this);
            //CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        }


        @Override
        public void onClick(View view) {
            Result item = getResults(getAdapterPosition());
            this.mItemListener.onPostClick(item.getShortUrl());
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.news_item, parent, false);
        return new ViewHolder(postView, this.mItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        Result item = mItems.get(position);
        List<Multimedium> multimediaItems = item.getMultimedia();
        try {
            Multimedium multimedia = multimediaItems.get(0);
            glide.load(multimedia.getUrl()).apply(RequestOptions.noTransformation()).into(holder.imageTv);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        TextView newsTitle;
        TextView newsDate;

        newsTitle = holder.titleTv;
        newsDate = holder.dateTv;
        newsTitle.setText(item.getTitle());
        newsDate.setText(dateAdapter(item.getUpdatedDate()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<Result> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private Result getResults(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(String url);
    }

    public static String dateAdapter(String newsDate){
        String mDate = newsDate;
        String mDay = mDate.substring(0,10);

        String format = "yyyy-MM-dd";
        @SuppressLint("SimpleDateFormat") java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
        java.util.Date date = new java.util.Date();
        String dayNewsDate = formater.format(date);

        if(dayNewsDate.equals(mDay)){
            mDay = "today";
        };

        newsDate = mDay+" "+mDate.substring(11,16);
        return newsDate;
    }

}