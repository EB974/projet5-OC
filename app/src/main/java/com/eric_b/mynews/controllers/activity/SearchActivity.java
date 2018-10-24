package com.eric_b.mynews.controllers.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
    @BindView(R.id.art_checkbox) CheckBox chkArt;
    @BindView(R.id.business_checkbox) CheckBox chkBusiness;
    @BindView(R.id.environment_checkbox) CheckBox chkEnvironment;
    @BindView(R.id.politics_checkbox) CheckBox chkPolitics;
    @BindView(R.id.sport_checkbox) CheckBox chkSport;
    @BindView(R.id.travel_checkbox) CheckBox chkTravel;
    //@SuppressLint("StaticFieldLeak")
    //@BindView(R.id.date_begin_editText)
    //static EditText mDateBegin;
    //@SuppressLint("StaticFieldLeak")
    //@BindView(R.id.date_end_editText)
    //static EditText mDateEnd;

    public static final String PREF_NOTIF = "NOTIF";
    public static final String PREF_WORD = "SEARCH_WORD" ;
    public static final String PREF_ART = "ART";
    public static final String PREF_BUSINESS = "BUSINESS";
    public static final String PREF_SPORT = "SPORT";
    public static final String PREF_POLITICS = "POLITICS";
    public static final String PREF_ENVIRONMENT = "ENVIRONMENT";
    public static final String PREF_TRAVEL = "TRAVEL";
    private SharedPreferences mPreferences;


    private static String dateSet;
    @SuppressLint("StaticFieldLeak")
    private static EditText mDateBegin;
    @SuppressLint("StaticFieldLeak")
    private static EditText mDateEnd;
    private static String mDBegin;
    private static String mDEnd;
    private String mMessage;
    View mView;
    DisposableObserver<SearchPojo> disposable;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(this,R.layout.activity_search, null);
        setContentView(mView);
        ButterKnife.bind(this, mView);
        mDateBegin = findViewById(R.id.date_begin_editText);
        mDateEnd = findViewById(R.id.date_end_editText);
        mPreferences = getSharedPreferences(PREF_NOTIF,MODE_PRIVATE);
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
                if (verification()) searchArticle();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPreferences.edit().putString(PREF_WORD,mImputEditText.getText().toString()).apply();
        memCheckbox();
        disposeWhenDestroy();
    }

    private boolean verification() {
        Boolean check =true;
        if(mDBegin != null && mDEnd != null) {
            if (Integer.parseInt(mDBegin) > Integer.parseInt(mDEnd)) {
                Toast.makeText(SearchActivity.this, R.string.DateMessage, Toast.LENGTH_LONG).show();
                check = false;
            }
        }

        if(CheckboxUtil.getCategory(chkArt.isChecked(),chkBusiness.isChecked(),chkPolitics.isChecked(),chkSport.isChecked(),chkEnvironment.isChecked(),chkTravel.isChecked()).length() == 0) {
            Toast.makeText(SearchActivity.this, R.string.CheckboxMessage, Toast.LENGTH_LONG).show();
            check = false;
        }
        return check;
    }

    public void showAlertDialogButtonClicked(View view) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mMessage);

        // add a button
        builder.setPositiveButton(R.string.Ok, null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void configureToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setTitle(getText(R.string.Search_article));
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
        dateSet = "End";
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void searchArticle(){
         disposable = TimesStream.streamFetchSearchNews("best",CheckboxUtil.getCategory(chkArt.isChecked(),chkBusiness.isChecked(),chkPolitics.isChecked(),chkSport.isChecked(),chkEnvironment.isChecked(),chkTravel.isChecked()),mImputEditText.getText().toString(),mDBegin,mDEnd).subscribeWith(new DisposableObserver <SearchPojo>() {
            @Override
            public void onNext(SearchPojo response) {

                if (response.getResponse().getMeta().getHits() > 0) {
                    Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
                    intent.putExtra("title",getText(R.string.Result_for) +mImputEditText.getText().toString());
                    intent.putExtra("inputTerm", mImputEditText.getText().toString());
                    intent.putExtra("searchCategory", CheckboxUtil.getCategory(chkArt.isChecked(),chkBusiness.isChecked(),chkPolitics.isChecked(),chkSport.isChecked(),chkEnvironment.isChecked(),chkTravel.isChecked()));
                    intent.putExtra("dateBegin", mDBegin);
                    intent.putExtra("dateEnd", mDEnd);
                    startActivity(intent);
                }
                else {
                    mMessage = getString(R.string.No_article);
                    showAlertDialogButtonClicked(mView);
                }
            }

            @Override
            public void onError(Throwable e) {
                mMessage = getString(R.string.No_article);
                showAlertDialogButtonClicked(mView);
            }

            @Override
            public void onComplete() { }
        });


    }
    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    private void memCheckbox(){
        if (chkArt.isChecked()) mPreferences.edit().putBoolean(PREF_ART,true).apply();
        else mPreferences.edit().putBoolean(PREF_ART,false).apply();
        if (chkBusiness.isChecked()) mPreferences.edit().putBoolean(PREF_BUSINESS,true).apply();
        else mPreferences.edit().putBoolean(PREF_BUSINESS,false).apply();
        if (chkPolitics.isChecked()) mPreferences.edit().putBoolean(PREF_POLITICS,true).apply();
        else mPreferences.edit().putBoolean(PREF_POLITICS,false).apply();
        if (chkSport.isChecked()) mPreferences.edit().putBoolean(PREF_SPORT,true).apply();
        else mPreferences.edit().putBoolean(PREF_SPORT,false).apply();
        if (chkEnvironment.isChecked()) mPreferences.edit().putBoolean(PREF_ENVIRONMENT,true).apply();
        else mPreferences.edit().putBoolean(PREF_ENVIRONMENT,false).apply();
        if (chkTravel.isChecked()) mPreferences.edit().putBoolean(PREF_TRAVEL,true).apply();
        else mPreferences.edit().putBoolean(PREF_TRAVEL,false).apply();
    }


}
