package com.crawler.crawlerFolder;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.FyjxwMysqlPipeline;
import com.crawler.processor.FyjxwDocumentPageProcessor;
import com.crawler.processor.HzjxwDocumentPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:19
 */
public class HzjxwDocumentCrawler {

    public static void crawl() {
        Spider.create(new HzjxwDocumentPageProcessor()).addUrl("http://www.hzjxw.gov.cn/hz/web/index.asp")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new FyjxwMysqlPipeline())
                .run();
    }

    public static void main(String[] args) {
        HzjxwDocumentCrawler hzjxwDocumentCrawler = new HzjxwDocumentCrawler();
        HzjxwDocumentCrawler.crawl();
    }
}
