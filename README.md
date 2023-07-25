# Web Crawler

## Design

#### the Web crawler work in a multi-threading technique to complete required task.
#### we start with root url (as the "Father" of all the child nodes) and then every child submitted into executor and crawl child page.
#### we keep continue as long as our currentDepth <= makDepth.
#### in every iteration we check if the page already visited (to avoid infinite running),
#### and using caching for store page content in case we have to download the same page multiple times.


## Main component

#### PageProcessor - interface which handle the calculation and metrics collection of a webPage (implement as DefaultPageProcessor)
#### pageFetcher - interface which handle web download and page content fetching (implement as DefaultPageFetcher).
#### CrawlingMechanism - interface which handle to declare and manage the crawling method ( in our case I implement DepthCrawlingMachismo)
#### CrawlerTaskExecutor - handle the crawling running (use crawlTask as Callable threads)


## Start the app
#### using maven for mange dependencies and packaging.
#### for running:
####  1. unzip the files (get in into the new folder if it was created)
####  2. mvn clean compile assembly:single
####  3. java -jar target/WebCrawlerApp-1.0-SNAPSHOT-jar-with-dependencies.jar <root-url> <depth>
####  4. wait until "Crawling finished" prompt (exception log to standard Output)