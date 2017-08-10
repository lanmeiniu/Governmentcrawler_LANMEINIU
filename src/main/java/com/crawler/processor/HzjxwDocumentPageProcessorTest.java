package com.crawler.processor;

import com.crawler.downloader.SeleniumDownloader;
import com.crawler.pipeline.HzjxwMysqlPipeline;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import javax.naming.CompositeName;
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
public class HzjxwDocumentPageProcessorTest implements PageProcessor {

    private Site site = Site.me().setDomain("www.hzjxw.gov.cn");

    private static final String URL_DOMAIN = "http://www.hzjxw.gov.cn/hz/web";
    private static final String URL_LIST = "http://www\\.hzjxw\\.gov\\.cn/hz/web/.*";
    private static final String URL_POST_SHOWINFO_FILE_PAGE = "http://www\\.hzjxw\\.gov\\.cn/hz/web/ShowInfo_File\\.asp\\?ID=\\d+&TypeID=\\d+&FileID=\\d+";
    private static final String URL_POST_SHOWINFO_PAGE = "http://www\\.hzjxw\\.gov\\.cn/hz/web/ShowInfo\\.asp\\?ID=\\d+&TypeId=\\d+&FileID=\\d+";

    private static final String URL_SHOWINFO_FILE_ATTACHMENT= ".*/hz/ew/UploadFile/.*";
    private static final String URL_ATTACHMENT = ".*/module/download/downfile\\.jsp\\?classid=\\d+&filename=.*";


    private static final String URL_SEARCH_SKIP = "http://www\\.hzjxw\\.gov\\.cn/hz/web/Search.asp\\?.*";


    @Override

