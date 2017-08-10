package com.crawler.service;

import com.crawler.dao.FyjxwDocumentMapper;
import com.crawler.pojo.FyjxwDocument;
import com.crawler.tools.DBTools;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;

public class FyjxwDocumentUserService {

    public static void main(String[] args) {
        insertUser();
    }


    /**
     * 新增用户
     */
    private static void insertUser() {
        SqlSession session = DBTools.getSession();
        FyjxwDocumentMapper fyjxwDocumentMapper = session.getMapper(FyjxwDocumentMapper.class);

        FyjxwDocument fyjxwDocumnet = new FyjxwDocument(1,
                "test",
                "http://test.html",
                new Date(),
                "test",
                "http://test.html",
                null,
                null,
                null);
        try {
            fyjxwDocumentMapper.insertSelective(fyjxwDocumnet);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
            System.out.println("TEST!!!!!!");
        }
    }

}
