package com.exportexcel.export.controller;

import com.exportexcel.export.server.NewImmAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

/**
 * @author: 12858
 * @date: 2021.05.31
 * @Description: 导出区间最多top得到报警值
 * @Version: 1.0
 */
@RestController
public class NewImmAlarmController {
    @Autowired(required = false)
    private NewImmAlarmService newImmAlarmService;
    @RequestMapping(value = "newImmAlarm")
    public void newImmAlarm(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        newImmAlarmService.newImmAlarm(request, response);
    }
}
