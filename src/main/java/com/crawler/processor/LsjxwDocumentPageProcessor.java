package com.crawler.processor;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.FyjxwMysqlPipeline;
import com.crawler.pipeline.LsjxwMysqlPipeline;
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
public class LsjxwDocumentPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("jmw.lishui.gov.cn");

    private static final String URL_DOMAIN = "http://jmw.lishui.gov.cn/";
    private static final String URL_LIST = "http://jmw\\.lishui\\.gov\\.cn/.*";
    private static final String URL_POST_PAGE = "http://jmw\\.lishui\\.gov\\.cn/.*/\\d+/.*\\.htm";
    private static final String URL_FILE_ATTACHMENT_DOC= "http://jmw\\.lishui\\.gov\\.cn/.*/\\d+/.*\\.doc";
    private static final String URL_FILE_ATTACHMENT_CEBX= "http://jmw\\.lishui\\.gov\\.cn/.*/\\d+/.*\\.cebx";
    private static final String URL_FILE_ATTACHMENT_XLS = "http://jmw\\.lishui\\.gov\\.cn/.*/.*/\\d+/.*\\.xls";

    @Override

    public void process(Page page) {
        //System.out.println("page.getUrl() = [" + page.getUrl() + "]");
//        List<String> listLinks = page.getHtml().links().all();
//        System.out.println(listLinks);

        if(page.getUrl().regex(URL_POST_PAGE).match()) {
            System.out.println("download page = [" + page.getUrl() + "]");
            String majorTitle = page.getHtml().xpath("/html/body/div/div/table[2]/tbody/tr/td/table[2]/tbody/tr[1]/td/table[1]/tbody/tr[1]/td/text()").toString();

            String Date = page.getHtml().xpath("/html/body/div/div/table[2]/tbody/tr/td/table[2]/tbody/tr[1]/td/table[1]/tbody/tr[2]/td/text()").toString();
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(Date);
                System.out.println("发布时间: " + date);
                page.putField("Date", date);
            } catch (ParseException e) {
                page.putField("Date", null);
            }

            String Content = page.getHtml().xpath("/html/body/div/div/table[2]/tbody/tr/td/table[2]/tbody/tr[1]/td/table[2]/tbody/tr[1]/td").toString();
            //过滤style标签
            String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>";
            Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
            Matcher m_style=p_style.matcher(Content);
            Content=m_style.replaceAll(""); //过滤style标签

            //过滤掉注释标签
            Pattern p=Pattern.compile("\\<!--(.+)--\\>");
            Matcher m_zhushi=p.matcher(Content);
            Content =m_zhushi.replaceAll("");
            //定义HTML标签的正则表达式
            String regEx_html="<[^>]+>";
            //定义nbsp标签的正则表达式
            String regEx_nbsp="\\s*&nbsp;";
            Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            Matcher m_html=p_html.matcher(Content);
            Content=m_html.replaceAll("");

            //过滤空格文本
            Pattern p_nbsp = Pattern.compile(regEx_nbsp,Pattern.CASE_INSENSITIVE);
            Matcher m_nbsp = p_nbsp.matcher(Content);
            Content=m_nbsp.replaceAll("");
            //过滤内嵌样式
            Pattern css = Pattern.compile("\\\"(.*?)\\\"");
            Matcher css_css = css.matcher(Content);
            Content = css_css.replaceAll("");
            String Content1 = Content.trim();
            //附件链接
            List <String> attachment_doc = page.getHtml().links().regex(URL_FILE_ATTACHMENT_DOC).all();
            List <String> attachment_cebx = page.getHtml().links().regex(URL_FILE_ATTACHMENT_CEBX).all();
            List<String> attachment_xls = page.getHtml().links().regex(URL_FILE_ATTACHMENT_XLS).all();

            if(attachment_doc.isEmpty()) {
                if(attachment_xls.isEmpty()) {
                    page.putField("Attachment", String.join(",",attachment_cebx));
                    System.out.println("新闻附件: " + attachment_cebx);

                } else {
                    page.putField("Attachment", String.join(",",attachment_xls));
                    System.out.println("新闻附件: " + attachment_xls);

                }

            } else {
                page.putField("Attachment", String.join(",",attachment_doc));
                System.out.println("新闻附件: " + attachment_doc);


            }

            System.out.println("新闻标题: " + majorTitle);
            System.out.println("新闻内容: " + Content1);
            System.out.println("=============END========================");

            page.putField("Title", majorTitle);
            page.putField("url", page.getUrl().toString());
            page.putField("Content", Content1);


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
        Spider.create(new LsjxwDocumentPageProcessor()).addUrl("http://jmw.lishui.gov.cn/")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new LsjxwMysqlPipeline())
                .thread(2)
                .run();
    }
}
