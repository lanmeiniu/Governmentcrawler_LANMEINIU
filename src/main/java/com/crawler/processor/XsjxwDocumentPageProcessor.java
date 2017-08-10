package com.crawler.processor;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.FyjxwMysqlPipeline;
import com.crawler.pipeline.XsjxwMysqlPipeline;
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
public class XsjxwDocumentPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("www.xsgy.gov.cn/");

    private static final String URL_LIST = "http://www\\.xsgy\\.gov\\.cn/.*";
    private static final String URL_POST_PAGE = "http://www.xsgy.gov.cn/html/\\d+/.*/\\d+\\.html";
    private static final String URL_FILE_ATTACHMENT="http://upgov\\.xsnet\\.cn/uploadfile/\\d+/\\d+/.*";
    @Override

    public void process(Page page) {
        System.out.println("page.getUrl() = [" + page.getUrl() + "]");
//        List<String> listLinks = page.getHtml().links().regex(URL_POST_PAGE).all();
//        System.out.println(listLinks);
//        List<String> attachmentUrlList = page.getHtml().links().regex(URL_FILE_ATTACHMENT).all();
        if(page.getUrl().regex(URL_POST_PAGE).match()) {
            System.out.println("download page = [" + page.getUrl() + "]");
            String majorTitle = page.getHtml().xpath("/html/body/div[2]/div[3]/h2/text()").toString();
            String Date = page.getHtml().xpath("/html/body/div[2]/div[3]/h6/text()").toString();
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
                System.out.println("catch an error");
            }

            String Content = page.getHtml().xpath("/html/body/div[2]/div[3]/div[1]").toString();
            String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
            String regEx_nbsp="\\s*&nbsp;";//定义nbsp标签的正则表达式

            Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            Matcher m_html=p_html.matcher(Content);
            Content=m_html.replaceAll(""); //过滤html标签

            Pattern p_nbsp = Pattern.compile(regEx_nbsp,Pattern.CASE_INSENSITIVE);
            Matcher m_nbsp = p_nbsp.matcher(Content);
            Content=m_nbsp.replaceAll("");//过滤空格文本

            String Content1 = Content.trim();

            List<String> attachmentUrlList = page.getHtml().links().regex(URL_FILE_ATTACHMENT).all();
            System.out.println("文章标题: " + majorTitle);
            System.out.println("发布时间: " + str);
            System.out.println("内容: " + Content1);
            System.out.println("附件: " + attachmentUrlList);

            page.putField("Title", majorTitle);
            page.putField("Url", page.getUrl().toString());
            page.putField("Content", Content1);
            page.putField("Attachment", String.join(",",attachmentUrlList));


        } else if (page.getUrl().regex(URL_LIST).match()){
            List<String> postLinksShowPostPage =
                    page.getHtml().links().regex(URL_POST_PAGE).all();
            List<String> listLinks =
                    page.getHtml().links().regex(URL_LIST).all();
            System.out.println(postLinksShowPostPage);
            System.out.println(listLinks);
            System.out.println("Enter");

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
        Spider.create(new XsjxwDocumentPageProcessor()).addUrl("http://www.xsgy.gov.cn/index.html")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new XsjxwMysqlPipeline())
                .run();
    }
}
