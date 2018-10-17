package com.eric_b.mynews;

import com.eric_b.mynews.models.mostpopular.MostPopularPojo;
import com.eric_b.mynews.models.search.SearchPojo;
import com.eric_b.mynews.models.topstories.TopStoriePojo;
import com.eric_b.mynews.utils.TimesStream;
import org.junit.Test;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import static org.hamcrest.MatcherAssert.assertThat;

public class ReceivingNewsTest {

    @Test
    public void streamFetchTopStoriesTest() throws Exception {
        //1 - Get the stream
        Observable<TopStoriePojo> observableNews = TimesStream.streamFetchTopStorieNews("home");
        //2 - Create a new TestObserver
        TestObserver<TopStoriePojo> testObserver = new TestObserver<>();
        //3 - Launch observable
        observableNews.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        TopStoriePojo newsFetched = testObserver.values().get(0);
        assertThat("Top Storie receive news.",newsFetched.getNumResults() > 0);

    }

    @Test
    public void streamFetchMostPopularTest() throws Exception {
        //1 - Get the stream
        Observable<MostPopularPojo> observableNews = TimesStream.streamFetchMostPopularNews();
        //2 - Create a new TestObserver
        TestObserver<MostPopularPojo> testObserver = new TestObserver<>();
        //3 - Launch observable
        observableNews.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        MostPopularPojo newsFetched = testObserver.values().get(0);
        assertThat("Most Popular receive news.",newsFetched.getNumResults() > 0);

    }

    @Test
    public void streamFetchBusinessTest() throws Exception {
        //1 - Get the stream
        Observable<TopStoriePojo> observableNews = TimesStream.streamFetchTopStorieNews("business");
        //2 - Create a new TestObserver
        TestObserver<TopStoriePojo> testObserver = new TestObserver<>();
        //3 - Launch observable
        observableNews.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        TopStoriePojo newsFetched = testObserver.values().get(0);
        assertThat("Business receive news.",newsFetched.getNumResults() > 0);
    }

    @Test
    public void streamFetchSearchTest() throws Exception {
        //1 - Get the stream
        Observable<SearchPojo> observableNews = TimesStream.streamFetchSearchNews("newest","Art Politics Travel Business Sport Environement","US","20000101","20180927");
        //2 - Create a new TestObserver
        TestObserver<SearchPojo> testObserver = new TestObserver<>();
        //3 - Launch observable
        observableNews.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        SearchPojo newsFetched = testObserver.values().get(0);
        assertThat("Search receive news.",newsFetched.getResponse().getMeta().getHits() > 0);

    }
}
