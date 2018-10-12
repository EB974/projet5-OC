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
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.eric_b.mynews.R;
import com.eric_b.mynews.utils.CastDateSearch;

import java.util.Calendar;
import java.util.Objects;


public class SearchActivity extends AppCompatActivity {
    private static final String SEARCH_CODE = "StringCode";

    //@BindView(R.id.search_activity_term_input) EditText mImputShearch;
    //@BindView(R.id.search_button) Button mSearchButton;
    //@BindView(R.id.date_begin_editText) EditText mDateBegin;

    private String searchCategory;
    private static String mDateBeginSearch;
    private static String dateSet;
    @SuppressLint("StaticFieldLeak")
    private static EditText mDateBegin;
    @SuppressLint("StaticFieldLeak")
    private static EditText mDateEnd;
    private static String mDBegin;
    private static String mDEnd;
    private Button mSearchButton;
    private EditText mImputSearch;
    private String mMessage;
    View mView;
    private CheckBox chkArt;
    private CheckBox chkBusiness;
    private CheckBox chkPolitics;
    private CheckBox chkSport;
    private CheckBox chkEnvironment;
    private CheckBox chkTravel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mDateBegin = findViewById(R.id.date_begin_editText);
        mDateEnd = findViewById(R.id.date_end_editText);
        mImputSearch = findViewById(R.id.search_activity_term_input);
        mSearchButton = findViewById(R.id.search_button);
        mSearchButton.setEnabled(false);
        this.configureToolbar();

        mImputSearch.addTextChangedListener(new TextWatcher() {
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

    private void readCheckbox(){
        searchCategory = "";
        chkArt = findViewById(R.id.art_checkbox);
        chkBusiness = findViewById(R.id.business_checkbox);
        chkEnvironment = findViewById(R.id.environment_checkbox);
        chkPolitics = findViewById(R.id.politics_checkbox);
        chkSport = findViewById(R.id.sport_checkbox);
        chkTravel = findViewById(R.id.travel_checkbox);
        if (chkArt.isChecked()) searchCategory = "Art";
        if (chkBusiness.isChecked()) searchCategory = searchCategory + " Business";
        if (chkPolitics.isChecked()) searchCategory = searchCategory + " Politics";
        if (chkSport.isChecked()) searchCategory = searchCategory + " Sport";
        if (chkEnvironment.isChecked()) searchCategory = searchCategory + " Environement";
        if (chkTravel.isChecked()) searchCategory = searchCategory + " Travel";
    }

    private void verification() {

        if(mDBegin != null && mDEnd != null) {
            if (Integer.parseInt(mDBegin) > Integer.parseInt(mDEnd)) {
                Toast.makeText(SearchActivity.this, "Begin date must be less than end date", Toast.LENGTH_LONG).show();
                return;
            }
        }
        readCheckbox();
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
        //Intent intent = new Intent(SearchActivity.this,ResultActivity.class);
       // startActivity(intent);
        Log.d("SearchAct","deskValue "+ searchCategory);
    }



}
