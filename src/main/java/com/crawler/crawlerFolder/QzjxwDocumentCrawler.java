package com.crawler.crawlerFolder;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.QzjxwMysqlPipeline;
import com.crawler.processor.QzjxwDocumentPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:19
 */
public class QzjxwDocumentCrawler {

    public static void crawl() {
        Spider.create(new QzjxwDocumentPageProcessor()).addUrl("http://www.qzjxw.gov.cn/")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new QzjxwMysqlPipeline())
                .run();
    }

    public static void main(String[] args) {
        QzjxwDocumentCrawler qzjxwDocumentCrawler = new QzjxwDocumentCrawler();
        QzjxwDocumentCrawler.crawl();
    }
}
