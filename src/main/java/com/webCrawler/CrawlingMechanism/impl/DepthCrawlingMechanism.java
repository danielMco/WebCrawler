package com.webCrawler.CrawlingMechanism.impl;

import com.webCrawler.CrawlingMechanism.CrawlingMechanism;
import com.webCrawler.Models.CrawledPage;
import com.webCrawler.PageFetcher.PageFetcher;
import com.webCrawler.PageProcessor.PageProcessor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DepthCrawlingMechanism implements CrawlingMechanism {

    private final CrawlerTaskExecutor crawlerTaskExecutor;
    private final PageFetcher pageFetcher;
    private final PageProcessor pageProcessor;


    public DepthCrawlingMechanism(PageFetcher pageFetcher, PageProcessor pageProcessor) {
        this.pageFetcher = pageFetcher;
        this.pageProcessor = pageProcessor;
        this.crawlerTaskExecutor = new CrawlerTaskExecutor();
    }

    @Override
    public void crawlUrl(String rootUrl, int depthLimit, Set<CrawledPage> crawledPages) {
        Set<String> visited = ConcurrentHashMap.newKeySet();
        crawlerTaskExecutor.startCrawling(new CrawledPage(rootUrl, 1,0.0), depthLimit, visited, pageFetcher, pageProcessor, crawledPages);
        System.out.println("Crawling finished");
    }


}
