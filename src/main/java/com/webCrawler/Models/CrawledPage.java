package com.webCrawler.Models;

public class CrawledPage {
    private final String url;
    private final int depth;
    private double sameDomainLinksRatio;

    public CrawledPage(String url, int depth, double sameDomainLinksRatio) {
        this.url = url;
        this.depth = depth;
        this.sameDomainLinksRatio = sameDomainLinksRatio;
    }

    public String getUrl() {
        return url;
    }

    public int getDepth() {
        return depth;
    }

    public double getSameDomainLinksRatio() {
        return sameDomainLinksRatio;
    }

    public void setSameDomainLinksRatio(double ratio) {
        this.sameDomainLinksRatio = ratio;
    }
}
