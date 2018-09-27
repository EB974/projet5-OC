package com.eric_b.mynews;

import com.eric_b.mynews.models.TopStoriePojo;
import com.eric_b.mynews.utils.TimesStream;
import org.junit.Test;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import static org.hamcrest.MatcherAssert.assertThat;

public class TopStoriesFragmentTest {

    @Test
    public void streamFetchNewsTest() throws Exception {
        //1 - Get the stream
        Observable<TopStoriePojo> observableNews = TimesStream.streamFetchNews("home");
        //2 - Create a new TestObserver
        TestObserver<TopStoriePojo> testObserver = new TestObserver<>();
        //3 - Launch observable
        observableNews.subscribeWith(testObserver)
                .assertNoErrors() // 3.1 - Check if no errors
                .assertNoTimeout() // 3.2 - Check if no Timeout
                .awaitTerminalEvent(); // 3.3 - Await the stream terminated before continue

        TopStoriePojo newsFetched = testObserver.values().get(0);
        assertThat("Top stories receive news.",newsFetched.getNumResults() > 0);

    }


}
