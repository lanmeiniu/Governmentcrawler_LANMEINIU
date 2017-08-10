package com.crawler.processor;

import com.crawler.downloader.ChromeSeleniumDownloader;
import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.HzjxwMysqlPipeline;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by guotao on 2017/7/14.
 * ${PACKAGE_NAME}.
 * governmentcrawler
 */
public class HzjxwDocumentPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("www.hzjxw.gov.cn");

    private static final String URL_DOMAIN = "http://www.hzjxw.gov.cn/hz/web";
    private static final String URL_LIST = "http://www\\.hzjxw\\.gov\\.cn/hz/web/.*";
    private static final String URL_POST_SHOWINFO_FILE_PAGE = "http://www\\.hzjxw\\.gov\\.cn/hz/web/ShowInfo_File\\.asp\\?ID=\\d+&TypeID=\\d+&FileID=\\d+";
    private static final String URL_POST_SHOWINFO_PAGE = "http://www\\.hzjxw\\.gov\\.cn/hz/web/ShowInfo\\.asp\\?ID=\\d+&TypeID=\\d+&FileID=\\d+";


    @Override

    public void process(Page page) {
        System.out.println("hdhdhdhdhdhdhdhdhdhdhdhdhd.getUrl() = [" + page.getUrl() + "]");

        String title = page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[1]/td[2]/text()").toString();

        String Content = page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table/tbody/tr/td").toString();

        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(Content);
        Content=m_html.replaceAll(""); //过滤html标签

        String regEx_nbsp="\\s*&nbsp;";//定义nbsp标签的正则表达式
        Pattern p_nbsp = Pattern.compile(regEx_nbsp,Pattern.CASE_INSENSITIVE);
        Matcher m_nbsp = p_nbsp.matcher(Content);
        Content=m_nbsp.replaceAll("");//过滤空格文本

        String Content1 = Content.trim();
        System.out.println("新闻内容" + Content1);
        page.putField("Content", Content1);


        System.out.println("title = [" + title + "]");
        System.out.println("content = [" +  Content1 + "]");


    }

    @Override
    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        Spider.create(new HzjxwDocumentPageProcessor()).addUrl("http://www.hzjxw.gov.cn/hz/web/ShowInfo.asp?ID=47141&TypeID=1&FileID=100")
                .setDownloader(new ChromeSeleniumDownloader("D:\\temp\\chromedriver.exe"))
                .addPipeline(new ConsolePipeline())
                .run();
    }
}
