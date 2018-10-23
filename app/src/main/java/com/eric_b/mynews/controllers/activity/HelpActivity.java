package com.eric_b.mynews.controllers.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.eric_b.mynews.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpActivity extends AppCompatActivity{

    @BindView(R.id.skip_button) Button skipButton;
    @BindView(R.id.next_button) Button nextButton;
    private Fragment help1Fragment;
    private ViewPager viewPager;
    private HelpPageAdapter helpViewPagerAdapter;
    private static final String PREF_NAME = "welcome_slider";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private SharedPreferences mFirstLauchPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this,R.layout.activity_help,null);
        ButterKnife.bind(this, view);
        setContentView(view);
        mFirstLauchPreference = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        mFirstLauchPreference.edit().putBoolean(IS_FIRST_TIME_LAUNCH, false).apply();
        this.configureViewPager();
        //this.configureAndShowHelp1Fragment();


        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMynewsActivity();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i>=HelpPageAdapter.getNbOfFragment()-1) nextButton.setText(getString(R.string.Finish));
                else nextButton.setText(getString(R.string.Next));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        }
        );

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextButton.getText()==getString(R.string.Finish)){
                    launchMynewsActivity();
                }
                else {
                    int next = viewPager.getCurrentItem()+1;
                    int numOfFragment = HelpPageAdapter.getNbOfFragment()-1;

                    Log.d("help","next "+next);
                    Log.d("help","NbOfFragment "+numOfFragment);

                    if (next <= numOfFragment) viewPager.setCurrentItem(next);

                }
            }
        });
    }

    private void launchMynewsActivity() {
        Intent intent = new Intent(HelpActivity.this, MynewsActivity.class);
        startActivity(intent);
    }


    private void configureViewPager(){
        viewPager = findViewById(R.id.activity_help_viewpager);
        viewPager.setAdapter(new HelpPageAdapter(getSupportFragmentManager()));
    }


}
