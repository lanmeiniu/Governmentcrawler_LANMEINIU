package com.crawler.dao;

import com.crawler.pojo.LsjxwDocument;

public interface LsjxwDocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LsjxwDocument record);

    int insertSelective(LsjxwDocument record);

    LsjxwDocument selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LsjxwDocument record);

    int updateByPrimaryKeyWithBLOBs(LsjxwDocument record);

    int updateByPrimaryKey(LsjxwDocument record);
}