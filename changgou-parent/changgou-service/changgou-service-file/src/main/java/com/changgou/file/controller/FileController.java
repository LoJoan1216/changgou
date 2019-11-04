package com.changgou.file.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.file.pojo.FastDFSFile;
import com.changgou.file.util.FastDFSClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Joan
 * @date 2019-11-02 15:55
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @PostMapping("/upload")
    public Result uploadFile(MultipartFile file) {

        try {
            if (file == null) {
                throw new RuntimeException("文件不存在");
            }
            //获取完整的文件名
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isEmpty(originalFilename)) {
                throw new RuntimeException("文件不存在");
            }
            //获取文件的扩展名称
            String extName = originalFilename.substring(originalFilename.lastIndexOf(",") + 1);
            //获取文件内容
            byte[] content = file.getBytes();
            //创建文件上传的实体封装类
            FastDFSFile fastDFSFile = new FastDFSFile(content, extName,originalFilename);
            //基于工具类进行文件的上传
            String[] upload = FastDFSClient.upload(fastDFSFile);
            /**
             *
             * getTrackerUrl:获取tracker服务区
             * upload[0]:获取组名
             * upload[1]:获取文件存储路径
             *
             */
            String url = FastDFSClient.getTrackerUrl() + upload[0] + "/" + upload[1];
            return new Result(true, StatusCode.OK, "文件上传成功", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, StatusCode.ERROR, "文件上传失败");
    }
}
