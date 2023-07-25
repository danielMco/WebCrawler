package com.webCrawler.OutputWriter.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.webCrawler.Models.CrawledPage;
import com.webCrawler.OutputWriter.OutputWriter;

public class TsvFileWriter implements OutputWriter {
    private static final Logger LOGGER = Logger.getLogger(TsvFileWriter.class.getName());

    @Override
    public void writeOutput(Set<CrawledPage> crawledPages, String outputFileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            writer.write("url\tdepth\tratio\n");
            for (CrawledPage crawledPage : crawledPages) {
                writer.write(crawledPage.getUrl() + "\t" + crawledPage.getDepth() + "\t" + crawledPage.getSameDomainLinksRatio() + "\n");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing output to file: " + outputFileName, e);
        }
    }
}
