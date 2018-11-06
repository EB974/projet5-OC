package com.eric_b.mynews.controllers.activity;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.eric_b.mynews.R;
import com.eric_b.mynews.utils.CheckboxUtil;
import com.eric_b.mynews.utils.NotifBroadcastReceiver;
import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationActivity extends AppCompatActivity {

    public static final String PREF_NOTIF = "NOTIF";
    public static final String PREF_SWITCH = "SWITCH";
    public static final String PREF_WORD = "SEARCH_WORD" ;
    public static final String PREF_ART = "ART";
    public static final String PREF_BUSINESS = "BUSINESS";
    public static final String PREF_SPORT = "SPORT";
    public static final String PREF_POLITICS = "POLITICS";
    public static final String PREF_ENVIRONMENT = "ENVIRONMENT";
    public static final String PREF_TRAVEL = "TRAVEL";
    private SharedPreferences mPreferences;
    private String searchWord;


    @BindView(R.id.art_checkbox) CheckBox chkArt;
    @BindView(R.id.business_checkbox) CheckBox chkBusiness;
    @BindView(R.id.environment_checkbox) CheckBox chkEnvironment;
    @BindView(R.id.politics_checkbox) CheckBox chkPolitics;
    @BindView(R.id.sport_checkbox) CheckBox chkSport;
    @BindView(R.id.travel_checkbox) CheckBox chkTravel;
    @BindView(R.id.switch_notification) Switch mSwitchNotification;
    @BindView(R.id.notif_activity_term_input) EditText mNotifTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create view for ButterKnife
        View mView = View.inflate(this,R.layout.activity_notification, null);
        setContentView(mView);
        ButterKnife.bind(this, mView);

        this.configureToolbar();
        mPreferences = getSharedPreferences(PREF_NOTIF,MODE_PRIVATE);
        mSwitchNotification.setEnabled(false);
        chechMemory(); //recover sharedPreferences memory


        // hide keyboard on tap on screen
        mNotifTerm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        mNotifTerm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // enable notification switch if key words are enter
                mSwitchNotification.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // put switch on false if key word is delete
                if(!mSwitchNotification.isEnabled()) {
                    mSwitchNotification.setChecked(false);
                    mSwitchNotification.setText(R.string.Notification_disable);
                }
            }
        });

        mSwitchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // verify if one or more checkbox is checked
                        if(CheckboxUtil.getCategory(chkArt.isChecked(),chkBusiness.isChecked(),chkPolitics.isChecked(),chkSport.isChecked(),chkEnvironment.isChecked(),chkTravel.isChecked()).length()==0){
                        Toast.makeText(NotificationActivity.this, R.string.CheckboxMessage, Toast.LENGTH_LONG).show();
                        mSwitchNotification.setChecked(false);
                        mSwitchNotification.setText(R.string.Notification_disable);
                    }else {

                        mSwitchNotification.setText(R.string.Notification_enable);
                        notificationAlarm(true);
                    }
                }
                else {
                    mSwitchNotification.setText(R.string.Notification_disable);
                    notificationAlarm(false);
                    }
            }
        });
    }

    private void chechMemory(){
        if(mPreferences.getString(PREF_WORD,"").length()>0){
            searchWord = mPreferences.getString(PREF_WORD,null);
            mNotifTerm.setText(searchWord);
            mNotifTerm.setSelection(searchWord.length());
            mSwitchNotification.setEnabled(true);
            if (mPreferences.getBoolean(PREF_SWITCH,false)) {
                mSwitchNotification.setChecked(true);
                mSwitchNotification.setText(R.string.Notification_enable);
                notificationAlarm(true); //restarts notification in case of service stop
            }
            else {
                mSwitchNotification.setChecked(false);
            }
            checkCheckbox();
        }
    }

    private void configureToolbar(){
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // save notification screen
        if(mSwitchNotification.isChecked() && searchWord.length() != 0) {
            mPreferences.edit().putBoolean(PREF_SWITCH,true).apply();
            mPreferences.edit().putString(PREF_WORD,searchWord).apply();
            memCheckbox();
        }
        else {
            mPreferences.edit().putBoolean(PREF_SWITCH,false).apply();
            mPreferences.edit().putString(PREF_WORD,"").apply();
        }
    }

    private void memCheckbox(){ // record the state of checkboxs
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

    private void checkCheckbox(){ // check the checkbox according to the records
        if (mPreferences.getBoolean(PREF_ART,false)) chkArt.setChecked(true);
        else chkArt.setChecked(false);
        if (mPreferences.getBoolean(PREF_BUSINESS,false)) chkBusiness.setChecked(true);
        else chkBusiness.setChecked(false);
        if (mPreferences.getBoolean(PREF_POLITICS,false)) chkPolitics.setChecked(true);
        else chkPolitics.setChecked(false);
        if (mPreferences.getBoolean(PREF_SPORT,false)) chkSport.setChecked(true);
        else chkSport.setChecked(false);
        if (mPreferences.getBoolean(PREF_ENVIRONMENT,false)) chkEnvironment.setChecked(true);
        else chkEnvironment.setChecked(false);
        if (mPreferences.getBoolean(PREF_TRAVEL,false)) chkTravel.setChecked(true);
        else chkTravel.setChecked(false);
    }

    private void notificationAlarm(boolean alarmOn) {
        // set the alarm manager
        searchWord = mNotifTerm.getText().toString();
        Intent alarmIntent = new Intent(NotificationActivity.this, NotifBroadcastReceiver.class);
        alarmIntent.putExtra("inputTerms", searchWord);
        alarmIntent.putExtra("searchCategory", CheckboxUtil.getCategory(chkArt.isChecked(),chkBusiness.isChecked(),chkPolitics.isChecked(),chkSport.isChecked(),chkEnvironment.isChecked(),chkTravel.isChecked()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationActivity.this, 234, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmOn){ //notifications on
            assert alarmManager != null;
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,0, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        else{ //notification off if it exists
            if (alarmManager != null) alarmManager.cancel(pendingIntent);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
