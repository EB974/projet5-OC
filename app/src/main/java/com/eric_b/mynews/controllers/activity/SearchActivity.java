package com.eric_b.mynews.controllers.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.eric_b.mynews.R;

import java.text.BreakIterator;
import java.util.Calendar;

import butterknife.BindView;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.search_activity_term_input)
    EditText mImputShearch;
    @BindView(R.id.search_button)
    Button mSearchButton;
    //@BindView(R.id.date_begin_editText) EditText mDateBegin;

    @BindView(R.id.date_end_editText)
    EditText mDateEnd;
    private String deskValues;
    private static String mDateBeginSearch;
    private static String dateSet;
    private static EditText mDateBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mDateBegin = findViewById(R.id.date_begin_editText);
        this.configureToolbar();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.art_checkbox:
                if (checked)
                deskValues = deskValues + " Art";
                break;
            case R.id.business_checkbox:
                if (checked)
                    deskValues = deskValues + " Business";
                break;
            case R.id.politics_checkbox:
                if (checked)
                    deskValues = deskValues + " Politics";
                break;
            case R.id.sport_checkbox:
                if (checked)
                    deskValues = deskValues + " Sport";
                break;
            case R.id.environment_checkbox:
                if (checked)
                    deskValues = deskValues + " Environement";
                break;
            case R.id.travel_checkbox:
                if (checked)
                    deskValues = deskValues + " Travel";
                break;
        }
    }

    private void configureToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public static class  DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Log.d("SearchAct","tag "+"datePicker");
            if (dateSet=="Begin") setBeginDate(day,month,year);
            Log.d("SearchAct","dateSet "+dateSet);
        }

        private void setBeginDate(int day, int month, int year) {
            mDateBegin.setText(day+"/"+month+"/"+year);
            mDateBeginSearch = String.format("tYtmtd", year, month, day);
            Log.d("SearchAct","mDateBeginSearch "+mDateBeginSearch);
            dateSet = "";
        }
    }

    public void showDateBeginPickerDialog(View v) {
        dateSet = "Begin";
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void searchArticle(){


    }


}
