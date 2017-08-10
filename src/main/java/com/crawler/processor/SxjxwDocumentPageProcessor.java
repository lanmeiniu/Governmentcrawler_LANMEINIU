package com.crawler.processor;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.SxjxwMysqlPipeline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;


/**
 * Created by guotao on 2017/7/14.
 * ${PACKAGE_NAME}.
 * governmentcrawler
 */
public class SxjxwDocumentPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("www.sxjxw.gov.cn");
    private static final String URL_LIST = "http://www\\.sxjxw\\.gov\\.cn/.*";
    private static final String URL_POST_PAGE = "http://www\\.sxjxw\\.gov\\.cn/art/\\d+/\\d+/.*\\.html";
    private static final String URL_ATTACHMENT = ".*/module/download/downfile\\.jsp\\?filename=.*";
    private static final String URL_ATTACHMENT_A =".*/attach/\\d+/.*";
    @Override

    public void process(Page page) {
        System.out.println("page.getUrl() = [" + page.getUrl() + "]");
//        List<String> listLinks = page.getHtml().links().all();
//        System.out.println(listLinks);

        if(page.getUrl().regex(URL_POST_PAGE).match()) {
            System.out.println("download page = [" + page.getUrl() + "]");
            String majorTitle = page.getHtml().xpath("/html/body/table/tbody/tr/td/table[1]/tbody/tr/td[3]/table/tbody/tr/td/table[2]/tbody/tr/td/table[1]/tbody/tr[1]/td/text()").toString();
            String Date = page.getHtml().xpath("/html/body/table/tbody/tr/td/table[1]/tbody/tr/td[3]/table/tbody/tr/td/table[2]/tbody/tr/td/table[1]/tbody/tr[2]/td/table/tbody/tr/td[1]/text()").toString();
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
                System.out.println("Catch an Error");
                page.putField("Date", null);
            }
            //内容过滤
            String Content = page.getHtml().xpath("/html/body/table/tbody/tr/td/table[1]/tbody/tr/td[3]/table/tbody/tr/td/table[2]/tbody/tr/td/table[1]/tbody/tr[3]/td").toString();
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
        //获取附件的正则表达式
            List<String> attachment = page.getHtml().links().regex(URL_ATTACHMENT).all();
            List<String> attachment_a = page.getHtml().links().regex(URL_ATTACHMENT_A).all();
            if(attachment.isEmpty()) {
                page.putField("Attachment", String.join(",", attachment_a));
                System.out.println("附件地址: " + attachment_a);
            } else {
                page.putField("Attachment", String.join(",",attachment));
                System.out.println("附件地址: " + attachment);
            }

            System.out.println("标题: "+ majorTitle);
            System.out.println("发布时间: " + str );
            System.out.println("正文内容: " + Content1);


            page.putField("Title", majorTitle);
            page.putField("Url", page.getUrl().toString());
            page.putField("Content", Content1);

        } else if (page.getUrl().regex(URL_LIST).match()){
            List<String> postLinksShowPostPage =
                    page.getHtml().links().regex(URL_POST_PAGE).all();
            List<String> listLinks = page.getHtml().links().regex(URL_LIST).all();

            System.out.println(postLinksShowPostPage);
            System.out.println(listLinks);

            page.addTargetRequests(postLinksShowPostPage);
            page.addTargetRequests(listLinks);
            page.setSkip(true);
            System.out.println("END");
        }

}

    @Override
    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        Spider.create(new SxjxwDocumentPageProcessor()).addUrl("http://www.sxjxw.gov.cn/index.html")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new SxjxwMysqlPipeline())
                .run();
    }
}
