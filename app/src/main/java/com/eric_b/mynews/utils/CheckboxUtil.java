package com.eric_b.mynews.utils;



public class CheckboxUtil {
    public static String getCategory(Boolean art, Boolean business, Boolean politics, Boolean sport, Boolean environment, Boolean travel) {
        String category = "";
        if (art) category = "Art";
        if (business) category = category + " Business";
        if (politics) category = category + " Politics";
        if (sport) category = category + " Sport";
        if (environment) category = category + " Environement";
        if (travel) category = category + " Travel";
        return category;
    }

}
