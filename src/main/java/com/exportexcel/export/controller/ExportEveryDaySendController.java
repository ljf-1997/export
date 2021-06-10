package com.exportexcel.export.controller;

import com.exportexcel.export.server.ExportDotValueService;
import com.exportexcel.export.server.ExportEveryDaySendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

/**
 * @author: 12858
 * @date: 2021.06.01
 * @Description: 统计每天报警推送信息
 * @Version: 1.0
 */
@RestController
public class ExportEveryDaySendController {
    @Autowired(required = false)
    private ExportEveryDaySendService exportEveryDaySendService;
    @RequestMapping(value = "exportEveryDaySend")
    public void exportEveryDaySend(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        exportEveryDaySendService.exportEveryDaySend(request, response);
    }
}
