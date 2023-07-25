package com.webCrawler.CrawlingMechanism.impl;

import com.webCrawler.Models.CrawledPage;
import com.webCrawler.Models.PageContent;
import com.webCrawler.PageFetcher.PageFetcher;
import com.webCrawler.PageFetcher.impl.DefaultPageFetcher;
import com.webCrawler.PageProcessor.PageProcessor;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrawlerTaskExecutor {

    private static final int NUM_THREADS = 100;
    private static final Logger LOGGER = Logger.getLogger(DefaultPageFetcher.class.getName());
    private ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

    public void startCrawling(CrawledPage crawledPage , int depthLimit , Set<String> visited, PageFetcher pageFetcher,
                              PageProcessor pageProcessor, Set<CrawledPage> crawledPages){
        CrawlerTask crawlerTask = new CrawlerTask(crawledPage, depthLimit,visited , pageFetcher, pageProcessor , crawledPages);
        Future<List<CrawledPage>> submitted = executorService.submit(crawlerTask);
        try {
            submitted.get();
            executorService.shutdown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private List<CrawledPage>  crawlUrl(CrawledPage crawledPage, int maxDepth, Set<String> visited, PageFetcher pageFetcher, PageProcessor pageProcessor, Set<CrawledPage> crawledPages){
        List<CrawledPage> result = new LinkedList<>();
        if(crawledPage.getDepth() <= maxDepth  && visited.add(crawledPage.getUrl())){
            crawledPages.add(crawledPage);
            try {
                PageContent pageContent = pageFetcher.fetchPageContent(crawledPage.getUrl());
                if(pageContent != null) {
                    List<String> linkedUrls = pageContent.getLinkedUrls();
                    crawledPage.setSameDomainLinksRatio(pageProcessor.process(crawledPage.getUrl(), linkedUrls));
                    List<CrawledPage> crawledPageList = buildCrawledPagesList(linkedUrls, crawledPage.getDepth() + 1, maxDepth, visited, pageFetcher, pageProcessor, crawledPages);
                    result.addAll(crawledPageList);
                }

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Exception while crawling url : " + crawledPage.getUrl(), e);
            }
        }
        return result;
    }

    private List<CrawledPage> buildCrawledPagesList(List<String> linkedList,int depth , int maxDepth, Set<String> visited, PageFetcher pageFetcher, PageProcessor pageProcessor, Set<CrawledPage> crawledPages){
        List<CrawledPage> crawledPagesList = new LinkedList<>();
        List<Future<List<CrawledPage>>> crTask = new LinkedList<>();
        for(String s : linkedList) {
            CrawledPage crawledPage1 = new CrawledPage(s, depth, 0.0);
            crawledPagesList.add(crawledPage1);
            CrawlerTask crawlerTask = new CrawlerTask(crawledPage1, maxDepth, visited, pageFetcher, pageProcessor, crawledPages);
            Future<List<CrawledPage>> listFuture = executorService.submit(crawlerTask);
            crTask.add(listFuture);
        }
        for (Future<List<CrawledPage>> future : crTask) {
            try {
                List<CrawledPage> crawledPageList = future.get();
                crawledPagesList.addAll(crawledPageList);
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.log(Level.SEVERE, "Exception while crawling", e);
                throw new RuntimeException(e);
            }
        }
        return crawledPagesList;
    }

    private  class CrawlerTask implements Callable<List<CrawledPage>> {
        private final CrawledPage crawledPage;
        private final int maxDepth;

        private final Set<String> visited;

        private final PageFetcher pageFetcher;
        private final PageProcessor pageProcessor;

        private final Set<CrawledPage> crawledPages;

        public CrawlerTask(CrawledPage crawledPage, int maxDepth, Set<String> visited, PageFetcher pageFetcher, PageProcessor pageProcessor, Set<CrawledPage> crawledPages) {
            this.crawledPage = crawledPage;
            this.maxDepth = maxDepth;
            this.visited = visited;
            this.pageFetcher = pageFetcher;
            this.pageProcessor = pageProcessor;
            this.crawledPages = crawledPages;
        }


        @Override
        public List<CrawledPage> call() throws Exception {
            List<CrawledPage> crawledPageList = crawlUrl(this.crawledPage, this.maxDepth, this.visited, this.pageFetcher, this.pageProcessor, this.crawledPages);
            return crawledPageList;
        }
    }
}

