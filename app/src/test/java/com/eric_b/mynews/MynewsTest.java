package com.eric_b.mynews;

import com.eric_b.mynews.models.TopStoriePojo;
import com.eric_b.mynews.utils.TimesStream;
import com.eric_b.mynews.views.NewsAdapter;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static com.eric_b.mynews.views.NewsAdapter.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MynewsTest {
    @Test
    public void dateApaterTest() {
        assertEquals("2018-05-25 06:42", NewsAdapter.dateAdapter("2018-05-25T06:42:57-04:00"));
    }

}