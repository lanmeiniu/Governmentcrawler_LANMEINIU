package com.crawler.pipeline;

import com.crawler.dao.ZsjxwDocumentMapper;
import com.crawler.pojo.ZsjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/19.
 */
public class ZsjxwMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        SqlSession session = DBTools.getSession();
        ZsjxwDocumentMapper zsjxwDocumnetMapper = session.getMapper(ZsjxwDocumentMapper.class);

//        System.out.println(resultItems);
//        System.out.println(resultItems.getRequest().getUrl());
//        System.out.println((String) resultItems.get("Title"));
//        System.out.println((String) resultItems.get("Content"));
//        System.out.println((Date) resultItems.get("Date"));
//        System.out.println((String) resultItems.get("Editor"));
//        System.out.println((String) resultItems.get("Attachment"));


        System.out.println("get page: " + resultItems.getRequest().getUrl());
        ZsjxwDocument zsjxwDocument = new ZsjxwDocument();

        /* 将下列数据项导入结果中：
        title,url,attachmentUrl,date,content
        */

        zsjxwDocument.setUrl(resultItems.getRequest().getUrl());
        zsjxwDocument.setTitle((String) resultItems.get("Title"));
        zsjxwDocument.setContent((String) resultItems.get("Content"));
        zsjxwDocument.setDate((Date)resultItems.get("Date"));
        zsjxwDocument.setUrlAttachment((String) resultItems.get("Attachment"));


        System.out.println(zsjxwDocument);

        try {
            zsjxwDocumnetMapper.insertSelective(zsjxwDocument);
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
