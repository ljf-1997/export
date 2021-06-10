package com.exportexcel.export.controller;

import com.exportexcel.export.server.ChangetuisongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

/**
 * @author: 12858
 * @date: 2021.05.17
 * @Description: 按钉钉换帽推送
 * @Version: 1.0
 */
@RestController
public class ChangetuisongController {
    @Autowired(required = false)
    private ChangetuisongService changetuisongService;
    @RequestMapping(value = "changetuisong")
    public void changetuisong(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        changetuisongService.changetuisong(request, response);
    }

}
