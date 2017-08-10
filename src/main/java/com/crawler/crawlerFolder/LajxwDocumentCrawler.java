package com.crawler.crawlerFolder;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.LajxwMysqlPipeline;
import com.crawler.processor.FyjxwDocumentPageProcessor;
import com.crawler.processor.LajxwDocumentPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:19
 */
public class LajxwDocumentCrawler {

    public static void crawl() {
        Spider.create(new LajxwDocumentPageProcessor()).addUrl("http://www.linan.gov.cn/jxj/index.html")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new LajxwMysqlPipeline())
                .run();
    }

    public static void main(String[] args) {
        LajxwDocumentCrawler lajxwDocumentCrawler = new LajxwDocumentCrawler();
        LajxwDocumentCrawler.crawl();
    }
}