    public void process(Page page) {
        System.out.println("page.getUrl() = [" + page.getUrl() + "]");

        if (page.getUrl().regex(URL_SEARCH_SKIP).match()) {
            page.setSkip(true);
            return;
        }

        if (page.getUrl().regex(URL_POST_SHOWINFO_FILE_PAGE).match()) {

            System.out.println("download page = [" + page.getUrl() + "]");
            String majorTitle = page.getHtml().xpath("//div[@class='STYLE101']/strong/text()").toString();
            if (StringUtils.isBlank(majorTitle)) {
                majorTitle = page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[5]/td/div/div[1]/div/text()||/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[1]/td[2]/text()||/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table/tbody/tr/td/text()").toString();
                System.out.println("新闻标题showinfo: " + majorTitle);
                if (StringUtils.isBlank(majorTitle)) {
                    page.setSkip(true);
                    return;
                }

                //新闻内容
                String Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table/tbody/tr/td").toString();
                if(StringUtils.isBlank(Content)) {
                    Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[5]/td/div/div[2]").toString();
                    if(StringUtils.isBlank(Content)) {
                        Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td").toString();
                        if(StringUtils.isBlank(Content)) {
                            Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table/tbody/tr/td/p[2]").toString();
                            if(StringUtils.isBlank(Content)) {
                                Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table").toString();
                                if(StringUtils.isBlank(Content)) {
                                    Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table/tbody").toString();
                                    if(StringUtils.isBlank(Content)) {
                                        Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table/tbody/tr/td/p[1]").toString();
                                        if(StringUtils.isBlank(Content)) {
                                            Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table/tbody/tr").toString();
                                            if (StringUtils.isBlank(Content)) {
                                                // 没找到文章标题
                                                page.setSkip(true);
                                                return;
                                            }
                                        }

                                    }
                                }


                            }
                        }
                    }



                }




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
                //附件
                List<String> attachmentUrlList = page.getHtml().links().regex(URL_SHOWINFO_FILE_ATTACHMENT).all();
                if(attachmentUrlList.isEmpty()) {
                    List<String> attachmentUrlList1 = page.getHtml().links().regex(URL_ATTACHMENT).all();
                    System.out.println("新闻附件: " + attachmentUrlList1);
                    page.putField("Attachment",String.join(",",attachmentUrlList1));

                } else {
                    System.out.println("新闻附件: " + attachmentUrlList);
                    page.putField("Attachment",String.join(",",attachmentUrlList));
                }

            }
            page.putField("Title", majorTitle);
            page.putField("url", page.getUrl().toString());

        } else if (page.getUrl().regex(URL_POST_SHOWINFO_PAGE).match()) {
            String majorTitle = page.getHtml().xpath("//td[@class='pp3']/text()").toString();
            if (StringUtils.isBlank(majorTitle)) {

                majorTitle = page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[5]/td/div/div[1]/div/text()|| /html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[1]/td[2]/text()||/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table/tbody/tr/td/text()").toString();
                System.out.println("新闻标题showPage:  " + majorTitle);
                if (StringUtils.isBlank(majorTitle)) {
                    page.setSkip(true);
                    return;
                }

            }
            //新闻内容
            String Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table/tbody/tr/td").toString();
            if(StringUtils.isBlank(Content)) {
                Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[5]/td/div/div[2]").toString();
                if(StringUtils.isBlank(Content)) {
                    Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td").toString();
                    if(StringUtils.isBlank(Content)) {
                        Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table/tbody/tr/td/p[2]").toString();
                        if(StringUtils.isBlank(Content)) {
                            Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table").toString();
                            if(StringUtils.isBlank(Content)) {
                                Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table/tbody").toString();
                                if(StringUtils.isBlank(Content)) {
                                    Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table/tbody/tr/td/p[1]").toString();
                                    if(StringUtils.isBlank(Content)) {
                                        Content =page.getHtml().xpath("/html/body/table/tbody/tr/td/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table/tbody/tr").toString();
                                        if (StringUtils.isBlank(Content)) {
                                            // 没找到文章标题
                                            page.setSkip(true);
                                            return;
                                        }
                                    }

                                }
                            }


                        }
                    }
                }



            }




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
            //附件
            List<String> attachmentUrlList = page.getHtml().links().regex(URL_SHOWINFO_FILE_ATTACHMENT).all();
            if(attachmentUrlList.isEmpty()) {
                List<String> attachmentUrlList1 = page.getHtml().links().regex(URL_ATTACHMENT).all();
                System.out.println("新闻附件: " + attachmentUrlList1);
                page.putField("Attachment",String.join(",",attachmentUrlList1));

            } else {
                System.out.println("新闻附件: " + attachmentUrlList);
                page.putField("Attachment",String.join(",",attachmentUrlList));
            }




            page.putField("Title", majorTitle);
            page.putField("url", page.getUrl().toString());

        } else if (page.getUrl().regex(URL_LIST).match()) {
            //如果跳转页面匹配到搜索类的网址，那么则跳过
            List<String> postLinksShowInfoFilePage = page.getHtml().links().regex(URL_POST_SHOWINFO_FILE_PAGE).all();
            List<String> postLinksShowInfoPage = page.getHtml().links().regex(URL_POST_SHOWINFO_PAGE).all();
            List<String> listLinks = page.getHtml().links().regex(URL_LIST).all();

            System.out.println("postLinksShowInfoFilePage = " +postLinksShowInfoFilePage);
            System.out.println("postLinksShowInfoPage = " +postLinksShowInfoPage);
            System.out.println("listLinks = " +listLinks);


            page.addTargetRequests(postLinksShowInfoFilePage);
            page.addTargetRequests(postLinksShowInfoPage);
            page.addTargetRequests(listLinks);
            //page.setSkip(true);


            List<Selectable> trList = page.getHtml().xpath("/html/body/table/tbody/tr/td/table[3]/tbody/tr/td/table[2]/tbody/tr/td[3]/table[3]/tbody/tr/td/table/tbody/tr").nodes();
            Map<String, Date> titleAndDateMap = new HashMap<>();

            try {
                for (Selectable trNode : trList) {
                    if (trNode.links().regex(".*ShowInfo\\.asp.*").match()) {
                        String majorTitle = trNode.xpath("//a/text()").toString().trim();

                        String Date = trNode.xpath("//td[3]/text()").toString();
                        Date date = new SimpleDateFormat("yyyy/MM/dd").parse(Date);
                        titleAndDateMap.put(majorTitle, date);
                    }
                }
                page.putField("titleAndDateMap", titleAndDateMap);
            } catch (ParseException e) {
                e.printStackTrace();
                page.putField("titleAndDateMap", null);
            }

        }

    }

    @Override
    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        Spider.create(new HzjxwDocumentPageProcessorTest()).addUrl("http://www.hzjxw.gov.cn/hz/web/index.asp")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new HzjxwMysqlPipeline())
                .thread(5)
                .run();
    }
}
