package com.crawler.pipeline;



import com.crawler.dao.JxjxwDocumentMapper;
import com.crawler.pojo.JxjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/19.
 */
public class JxjxwMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        System.out.println("=================================in");

        SqlSession session = DBTools.getSession();
        JxjxwDocumentMapper jxjxwDocumnetMapper = session.getMapper(JxjxwDocumentMapper.class);

        System.out.println("=================================in2");
        System.out.println("titleAndDateMap" + (HashMap<String, Date>) resultItems.get("titleAndDateMap"));
        System.out.println("get page: " + resultItems.getRequest().getUrl());

        JxjxwDocument jxjxwDocument = new JxjxwDocument();

        //先将目标页面中的url,attachmentUrl,content，存入数据库
        jxjxwDocument.setUrl(resultItems.getRequest().getUrl());
        jxjxwDocument.setDate((Date) resultItems.get("Date"));
        jxjxwDocument.setTitle((String) resultItems.get("Title"));
        jxjxwDocument.setContent((String) resultItems.get("Content"));
        jxjxwDocument.setUrlAttachment((String) resultItems.get("Attachment"));
        try {
            jxjxwDocumnetMapper.insertSelective(jxjxwDocument);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
            System.out.println("MySql_End");
        }
    }
}
