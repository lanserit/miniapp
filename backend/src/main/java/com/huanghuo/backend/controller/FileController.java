package com.huanghuo.backend.controller;

import com.huanghuo.common.LotteryConst;
import com.huanghuo.common.model.LotteryActivity;
import com.huanghuo.common.service.AliOssService;
import com.huanghuo.common.util.AjaxResult;
import com.huanghuo.common.util.BusinessCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by huangcheng on 2018/7/17.
 */

@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    private AliOssService aliOssService;

    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult upload(@RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            return AjaxResult.ajaxFailed(BusinessCode.FAILED);
        }
        try {
           String fileId =  aliOssService.upload(file.getInputStream());
           return AjaxResult.ajaxSuccess(fileId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResult.ajaxFailed(BusinessCode.FAILED);
    }
}
