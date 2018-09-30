package com.kysc.service;

import com.kysc.bean.IdentifyCode;

public interface IdentifyCodeService {

    int hasValidSms(String mobile);

    int insert(IdentifyCode identifyCode);

    int hasValidSmsCode(IdentifyCode identifyCode);
}
