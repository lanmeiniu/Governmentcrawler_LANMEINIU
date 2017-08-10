package com.crawler.dao;

import com.crawler.pojo.JhjxwDocument;

public interface JhjxwDocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JhjxwDocument record);

    int insertSelective(JhjxwDocument record);

    JhjxwDocument selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JhjxwDocument record);

    int updateByPrimaryKeyWithBLOBs(JhjxwDocument record);

    int updateByPrimaryKey(JhjxwDocument record);
}