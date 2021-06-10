package com.exportexcel.export.controller;

import com.exportexcel.export.server.ExportTimeMaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

/**
 * @author: 12858
 * @date: 2021.05.12
 * @Description: 导出时间段最大打点数
 * @Version: 1.0
 */
@Controller
public class ExportTimeMaxController {
    @Autowired(required = false)
    private ExportTimeMaxService exportTimeMaxService;

    @RequestMapping(value = "exportTimeMaxService")
    public void export(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        exportTimeMaxService.exportTimeMaxService(request, response);
    }

}
