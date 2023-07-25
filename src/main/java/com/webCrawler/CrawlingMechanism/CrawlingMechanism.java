package com.webCrawler.CrawlingMechanism;

import com.webCrawler.Models.CrawledPage;

import java.util.Set;

public interface CrawlingMechanism   {
    void crawlUrl(String url, int depth, Set<CrawledPage> crawledPages);
}
