package com.crawler.crawlerFolder;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.ZsjxwMysqlPipeline;
import com.crawler.processor.ZsjxwDocumentPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:19
 */
public class ZsjxwDocumentCrawler {

    public static void crawl() {
        Spider.create(new ZsjxwDocumentPageProcessor()).addUrl("")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new ZsjxwMysqlPipeline())
                .run();
    }

    public static void main(String[] args) {
        ZsjxwDocumentCrawler zsjxwDocumentCrawler = new ZsjxwDocumentCrawler();
        ZsjxwDocumentCrawler.crawl();
    }
}
