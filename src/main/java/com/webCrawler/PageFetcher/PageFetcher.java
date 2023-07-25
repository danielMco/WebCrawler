package com.webCrawler.PageFetcher;

import com.webCrawler.Models.PageContent;

public interface PageFetcher {
    PageContent fetchPageContent(String url) throws Exception;
}
