package com.crawler.pipeline;


import com.crawler.dao.HzjxwDocumentMapper;
import com.crawler.pojo.HzjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;
import java.lang.String;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/19.
 */
public class HzjxwMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        System.out.println("=================================in");

        SqlSession session = DBTools.getSession();
        HzjxwDocumentMapper hzjxwDocumnetMapper = session.getMapper(HzjxwDocumentMapper.class);

        System.out.println("=================================in2");
        System.out.println("titleAndDateMap" + (HashMap<String, Date>) resultItems.get("titleAndDateMap"));
        System.out.println("get page: " + resultItems.getRequest().getUrl());

        try {
            if (resultItems.get("Title") != null) {

                HzjxwDocument hzjxwDocument = new HzjxwDocument();

                //先将目标页面中的url,attachmentUrl,content，存入数据库
                hzjxwDocument.setUrl(resultItems.getRequest().getUrl());
                hzjxwDocument.setDate((Date) resultItems.get("Date"));
                hzjxwDocument.setTitle((String) resultItems.get("Title"));
                hzjxwDocument.setContent((String) resultItems.get("Content"));
                hzjxwDocument.setUrlAttachment((String) resultItems.get("Attachment"));

                System.out.println(hzjxwDocument);


                String Title = hzjxwDocument.getTitle();
                if (hzjxwDocumnetMapper.checkTitle(Title) == 0) {
                    //数据库中不存在相同标题,做插入操作,将获取的标题,内容，附件插入
                    System.out.println("大if 数据库中存在不相同标题，做插入操作");
                    hzjxwDocumnetMapper.insertSelective(hzjxwDocument);
                } else {
                    //数据库中存在相同标题，做更新操作
                    System.out.println("大if数据库中存在相同标题，做更新操作");
                    hzjxwDocumnetMapper.updateByPrimaryKeySelective(hzjxwDocument);
                }

                //hzjxwDocumnetMapper.insertSelective(hzjxwDocument);
            } else {

                //数据中的时间值可能是空，需要从跳转页面获取标题和时间进行更新操作,匹配原则是 查看标题是否相等
                //先新增一个map类型
                Map<String, Date> titleAndDateMap;

                //然后将PageProcess中的titleAndDateMap，赋值给新创建的map类型

                titleAndDateMap = resultItems.get("titleAndDateMap");
                System.out.println("*****从pageProcess中赋值给mysql中的titleAndDateMap*****" + titleAndDateMap);

                if (titleAndDateMap != null) {
                    System.out.println("*****开始titleAndDateMap工作*****");

                    HzjxwDocument hzjxwDocument = new HzjxwDocument();

                    //遍历map中的键
                    for (String keyMap : titleAndDateMap.keySet()) {
                        //从map中获得键，并将此赋值给时间
                        hzjxwDocument.setDate(titleAndDateMap.get(keyMap));
//                      //将获得的keyMap，设置成title
                        hzjxwDocument.setTitle(keyMap);

                        if (hzjxwDocumnetMapper.checkTitle(keyMap) == 0) {
                            //数据库中不存在相同标题,做插入操作,将获取的标题,内容，附件插入
                            System.out.println("大else 数据库中不存在相同标题,做插入操作");
                            hzjxwDocumnetMapper.insertSelective(hzjxwDocument);
                        } else {
                            //数据库中存在相同标题，做更新操作
                            System.out.println("大else数据库中存在相同标题，做更新操作");
                            hzjxwDocumnetMapper.updateByPrimaryKeySelective(hzjxwDocument);
                        }
                    }
                }
            }
            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
            System.out.println("MySql_End");
        }
    }
}
