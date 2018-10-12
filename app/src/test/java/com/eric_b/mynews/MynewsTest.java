package com.eric_b.mynews;

import android.annotation.SuppressLint;

import com.eric_b.mynews.utils.CastDateSearch;
import com.eric_b.mynews.utils.DateAdapter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MynewsTest {
    @Test
    public void getDateTopStoriesTest() {
        assertEquals("2018-05-25 06:42", DateAdapter.getDateTopStories("2018-05-25T06:42:57-04:00"));
    }


    @Test
    public void getDateMostpopularTest() {
        String format = "yyyy-MM-dd";
        @SuppressLint("SimpleDateFormat") java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
        java.util.Date date = new java.util.Date();
        String dayNewsDate = formater.format(date);
        assertEquals("today", DateAdapter.getDateMostPopular(dayNewsDate));
    }

    @Test
    public void castDateSearchTest() {
        new CastDateSearch(2018, 5, 25);
        assertEquals("20180525", CastDateSearch.getDateSearch());
    }
    @Test
    public void castDateTest() {
        new CastDateSearch(2018, 5, 25);
        assertEquals("25/05/2018", CastDateSearch.getDate());
    }
}