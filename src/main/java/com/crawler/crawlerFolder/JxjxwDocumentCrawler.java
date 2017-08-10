package com.crawler.crawlerFolder;

import com.crawler.downloader.ChromeSeleniumDownloader;
import com.crawler.pipeline.JxjxwMysqlPipeline;
import com.crawler.processor.JxjxwDocumentPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:19
 */
public class JxjxwDocumentCrawler {

    public static void crawl() {
        Spider.create(new JxjxwDocumentPageProcessor()).addUrl("http://www.jxet.gov.cn/")
                .setDownloader(new ChromeSeleniumDownloader())
                .addPipeline(new JxjxwMysqlPipeline())
                .run();
    }

    public static void main(String[] args) {
        JxjxwDocumentCrawler jxjxwDocumentCrawler = new JxjxwDocumentCrawler();
        JxjxwDocumentCrawler.crawl();
    }
}
