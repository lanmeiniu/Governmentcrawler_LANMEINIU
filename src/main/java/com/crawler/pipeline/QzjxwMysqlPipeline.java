package com.crawler.pipeline;


import com.crawler.dao.QzjxwDocumentMapper;;
import com.crawler.pojo.QzjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/19.
 */
public class QzjxwMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        SqlSession session = DBTools.getSession();
        QzjxwDocumentMapper qzjxwDocumnetMapper = session.getMapper(QzjxwDocumentMapper.class);

//        System.out.println(resultItems);
//        System.out.println(resultItems.getRequest().getUrl());
//        System.out.println((String) resultItems.get("Title"));
//        System.out.println((String) resultItems.get("Content"));
//        System.out.println((Date) resultItems.get("Date"));
//        System.out.println((String) resultItems.get("Editor"));
//        System.out.println((String) resultItems.get("Attachment"));


        System.out.println("get page: " + resultItems.getRequest().getUrl());
        QzjxwDocument qzjxwDocument = new QzjxwDocument();

        /* 将下列数据项导入结果中：
        title,url,attachmentUrl,date,content
        */

        qzjxwDocument.setUrl(resultItems.getRequest().getUrl());
        qzjxwDocument.setTitle((String) resultItems.get("Title"));
        qzjxwDocument.setContent((String) resultItems.get("Content"));
        qzjxwDocument.setDate((Date)resultItems.get("Date"));
        qzjxwDocument.setUrlAttachment((String) resultItems.get("Attachment"));


        System.out.println(qzjxwDocument);

        try {
            qzjxwDocumnetMapper.insertSelective(qzjxwDocument);
            session.commit();
        } catch (Exception e) {
            System.out.println("resultItems = [" + resultItems + "], task = [" + task + "]");
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }
}
