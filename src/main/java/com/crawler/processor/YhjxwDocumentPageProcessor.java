package com.crawler.processor;

import com.crawler.downloader.SeleniumDownloader;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by guotao on 2017/7/14.
 * ${PACKAGE_NAME}.
 * governmentcrawler
 */
public class YhjxwDocumentPageProcessor implements PageProcessor {

    private final String MAIN_PAGE_YUHANG = "http://yuhang.gov.cn";

    private Site site = Site.me().setDomain("yuhang.gov.cn");

    private static final String URL_DOMAIN = "http://www.fuyang.gov.cn/fy/jxj";
    private static final String URL_LIST = "http://yuhang\\.gov\\.cn\\.*";
    private static final String URL_POST_PAGE = "http://www\\.yuhang\\.gov\\.cn/zwgk/bumen/I003/.*/.*/.*\\.html";
//    private static final String URL_FILE_ATTACHMENT= ".*/u/cms/jxj/\\d+/.*";
    @Override

    public void process(Page page) {
        System.out.println("page.getUrl() = [" + page.getUrl() + "]");
        List<String> listLinks = page.getHtml().links().all();
        System.out.println(listLinks);

        Selectable temp = page.getHtml().xpath("/html/body/table[6]/tbody/tr/td[4]/table[1]/tbody/tr/td/table/tbody/tr[2]/td/iframe/@src");
        System.out.println("temp = [" + temp + "]");
        List<String> stringListListPage = new ArrayList<>();
//        System.out.println("page = [" + MAIN_PAGE_YUHANG + temp.toString() + "]");
//        stringListListPage.add(MAIN_PAGE_YUHANG + temp.toString());
//        page.addTargetRequests(stringListListPage);

//        List<String> listLinks = page.getHtml().links().regex(URL_LIST).all();
//        System.out.println(listLinks);
        if(page.getHtml().regex(URL_POST_PAGE).match()) {
            System.out.println("page.getUrl() = [" + page.getUrl() + "]");
        } else if (page.getHtml() == stringListListPage) {
            List<String> postLinksShowPostPage = page.getHtml().links().regex(URL_POST_PAGE).all();

            stringListListPage.add(MAIN_PAGE_YUHANG + temp.toString());

            System.out.println(postLinksShowPostPage);
            System.out.println(stringListListPage);

            page.addTargetRequests(stringListListPage);
            page.addTargetRequests(postLinksShowPostPage);

            page.setSkip(true);
        }


}

    @Override
    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        Spider.create(new YhjxwDocumentPageProcessor()).addUrl("http://yuhang.gov.cn/zwgk/bumen/I003")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new ConsolePipeline())
                .run();
    }
}
