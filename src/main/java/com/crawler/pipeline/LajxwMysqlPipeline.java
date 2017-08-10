package com.crawler.pipeline;

import com.crawler.dao.LajxwDocumentMapper;
import com.crawler.pojo.LajxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/19.
 */
public class LajxwMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        SqlSession session = DBTools.getSession();
        LajxwDocumentMapper lajxwDocumnetMapper = session.getMapper(LajxwDocumentMapper.class);

//        System.out.println(resultItems);
//        System.out.println(resultItems.getRequest().getUrl());
//        System.out.println((String) resultItems.get("Title"));
//        System.out.println((String) resultItems.get("Content"));
//        System.out.println((Date) resultItems.get("Date"));
//        System.out.println((String) resultItems.get("Attachment"));


        System.out.println("get page: " + resultItems.getRequest().getUrl());
        LajxwDocument lajxwDocument = new LajxwDocument();

        /* 将下列数据项导入结果中：
        title,url,attachmentUrl,date,content
        */

        lajxwDocument.setUrl(resultItems.getRequest().getUrl());
        lajxwDocument.setTitle((String) resultItems.get("Title"));
        lajxwDocument.setContent((String) resultItems.get("Content"));
        lajxwDocument.setDate((Date)resultItems.get("Date"));
        lajxwDocument.setUrlAttachment((String) resultItems.get("Attachment"));


        System.out.println(lajxwDocument);

        try {
            lajxwDocumnetMapper.insertSelective(lajxwDocument);
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
