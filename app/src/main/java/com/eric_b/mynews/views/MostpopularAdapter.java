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
import com.eric_b.mynews.models.mostpopular.MediaMetadatum;
import com.eric_b.mynews.models.mostpopular.Medium;
import com.eric_b.mynews.models.mostpopular.MostPopularResult;
import com.eric_b.mynews.utils.DateAdapter;

import java.util.List;

public class MostpopularAdapter extends RecyclerView.Adapter<MostpopularAdapter.ViewHolder> {

    private List<MostPopularResult> mItems;
    private PostItemListener mItemListener;
    private RequestManager glide;

    public MostpopularAdapter(List<MostPopularResult> results, RequestManager glide, PostItemListener itemListener) {
        mItemListener = itemListener;
        mItems = results;
        this.glide = glide;
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
            MostPopularResult item = getResults(getAdapterPosition());
            this.mItemListener.onPostClick(item.getUrl());
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public MostpopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.news_item, parent, false);
        return new ViewHolder(postView, this.mItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MostPopularResult item = mItems.get(position);
        List<Medium> multimediaItems = item.getMedia();
        Medium multimedia = multimediaItems.get(0);
        try {
            List<MediaMetadatum> multimediaData = multimedia.getMediaMetadata();
            MediaMetadatum multimediaUrl = multimediaData.get(0);
            glide.load(multimediaUrl.getUrl()).apply(RequestOptions.noTransformation()).into(holder.imageTv);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        TextView newsTitle;
        TextView newsDate;

        newsTitle = holder.titleTv;
        newsDate = holder.dateTv;
        newsTitle.setText(item.getTitle());
        newsDate.setText(DateAdapter.getDateMostPopular(item.getPublishedDate()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<MostPopularResult> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private MostPopularResult getResults(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(String url);
    }

}
