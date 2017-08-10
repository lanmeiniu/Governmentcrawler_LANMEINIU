package com.crawler.pipeline;

import com.crawler.dao.WzjxwDocumentMapper;
import com.crawler.pojo.WzjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/19.
 */
public class WzjxwMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        SqlSession session = DBTools.getSession();
        WzjxwDocumentMapper wzjxwDocumnetMapper = session.getMapper(WzjxwDocumentMapper.class);

//        System.out.println(resultItems);
//        System.out.println(resultItems.getRequest().getUrl());
//        System.out.println((String) resultItems.get("Title"));
//        System.out.println((String) resultItems.get("Content"));
//        System.out.println((Date) resultItems.get("Date"));
//        System.out.println((String) resultItems.get("Editor"));
//        System.out.println((String) resultItems.get("Attachment"));


        System.out.println("get page: " + resultItems.getRequest().getUrl());
        WzjxwDocument wzjxwDocument = new WzjxwDocument();

        /* 将下列数据项导入结果中：
        title,url,attachmentUrl,date,content
        */

        wzjxwDocument.setUrl(resultItems.getRequest().getUrl());
        wzjxwDocument.setTitle((String) resultItems.get("Title"));
        wzjxwDocument.setContent((String) resultItems.get("Content"));
        wzjxwDocument.setDate((Date)resultItems.get("Date"));
        wzjxwDocument.setUrlAttachment((String) resultItems.get("Attachment"));


        System.out.println(wzjxwDocument);

        try {
            wzjxwDocumnetMapper.insertSelective(wzjxwDocument);
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
