package com.crawler.crawlerFolder;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.LsjxwMysqlPipeline;
import com.crawler.processor.LsjxwDocumentPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:19
 */
public class LsjxwDocumentCrawler {

    public static void crawl() {
        Spider.create(new LsjxwDocumentPageProcessor()).addUrl("http://jmw.lishui.gov.cn/")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new LsjxwMysqlPipeline())
                .run();
    }

    public static void main(String[] args) {
        LsjxwDocumentCrawler lsjxwDocumentCrawler = new LsjxwDocumentCrawler();
        LsjxwDocumentCrawler.crawl();
    }
}
