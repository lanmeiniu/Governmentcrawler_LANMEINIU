package com.crawler.pipeline;

import com.crawler.dao.JhjxwDocumentMapper;
import com.crawler.pojo.JhjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/19.
 */
public class JhjxwMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        SqlSession session = DBTools.getSession();
        JhjxwDocumentMapper jhjxwDocumentMapper = session.getMapper(JhjxwDocumentMapper.class);

//        System.out.println(resultItems);
//        System.out.println(resultItems.getRequest().getUrl());
//        System.out.println((String) resultItems.get("Title"));
//        System.out.println((String) resultItems.get("Content"));
//        System.out.println((Date) resultItems.get("Date"));
//        System.out.println((String) resultItems.get("Attachment"));


        System.out.println("get page: " + resultItems.getRequest().getUrl());
        JhjxwDocument jhjxwDocument = new JhjxwDocument();

        /* 将下列数据项导入结果中：
        title,url,attachmentUrl,date,content
        */

        jhjxwDocument.setUrl(resultItems.getRequest().getUrl());
        jhjxwDocument.setTitle((String) resultItems.get("Title"));
        jhjxwDocument.setContent((String) resultItems.get("Content"));
        jhjxwDocument.setDate((Date)resultItems.get("Date1"));
        jhjxwDocument.setUrlAttachment((String) resultItems.get("Attachment"));


        System.out.println(jhjxwDocument);

        try {
            jhjxwDocumentMapper.insertSelective(jhjxwDocument);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            System.out.println("");
            session.close();

        }
    }
}
