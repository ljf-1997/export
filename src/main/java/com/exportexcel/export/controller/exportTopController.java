package com.exportexcel.export.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.exportexcel.export.server.exportTop;
import java.io.FileNotFoundException;

@Controller
public class exportTopController {
    @Autowired
    private exportTop exportTopService;
    @RequestMapping(value = "dealAndExportValues")
    public void export() throws FileNotFoundException {
        exportTopService.dealAndExportValues();
    }
}
