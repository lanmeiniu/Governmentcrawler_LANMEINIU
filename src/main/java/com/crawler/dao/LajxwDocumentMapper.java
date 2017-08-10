package com.crawler.dao;

import com.crawler.pojo.LajxwDocument;

public interface LajxwDocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LajxwDocument record);

    int insertSelective(LajxwDocument record);

    LajxwDocument selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LajxwDocument record);

    int updateByPrimaryKeyWithBLOBs(LajxwDocument record);

    int updateByPrimaryKey(LajxwDocument record);
}