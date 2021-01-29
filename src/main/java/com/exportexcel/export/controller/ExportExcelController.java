package com.exportexcel.export.controller;

import com.exportexcel.utils.Result;
import com.exportexcel.export.server.ExportExcel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@Controller
public class ExportExcelController {
    @Autowired
    private ExportExcel ExportExcelservice;

    @SneakyThrows
    @RequestMapping(value = "importValue")
    public Result importValue() {
        return ExportExcelservice.importValue();
    }
}
