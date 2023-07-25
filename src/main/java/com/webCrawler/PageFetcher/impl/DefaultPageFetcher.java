package com.webCrawler.PageFetcher.impl;

import com.webCrawler.PageFetcher.PageFetcher;
import com.webCrawler.Models.PageContent;

import java.net.HttpURLConnection;
import java.net.URI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class DefaultPageFetcher implements PageFetcher {
    private static final Logger LOGGER = Logger.getLogger(DefaultPageFetcher.class.getName());
    private final Map<String, PageContent> pageCache = new HashMap<>();

    @Override
    public PageContent fetchPageContent(String url) throws Exception {
        try {
            if (pageCache.containsKey(url)) {
                return pageCache.get(url);
            }
    
            Document document;
            document = Jsoup.parse(new URI(url).toURL(), 10000);
            List<String> linkedUrls = extractLinkedUrls(url, document);
            PageContent pageContent = new PageContent(url, document.html(), linkedUrls);
            pageCache.put(url, pageContent);
            return pageContent;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching page content for URL: " + url, e);
            throw e;
        }
    }

    private List<String> extractLinkedUrls(String baseUrl, Document document) {
        Elements anchorTags = document.select("a[href]");
        List<String> linkedUrls =  anchorTags.parallelStream()
                .filter(anc -> !anc.attr("href").isEmpty())
                .map(anc -> resolveUrl(baseUrl , anc.attr("href"))
                        .filter(this::isHtmlDocument)
                        .orElse("")).filter(tt -> !tt.isEmpty()).collect(Collectors.toList());
        return linkedUrls;
    }

    private Optional<String> resolveUrl(String baseUrl, String url) {
        try {
            URI baseUri = new URI(baseUrl);
            URI resolvedUri;
            if (url.startsWith("#") || !url.contains("://")) {
                resolvedUri = baseUri.resolve(url);
            } else {
                resolvedUri = new URI(url);
            }
            return Optional.of(resolvedUri.toString());
        } catch (Exception e) {
            // If got failed,continue with the next URL.
            LOGGER.log(Level.SEVERE, "Error resolving URL: " + url, e);
            return Optional.empty();
        }
    }
    
    public boolean isHtmlDocument(String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                return false;
            }
            HttpURLConnection connection = (HttpURLConnection) new URI(url).toURL().openConnection();
            connection.setConnectTimeout(1000);
            connection.setInstanceFollowRedirects(false);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String contentType = connection.getContentType();
                return contentType != null && contentType.startsWith("text/html");
            } else if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                String redirectUrl = connection.getHeaderField("Location");
                if (redirectUrl != null) {
                    isHtmlDocument(redirectUrl);
                }
            }
            return false;
        } catch (Exception e) {
            // can't validate, continue with the next URL.
            LOGGER.log(Level.SEVERE, "Error validate: " + url, e);
            return false;
        }
    }
}