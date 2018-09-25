package com.kysc.utils;
/**
 * @author Tony
 * @date 2018-01-10
 * @project rest_demo
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import com.kysc.utils.SMS.client.AbsRestClient;
import com.kysc.utils.SMS.client.JsonReqClient;

public class SMSUtils {

    static AbsRestClient InstantiationRestAPI() {
        return new JsonReqClient();
    }

    public static void testSendSms(String mobile,String param){
        String sid = "f4e220382806e7d8fc7a5555570b914e";
        String token = "f87022733a39ae9f3b995ca15e80a041";
        String appid = "e82b1e396730416dbdbaa6873d85b940";
        String templateid = "378957";
        String uid = "";
        try {
            String result=InstantiationRestAPI().sendSms(sid, token, appid, templateid, param, mobile, uid);
            System.out.println("Response content is: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
