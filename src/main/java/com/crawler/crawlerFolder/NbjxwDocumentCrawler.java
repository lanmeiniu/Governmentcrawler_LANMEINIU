package com.crawler.crawlerFolder;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.NbjxwMysqlPipeline;
import com.crawler.processor.NbjxwDocumentPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:19
 */
public class NbjxwDocumentCrawler {

    public static void crawl() {
        Spider.create(new NbjxwDocumentPageProcessor()).addUrl("http://www.nbec.gov.cn/")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new NbjxwMysqlPipeline())
                .run();
    }

    public static void main(String[] args) {
        NbjxwDocumentCrawler nbjxwDocumentCrawler = new NbjxwDocumentCrawler();
        NbjxwDocumentCrawler.crawl();
    }
}
