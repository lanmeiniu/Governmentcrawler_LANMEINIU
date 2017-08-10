package com.crawler.processor;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.WzjxwMysqlPipeline;
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
public class WzjxwDocumentPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("wzjxw.wenzhou.gov.cn");

    private static final String URL_DOMAIN = "http://www.fuyang.gov.cn/fy/jxj";
    private static final String URL_LIST = "http://wzjxw\\.wenzhou\\.gov\\.cn/.*";
    private static final String URL_POST_PAGE = "http://wzjxw\\.wenzhou\\.gov\\.cn/art/\\d+/\\d+/\\d+/.*\\.html";
    private static final String URL_FILE_ATTACHMENT= ".*/module/download/downfile\\.jsp\\?classid=\\d+&filename=.*";
    @Override

    public void process(Page page) {
//        System.out.println("page.getUrl() = [" + page.getUrl() + "]");
//        List<String> listLinks = page.getHtml().links().all();
//        System.out.println(listLinks);

        if(page.getUrl().regex(URL_POST_PAGE).match()) {
            System.out.println("download page = [" + page.getUrl() + "]");
            String title = page.getHtml().xpath("//*[@id=\"c\"]/tbody/tr[1]/td").toString();

            //首先要过滤掉注释，其次在过滤掉html标签
            Pattern p=Pattern.compile("\\<!--(.+)--\\>");
            Matcher m_zhushi=p.matcher(title);
            title =m_zhushi.replaceAll("");

            String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
            Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            Matcher m_html=p_html.matcher(title);
            title=m_html.replaceAll(""); //过滤html标签

            //获取新闻时间
            String Date = page.getHtml().xpath("//*[@id=\"c\"]/tbody/tr[2]/td/table/tbody/tr/td[1]").toString();

            //去除"发布时间",首先定义时间的正则表达式
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
                System.out.println("发布时间: " + date);
                page.putField("Date", date);
            } catch (ParseException e) {
                page.putField("Date", null);
            }
            //新闻内容
            String Content = page.getHtml().xpath("//*[@id=\"zoom\"]").toString();
            //过滤新闻内容，先过滤掉注释再过滤掉html标签再过滤掉&nbsp标签
            Matcher c_zhushi=p.matcher(Content);
            Content = c_zhushi.replaceAll("");

            Matcher c_html=p_html.matcher(Content);
            Content=c_html.replaceAll(""); //过滤html标签

            String c_nbsp="\\s*&nbsp;";
            Pattern p_nbsp = Pattern.compile(c_nbsp,Pattern.CASE_INSENSITIVE);
            Matcher m_nbsp = p_nbsp.matcher(Content);
            Content=m_nbsp.replaceAll("");
            String Content1 = Content.trim();

            List<String> attachmentUrlList = page.getHtml().links().regex(URL_FILE_ATTACHMENT).all();



            System.out.println("新闻标题: " + title);
            System.out.println("新闻内容: " + Content);
            System.out.println("新闻附件: " +attachmentUrlList);
            System.out.println("================END======================");

            page.putField("Title", title);
            page.putField("url", page.getUrl().toString());
            page.putField("Content", Content1);
            page.putField("Attachment", String.join(",",attachmentUrlList));


        } else if (page.getUrl().regex(URL_LIST).match()) {

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
        Spider.create(new WzjxwDocumentPageProcessor()).addUrl("http://wzjxw.wenzhou.gov.cn/")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new WzjxwMysqlPipeline())
                .run();
    }
}
