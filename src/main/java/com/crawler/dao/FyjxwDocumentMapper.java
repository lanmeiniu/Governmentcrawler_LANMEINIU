package com.crawler.dao;

import com.crawler.pojo.FyjxwDocument;

public interface FyjxwDocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FyjxwDocument record);

    int insertSelective(FyjxwDocument record);

    FyjxwDocument selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FyjxwDocument record);

    int updateByPrimaryKeyWithBLOBs(FyjxwDocument record);

    int updateByPrimaryKey(FyjxwDocument record);

}