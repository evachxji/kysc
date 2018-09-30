package com.kysc.service.impl;

import com.kysc.bean.IdentifyCode;
import com.kysc.dao.IdentifyCodeMapper;
import com.kysc.service.IdentifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("identifyCodeService")
public class IdentifyCodeServiceImpl implements IdentifyCodeService {

    @Autowired
    IdentifyCodeMapper identifyCodeMapper;

    public int insert(IdentifyCode identifyCode){
        return identifyCodeMapper.insert(identifyCode);
    }

    @Override
    public int hasValidSms(String mobile) {
        return identifyCodeMapper.hasValidSms(mobile);
    }
}
