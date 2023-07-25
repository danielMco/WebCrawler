package com.webCrawler.OutputWriter;

import com.webCrawler.Models.CrawledPage;

import java.util.Set;

public interface OutputWriter{
    void writeOutput(Set<CrawledPage> crawledPages, String outputPath);
}