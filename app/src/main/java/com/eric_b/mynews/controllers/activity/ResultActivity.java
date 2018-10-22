package com.eric_b.mynews.controllers.activity;


import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.bumptech.glide.Glide;
import com.eric_b.mynews.R;
import com.eric_b.mynews.models.search.SearchDoc;
import com.eric_b.mynews.models.search.SearchPojo;
import com.eric_b.mynews.utils.TimesStream;
import com.eric_b.mynews.views.NewsWebView;
import com.eric_b.mynews.views.ResultAdapter;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ResultActivity extends AppCompatActivity {
    @BindView(R.id.result_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.result_swipe_container) SwipeRefreshLayout swipeRefreshLayout;

    private int recoverPosition;
    private Disposable disposable;
    private final String NEWS_URL = "News_URL";
    private static final String TAG = ResultActivity.class.getSimpleName();
    private ResultAdapter mAdapter;
    private CharSequence title;
    private String inputTerm;
    private String searchCategory;
    private String searchBegin;
    private String searchEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this,R.layout.activity_result,null);
        ButterKnife.bind(this, view);
        setContentView(view);
        Intent intent = getIntent();
        title = intent.getCharSequenceExtra("title");
        inputTerm = intent.getStringExtra("inputTerm");
        searchCategory = intent.getStringExtra("searchCategory");
        searchBegin = intent.getStringExtra("dateBegin");
        searchEnd = intent.getStringExtra("dateEnd");
        this.configureToolbar();
        this.configureSwipeRefreshLayout();
        configureRecyclerView();
        if (savedInstanceState!= null) {
            recoverPosition = savedInstanceState.getInt("POSITION");
        }
        loadAnswers();

    }
    private void configureToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setTitle(title);
        ab.setDisplayHomeAsUpEnabled(true);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposeWhenDestroy();
    }

    private void configureRecyclerView(){
        this.mAdapter = new ResultAdapter( new ArrayList<SearchDoc>(0), Glide.with(this), new ResultAdapter.PostItemListener() {

            @Override
            public void onPostClick(String url) {
                Intent intent = new Intent(ResultActivity.this,NewsWebView.class);
                intent = intent.putExtra(NEWS_URL, url);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(this), DividerItemDecoration.VERTICAL);
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
        this.disposable = TimesStream.streamFetchSearchNews("best",searchCategory,inputTerm,searchBegin,searchEnd).subscribeWith(new DisposableObserver<SearchPojo>() {

            @Override
            public void onNext(SearchPojo response) {
                if (response.getResponse().getMeta().getHits() > 0) {
                    mAdapter.updateAnswers(response.getResponse().getDocs());
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
