package com.kysc.utils.SMS;


import io.swagger.models.auth.In;

import java.util.Random;

/**
 * 生成6位数随机验证码
 * 
 * @author yejuncheng
 * @date 2018/9/25 15:55
 */
public class RandomUtils {

    public static Integer getParam(){
        Random random = new Random();
        String result="";
        for(int i=0;i<6;i++){
            result+=random.nextInt(10);
        }
        return Integer.valueOf(result);
    }

}
