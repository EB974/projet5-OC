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
import com.eric_b.mynews.models.search.SearchDoc;
import com.eric_b.mynews.models.search.SearchMultimedium;
import com.eric_b.mynews.utils.DateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<SearchDoc> mItems;
    private PostItemListener mItemListener;
    private RequestManager glide;

    public ResultAdapter(ArrayList<SearchDoc> results, RequestManager glide, PostItemListener itemListener) {
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
            SearchDoc item = getResults(getAdapterPosition());
            this.mItemListener.onPostClick(item.getWebUrl());
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.news_item, parent, false);
        return new ViewHolder(postView, this.mItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.ViewHolder holder, int position) {
        SearchDoc item = mItems.get(position);
        List<SearchMultimedium> multimediaItems = item.getMultimedia();
        try {
            SearchMultimedium multimedia = multimediaItems.get(2);
            glide.load("https://static01.nyt.com/"+multimedia.getUrl()).apply(RequestOptions.noTransformation()).into(holder.imageTv);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        TextView newsTitle;
        TextView newsDate;

        newsTitle = holder.titleTv;
        newsDate = holder.dateTv;
        newsTitle.setText(item.getHeadline().getMain());
        newsDate.setText(DateAdapter.getDateTopStories(item.getPubDate()));

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<SearchDoc> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private SearchDoc getResults(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(String url);
    }

}
