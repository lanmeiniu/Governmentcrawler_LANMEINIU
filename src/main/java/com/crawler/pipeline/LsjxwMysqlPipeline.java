package com.crawler.pipeline;


import com.crawler.dao.LsjxwDocumentMapper;
import com.crawler.pojo.LsjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/19.
 */
public class LsjxwMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        SqlSession session = DBTools.getSession();
        LsjxwDocumentMapper lsjxwDocumnetMapper = session.getMapper(LsjxwDocumentMapper.class);

//        System.out.println(resultItems);
//        System.out.println(resultItems.getRequest().getUrl());
//        System.out.println((String) resultItems.get("Title"));
//        System.out.println((String) resultItems.get("Content"));
//        System.out.println((Date) resultItems.get("Date"));
//        System.out.println((String) resultItems.get("Editor"));
//        System.out.println((String) resultItems.get("Attachment"));


        System.out.println("get page: " + resultItems.getRequest().getUrl());
        LsjxwDocument lsjxwDocument = new LsjxwDocument();

        /* 将下列数据项导入结果中：
        title,url,attachmentUrl,date,content
        */

        lsjxwDocument.setUrl(resultItems.getRequest().getUrl());
        lsjxwDocument.setTitle((String) resultItems.get("Title"));
        lsjxwDocument.setContent((String) resultItems.get("Content"));
        lsjxwDocument.setDate((Date)resultItems.get("Date"));
        lsjxwDocument.setUrlAttachment((String) resultItems.get("Attachment"));


        System.out.println(lsjxwDocument);

        try {
            lsjxwDocumnetMapper.insertSelective(lsjxwDocument);
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
