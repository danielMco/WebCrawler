package com.webCrawler.DefaultPageProcessorTests;


import com.webCrawler.PageProcessor.impl.DefaultPageProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultPageProcessorTest {

    private DefaultPageProcessor pageProcessor;

    @Before
    public void setUp() {
        pageProcessor = new DefaultPageProcessor();
    }

    @Test
    public void testProcessWithSameDomainLinks() {
        String pageUrl = "http://example.com";
        List<String> linkedUrls = new ArrayList<>();
        linkedUrls.add("http://example.com/child1");
        linkedUrls.add("http://example.com/child2");
        linkedUrls.add("http://example.com/child3");

        double result = pageProcessor.process(pageUrl, linkedUrls);

        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testProcessWithDifferentDomainLinks() {
        String pageUrl = "https://example.com";
        List<String> linkedUrls = new ArrayList<>();
        linkedUrls.add("https://anotherdomain.com/link1");
        linkedUrls.add("https://yetanotherdomain.com/link2");

        double result = pageProcessor.process(pageUrl, linkedUrls);

        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testProcessWithEmptyLinkedUrls() {
        String pageUrl = "https://example.com";
        List<String> linkedUrls = new ArrayList<>();

        double result = pageProcessor.process(pageUrl, linkedUrls);

        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testProcessWithInvalidUrls() {
        String pageUrl = "https://example.com";
        List<String> linkedUrls = new ArrayList<>();
        linkedUrls.add("invalid-url");

        double result = pageProcessor.process(pageUrl, linkedUrls);

        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testProcessWithNullHost() {
        DefaultPageProcessor pageProcessorMock = mock(DefaultPageProcessor.class);
        when(pageProcessorMock.getHostFromUrl("https://example.com")).thenReturn(null);

        double result = pageProcessorMock.process("https://example.com", new ArrayList<>());

        assertEquals(0.0, result, 0.001);
    }
}
