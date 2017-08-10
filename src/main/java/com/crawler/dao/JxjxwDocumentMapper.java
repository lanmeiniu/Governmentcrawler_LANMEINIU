package com.crawler.dao;

import com.crawler.pojo.JxjxwDocument;

public interface JxjxwDocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JxjxwDocument record);

    int insertSelective(JxjxwDocument record);

    JxjxwDocument selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JxjxwDocument record);

    int updateByPrimaryKeyWithBLOBs(JxjxwDocument record);

    int updateByPrimaryKey(JxjxwDocument record);
}