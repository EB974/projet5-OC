package com.eric_b.mynews.utils;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import com.eric_b.mynews.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckboxUtil {

    static String category = "";

    public static String getCategory(Boolean art, Boolean business, Boolean politics, Boolean sport, Boolean environment, Boolean travel) {

        if (art) category = "Art";
        if (business) category = category + " Business";
        if (politics) category = category + " Politics";
        if (sport) category = category + " Sport";
        if (environment) category = category + " Environement";
        if (travel) category = category + " Travel";
        return category;
    }

}
