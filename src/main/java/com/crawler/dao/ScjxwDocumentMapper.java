package com.crawler.dao;

import com.crawler.pojo.ScjxwDocument;

public interface ScjxwDocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScjxwDocument record);

    int insertSelective(ScjxwDocument record);

    ScjxwDocument selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScjxwDocument record);

    int updateByPrimaryKeyWithBLOBs(ScjxwDocument record);

    int updateByPrimaryKey(ScjxwDocument record);
}