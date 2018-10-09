package com.eric_b.mynews.views;

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
import com.eric_b.mynews.models.TopStorieResult;
import com.eric_b.mynews.utils.DateAdapter;

import java.util.ArrayList;
import java.util.List;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder> {

    private List<TopStorieResult> mItems;
    private PostItemListener mItemListener;
    private RequestManager glide;

    public BusinessAdapter(ArrayList<TopStorieResult> results, RequestManager glide, PostItemListener itemListener) {
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
            TopStorieResult item = getResults(getAdapterPosition());
            this.mItemListener.onPostClick(item.getShortUrl());
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public BusinessAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.news_item, parent, false);
        return new ViewHolder(postView, this.mItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessAdapter.ViewHolder holder, int position) {
        TopStorieResult item = mItems.get(position);
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
        newsDate.setText(DateAdapter.getDateTopStories(item.getUpdatedDate()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<TopStorieResult> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private TopStorieResult getResults(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(String url);
    }

}
