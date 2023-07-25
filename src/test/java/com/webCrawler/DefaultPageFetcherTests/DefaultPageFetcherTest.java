package com.webCrawler.DefaultPageFetcherTests;

import com.webCrawler.PageFetcher.impl.DefaultPageFetcher;
import com.webCrawler.Models.PageContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;


public class DefaultPageFetcherTest {

    private DefaultPageFetcher pageFetcher;

    @Before
    public void setUp() throws Exception {
        pageFetcher = spy(new DefaultPageFetcher());
        Document mockedDocument = mock(Document.class);
        Element mockedElement = mock(Element.class);
        Elements mockedElements = mock(Elements.class);
        when(mockedDocument.html()).thenReturn("<html><body><a href='http://example.com/child'>Link</a></body></html>");
        when(mockedElement.attr("href")).thenReturn("http://example.com/child");
        when(mockedElements.parallelStream()).thenReturn(Stream.of(mockedElement));
        when(mockedDocument.select("a[href]")).thenReturn(mockedElements);
        mockStatic(Jsoup.class, withSettings().defaultAnswer(invocation -> mockedDocument));
        when(Jsoup.parse(any(URL.class),anyInt())).thenReturn(mockedDocument);
    }

    @Test
    public void testFetchPageContent() throws Exception {
        String url = "http://example.com";
        List<String> linkedUrls = new ArrayList<>();
        linkedUrls.add("http://example.com/child");
        PageContent expectedPageContent = new PageContent(url, "<html><body><a href='http://example.com/child'>Link</a></body></html>", linkedUrls);
        when(pageFetcher.isHtmlDocument(anyString())).thenReturn(true);
        PageContent actualPageContent = pageFetcher.fetchPageContent(url);
        assertEquals(expectedPageContent.getUrl(), actualPageContent.getUrl());
        assertEquals(expectedPageContent.getHtmlContent(), actualPageContent.getHtmlContent());
        assertEquals(expectedPageContent.getLinkedUrls().size(), actualPageContent.getLinkedUrls().size());
        for(int i =0; i < expectedPageContent.getLinkedUrls().size(); i++) {
            assertEquals(expectedPageContent.getLinkedUrls().get(i), actualPageContent.getLinkedUrls().get(i));
        }
    }
}
