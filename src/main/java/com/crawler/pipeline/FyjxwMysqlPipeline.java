package com.crawler.pipeline;

import com.crawler.dao.FyjxwDocumentMapper;
import com.crawler.pojo.FyjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/19.
 */
public class FyjxwMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        SqlSession session = DBTools.getSession();
        FyjxwDocumentMapper fyjxwDocumnetMapper = session.getMapper(FyjxwDocumentMapper.class);

//        System.out.println(resultItems);
//        System.out.println(resultItems.getRequest().getUrl());
//        System.out.println((String) resultItems.get("Title"));
//        System.out.println((String) resultItems.get("Content"));
//        System.out.println((Date) resultItems.get("Date"));
//        System.out.println((String) resultItems.get("Editor"));
//        System.out.println((String) resultItems.get("Attachment"));


        System.out.println("get page: " + resultItems.getRequest().getUrl());
        FyjxwDocument fyjxwDocument = new FyjxwDocument();

        /* 将下列数据项导入结果中：
        title,url,attachmentUrl,date,content
        */

        fyjxwDocument.setUrl(resultItems.getRequest().getUrl());
        fyjxwDocument.setTitle((String) resultItems.get("Title"));
        fyjxwDocument.setContent((String) resultItems.get("Content"));
        fyjxwDocument.setDate((Date)resultItems.get("Date"));
        fyjxwDocument.setEditor((String) resultItems.get("Editor"));
        fyjxwDocument.setUrlAttachment((String) resultItems.get("Attachment"));


        System.out.println(fyjxwDocument);

        try {
            fyjxwDocumnetMapper.insertSelective(fyjxwDocument);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
            System.out.println("Data is stored in the database");
        }
    }
}
