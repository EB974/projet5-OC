package com.eric_b.mynews.controllers.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v4.app.DialogFragment;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.eric_b.mynews.R;
import com.eric_b.mynews.models.search.SearchPojo;
import com.eric_b.mynews.utils.CastDateSearch;
import com.eric_b.mynews.utils.CheckboxUtil;
import com.eric_b.mynews.utils.TimesStream;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.observers.DisposableObserver;



public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.search_activity_term_input) EditText mImputEditText;
    @BindView(R.id.search_button) Button mSearchButton;
    //@SuppressLint("StaticFieldLeak")
    //@BindView(R.id.date_begin_editText)
    //static EditText mDateBegin;
    //@SuppressLint("StaticFieldLeak")
    //@BindView(R.id.date_end_editText)
    //static EditText mDateEnd;

    private String searchCategory;
    private static String dateSet;
    @SuppressLint("StaticFieldLeak")
    private static EditText mDateBegin;
    @SuppressLint("StaticFieldLeak")
    private static EditText mDateEnd;
    private static String mDBegin;
    private static String mDEnd;
    //private Button mSearchButton;
    //private EditText mImputEditText;
    private String mMessage;
    View mView;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(this,R.layout.activity_search, null);
        setContentView(mView);
        ButterKnife.bind(this, mView);
        mDateBegin = findViewById(R.id.date_begin_editText);
        mDateEnd = findViewById(R.id.date_end_editText);
        //mImputEditText = findViewById(R.id.search_activity_term_input);
        //mSearchButton = findViewById(R.id.search_button);
        mSearchButton.setEnabled(false);
        this.configureToolbar();

        mImputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verification();
            }
        });
    }

    private void verification() {

        if(mDBegin != null && mDEnd != null) {
            if (Integer.parseInt(mDBegin) > Integer.parseInt(mDEnd)) {
                Toast.makeText(SearchActivity.this, "Begin date must be less than end date", Toast.LENGTH_LONG).show();
                return;
            }
        }
        CheckboxUtil box = new CheckboxUtil(this);
        searchCategory = box.GetSearchCategory();
        if(searchCategory == null) {
            Toast.makeText(SearchActivity.this, "One category or more must be checked", Toast.LENGTH_LONG).show();
            return;
        }
        searchArticle();

    }

    public void showAlertDialogButtonClicked(View view) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mMessage);

        // add a button
        builder.setPositiveButton("OK", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void configureToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public static class  DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            if (dateSet.equals("Begin")) setBeginDate(day,month+1,year);
            else setEndDate(day,month+1,year);
        }

        private void setBeginDate(int day, int month, int year) {
            new CastDateSearch(year, month, day);
            mDateBegin.setText(CastDateSearch.getDate());
            mDBegin = CastDateSearch.getDateSearch();
            dateSet = "";
        }
        private void setEndDate(int day, int month, int year) {
            new CastDateSearch(year, month, day);
            mDateEnd.setText(CastDateSearch.getDate());
            mDEnd = CastDateSearch.getDateSearch();
        }
    }

    public void showDateBeginPickerDialog(View v) {
        dateSet = "Begin";
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void showDateEndPickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void searchArticle(){

        DisposableObserver<SearchPojo> disposable = TimesStream.streamFetchSearchNews("newest",searchCategory,mImputEditText.getText().toString(),mDBegin,mDEnd).subscribeWith(new DisposableObserver <SearchPojo>() {
            @Override
            public void onNext(SearchPojo response) {

                if (response.getResponse().getMeta().getHits() > 0) {
                    Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
                    intent.putExtra("inputTerm", mImputEditText.getText().toString());
                    intent.putExtra("searchCategory", searchCategory);
                    intent.putExtra("dateBegin", mDBegin);
                    intent.putExtra("dateEnd", mDEnd);
                    startActivity(intent);
                }
                else {
                    mMessage = "No articles containing your search were found";
                    showAlertDialogButtonClicked(mView);
                }
            }

            @Override
            public void onError(Throwable e) {
                mMessage = "No articles containing your search were found";
                showAlertDialogButtonClicked(mView);
            }

            @Override
            public void onComplete() { }
        });


    }

}
