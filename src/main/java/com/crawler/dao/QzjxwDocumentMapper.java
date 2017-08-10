package com.crawler.dao;

import com.crawler.pojo.QzjxwDocument;

public interface QzjxwDocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QzjxwDocument record);

    int insertSelective(QzjxwDocument record);

    QzjxwDocument selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QzjxwDocument record);

    int updateByPrimaryKeyWithBLOBs(QzjxwDocument record);

    int updateByPrimaryKey(QzjxwDocument record);
}