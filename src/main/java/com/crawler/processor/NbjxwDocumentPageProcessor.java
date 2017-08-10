package com.crawler.processor;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.FyjxwMysqlPipeline;
import com.crawler.pipeline.NbjxwMysqlPipeline;
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
public class NbjxwDocumentPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("www.nbec.gov.cn");

    private static final String URL_LIST = ".*\\.gov\\.cn/.*";
    private static final String URL_POST_PAGE = ".*/art/\\d+/\\d+/\\d+/.*\\.html";
    private static final String URL_LIST_SKIP = "http://www\\.miit\\.gov\\.cn/.*";
    private static final String URL_ATTACHMENT_A = ".*/u/cms/www/\\d+/.*";
    private static final String URL_ATTACHMENT_B = ".*/Portal/eWebEditor/uploadfile/.*";
    private static final String URL_ATTACHMENT_C = ".*/module/download/downfile\\.jsp\\?classid=\\d+&filename=.*";


    @Override

    public void process(Page page) {
        System.out.println("page.getUrl() = [" + page.getUrl() + "]");
//        List<String> listLinks = page.getHtml().links().regex(URL_POST_PAGE).all();
//        System.out.println(listLinks);
        if(page.getUrl().regex(URL_LIST_SKIP).match()) {
            page.setSkip(true);
        }
        if(page.getUrl().regex(URL_POST_PAGE).match()) {
            System.out.println("download page = [" + page.getUrl() + "]");
            //1.标题过滤
            String majorTitle = page.getHtml().xpath("//*[@id=\"ivs_title\"]/h1/text()||/html/body/div[2]/div[1]/h1/text() ").toString();

            //过滤掉注释标签
            Pattern p=Pattern.compile("\\<!--(.+)--\\>");
            Matcher m_zhushi=p.matcher(majorTitle);
            majorTitle =m_zhushi.replaceAll("");
            //需要去除html标签
            String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
            Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            Matcher m_html=p_html.matcher(majorTitle);
            majorTitle=m_html.replaceAll(""); //过滤html标签
            System.out.println("新闻标题: " + majorTitle);
            //2.时间过滤
            String Date = page.getHtml().xpath("//*[@id=\"text\"]/div[1]/p[2]||//*[@id=\"text\"]/div[1]/p|| /html/body/div[2]/div[1]/p/text()[1]").toString();
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
                java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
                System.out.println("发布时间: " + date);
                page.putField("Date", date);
            } catch (ParseException e) {
                page.putField("Date", null);
            }
            //3.内容过滤
            String Content = page.getHtml().xpath("//*[@id=\"ivs_content\"] || //*[@id=\"text\"]/div[2] ||/html/body/div[2]/div[2]").toString();

            Matcher m_zs=p.matcher(Content);
            Content =m_zs.replaceAll("");

            //过滤html标签

            Pattern p_html1=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            Matcher m_html1=p_html1.matcher(Content);
            Content=m_html1.replaceAll(""); //过滤html标签
            //过滤&nbsp标签
            String regEx_nbsp="\\s*&nbsp;";//定义nbsp标签的正则表达式
            Pattern p_nbsp = Pattern.compile(regEx_nbsp,Pattern.CASE_INSENSITIVE);
            Matcher m_nbsp = p_nbsp.matcher(Content);
            Content=m_nbsp.replaceAll("");//过滤空格文本

            String Content1 = Content.trim();
            System.out.println("文章内容 : " + Content1);

            //4.附件过滤
            List<String> attachment_a = page.getHtml().links().regex(URL_ATTACHMENT_A).all();
            List<String> attachment_b = page.getHtml().links().regex(URL_ATTACHMENT_B).all();
            List<String> attachment_c = page.getHtml().links().regex(URL_ATTACHMENT_C).all();
            if(attachment_a.isEmpty()) {
                if(attachment_b.isEmpty()) {
                    System.out.println("新闻附件: " + attachment_c);
                    page.putField("Attachment", String.join(",", attachment_c));
                } else {
                    System.out.println("新闻附件: " + attachment_b);
                    page.putField("Attachment", String.join(",", attachment_b));

                }
            } else {
                System.out.println("新闻附件: " + attachment_a);
                page.putField("Attachment", String.join(",", attachment_a));
            }

            page.putField("Title", majorTitle);
            page.putField("Url", page.getUrl().toString());
            page.putField("Content", Content);




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
        Spider.create(new NbjxwDocumentPageProcessor()).addUrl("http://www.nbec.gov.cn/")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new NbjxwMysqlPipeline())
                .run();
    }
}
