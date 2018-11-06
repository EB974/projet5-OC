package com.eric_b.mynews.controllers.activity;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.eric_b.mynews.controllers.fragments.BusinessFragment;
import com.eric_b.mynews.controllers.fragments.MostPopularFragment;
import com.eric_b.mynews.controllers.fragments.TopStoriesFragment;
import com.eric_b.mynews.R;


public class MynewsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    //FOR FRAGMENTS
    private Fragment fragmentTop;
    private Fragment fragmentPopular;
    private Fragment fragmentBusiness;

    //FOR DATAS
    private static final int FRAGMENT_TOP = 0;
    private static final int FRAGMENT_POPULAR = 1;
    private static final int FRAGMENT_BUSSINESS = 2;

    //FIRST TIME LAUNCH 
    private static final String PREF_NAME = "welcome_slider";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private View mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(this,R.layout.activity_search, null);
        setContentView(mView);

        setContentView(R.layout.activity_mynews);
        
        firstLaunch();

        // - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureViewPagerAndTabs(0);
        this.configureNavigationView();
    }

    private void firstLaunch() {
        SharedPreferences firstLaunchPreference = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean firstTimeLaunch = firstLaunchPreference.getBoolean(IS_FIRST_TIME_LAUNCH, true);
        if (firstTimeLaunch) startHelpActivity();
    }


    private void configureViewPagerAndTabs(int setTabs){
        // 1 - Get ViewPager from layout
        ViewPager pager = findViewById(R.id.activity_mynews_viewpager);
        // 2 - Set Adapter PageAdapter and glue it together
        pager.setAdapter(new PageAdapter(getSupportFragmentManager()));

        // 1 - Get TabLayout from layout
        TabLayout tabs= findViewById(R.id.activity_mynews_tabs);

        // 2 - Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager);
        // 3 - Design purpose. Tabs have the same width
        tabs.setTabMode(TabLayout.MODE_FIXED);

        TabLayout.Tab tab = tabs.getTabAt(setTabs);
        assert tab != null;
        tab.select();
    }


    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // 4 - Handle Navigation Item Click
        switch (item.getItemId()){
            case R.id.activity_mynews_drawer_topstories :
                this.showFragment(FRAGMENT_TOP);
                break;
            case R.id.activity_mynews_drawer_mostpopular:
                this.showFragment(FRAGMENT_POPULAR);
                break;
            case R.id.activity_mynews_drawer_business:
                this.showFragment(FRAGMENT_BUSSINESS);
                break;
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //3 - Handle actions on menu items
        switch (item.getItemId()) {
            case R.id.menu_activity_mynews_search:
                startShearActivity();
                return true;
            case R.id.menu_activity_mynews_notifications:
                startNotificationActivity();
                return true;
            case R.id.menu_activity_mynews_help:
                startHelpActivity();
                return true;
            case R.id.menu_activity_mynews_about:
                showAlertDialogButtonClicked(mView);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

// ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    private void configureToolBar(){
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    // 2 - Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout =  findViewById(R.id.activity_mynews_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    // 3 - Configure NavigationView
    public void configureNavigationView(){
        NavigationView navigationView = findViewById(R.id.activity_mynews_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_mynews, menu);
        return true;
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    private void showFragment(int fragmentIdentifier){

         switch (fragmentIdentifier){
            case FRAGMENT_TOP :
                this.showTopFragment();
                break;
            case FRAGMENT_POPULAR:
                this.showPopularFragment();
                break;
            case FRAGMENT_BUSSINESS:
                this.showBusinessFragment();
                break;
            default:
                break;
        }
    }

    private void showTopFragment(){
        if (this.fragmentTop == null) this.fragmentTop = new TopStoriesFragment();
        this.startTransactionFragment(this.fragmentTop,"TopStories");
        configureViewPagerAndTabs(0);
    }

    private void showPopularFragment(){
        if (this.fragmentPopular == null) this.fragmentPopular = new MostPopularFragment();
        this.startTransactionFragment(this.fragmentPopular,"MostPopular");
        configureViewPagerAndTabs(1);
    }

    private void showBusinessFragment(){
        if (this.fragmentBusiness == null) this.fragmentBusiness = new BusinessFragment();
        this.startTransactionFragment(this.fragmentBusiness,"Business");
        configureViewPagerAndTabs(2);
    }

    private void startTransactionFragment(Fragment fragment, String nameFragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_mynews_frame_layout, fragment,nameFragment).commit();
        }
    }

//-------------------
// Start Activities
//-------------------

    private void startShearActivity(){
        Intent intent = new Intent(MynewsActivity.this, SearchActivity.class);
        this.startActivity(intent);
    }

    private void startNotificationActivity(){
        Intent intent = new Intent(MynewsActivity.this, NotificationActivity.class);
        startActivity(intent);
    }

    private void startHelpActivity(){
        Intent intent = new Intent(MynewsActivity.this, HelpActivity.class);
        startActivity(intent);
    }

//-------------------
// Dialogue message
//-------------------

    public void showAlertDialogButtonClicked(View view) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.about_text));

        // add a button
        builder.setPositiveButton(R.string.Ok, null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
