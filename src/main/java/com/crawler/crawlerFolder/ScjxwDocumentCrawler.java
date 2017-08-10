package com.crawler.crawlerFolder;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.FyjxwMysqlPipeline;
import com.crawler.processor.FyjxwDocumentPageProcessor;
import com.crawler.processor.ScjxwDocumentPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:19
 */
public class ScjxwDocumentCrawler {

    public static void crawl() {
        Spider.create(new ScjxwDocumentPageProcessor()).addUrl("http://www.hzsc.gov.cn/col/col1268166/index.html")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new FyjxwMysqlPipeline())
                .run();
    }

    public static void main(String[] args) {
        ScjxwDocumentCrawler scjxwDocumentCrawler = new ScjxwDocumentCrawler();
        ScjxwDocumentCrawler.crawl();
    }
}
