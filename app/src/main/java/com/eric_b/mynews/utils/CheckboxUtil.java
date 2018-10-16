package com.eric_b.mynews.utils;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import com.eric_b.mynews.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckboxUtil extends View {

    @BindView(R.id.art_checkbox) CheckBox chkArt;
    @BindView(R.id.business_checkbox) CheckBox chkBusiness;
    @BindView(R.id.environment_checkbox) CheckBox chkEnvironment;
    @BindView(R.id.politics_checkbox) CheckBox chkPolitics;
    @BindView(R.id.sport_checkbox) CheckBox chkSport;
    @BindView(R.id.travel_checkbox) CheckBox chkTravel;

    String searchCategory = "";

    public CheckboxUtil(Context context) {
        super(context);
        View view = inflate(context, R.layout.activity_search, null);
        ButterKnife.bind(this, view);

    }

    private void read(){

        if (chkArt.isChecked()) searchCategory = "Art";
        if (chkBusiness.isChecked()) searchCategory = searchCategory + " Business";
        if (chkPolitics.isChecked()) searchCategory = searchCategory + " Politics";
        if (chkSport.isChecked()) searchCategory = searchCategory + " Sport";
        if (chkEnvironment.isChecked()) searchCategory = searchCategory + " Environement";
        if (chkTravel.isChecked()) searchCategory = searchCategory + " Travel";
    }


    public String GetSearchCategory() {
        read();
        return searchCategory;
    }
}
