package com.crawler.processor;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.QzjxwMysqlPipeline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
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
public class QzjxwDocumentPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("www.qzjxw.gov.cn");
    private static final String URL_LIST = "http://www\\.qzjxw\\.gov\\.cn/.*";
    private static final String URL_POST_PAGE = "http://www\\.qzjxw\\.gov\\.cn/infoview\\.aspx\\?.*=\\d+";
    private static final String URL_POST_PAGE_Captical = "http://www\\.qzjxw\\.gov\\.cn/InfoView\\.aspx\\?ID=\\d+";

    private static final String URL_ATTACHMENT = ".*/UpLoad/file/\\d+/.*";
    private static final String URL_ATTACHMENT_A =".*/attach/\\d+/.*";
    @Override

    public void process(Page page) {
        System.out.println("page.getUrl() = [" + page.getUrl() + "]");
//        List<String> listLinks = page.getHtml().links().all();
//        System.out.println(listLinks);
        if(page.getUrl().regex(URL_POST_PAGE).match()||page.getUrl().regex(URL_POST_PAGE_Captical).match()) {
            System.out.println("download page = [" + page.getUrl() + "]");
            //1.新闻标题
            String majorTitle = page.getHtml().xpath("/html/body/div[4]/div[1]/text()").toString();
            System.out.println("新闻标题: " + majorTitle);
            //2.发表时间
            String Date = page.getHtml().xpath("/html/body/div[4]/div[2]/text()").toString();
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
                System.out.println("发表时间: " +date);
                page.putField("Date", date);
            } catch (ParseException e) {
                page.putField("Date", null);
                System.out.println("page = [" + page + "]");
            }

            //3.新闻内容  /html/body/div[4]/div[3]
            String Content =  page.getHtml().xpath("/html/body/div[4]/div[3]").toString();
            //过滤掉注释标签
            Pattern p=Pattern.compile("\\<!--(.+)--\\>");
            Matcher m_zhushi=p.matcher(Content);
            Content =m_zhushi.replaceAll("");
            //过滤掉html标签
            String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
            String regEx_nbsp="\\s*&nbsp;";//定义nbsp标签的正则表达式
            Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            Matcher m_html=p_html.matcher(Content);
            Content=m_html.replaceAll(""); //过滤html标签

            Pattern p_nbsp = Pattern.compile(regEx_nbsp,Pattern.CASE_INSENSITIVE);
            Matcher m_nbsp = p_nbsp.matcher(Content);
            Content=m_nbsp.replaceAll("");//过滤空格文本

            String Content1 = Content.trim();
            System.out.println("新闻内容: " + Content1);
            page.putField("Content",Content1);

            //4.新闻附件
            List<String> attachment = page.getHtml().links().regex(URL_ATTACHMENT).all();
            System.out.println("新闻附件: " + attachment);

            page.putField("Title", majorTitle);
            page.putField("Url", page.getUrl().toString());
            page.putField("Attachment",String.join(",",attachment));

        } else if (page.getUrl().regex(URL_LIST).match()) {
            List<String> postLinksShowPostPage =
                    page.getHtml().links().regex(URL_POST_PAGE).all();

            List<String> postLinksShowPostPageCaptical =
                    page.getHtml().links().regex(URL_POST_PAGE_Captical).all();

            List<String> listLinks =
                    page.getHtml().links().regex(URL_LIST).all();
            System.out.println(postLinksShowPostPage);
            System.out.println(postLinksShowPostPageCaptical);
            System.out.println(listLinks);

            page.addTargetRequests(postLinksShowPostPage);
            page.addTargetRequests(postLinksShowPostPageCaptical);
            page.addTargetRequests(listLinks);
            page.setSkip(true);
        }




    }

    @Override
    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        Spider.create(new QzjxwDocumentPageProcessor()).addUrl("http://www.qzjxw.gov.cn/")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new QzjxwMysqlPipeline())
                .run();
    }
}
