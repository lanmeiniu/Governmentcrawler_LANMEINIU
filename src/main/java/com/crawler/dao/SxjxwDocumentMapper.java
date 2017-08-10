package com.crawler.dao;

import com.crawler.pojo.SxjxwDocument;

public interface SxjxwDocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SxjxwDocument record);

    int insertSelective(SxjxwDocument record);

    SxjxwDocument selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SxjxwDocument record);

    int updateByPrimaryKeyWithBLOBs(SxjxwDocument record);

    int updateByPrimaryKey(SxjxwDocument record);
}