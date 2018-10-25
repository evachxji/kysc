package com.kysc.dao;

import com.kysc.bean.IdentifyCode;

/**
 * 验证码
 * 
 * @author yejuncheng
 * @date 2018/10/25 10:21
 */
public interface IdentifyCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IdentifyCode record);

    int insertSelective(IdentifyCode record);

    IdentifyCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IdentifyCode record);

    int updateByPrimaryKey(IdentifyCode record);

    int hasValidSms(String mobile);

    /**
     * 是否为合法的验证码
     * @param identifyCode
     * @return
     */
    int hasValidSmsCode(IdentifyCode identifyCode);
}