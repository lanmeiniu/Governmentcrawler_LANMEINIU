package com.crawler.pipeline;


import com.crawler.dao.ScjxwDocumentMapper;
import com.crawler.pojo.ScjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/19.
 */
public class ScjxwMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        SqlSession session = DBTools.getSession();
        ScjxwDocumentMapper scjxwDocumnetMapper = session.getMapper(ScjxwDocumentMapper.class);

//        System.out.println(resultItems);
//        System.out.println(resultItems.getRequest().getUrl());
//        System.out.println((String) resultItems.get("Title"));
//        System.out.println((String) resultItems.get("Content"));
//        System.out.println((Date) resultItems.get("Date"));
//        System.out.println((String) resultItems.get("Editor"));
//        System.out.println((String) resultItems.get("Attachment"));


        System.out.println("get page: " + resultItems.getRequest().getUrl());
        ScjxwDocument scjxwDocument = new ScjxwDocument();

        /* 将下列数据项导入结果中：
        title,url,attachmentUrl,date,content
        */

        scjxwDocument.setUrl(resultItems.getRequest().getUrl());
        scjxwDocument.setTitle((String) resultItems.get("Title"));
        scjxwDocument.setContent((String) resultItems.get("Content"));
        scjxwDocument.setDate((Date)resultItems.get("Date"));
        scjxwDocument.setUrlAttachment((String) resultItems.get("Attachment"));

        System.out.println("+++++++++++++++++++++++");
        System.out.println(scjxwDocument);
        System.out.println("+++++++++++++++++++++++");
        try {
            scjxwDocumnetMapper.insertSelective(scjxwDocument);
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
