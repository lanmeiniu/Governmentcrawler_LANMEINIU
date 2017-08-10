package com.crawler.processor;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.LajxwMysqlPipeline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by guotao on 2017/7/14.
 * ${PACKAGE_NAME}.
 * governmentcrawler
 */
public class LajxwDocumentPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("www.linan.gov.cn/jxj/index.html/");

    private static final String URL_LIST = "http://www\\.linan\\.gov\\.cn/jxj/.*";
    private static final String URL_POST_PAGE = "http://www\\.linan\\.gov\\.cn/jxj/.*/\\d+/\\w+\\.html";
    @Override

    public void process(Page page) {
        System.out.println("page.getUrl() = [" + page.getUrl() + "]");
//        List<String> listlinks = page.getHtml().links().regex(URL_LIST).all();
//        System.out.println(listlinks);
//        List<String> listLinks = page.getHtml().links().all();
//        System.out.println(listLinks);
        if(page.getUrl().regex(URL_POST_PAGE).match()) {
            System.out.println("download page = [" + page.getUrl() + "]");
            String majorTitle = page.getHtml().xpath("/html/body/div[4]/div[2]/div[1]/text()").toString();
            String Date = page.getHtml().xpath("/html/body/div[4]/div[2]/div[2]/text()").toString();
            //日期的正则表达式
            String regDate = "(\\d{4})-(\\d{1,2})-(\\d{1,2})";
            Pattern pat = Pattern.compile(regDate);
            Matcher mat = pat.matcher(Date);
            String dateStr = null;
            if(mat.find()){
                dateStr = mat.group(0);
            }
            String str =dateStr.toString();
            //将string类型转为Date类型
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
                System.out.println(date);
                page.putField("Date", date);
            } catch (ParseException e) {
                page.putField("Date", null);
                System.out.println("Catch an Error");
            }

            //内容过滤
            String Content = page.getHtml().xpath("/html/body/div[4]/div[2]/div[3]/div[1]/p[2]/text()|/html/body/div[4]/div[2]/div[3]/div[1]/p[3]/text()|/html/body/div[4]/div[2]/div[3]/text()").toString();
            //附件过滤
            String attachment = page.getHtml().xpath("/html/body/div[4]/div[2]/div/div[1]/a").toString();

            System.out.println("标题: "+majorTitle);
            System.out.println("发布时间: "+str);
            System.out.println("新闻内容： "+Content);
            System.out.println("附件: "+attachment);

            page.putField("Title", majorTitle);
            page.putField("Url", page.getUrl().toString());
            page.putField("Content", Content);
            page.putField("Attachment", attachment);



        } else if (page.getUrl().regex(URL_LIST).match()){
            List<String> postLinksShowPostPage =
                    page.getHtml().links().regex(URL_POST_PAGE).all();
            List<String> listLinks = page.getHtml().links().regex(URL_LIST).all();

            System.out.println(postLinksShowPostPage);
            System.out.println(listLinks);
            System.out.println("TEST");

            page.addTargetRequests(postLinksShowPostPage);
            page.addTargetRequests(listLinks);
            page.setSkip(true);
        }
}

    @Override
    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        Spider.create(new LajxwDocumentPageProcessor()).addUrl("http://www.linan.gov.cn/jxj/index.html")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new LajxwMysqlPipeline())
                .run();
    }
}
