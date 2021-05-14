package com.exportexcel.export.controller;

import com.exportexcel.export.server.ChangeCupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

/**
 * @author: 12858
 * @date: 2021.05.13
 * @Description: 换帽信息
 * @Version: 1.0
 */
@RestController
public class ChangeCupController {
    @Autowired(required = false)
    private ChangeCupService changeCupService;
    @RequestMapping(value = "changeCup")
    public void changeCup(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        changeCupService.changeCup(request, response);
    }

}
