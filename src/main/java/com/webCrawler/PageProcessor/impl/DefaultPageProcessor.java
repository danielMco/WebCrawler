package com.webCrawler.PageProcessor.impl;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.webCrawler.PageFetcher.impl.DefaultPageFetcher;
import com.webCrawler.PageProcessor.PageProcessor;

public class DefaultPageProcessor implements PageProcessor {
    private static final Logger LOGGER = Logger.getLogger(DefaultPageFetcher.class.getName());

    @Override
    public double process(String pageUrl, List<String> linkedUrls) {
        Optional<String> currentHostOptional = getHostFromUrl(pageUrl);
        if(currentHostOptional.isEmpty()) {
            return 0.0;
        }
        String currentHost = currentHostOptional.get();

        int sameDomainLinksCount = 0;
        for (String linkedUrl : linkedUrls) {
            Optional<String> linkedHost = getHostFromUrl(linkedUrl);
            if (linkedHost.isPresent() && currentHost.equals(linkedHost.get())) {
                sameDomainLinksCount++;
            }
        }

        // Avoid division by zero error when the linkedUrls list is empty
        if (linkedUrls.isEmpty()) {
            return 0.0;
        }

        return (double) sameDomainLinksCount / linkedUrls.size();
    }

    public Optional<String> getHostFromUrl(String url) {
        try {
            URI uri = new URI(url);
            return Optional.ofNullable(uri.getHost());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting host from: " + url, e);
            return Optional.empty();
        }
    }
}

