package com.crawler.dao;

import com.crawler.pojo.ZsjxwDocument;

public interface ZsjxwDocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ZsjxwDocument record);

    int insertSelective(ZsjxwDocument record);

    ZsjxwDocument selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ZsjxwDocument record);

    int updateByPrimaryKeyWithBLOBs(ZsjxwDocument record);

    int updateByPrimaryKey(ZsjxwDocument record);
}