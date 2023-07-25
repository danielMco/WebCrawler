package com.webCrawler.PageProcessor;

import java.util.List;

public interface PageProcessor {
    double process(String pageUrl, List<String> linkedUrls);
}
