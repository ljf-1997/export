package com.exportexcel.export.controller;

import com.exportexcel.export.server.ExportDotValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

/**
 * @author: 12858
 * @date: 2021.05.24
 * @Description: 报警信息打点情况导出
 * @Version: 1.0
 */
@RestController
public class ExportDotValueController {
    @Autowired(required = false)
    private ExportDotValueService exportDotValueService;
    @RequestMapping(value = "exportDotValue")
    public void exportDotValue(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        exportDotValueService.exportDotValue(request, response);
    }
}
