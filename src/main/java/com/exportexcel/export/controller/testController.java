package com.exportexcel.export.controller;

import com.exportexcel.export.server.TEST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class testController {

    @Autowired
    private TEST testService;

    @RequestMapping(value = "test")
    public void export(){
//        testService.test();
    }
}
