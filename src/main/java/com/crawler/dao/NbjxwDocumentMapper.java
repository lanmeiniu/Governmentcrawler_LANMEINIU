package com.crawler.dao;

import com.crawler.pojo.NbjxwDocument;

public interface NbjxwDocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NbjxwDocument record);

    int insertSelective(NbjxwDocument record);

    NbjxwDocument selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NbjxwDocument record);

    int updateByPrimaryKeyWithBLOBs(NbjxwDocument record);

    int updateByPrimaryKey(NbjxwDocument record);
}