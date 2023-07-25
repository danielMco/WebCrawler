package com.webCrawler.WebCrawler;

import com.webCrawler.CrawlingMechanism.CrawlingMechanism;
import com.webCrawler.CrawlingMechanism.impl.DepthCrawlingMechanism;
import com.webCrawler.Models.CrawledPage;
import com.webCrawler.OutputWriter.OutputWriter;
import com.webCrawler.OutputWriter.impl.TsvFileWriter;
import com.webCrawler.PageFetcher.PageFetcher;
import com.webCrawler.PageFetcher.impl.DefaultPageFetcher;
import com.webCrawler.PageProcessor.PageProcessor;
import com.webCrawler.PageProcessor.impl.DefaultPageProcessor;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class WebCrawler {
    private final String rootUrl;
    private final int depthLimit; 

    public WebCrawler(String rootUrl, int depthLimit) {
        this.depthLimit = depthLimit;
        this.rootUrl = rootUrl;
    }

    public void crawl() {
        PageFetcher pageFetcher = new DefaultPageFetcher();
        PageProcessor pageProcessor = new DefaultPageProcessor();
        OutputWriter outputWriter = new TsvFileWriter();
        CrawlingMechanism crawlingMechanism = new DepthCrawlingMechanism(pageFetcher, pageProcessor);
        Set<CrawledPage> crawledPages = Collections.synchronizedSet(new LinkedHashSet<>());
        crawlingMechanism.crawlUrl(rootUrl, depthLimit, crawledPages);

        // Write the crawled data using the injected OutputWriter.
        outputWriter.writeOutput(crawledPages, "output.tsv");
    }
 }

