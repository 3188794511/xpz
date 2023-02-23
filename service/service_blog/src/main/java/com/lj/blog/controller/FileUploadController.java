package com.lj.blog.controller;

import com.lj.annotation.MyLog;
import com.lj.base.Result;
import com.lj.blog.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping({"/xpz/admin/blog/file","/xpz/api/blog/file"})
public class FileUploadController {
    @Autowired
    private FileService fileService;

    /**
     * 文件上传
     */
    @PostMapping("upload")
    @MyLog(type = "admin",value = "上传图片")
    public Result adminUpload(@RequestParam("file") MultipartFile file) {
        String uploadUrl = fileService.upload(file);
        return Result.ok(uploadUrl).message("图片上传成功");
    }

    @PostMapping("user-upload")
    @MyLog("上传图片")
    public Result userUpload(@RequestParam("file") MultipartFile file) {
        String uploadUrl = fileService.upload(file);
        return Result.ok(uploadUrl).message("图片上传成功");
    }
}
