package com.crawler.pipeline;


import com.crawler.dao.XsjxwDocumentMapper;
import com.crawler.pojo.XsjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/19.
 */
public class XsjxwMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        SqlSession session = DBTools.getSession();
        XsjxwDocumentMapper xsjxwDocumnetMapper = session.getMapper(XsjxwDocumentMapper.class);

//        System.out.println(resultItems);
//        System.out.println(resultItems.getRequest().getUrl());
//        System.out.println((String) resultItems.get("Title"));
//        System.out.println((String) resultItems.get("Content"));
//        System.out.println((Date) resultItems.get("Date"));
//        System.out.println((String) resultItems.get("Editor"));
//        System.out.println((String) resultItems.get("Attachment"));


        System.out.println("get page: " + resultItems.getRequest().getUrl());
       XsjxwDocument xsjxwDocument = new XsjxwDocument();

        /* 将下列数据项导入结果中：
        title,url,attachmentUrl,date,content
        */

        xsjxwDocument.setUrl(resultItems.getRequest().getUrl());
        xsjxwDocument.setTitle((String) resultItems.get("Title"));
        xsjxwDocument.setContent((String) resultItems.get("Content"));
        xsjxwDocument.setDate((Date)resultItems.get("Date"));
        xsjxwDocument.setUrlAttachment((String) resultItems.get("Attachment"));


        System.out.println(xsjxwDocument);

        try {
            xsjxwDocumnetMapper.insertSelective(xsjxwDocument);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
            System.out.println("TEST CLOSED");
        }
    }
}
