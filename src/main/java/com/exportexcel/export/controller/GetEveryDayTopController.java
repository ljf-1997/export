package com.exportexcel.export.controller;

import com.exportexcel.export.server.GetEveryDayTopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

/**
 * @author: 12858
 * @date: 2021.05.24
 * @Description: 导出每天的top值
 * @Version: 1.0
 */
@RestController

public class GetEveryDayTopController {
    @Autowired(required = false)
    private GetEveryDayTopService getEveryDayTopService;
    @RequestMapping(value = "getEveryDayTop")
    public void getEveryDayTop(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        getEveryDayTopService.getEveryDayTop(request, response);
    }
}
