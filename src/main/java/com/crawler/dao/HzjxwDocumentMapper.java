package com.crawler.dao;

import com.crawler.pojo.HzjxwDocument;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HzjxwDocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HzjxwDocument record);

    int insertSelective(HzjxwDocument record);

    HzjxwDocument selectByPrimaryKey(Integer id);

    HzjxwDocument selectByTitle (String title);

    int updateByPrimaryKeySelective(HzjxwDocument record);

    int updateByPrimaryKeyWithBLOBs(HzjxwDocument record);

    int updateByPrimaryKey(HzjxwDocument record);

    int  checkTitle (String title ) ;

    int updateByTitle (Date date);



}