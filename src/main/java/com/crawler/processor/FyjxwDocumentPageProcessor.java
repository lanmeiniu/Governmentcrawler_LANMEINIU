package com.crawler.processor;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.FyjxwMysqlPipeline;

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
public class FyjxwDocumentPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("www.fuyang.gov.cn");

    private static final String URL_LIST = "http://www\\.fuyang\\.gov\\.cn/fy/jxj/.*";
    private static final String URL_POST_PAGE = "http://www\\.fuyang\\.gov\\.cn/.*/\\d+\\.jhtml";
    private static final String URL_FILE_ATTACHMENT= ".*/u/cms/jxj/\\d+/.*";
    @Override

    public void process(Page page) {
        System.out.println("page.getUrl() = [" + page.getUrl() + "]");
//        List<String> listLinks = page.getHtml().links().all();
//        System.out.println(listLinks);
        List<String> attachmentUrlList = page.getHtml().links().regex(URL_FILE_ATTACHMENT).all();

    if(page.getUrl().regex(URL_POST_PAGE).match()) {
        System.out.println("download page = [" + page.getUrl() + "]");
        String majorTitle = page.getHtml().xpath("/html/body/div[1]/table[3]/tbody/tr/td/div[3]/div/div/h3/text() || /html/body/div[2]/div[2]/div[1]/div/div[2]/div[1]/h1/text()").toString();
        String Editor = page.getHtml().xpath("/html/body/div[1]/table[3]/tbody/tr/td/div[3]/div/div/div[1]/span/text() || /html/body/div[2]/div[2]/div[1]/div/div[2]/div[1]/div/text()").toString();

        String Content = page.getHtml().xpath("//*[@id=\"MyContent\"] || /html/body/div[2]/div[2]/div[1]/div/div[2]/p[2]/text()").toString();

        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
        String regEx_nbsp="\\s*&nbsp;";//定义nbsp标签的正则表达式

        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(Content);
        Content=m_script.replaceAll(""); //过滤script标签

        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(Content);
        Content=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(Content);
        Content=m_html.replaceAll(""); //过滤html标签

        Pattern p_nbsp = Pattern.compile(regEx_nbsp,Pattern.CASE_INSENSITIVE);
        Matcher m_nbsp = p_nbsp.matcher(Content);
        Content=m_nbsp.replaceAll("");//过滤空格文本

        String Content1 = Content.trim();


        String Date = page.getHtml().xpath("/html/body/div[1]/table[3]/tbody/tr/td/div[3]/div/div/div[1]/text()").toString();
        //日期的正则表达式
        String regDate = "(\\d{4})-(\\d{1,2})-(\\d{1,2})";
        Pattern pat = Pattern.compile(regDate);
        Matcher mat = pat.matcher(Date);
        String dateStr = null;
        if(mat.find()){
            dateStr = mat.group(0);
        }
        String str =dateStr.toString();

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
            System.out.println(date);
            page.putField("Date", date);
        } catch (ParseException e) {
            page.putField("Date", null);
        }

        page.putField("Title", majorTitle);
        page.putField("Url", page.getUrl().toString());
        page.putField("Editor",Editor);
        page.putField("Content", Content1);
        page.putField("Attachment", String.join(",",attachmentUrlList));


    } else if (page.getUrl().regex(URL_LIST).match()){
        List<String> postLinksShowPostPage = page.getHtml().links().regex(URL_POST_PAGE).all();
        List<String> listLinks = page.getHtml().links().regex(URL_LIST).all();


        System.out.println(postLinksShowPostPage);
        System.out.println(listLinks);

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
        Spider.create(new FyjxwDocumentPageProcessor()).addUrl("http://www.fuyang.gov.cn/fy/jxj/index.jhtml")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new FyjxwMysqlPipeline())
                .run();
    }
}
