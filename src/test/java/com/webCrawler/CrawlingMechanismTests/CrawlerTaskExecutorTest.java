package com.webCrawler.CrawlingMechanismTests;

import com.webCrawler.CrawlingMechanism.impl.CrawlerTaskExecutor;
import com.webCrawler.Models.CrawledPage;
import com.webCrawler.Models.PageContent;
import com.webCrawler.PageFetcher.PageFetcher;
import com.webCrawler.PageProcessor.PageProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class CrawlerTaskExecutorTest {

    private CrawlerTaskExecutor crawlerTaskExecutor;
    private Set<String> visitedUrls;
    private Set<CrawledPage> crawledPages;

    @Before
    public void setUp() {
        visitedUrls = new HashSet<>();
        crawledPages = new HashSet<>();
        crawlerTaskExecutor = new CrawlerTaskExecutor();
    }

    @Test
    public void testStartCrawling() {
        PageFetcher pageFetcher = url -> new PageContent(url, "<html><body>Mock content</body></html>", new ArrayList<>());
        PageProcessor pageProcessor = (url, linkedUrls) -> 0.5;
        CrawledPage rootPage = new CrawledPage("https://example.com", 0, 0.0);
        int depthLimit = 2;

        crawlerTaskExecutor.startCrawling(rootPage, depthLimit, visitedUrls,pageFetcher, pageProcessor, crawledPages);

        assertEquals(1, crawledPages.size());
    }

    @Test
    public void testCrawlingWithChildNodes() {
        visitedUrls = new HashSet<>();
        crawledPages = new HashSet<>();

        PageFetcher pageFetcher = url -> {
            if (url.equals("https://example.com")) {
                return new PageContent(url, "<html><body><a href='https://example.com/child'>Link</a></body></html>", Collections.singletonList("http://example.com/child"));
            } else if (url.equals("https://example.com/child")) {
                return new PageContent(url, "<html><body>Child Page</body></html>", Collections.emptyList());
            }
            return null;
        };

        PageProcessor pageProcessor = (url, linkedUrls) -> {
            if (url.equals("https://example.com")) {
                return 0.2; 
            } else if (url.equals("https://example.com/child")) {
                return 0.5;
            }
            return 0.0;
        };

        CrawledPage rootPage = new CrawledPage("https://example.com", 0, 0.0);
        int depthLimit = 1;

        crawlerTaskExecutor.startCrawling(rootPage, depthLimit, visitedUrls,pageFetcher,pageProcessor, crawledPages);

        assertEquals(2, crawledPages.size());
        for (CrawledPage page : crawledPages) {
            if (page.getUrl().equals("https://example.com")) {
                assertEquals(0.2, page.getSameDomainLinksRatio(), 0.001);
            } else if (page.getUrl().equals("https://example.com/child")) {
                assertEquals(0.5, page.getSameDomainLinksRatio(), 0.001);
            }
        }
    }
}
