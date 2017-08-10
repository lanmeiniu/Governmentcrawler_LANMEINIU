package com.crawler.pipeline;

import com.crawler.dao.ZjjxwDocumentMapper;
import com.crawler.pojo.ZjjxwDocumnet;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/19.
 */
public class TestMysqlPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        SqlSession session = DBTools.getSession();

        System.out.println("get page: " + resultItems.getRequest().getUrl());


        try {
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }
}
