package com.crawler.crawlerFolder;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.SxjxwMysqlPipeline;
import com.crawler.processor.SxjxwDocumentPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:19
 */
public class SxjxwDocumentCrawler {

    public static void crawl() {
        Spider.create(new SxjxwDocumentPageProcessor()).addUrl("http://www.sxjxw.gov.cn/index.html")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new SxjxwMysqlPipeline())
                .run();
    }

    public static void main(String[] args) {
        SxjxwDocumentCrawler sxjxwDocumentCrawler = new SxjxwDocumentCrawler();
        SxjxwDocumentCrawler.crawl();
    }
}
