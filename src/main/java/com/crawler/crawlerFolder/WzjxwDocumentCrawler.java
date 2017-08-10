package com.crawler.crawlerFolder;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.WzjxwMysqlPipeline;
import com.crawler.processor.WzjxwDocumentPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:19
 */
public class WzjxwDocumentCrawler {

    public static void crawl() {
        Spider.create(new WzjxwDocumentPageProcessor()).addUrl("http://wzjxw.wenzhou.gov.cn/")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new WzjxwMysqlPipeline())
                .run();
    }

    public static void main(String[] args) {
        WzjxwDocumentCrawler wzjxwDocumentCrawler = new WzjxwDocumentCrawler();
        WzjxwDocumentCrawler.crawl();
    }
}
