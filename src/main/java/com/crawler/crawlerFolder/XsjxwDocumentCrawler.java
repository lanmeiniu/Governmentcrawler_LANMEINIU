package com.crawler.crawlerFolder;

import com.crawler.downloader.SeleniumDownloader;

import com.crawler.pipeline.XsjxwMysqlPipeline;
import com.crawler.processor.XsjxwDocumentPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:19
 */
public class XsjxwDocumentCrawler {

    public static void crawl() {
        Spider.create(new XsjxwDocumentPageProcessor()).addUrl("http://www.xsgy.gov.cn/index.html")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new XsjxwMysqlPipeline())
                .run();
    }

    public static void main(String[] args) {
        XsjxwDocumentCrawler xsjxwDocumentCrawler = new XsjxwDocumentCrawler();
        XsjxwDocumentCrawler.crawl();
    }
}
