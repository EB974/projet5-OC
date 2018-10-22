package com.eric_b.mynews.controllers.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.eric_b.mynews.R;
import com.eric_b.mynews.models.topstories.TopStorieResult;
import com.eric_b.mynews.models.topstories.TopStoriePojo;
import com.eric_b.mynews.utils.TimesStream;
import com.eric_b.mynews.views.TopStorieAdapter;
import com.eric_b.mynews.views.NewsWebView;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.State;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;



public class TopStoriesFragment extends BaseFragment implements TopStorieAdapter.Listeners {


    @BindView(R.id.topstories_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.topstories_fragment_swipe_container) SwipeRefreshLayout swipeRefreshLayout;

    @State Bundle memory;

    private Disposable disposable;
    private TopStorieAdapter mAdapter;
    private final String NEWS_URL = "News_URL";
    private int recoverPosition;
    private static final String TAG = TopStoriesFragment.class.getSimpleName();
    public TopStoriesFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int lastFirstVisiblePosition = ((LinearLayoutManager) Objects.requireNonNull(mRecyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition();
        outState.putInt("POSITION",lastFirstVisiblePosition);
    }

// --------------
    // BASE METHODS
    // --------------

    @Override
    protected BaseFragment newInstance() {
        return null;
    }

    @Override
    protected int getFragmentLayout() {
        return 0;
    }

    @Override
    protected void configureDesign() { }

    @Override
    protected void updateDesign() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_stories, container, false);
        ButterKnife.bind(this, view);
        this.configureSwipeRefreshLayout();
        configureRecyclerView();
        if (savedInstanceState!= null) {
            recoverPosition = savedInstanceState.getInt("POSITION");
        }
        loadAnswers();
        return view;
    }

    private void configureRecyclerView(){
        this.mAdapter = new TopStorieAdapter( new ArrayList<TopStorieResult>(0), Glide.with(this), new TopStorieAdapter.PostItemListener() {

            @Override
            public void onPostClick(String url) {
                Intent intent = new Intent(getActivity(),NewsWebView.class);
                intent = intent.putExtra(NEWS_URL, url);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadAnswers();
            }
        });
    }

    private void loadAnswers() {
        this.disposable = TimesStream.streamFetchTopStorieNews("home").subscribeWith(new DisposableObserver <TopStoriePojo>() {

            @Override
            public void onNext(TopStoriePojo response) {
                if (response.getNumResults() > 0) {
                    mAdapter.updateAnswers(response.getResults());
                    updateUI();
                }
            }

            @Override
            public void onError(Throwable e) {
                //showErrorMessage();
                Log.e(TAG, "onError() called with: e = [" + e + "]");
            }

            @Override
            public void onComplete() { }
        });
    }

    private void updateUI(){
        swipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
        Objects.requireNonNull(mRecyclerView.getLayoutManager()).scrollToPosition(recoverPosition);
        recoverPosition = 0;
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

}
