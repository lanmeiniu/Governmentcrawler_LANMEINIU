package com.crawler.service;

import com.crawler.dao.FyjxwDocumentMapper;
import com.crawler.dao.ScjxwDocumentMapper;
import com.crawler.pojo.FyjxwDocument;
import com.crawler.pojo.ScjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;

public class ScjxwDocumentUserService {

    public static void main(String[] args) {
        insertUser();
    }


    /**
     * 新增用户
     */
    private static void insertUser() {
        SqlSession session = DBTools.getSession();
        ScjxwDocumentMapper scjxwDocumentMapper = session.getMapper(ScjxwDocumentMapper.class);

            ScjxwDocument scjxwDocument = new ScjxwDocument(1,
                "test","http:",new Date(), "https://",null, null, null
                );

        try {
            scjxwDocumentMapper.insertSelective(scjxwDocument);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
            System.out.println("TEST!!!!!!");
        }
    }

}
