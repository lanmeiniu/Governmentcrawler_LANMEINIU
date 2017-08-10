package com.crawler.processor;

import com.crawler.downloader.SeleniumDownloader;

import com.crawler.pipeline.JhjxwMysqlPipeline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
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
public class JhjxwDocumentPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("www.jhjxw.gov.cn/jxw/");

    private static final String URL_DOMAIN = "http://www.fuyang.gov.cn/fy/jxj";
    private static final String URL_LIST = "http://www\\.jhjxw\\.gov\\.cn/jxw/.*";
    private static final String URL_POST_PAGE = "http://www.jhjxw.gov.cn/\\w+/\\w+/\\w+/\\d+\\.shtml";
    private static final String URL_FILE_ATTACHMENT= ".*/jxw/uploadfiles/.*";
    @Override

    public void process(Page page) {
        System.out.println("page.getUrl() = [" + page.getUrl() + "]");
//        List<String> listLinks = page.getHtml().links().all();
//        System.out.println(listLinks);
        if(page.getUrl().regex(URL_POST_PAGE).match()) {
            System.out.println("download page = [" + page.getUrl() + "]");
            //1.文章标题
            String majorTitle = page.getHtml().xpath("/html/body/table[3]/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table[2]/tbody/tr[1]/td/text()").toString();
            System.out.println("新闻标题: " + majorTitle);
            //2.发表时间
            String Date = page.getHtml().xpath("/html/body/table[3]/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table[2]/tbody/tr[4]/td/text()").toString();
//            System.out.println("发表时间: " + Date );
            //日期的正则表达式
            String regDate = "(\\d{4})-(\\d{1,2})-(\\d{1,2})";
            Pattern pat = Pattern.compile(regDate);
            Matcher mat = pat.matcher(Date);
            String dateStr = null;
            if(mat.find()){
                dateStr = mat.group(0);
            }
            String str =dateStr.toString();
//            System.out.println("发表时间: " + str );
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
                System.out.println("发表时间: " + date);
                page.putField("Date1", date);
            } catch (ParseException e) {
                System.out.println("Catch an Error");
                page.putField("Date1", null);
            }
            //3.文章内容
            String Content = page.getHtml().xpath("/html/body/table[3]/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table[2]/tbody/tr[6]/td").toString();
            //过滤注解
            Pattern p=Pattern.compile("\\<!--(.+)--\\>");
            Matcher m_zhushi=p.matcher(Content);
            Content =m_zhushi.replaceAll("");
            //过滤html标签
            String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
            Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            Matcher m_html=p_html.matcher(Content);
            Content=m_html.replaceAll(""); //过滤html标签
            //过滤&nbsp标签
            String regEx_nbsp="\\s*&nbsp;";//定义nbsp标签的正则表达式
            Pattern p_nbsp = Pattern.compile(regEx_nbsp,Pattern.CASE_INSENSITIVE);
            Matcher m_nbsp = p_nbsp.matcher(Content);
            Content=m_nbsp.replaceAll("");//过滤空格文本

            String Content1 = Content.trim();
            System.out.println("文章内容 : " + Content1);

            //4.过滤附件标签
            List<String> attachment = page.getHtml().links().regex(URL_FILE_ATTACHMENT).all();
            System.out.println("文章附件: " + attachment);
            page.putField("Attachment", String.join(",",attachment));

            //保存抽取结果
            page.putField("Title", majorTitle);
            page.putField("Url", page.getUrl().toString());
            page.putField("Content", Content1);



        } else if(page.getUrl().regex(URL_LIST).match()) {
            List<String> postLinksShowPostPage =
                    page.getHtml().links().regex(URL_POST_PAGE).all();
            List<String> listLinks = page.getHtml().links().regex(URL_LIST).all();

            System.out.println(postLinksShowPostPage);
            System.out.println(listLinks);
            System.out.println("");

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
        Spider.create(new JhjxwDocumentPageProcessor()).addUrl("http://www.jhjxw.gov.cn/jxw/")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new JhjxwMysqlPipeline())
                .thread(2)
                .run();
    }
}
