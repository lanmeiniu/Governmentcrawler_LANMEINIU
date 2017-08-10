package com.crawler.dao;

import com.crawler.pojo.WzjxwDocument;

public interface WzjxwDocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WzjxwDocument record);

    int insertSelective(WzjxwDocument record);

    WzjxwDocument selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WzjxwDocument record);

    int updateByPrimaryKeyWithBLOBs(WzjxwDocument record);

    int updateByPrimaryKey(WzjxwDocument record);
}