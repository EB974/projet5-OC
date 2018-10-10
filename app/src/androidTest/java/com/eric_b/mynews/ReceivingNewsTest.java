package com.eric_b.mynews;

import com.eric_b.mynews.models.MostPopularPojo;
import com.eric_b.mynews.models.TopStoriePojo;
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
}
