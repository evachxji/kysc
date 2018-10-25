package com.kysc.service.impl;

import com.kysc.bean.IdentifyCode;
import com.kysc.dao.IdentifyCodeMapper;
import com.kysc.service.IdentifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 验证码实现
 *
 * @author yejuncheng
 * @date 2018/10/25 9:44
 */
@Service("identifyCodeService")
@Transactional(rollbackFor = Exception.class)
public class IdentifyCodeServiceImpl implements IdentifyCodeService {

    @Autowired
    IdentifyCodeMapper identifyCodeMapper;

    @Override
    public int insert(IdentifyCode identifyCode){
        return identifyCodeMapper.insert(identifyCode);
    }

    @Override
    public int hasValidSmsCode(IdentifyCode identifyCode) {
        return identifyCodeMapper.hasValidSmsCode(identifyCode);
    }

    @Override
    public int hasValidSms(String mobile) {
        return identifyCodeMapper.hasValidSms(mobile);
    }
}
