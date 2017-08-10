package com.crawler.dao;

import com.crawler.pojo.XsjxwDocument;

public interface XsjxwDocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XsjxwDocument record);

    int insertSelective(XsjxwDocument record);

    XsjxwDocument selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XsjxwDocument record);

    int updateByPrimaryKeyWithBLOBs(XsjxwDocument record);

    int updateByPrimaryKey(XsjxwDocument record);
}