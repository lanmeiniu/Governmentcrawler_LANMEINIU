package com.crawler.pipeline;

import com.crawler.dao.SxjxwDocumentMapper;
import com.crawler.pojo.SxjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/19.
 */
public class SxjxwMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        SqlSession session = DBTools.getSession();
       SxjxwDocumentMapper sxjxwDocumnetMapper = session.getMapper(SxjxwDocumentMapper.class);

//        System.out.println(resultItems);
//        System.out.println(resultItems.getRequest().getUrl());
//        System.out.println((String) resultItems.get("Title"));
//        System.out.println((String) resultItems.get("Content"));
//        System.out.println((Date) resultItems.get("Date"));
//        System.out.println((String) resultItems.get("Attachment"));


        System.out.println("get page: " + resultItems.getRequest().getUrl());
        SxjxwDocument fyjxwDocument = new SxjxwDocument();

        /* 将下列数据项导入结果中：
        title,url,attachmentUrl,date,content
        */

        fyjxwDocument.setUrl(resultItems.getRequest().getUrl());
        fyjxwDocument.setTitle((String) resultItems.get("Title"));
        fyjxwDocument.setContent((String) resultItems.get("Content"));
        fyjxwDocument.setDate((Date)resultItems.get("Date"));
        fyjxwDocument.setUrlAttachment((String) resultItems.get("Attachment"));


        System.out.println(fyjxwDocument);

        try {
            sxjxwDocumnetMapper.insertSelective(fyjxwDocument);
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
