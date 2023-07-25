package com.webCrawler;

import com.webCrawler.WebCrawler.WebCrawler;

public class WebCrawlerApp {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java WebCrawlerApp <root-url> <depth-limit>");
            return;
        }

        String rootUrl = args[0];
        int depthLimit = Integer.parseInt(args[1]);

        if(depthLimit < 1) {
            System.out.println("Error: <depth-limit> must be greater then 1");
            return;
        }

        //http://info.cern.ch/ site for tests
        WebCrawler webCrawler = new WebCrawler(rootUrl, depthLimit);
        webCrawler.crawl();
    }
}