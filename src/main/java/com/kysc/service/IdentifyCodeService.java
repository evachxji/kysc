package com.kysc.service;

import com.kysc.bean.IdentifyCode;

/**
 * 验证码接口
 *
 * @author yejuncheng
 * @date 2018/10/25 9:44
 */
public interface IdentifyCodeService {

    /**
     * 是否存在生效时间内的验证码
     * @param mobile
     * @return
     */
    int hasValidSms(String mobile);

    /**
     * 插入一条验证码记录
     * @param identifyCode
     * @return
     */
    int insert(IdentifyCode identifyCode);

    /**
     * 是否为正确有效的验证码
     * @param identifyCode
     * @return
     */
    int hasValidSmsCode(IdentifyCode identifyCode);
}
