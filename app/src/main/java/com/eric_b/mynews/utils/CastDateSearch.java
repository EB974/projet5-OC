package com.eric_b.mynews.utils;

public class CastDateSearch {

    private static String dateSearch;
    private static String date;

    public CastDateSearch(int yy, int mm, int dd) {
        dateSearch=String.valueOf(yy);
        if (mm<10) {
            dateSearch=dateSearch+"0"+String.valueOf(mm); //format date for search API
            date = "0"+String.valueOf(mm)+"/"; //format date for searchActivity
        }
            else {
            dateSearch=dateSearch+String.valueOf(mm);
            date = String.valueOf(mm)+"/";
        }
        if (dd<10) {
            dateSearch=dateSearch+"0"+String.valueOf(dd);
            date = "0"+String.valueOf(dd)+"/"+date;
        }
        else {
            dateSearch=dateSearch+String.valueOf(dd);
            date = String.valueOf(dd)+"/"+date;
        }
        date=date + String.valueOf(yy);
    }

    public static String getDateSearch(){
        return dateSearch;
    } //return date for API

    public static String getDate(){
        return date;
    } //return date for searchActivity
}
