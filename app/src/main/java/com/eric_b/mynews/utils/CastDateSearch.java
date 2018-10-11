package com.eric_b.mynews.utils;

public class CastDateSearch {
    int yy;
    int mm;
    int dd;
    static String dateSearch;

    public CastDateSearch(int yy, int mm, int dd) {
        dateSearch=String.valueOf(yy);
        if (mm<10) dateSearch=dateSearch+"0"+String.valueOf(mm);
            else dateSearch=dateSearch+String.valueOf(mm);
        if (dd<10) dateSearch=dateSearch+"0"+String.valueOf(dd);
        else dateSearch=dateSearch+String.valueOf(dd);
    }

    public static String getDateSearch(){
        return dateSearch;
    }

}
