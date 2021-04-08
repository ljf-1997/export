package com.exportexcel.export.controller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.exportexcel.export.server.ReadealExcelService;

import java.io.FileNotFoundException;

/**
 * @author: 12858
 * @date: 2021.04.06
 * @Description: 阅读并处理execl文档
 * @Version: 1.0
 */
@RestController
public class ReadealExcelController {
    @Autowired(required = false)
    private ReadealExcelService readealExcelService;

    @SneakyThrows
    @RequestMapping(value = "ReadAndDeal")
    public void export() throws FileNotFoundException {
        readealExcelService.readeal();
    }

}
