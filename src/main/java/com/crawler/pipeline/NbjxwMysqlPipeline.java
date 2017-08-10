package com.crawler.pipeline;


import com.crawler.dao.NbjxwDocumentMapper;
import com.crawler.pojo.NbjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/19.
 */
public class NbjxwMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        SqlSession session = DBTools.getSession();
        NbjxwDocumentMapper nbjxwDocumnetMapper = session.getMapper(NbjxwDocumentMapper.class);

//        System.out.println(resultItems);
//        System.out.println(resultItems.getRequest().getUrl());
//        System.out.println((String) resultItems.get("Title"));
//        System.out.println((String) resultItems.get("Content"));
//        System.out.println((Date) resultItems.get("Date"));
//        System.out.println((String) resultItems.get("Editor"));
//        System.out.println((String) resultItems.get("Attachment"));


        System.out.println("get page: " + resultItems.getRequest().getUrl());
        NbjxwDocument nbjxwDocument = new NbjxwDocument();

        /* 将下列数据项导入结果中：
        title,url,attachmentUrl,date,content
        */

        nbjxwDocument.setUrl(resultItems.getRequest().getUrl());
        nbjxwDocument.setTitle((String) resultItems.get("Title"));
        nbjxwDocument.setContent((String) resultItems.get("Content"));
        nbjxwDocument.setDate((Date)resultItems.get("Date"));
        nbjxwDocument.setUrlAttachment((String) resultItems.get("Attachment"));


        System.out.println(nbjxwDocument);

        try {
            nbjxwDocumnetMapper.insertSelective(nbjxwDocument);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Catch an ERROR");
            session.rollback();
        } finally {
            session.close();
        }
    }
}
