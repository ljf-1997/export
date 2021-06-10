package com.exportexcel.export.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileNotFoundException;
import com.exportexcel.export.server.ChangeValueServer;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: 12858
 * @date: 2021.03.24
 * @Description: 转换红旗报警数据
 * @Version: 1.0
 */
@RestController
public class ChangeValueController {
    @Autowired
    private ChangeValueServer changeValueServer;
    @RequestMapping(value = "ChangeValue")
    public void ChangeValue(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        changeValueServer.ChangeValue(request, response);
    }
}