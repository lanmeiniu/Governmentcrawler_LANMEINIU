package com.crawler.processor;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.ScjxwMysqlPipeline;
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
public class ScjxwDocumentPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("www.hzsc.gov.cn");

    private static final String URL_LIST = "http://www\\.hzsc\\.gov\\.cn/.*";
    private static final String URL_POST_PAGE = "http://www\\.hzsc\\.gov\\.cn/art/\\d+/\\d+/\\d+/.*\\.html";
    private static final String URL_FILE_ATTACHMENT_A= ".*/module/download/downfile\\.jsp\\?classid=\\d+&filename=.*";
    private static final String URL_FILE_ATTACHMENT_B= ".*/Admin/Informational/FileUpload/.*";
    private static final String URL_FILE_ATTACHMENT_C= ".*/news/downxw/.*";
    private static final String URL_FILE_ATTACHMENT_D= ".*/picture/old/.*";
    @Override

    public void process(Page page) {
        System.out.println("page.getUrl() = [" + page.getUrl() + "]");
//        List<String> listLinks = page.getHtml().links().all();
//        System.out.println(listLinks);

//        List<String> listLinks =
//                page.getUrl().links().regex(URL_POST_PAGE).all();
//        System.out.println(listLinks);

        if(page.getUrl().regex(URL_POST_PAGE).match()) {
            System.out.println("download page = [" + page.getUrl() + "]");
            String majorTitle = page.getHtml().xpath("//*[@id=\"barrierfree_container\"]/div/div[2]/div[3]/div[2]/span[1]/text()|| //*[@id=\"barrierfree_container\"]/div/div[2]/div[2]/div[2]/div[2]/span[1]/text()||//*[@id=\"container\"]/div/div[2]/div[2]/div[1]").toString();

            String Date = page.getHtml().xpath("//*[@id=\"barrierfree_container\"]/div/div[2]/div[3]/div[2]/span[2] || //*[@id=\"barrierfree_container\"]/div/div[2]/div[2]/div[2]/div[2]/span[2]/text()[4]|| //*[@id=\"container\"]/div/div[2]/div[2]/div[2]").toString();
            //时间过滤掉注释
            Pattern p=Pattern.compile("\\<!--(.+)--\\>");
            Matcher m_zhushi=p.matcher(Date);
            Date =m_zhushi.replaceAll("");
            //将时间转换格式
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
                System.out.println("发表时间: " + date);
                page.putField("Date", date);
            } catch (ParseException e) {
                page.putField("Date", null);
            }


            String Content = page.getHtml().xpath("//*[@id=\"barrierfree_container\"]/div/div[2]/div[3]/div[2]/div[1]|| //*[@id=\"container\"]/div/div[2]/div/div[2]/div[2]/div[3] || //*[@id=\"container\"]/div/div[2]/div[2]/div[3]/div/div || //*[@id=\"container\"]/div/div[2]/div[2]/div[3]/div/img/text()").toString();
            //过滤掉注释标签
            Matcher m_content=p.matcher(Content);
            Content =m_content.replaceAll("");
            //定义HTML标签的正则表达式
            String regEx_html="<[^>]+>";
            Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            Matcher m_html=p_html.matcher(Content);
            Content=m_html.replaceAll(""); //过滤html标签

            //过滤掉&nbsp
            String regEx_nbsp="\\s*&nbsp;";//定义nbsp标签的正则表达式
            Pattern p_nbsp = Pattern.compile(regEx_nbsp,Pattern.CASE_INSENSITIVE);
            Matcher m_nbsp = p_nbsp.matcher(Content);
            Content=m_nbsp.replaceAll("");//过滤空格文本

            String Content1 = Content.trim();
            System.out.println("发表内容: " + Content1);
            List <String> attachment_a = page.getHtml().links().regex(URL_FILE_ATTACHMENT_A).all();
            List <String> attachment_b = page.getHtml().links().regex(URL_FILE_ATTACHMENT_B).all();
            List<String> attachment_c = page.getHtml().links().regex(URL_FILE_ATTACHMENT_C).all();
            List <String> attachment_d = page.getHtml().links().regex(URL_FILE_ATTACHMENT_D).all();
            if(attachment_a.isEmpty()) {
                if(attachment_b.isEmpty()) {
                    if(attachment_c.isEmpty()) {
                        page.putField("Attachment",
                                String.join(",",attachment_d));
                        System.out.println("新闻附件: " + attachment_d);

                    } else {
                        page.putField("Attachment",
                                String.join(",",attachment_c));
                        System.out.println("新闻附件: " + attachment_c);
                    }

                } else {
                    page.putField("Attachment",
                            String.join(",",attachment_b));
                    System.out.println("新闻附件: " + attachment_b);

                }

            } else {
                page.putField("Attachment",
                        String.join(",",attachment_a));
                System.out.println("新闻附件: " + attachment_a);

            }

            System.out.println("新闻标题: " + majorTitle);
            System.out.println("新闻内容: " + Content);

            page.putField("Title",majorTitle);
            page.putField("Content",Content1);

        } else if (page.getUrl().regex(URL_LIST).match()){

            System.out.println("");
            List<String> postLinksShowPostPage =
                    page.getHtml().links().regex(URL_POST_PAGE).all();
            List<String> listLinks =
                    page.getHtml().links().regex(URL_LIST).all();
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
        Spider.create(new ScjxwDocumentPageProcessor()).addUrl("http://www.hzsc.gov.cn/col/col1268166/index.html")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new ConsolePipeline())
                .run();
    }
}
