package com.crawler.crawlerFolder;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.FyjxwMysqlPipeline;
import com.crawler.processor.FyjxwDocumentPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:19
 */
public class FyjxwDocumentCrawler {

    public static void crawl() {
        //创建Spider，addUrl添加初始的URL
        Spider.create(new FyjxwDocumentPageProcessor()).addUrl("http://www.fuyang.gov.cn/fy/jxj")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new FyjxwMysqlPipeline())
                .run();
    }

    public static void main(String[] args) {
        FyjxwDocumentCrawler fyjxwDocumentCrawler = new FyjxwDocumentCrawler();
        FyjxwDocumentCrawler.crawl();
    }
}
