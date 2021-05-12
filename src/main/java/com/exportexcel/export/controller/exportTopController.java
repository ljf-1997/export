package com.exportexcel.export.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.exportexcel.export.server.ExportTop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

@Controller
public class exportTopController {
    @Autowired
    private ExportTop exportTopService;
    @RequestMapping(value = "dealAndExportValues")
    public void dealAndExportValues(HttpServletRequest request, HttpServletResponse respons) throws FileNotFoundException {
        exportTopService.dealAndExportValues(request,respons);
    }
}
