package com.webCrawler.Models;

import java.util.List;

public class PageContent {
    private final String url;
    private final String htmlContent;
    private final List<String> linkedUrls;

    public PageContent(String url, String htmlContent, List<String> linkedUrls) {
        this.url = url;
        this.htmlContent = htmlContent;
        this.linkedUrls = linkedUrls;
    }

    public String getUrl() {
        return url;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public List<String> getLinkedUrls() {
        return linkedUrls;
    }
}
