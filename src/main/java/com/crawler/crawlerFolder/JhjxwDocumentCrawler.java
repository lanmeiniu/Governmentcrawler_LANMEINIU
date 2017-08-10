package com.crawler.crawlerFolder;

import com.crawler.downloader.SeleniumDownloader;

import com.crawler.pipeline.JhjxwMysqlPipeline;
import com.crawler.processor.JhjxwDocumentPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:19
 */
public class JhjxwDocumentCrawler {

    public static void crawl() {
        Spider.create(new JhjxwDocumentPageProcessor()).addUrl("http://www.jhjxw.gov.cn/jxw/")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new JhjxwMysqlPipeline())
                .run();
    }

    public static void main(String[] args) {
        JhjxwDocumentCrawler jhjxwDocumentCrawler = new JhjxwDocumentCrawler();
        JhjxwDocumentCrawler.crawl();
    }
}
