package com.kysc.dao;

import com.kysc.bean.IdentifyCode;
import com.kysc.bean.IdentifyCodeExample;

import java.util.List;

public interface IdentifyCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IdentifyCode record);

    int insertSelective(IdentifyCode record);

    List<IdentifyCode> selectByExample(IdentifyCodeExample example);

    IdentifyCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IdentifyCode record);

    int updateByPrimaryKey(IdentifyCode record);

    int hasValidSms(String mobile);
}