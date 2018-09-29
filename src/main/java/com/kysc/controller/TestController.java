package com.kysc.controller;

import com.kysc.bean.R;
import com.kysc.bean.User;
import com.kysc.service.TestService;
import com.kysc.utils.FTPUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public R test(){
        return R.ok(testService.test());
    }



    @RequestMapping(value = "/t1",method = RequestMethod.POST)
    public R t1(@RequestBody User user){
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        return R.ok();
    }

}
