package com.kysc.service;

import com.kysc.bean.IdentifyCode;

/**
 * 验证码接口
 *
 * @author yejuncheng
 * @date 2018/10/25 9:44
 */
public interface IdentifyCodeService {

    int hasValidSms(String mobile);

    int insert(IdentifyCode identifyCode);

    /**
     * 是否为合法的验证码
     * @param identifyCode
     * @return
     */
    int hasValidSmsCode(IdentifyCode identifyCode);
}
