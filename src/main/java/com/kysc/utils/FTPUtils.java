package com.kysc.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * Apache commons-net 试用一把，看看FTP客户端工具做的好用不
 *
 */
public class FTPUtils {

    /*public static void main(String[] args) {
        Upload();
    }*/

    /**
     * FTP上传单个文件测试
     * 1.连接服务
     * 2.确定你要上传的文件
     * 3.指定你在服务器端存放的位置
     */
    public static void Upload(File file) throws IOException{


        FTPClient ftpClient = new FTPClient();
        FileInputStream fis = null;

        try {
            ftpClient.connect("118.24.137.182");
            ftpClient.login("yechen", "yjc123!@#");

            fis = new FileInputStream(file);
            //设置上传目录
            ftpClient.changeWorkingDirectory("/touxiang");
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("GBK");
            //设置文件类型（二进制）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.storeFile(file.getName(), fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fis);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
        //程序结束时，删除临时文件
        deleteFile(file);
    }

    /**
     * FTP下载单个文件测试
     */
    public static void Download() {
        FTPClient ftpClient = new FTPClient();
        FileOutputStream fos = null;

        try {
            ftpClient.connect("192.168.1.111");
            ftpClient.login("admin", "javaf");

            String remoteFileName = "/lanjie/pic/girl.jpg";
            fos = new FileOutputStream("c:/down.jpg");

            ftpClient.setBufferSize(1024);
            //设置文件类型（二进制）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.retrieveFile(remoteFileName, fos);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fos);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
    }

    public static R MultipartFiletoFile(MultipartFile multipartFile){
        // 上传文件全名
        String fileName = multipartFile.getOriginalFilename();
        // 文件名后缀（文件类型）
        String prefix=fileName.substring(fileName.lastIndexOf("."));

        if(prefix.equals(".jpg") || prefix.equals(".jpeg") || prefix.equals(".png")){
            if(multipartFile.getSize()<2097152){        //文件大小小于2MB
                //生成uuid文件名
                String newFileName = UUID.randomUUID().toString();
                try {
                    final File file = File.createTempFile(newFileName, prefix);     //创建空白File文件
                    multipartFile.transferTo(file);                 //multipartfile转file
                    return R.ok().put("file",file);
                } catch (IOException e) {
                    e.printStackTrace();    //创建file文件失败
                }
            }else
                return R.error(ErrorMsg.ERROR_MSG8.getCode(),ErrorMsg.ERROR_MSG8.getMsg());

        }else
            return R.error(ErrorMsg.ERROR_MSG7.getCode(),ErrorMsg.ERROR_MSG7.getMsg());
        return R.error();
    }

    public static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
