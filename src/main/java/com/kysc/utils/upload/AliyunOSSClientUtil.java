package com.kysc.utils.upload;

import java.io.ByteArrayInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import com.kysc.utils.R;
import org.apache.log4j.Logger;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @class:AliyunOSSClientUtil
 * @descript:java使用阿里云OSS存储对象上传图片
 * @date:2017年3月16日 下午5:58:08
 * @author wangqi
 */
public class AliyunOSSClientUtil {

    /**
     * log日志
     */
    private static Logger logger = Logger.getLogger(AliyunOSSClientUtil.class);

    /**
     * 阿里云API的内或外网域名
      */
    private static String ENDPOINT;

    /**
     * 阿里云API的密钥Access Key ID
     */
    private static String ACCESS_KEY_ID;

    /**
     * 阿里云API的密钥Access Key Secret
     */
    private static String ACCESS_KEY_SECRET;

    /**
     * 阿里云API的bucket名称
     */
    private static String BACKET_NAME;

    /**
     * 阿里云API的文件夹名称
     */
    private static String FOLDER;

    //初始化属性
    static{
        ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";
        ACCESS_KEY_ID = "LTAI5VnNKBZxRoWJ";
        ACCESS_KEY_SECRET = "TfzlKqu9a7nBI4gTjQdU1amUyK8f6t";
        BACKET_NAME = "kysc";
        FOLDER = "tx/";
    }

    /**
     * 获取阿里云OSS客户端对象
     * @return ossClient
     */
    public static  OSSClient getOSSClient(){
        return new OSSClient(ENDPOINT,ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * TODO 创建存储空间
     * @param ossClient      OSS连接
     * @param bucketName 存储空间
     * @return
     */
    public  static String createBucketName(OSSClient ossClient,String bucketName){
        //存储空间
        final String bucketNames=bucketName;
        if(!ossClient.doesBucketExist(bucketName)){
            //创建存储空间
            Bucket bucket=ossClient.createBucket(bucketName);
            logger.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketNames;
    }

    /**
     * TODO 删除存储空间buckName
     * @param ossClient  oss对象
     * @param bucketName  存储空间
     */
    public static  void deleteBucket(OSSClient ossClient, String bucketName){
        ossClient.deleteBucket(bucketName);
        logger.info("删除" + bucketName + "Bucket成功");
    }

    /**
     * TODO 创建模拟文件夹
     * @param ossClient oss连接
     * @param bucketName 存储空间
     * @param folder   模拟文件夹名如"qj_nanjing/"
     * @return  文件夹名
     */
    public  static String createFolder(OSSClient ossClient,String bucketName,String folder){
        //文件夹名
        final String keySuffixWithSlash =folder;
        //判断文件夹是否存在，不存在则创建
        if(!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)){
            //创建文件夹
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            logger.info("创建文件夹成功");
            //得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir=object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    /**
     * TODO 根据key删除OSS服务器上的文件
     * @param folder  文件夹名
     * @param key 文件名
     */
    public static void deleteFile(String folder, String key){
        folder += "/";
        String fileName =key.substring(key.lastIndexOf("/")+1);
        OSSClient ossClient=AliyunOSSClientUtil.getOSSClient();
        ossClient.deleteObject("kysc", folder + fileName);
        logger.info("删除" + folder + "下的文件" + fileName + "成功");
    }

    /**
     * TODO 上传文件
     * @param multipartFile
     * @param folder    文件夹名称
     * @return
     */
    public static String upload(MultipartFile multipartFile,String folder) {
        String bucketName = BACKET_NAME;
        folder += "/";
        //初始化OSSClient
        OSSClient ossClient=AliyunOSSClientUtil.getOSSClient();
        try {
            //以输入流的形式上传文件
            InputStream is = multipartFile.getInputStream();
            String path = multipartFile.getOriginalFilename();
            //原始文件名
            String fileName =path.substring(path.lastIndexOf("\\")+1);
            String type=path.substring(path.lastIndexOf(".")+1);
            int random = (int)(Math.random()*900)+10000;
            //新文件名（路径）
            String fileNameNew = fileName.replace(fileName, String.valueOf(System.currentTimeMillis())+String.valueOf(random)+"."+type);
            //文件大小
            Long fileSize = multipartFile.getSize();
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileNameNew));
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileNameNew + "/" + fileSize + "Byte.");
            //上传文件   (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, folder + fileNameNew, is, metadata);
            //解析结果
            String url = "http://kysc.oss-cn-hangzhou.aliyuncs.com/"+folder+fileNameNew;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public static  String getContentType(String fileName){
        //文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if(".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if(".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if(".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)  || ".png".equalsIgnoreCase(fileExtension) ) {
            return "image/jpeg";
        }
        if(".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if(".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if(".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if(".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if(".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if(".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        //默认返回类型
        return "image/jpeg";
    }

    public static URL getUrl(OSSClient ossClient,String key) {
        // 设置URL过期时间为10年
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(BACKET_NAME, key, expiration);
        return url;
    }

    //测试
    /*public static void main(String[] args) {
        //初始化OSSClient
        //上传文件
        String files="E:\\timg.jpg";
        String[] file=files.split(",");
        for(String filename:file){
            File filess=new File(filename);
            URL url = AliyunOSSClientUtil.upload(filess);
            //上传后的文件MD5数字唯一签名:40F4131427068E08451D37F02021473A
            System.out.println(url);
        }
    }*/

}
